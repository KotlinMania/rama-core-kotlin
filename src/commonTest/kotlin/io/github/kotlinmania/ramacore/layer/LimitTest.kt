// port-lint: tests layer/limit/mod.rs
// port-lint: tests layer/limit/policy/matcher.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.ServiceInput
import io.github.kotlinmania.ramacore.layer.limit.Limit
import io.github.kotlinmania.ramacore.layer.limit.LimitLayer
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentCounter
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentPolicy
import io.github.kotlinmania.ramacore.layer.limit.policy.LimitReached
import io.github.kotlinmania.ramacore.layer.limit.policy.MatcherPolicyMap
import io.github.kotlinmania.ramacore.layer.limit.policy.MatcherPolicyRoute
import io.github.kotlinmania.ramacore.layer.limit.policy.PolicyOutput
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.matcher.Matcher
import io.github.kotlinmania.ramacore.service.serviceFn
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class LimitTest {
    @Test
    fun testUnlimitedPolicy() =
        runTest {
            val policy = UnlimitedPolicy.instance<String>()
            val result = policy.check("test")
            assertEquals("test", result.input)
            assertIs<PolicyOutput.Ready<Unit>>(result.output)
        }

    @Test
    fun testConcurrentCounter() {
        val counter = ConcurrentCounter.new(2)
        val g1 = counter.tryAccess()
        assertTrue(g1.isSuccess)

        val g2 = counter.tryAccess()
        assertTrue(g2.isSuccess)

        val g3 = counter.tryAccess()
        assertTrue(g3.isFailure)

        g1.getOrNull()?.releaseGuard()

        val g4 = counter.tryAccess()
        assertTrue(g4.isSuccess)
    }

    @Test
    fun testConcurrentPolicyLimit() =
        runTest {
            val baseSvc =
                serviceFn<Int, String, LimitReached> { input: Int ->
                    RamaResult.ok("Result: $input")
                }
            val policy = ConcurrentPolicy.max<Int>(1)
            val limitedSvc = Limit.new(baseSvc, policy)

            val g1 = policy.tracker.tryAccess()
            assertTrue(g1.isSuccess)

            // With capacity exhausted, serve should fail
            val res = limitedSvc.serve(10)
            assertTrue(res.isFailure())

            g1.getOrNull()?.releaseGuard()

            // With capacity released, serve should succeed
            val res2 = limitedSvc.serve(20)
            assertTrue(res2.isSuccess())
            assertEquals("Result: 20", res2.value)
        }

    @Test
    fun testLimit() =
        runTest {
            val handleRequest =
                serviceFn<String, String, Nothing> { req ->
                    delay(100.milliseconds)
                    RamaResult.ok(req)
                }

            val layer =
                LimitLayer.new<String, String, LimitReached, ConcurrentCounter.Guard, LimitReached>(
                    ConcurrentPolicy.max(1),
                )

            val service1 = layer.layer(handleRequest)
            val service2 = layer.layer(handleRequest)

            val d1 = async { service1.serve("Hello") }
            val d2 = async { service2.serve("Hello") }

            val result1 = d1.await()
            val result2 = d2.await()

            val oneSuccess =
                (result1.isSuccess() && result2.isFailure()) ||
                    (result2.isSuccess() && result1.isFailure())
            assertTrue(oneSuccess, "Expected one service call to succeed and one to fail due to limit")
            if (result1.isSuccess()) {
                assertEquals("Hello", result1.value)
            } else {
                assertEquals("Hello", result2.value)
            }
        }

    @Test
    fun testWithErrorIntoResponseFn() =
        runTest {
            val handleRequest =
                serviceFn<String, String, Nothing> { _ ->
                    RamaResult.ok("good")
                }

            val layer =
                LimitLayer
                    .new<String, String, Nothing, ConcurrentCounter.Guard, LimitReached>(
                        ConcurrentPolicy.max(0),
                    ).withErrorIntoResponseFn { _ -> RamaResult.ok("bad") }

            val service = layer.layer(handleRequest)
            val resp = service.serve("Hello")
            assertTrue(resp.isSuccess())
            assertEquals("bad", resp.value)
        }

    @Test
    fun testZeroLimit() =
        runTest {
            val handleRequest =
                serviceFn<String, String, LimitReached> { req ->
                    RamaResult.ok(req)
                }

            val layer =
                LimitLayer.new<String, String, LimitReached, ConcurrentCounter.Guard, LimitReached>(
                    ConcurrentPolicy.max(0),
                )

            val service = layer.layer(handleRequest)
            val result = service.serve("Hello")
            assertTrue(result.isFailure())
        }

    @Test
    fun testMatcherPolicyEmpty() =
        runTest {
            val policy = MatcherPolicyMap<ServiceInput<Int>, ConcurrentCounter.Guard, LimitReached>(emptyList())
            for (i in 0 until 10) {
                val result = policy.check(ServiceInput.new(i))
                assertIs<PolicyOutput.Ready<*>>(result.output)
                assertEquals(null, (result.output as PolicyOutput.Ready).guard)
            }
        }

    @Test
    fun testMatcherPolicyAlways() =
        runTest {
            val concurrencyPolicy = ConcurrentPolicy.max<Extensions>(2)
            val route =
                MatcherPolicyRoute<Extensions, ConcurrentCounter.Guard, LimitReached>(
                    matcher = { _: Extensions?, _: Extensions -> true },
                    policy = concurrencyPolicy,
                )
            val policy = MatcherPolicyMap(listOf(route))

            val r1 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Ready<*>>(r1.output)
            val guard1 = (r1.output as PolicyOutput.Ready).guard

            val r2 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Ready<*>>(r2.output)
            val guard2 = (r2.output as PolicyOutput.Ready).guard

            val r3 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Abort<*>>(r3.output)

            guard1?.releaseGuard()
            val r4 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Ready<*>>(r4.output)

            val r5 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Abort<*>>(r5.output)

            guard2?.releaseGuard()
            val r6 = policy.check(Extensions.new())
            assertIs<PolicyOutput.Ready<*>>(r6.output)
        }

    private sealed class TestMatchers : Matcher<ServiceInput<Int>> {
        data class Const(
            val n: Int,
        ) : TestMatchers() {
            override fun matches(
                ext: Extensions?,
                input: ServiceInput<Int>,
            ): Boolean = n == input.input
        }

        data object Odd : TestMatchers() {
            override fun matches(
                ext: Extensions?,
                input: ServiceInput<Int>,
            ): Boolean = input.input % 2 != 0
        }
    }

    @Test
    fun testMatcherPolicyScopedLimits() =
        runTest {
            val oddRoute =
                MatcherPolicyRoute<ServiceInput<Int>, ConcurrentCounter.Guard, LimitReached>(
                    matcher = TestMatchers.Odd,
                    policy = ConcurrentPolicy.max(2),
                )
            val constRoute =
                MatcherPolicyRoute<ServiceInput<Int>, ConcurrentCounter.Guard, LimitReached>(
                    matcher = TestMatchers.Const(42),
                    policy = ConcurrentPolicy.max(1),
                )
            val policy = MatcherPolicyMap(listOf(oddRoute, constRoute))

            // Even numbers (except 42) match nothing and have no limit
            for (i in 1 until 10) {
                val res = policy.check(ServiceInput.new(i * 2))
                assertIs<PolicyOutput.Ready<*>>(res.output)
                assertEquals(null, (res.output as PolicyOutput.Ready).guard)
            }

            val oddRes1 = policy.check(ServiceInput.new(1))
            assertIs<PolicyOutput.Ready<*>>(oddRes1.output)
            val oddGuard1 = (oddRes1.output as PolicyOutput.Ready).guard

            val constRes1 = policy.check(ServiceInput.new(42))
            assertIs<PolicyOutput.Ready<*>>(constRes1.output)
            val constGuard1 = (constRes1.output as PolicyOutput.Ready).guard

            val oddRes2 = policy.check(ServiceInput.new(3))
            assertIs<PolicyOutput.Ready<*>>(oddRes2.output)
            val oddGuard2 = (oddRes2.output as PolicyOutput.Ready).guard

            // Both odd and 42 limit reached
            val abort5 = policy.check(ServiceInput.new(5))
            assertIs<PolicyOutput.Abort<*>>(abort5.output)

            val abort42 = policy.check(ServiceInput.new(42))
            assertIs<PolicyOutput.Abort<*>>(abort42.output)

            // Even numbers still allowed
            for (i in 1 until 10) {
                val res = policy.check(ServiceInput.new(i * 2))
                assertIs<PolicyOutput.Ready<*>>(res.output)
            }

            // Drop first odd guard
            oddGuard1?.releaseGuard()
            val oddRes3 = policy.check(ServiceInput.new(9))
            assertIs<PolicyOutput.Ready<*>>(oddRes3.output)

            // Const guard 42 still full
            val abort422 = policy.check(ServiceInput.new(42))
            assertIs<PolicyOutput.Abort<*>>(abort422.output)

            // Release 42 guard
            constGuard1?.releaseGuard()
            val constRes2 = policy.check(ServiceInput.new(42))
            assertIs<PolicyOutput.Ready<*>>(constRes2.output)

            // Odd limit reached again
            val abort11 = policy.check(ServiceInput.new(11))
            assertIs<PolicyOutput.Abort<*>>(abort11.output)

            // Releasing second odd guard makes room
            oddGuard2?.releaseGuard()
            val oddRes4 = policy.check(ServiceInput.new(13))
            assertIs<PolicyOutput.Ready<*>>(oddRes4.output)

            for (i in 1 until 10) {
                val res = policy.check(ServiceInput.new(i * 2))
                assertIs<PolicyOutput.Ready<*>>(res.output)
            }
        }
}
