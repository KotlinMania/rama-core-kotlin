// port-lint: tests rama-core/src/graceful.rs
package io.github.kotlinmania.ramacore.graceful

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class GracefulTest {
    @Test
    fun testShutdownLifecycle() =
        runTest {
            val shutdown = Shutdown.new()
            assertFalse(shutdown.isShuttingDown)

            val guard = shutdown.guard()
            assertFalse(guard.isCancelled)

            val task =
                guard.spawnTask {
                    42
                }

            val result = task.await()
            assertEquals(42, result)

            shutdown.shutdown()
            assertTrue(shutdown.isShuttingDown)
            assertTrue(guard.isCancelled)
        }

    @Test
    fun testShutdownBuilder() {
        val shutdown =
            Shutdown
                .builder()
                .withTimeout(500.milliseconds)
                .build()

        assertFalse(shutdown.isShuttingDown)
        shutdown.shutdown()
        assertTrue(shutdown.isShuttingDown)
    }

    @Test
    fun testShutdownGuardSpawn() =
        runTest {
            val shutdown = Shutdown.new()
            val guard = shutdown.guard()

            var executed = false
            val job =
                guard.spawn {
                    executed = true
                }

            job.join()
            assertTrue(executed)
        }
}
