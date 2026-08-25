// port-lint: tests layer/limit/policy/matcher.rs
package io.github.kotlinmania.ramacore.layer.limit.policy

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ServiceInput
import io.github.kotlinmania.ramacore.matcher.Matcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class MatcherPolicyTest {
    private fun <Input, Guard, Error : Any> assertReady(result: PolicyResult<Input, Guard, Error>): Guard =
        when (val output = result.output) {
            is PolicyOutput.Ready -> output.guard
            is PolicyOutput.Abort -> fail("unexpected abort: ${output.error}")
            is PolicyOutput.Retry -> fail("unexpected retry")
        }

    private fun <Input, Guard, Error : Any> assertAbort(result: PolicyResult<Input, Guard, Error>) {
        when (result.output) {
            is PolicyOutput.Abort -> Unit
            is PolicyOutput.Ready -> fail("unexpected ready")
            is PolicyOutput.Retry -> fail("unexpected retry")
        }
    }

    @Test
    fun matcherPolicyEmpty() =
        runTest {
            val policy = MatcherPolicyMap<ServiceInput<Int>, ConcurrentCounter.Guard, LimitReached>(emptyList())

            for (i in 0 until 10) {
                val guard = assertReady(policy.check(ServiceInput.new(i)))
                assertEquals(null, guard)
            }
        }

    @Test
    fun matcherPolicyAlways() =
        runTest {
            val concurrencyPolicy = ConcurrentPolicy.max<Extensions>(2)
            val policy =
                MatcherPolicyMap(
                    routes =
                        listOf(
                            MatcherPolicyRoute(
                                matcher = Matcher { _, _ -> true },
                                policy = concurrencyPolicy,
                            ),
                        ),
                )

            val guard1 = assertReady(policy.check(Extensions.new()))
            val guard2 = assertReady(policy.check(Extensions.new()))

            assertAbort(policy.check(Extensions.new()))

            guard1?.releaseGuard()
            val guard3 = assertReady(policy.check(Extensions.new()))

            assertAbort(policy.check(Extensions.new()))

            guard2?.releaseGuard()
            val guard4 = assertReady(policy.check(Extensions.new()))
            guard3?.releaseGuard()
            guard4?.releaseGuard()
        }

    private sealed class TestMatchers : Matcher<ServiceInput<Int>> {
        data class Const(
            val n: Int,
        ) : TestMatchers() {
            override fun matches(ext: Extensions?, input: ServiceInput<Int>): Boolean = input.input == n
        }

        data object Odd : TestMatchers() {
            override fun matches(ext: Extensions?, input: ServiceInput<Int>): Boolean = input.input % 2 == 1
        }
    }

    @Test
    fun matcherPolicyScopedLimits() =
        runTest {
            val policy =
                MatcherPolicyMap(
                    routes =
                        listOf(
                            MatcherPolicyRoute(
                                matcher = TestMatchers.Odd,
                                policy = ConcurrentPolicy.max(2),
                            ),
                            MatcherPolicyRoute(
                                matcher = TestMatchers.Const(42),
                                policy = ConcurrentPolicy.max(1),
                            ),
                        ),
                )

            // even numbers (except 42) will always be allowed
            for (i in 1 until 10) {
                assertReady(policy.check(ServiceInput.new(i * 2)))
            }

            val oddGuard1 = assertReady(policy.check(ServiceInput.new(1)))
            val constGuard1 = assertReady(policy.check(ServiceInput.new(42)))
            val oddGuard2 = assertReady(policy.check(ServiceInput.new(3)))

            // both the odd and 42 limit is reached
            assertAbort(policy.check(ServiceInput.new(5)))
            assertAbort(policy.check(ServiceInput.new(42)))

            // even numbers except 42 will match nothing and thus have no limit
            for (i in 1 until 10) {
                assertReady(policy.check(ServiceInput.new(i * 2)))
            }

            // only once we drop a guard can we make a new odd input
            oddGuard1?.releaseGuard()
            val oddGuard3 = assertReady(policy.check(ServiceInput.new(9)))

            // only once we drop the current 42 guard can we get a new guard
            assertAbort(policy.check(ServiceInput.new(42)))
            constGuard1?.releaseGuard()
            val constGuard2 = assertReady(policy.check(ServiceInput.new(42)))

            // odd limit reached again so no luck here
            assertAbort(policy.check(ServiceInput.new(11)))

            // dropping another odd guard makes room for a new odd input
            oddGuard2?.releaseGuard()
            val oddGuard4 = assertReady(policy.check(ServiceInput.new(13)))

            // even numbers (except 42) will always be allowed
            for (i in 1 until 10) {
                assertReady(policy.check(ServiceInput.new(i * 2)))
            }

            oddGuard3?.releaseGuard()
            constGuard2?.releaseGuard()
            oddGuard4?.releaseGuard()
        }
}
