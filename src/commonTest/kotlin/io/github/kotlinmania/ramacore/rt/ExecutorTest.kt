// port-lint: tests rama-core/src/rt/executor.rs
package io.github.kotlinmania.ramacore.rt

import io.github.kotlinmania.ramacore.graceful.Shutdown
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExecutorTest {
    @Test
    fun testDefaultExecutor() =
        runTest {
            val executor = Executor.new()
            assertNull(executor.guard())

            val deferred =
                executor.spawnTask {
                    10 + 20
                }

            assertEquals(30, deferred.await())
        }

    @Test
    fun testGracefulExecutor() =
        runTest {
            val shutdown = Shutdown.new()
            val guard = shutdown.guard()
            val executor = Executor.graceful(guard)

            assertNotNull(executor.guard())

            val deferred =
                executor.spawnTask {
                    "hello world"
                }

            assertEquals("hello world", deferred.await())

            var executed = false
            val job =
                executor.spawn {
                    executed = true
                }

            job.join()
            assertTrue(executed)
        }
}
