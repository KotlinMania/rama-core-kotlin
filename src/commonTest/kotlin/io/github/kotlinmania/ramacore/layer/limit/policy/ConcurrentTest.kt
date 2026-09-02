// port-lint: tests layer/limit/policy/concurrent.rs
package io.github.kotlinmania.ramacore.layer.limit.policy

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.fail

class ConcurrentTest {
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
    fun concurrentPolicyZero() =
        runTest {
            val policy = ConcurrentPolicy.max<Unit>(0)
            assertAbort(policy.check(Unit))
        }

    @Test
    fun concurrentPolicy() =
        runTest {
            val policy = ConcurrentPolicy.max<Unit>(2)

            val guard1 = assertReady(policy.check(Unit))
            val guard2 = assertReady(policy.check(Unit))

            assertAbort(policy.check(Unit))

            guard1.releaseGuard()
            val guard3 = assertReady(policy.check(Unit))

            assertAbort(policy.check(Unit))

            guard2.releaseGuard()
            val guard4 = assertReady(policy.check(Unit))
            guard3.releaseGuard()
            guard4.releaseGuard()
        }

    @Test
    fun concurrentPolicyClone() =
        runTest {
            val policy = ConcurrentPolicy.max<Unit>(2)
            val policyClone = ConcurrentPolicy<Unit, ConcurrentCounter.Guard, LimitReached>(policy.tracker)

            val guard1 = assertReady(policy.check(Unit))
            val guard2 = assertReady(policyClone.check(Unit))

            assertAbort(policy.check(Unit))

            guard1.releaseGuard()
            val guard3 = assertReady(policy.check(Unit))
            guard2.releaseGuard()
            guard3.releaseGuard()
        }
}
