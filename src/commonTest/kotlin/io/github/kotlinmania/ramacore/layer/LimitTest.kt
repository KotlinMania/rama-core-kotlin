// port-lint: tests layer/limit/mod.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.limit.Limit
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentCounter
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentPolicy
import io.github.kotlinmania.ramacore.layer.limit.policy.LimitReached
import io.github.kotlinmania.ramacore.layer.limit.policy.PolicyOutput
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.service.serviceFn
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

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

        g1.getOrNull()?.release()

        val g4 = counter.tryAccess()
        assertTrue(g4.isSuccess)
    }

    @Test
    fun testConcurrentPolicyLimit() =
        runTest {
            val baseSvc =
                serviceFn<Int, String, LimitReached> { input ->
                    RamaResult.ok("Result: $input")
                }
            val policy = ConcurrentPolicy.max<Int>(1)
            val limitedSvc = Limit.new(baseSvc, policy)

            val g1 = policy.tracker.tryAccess()
            assertTrue(g1.isSuccess)

            // With capacity exhausted, serve should fail
            val res = limitedSvc.serve(10)
            assertTrue(res.isFailure())

            g1.getOrNull()?.release()

            // With capacity released, serve should succeed
            val res2 = limitedSvc.serve(20)
            assertTrue(res2.isSuccess())
            assertEquals("Result: 20", res2.value)
        }
}
