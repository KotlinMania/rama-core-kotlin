// port-lint: source rt/executor.rs
package io.github.kotlinmania.ramacore.rt

import io.github.kotlinmania.ramacore.graceful.AsyncTaskHandle
import io.github.kotlinmania.ramacore.graceful.ShutdownGuard
import io.github.kotlinmania.ramacore.graceful.TaskHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Future / task executor that utilises coroutine dispatchers.
 */
public class Executor internal constructor(
    private val guard: ShutdownGuard?,
    private val scope: CoroutineScope,
) {
    internal constructor(guard: ShutdownGuard? = null) : this(guard, CoroutineScope(Dispatchers.Default))
    /**
     * Spawns a task on the executor, returning an [AsyncTaskHandle] result.
     *
     * If a [ShutdownGuard] is registered, the task is tracked gracefully.
     */
    public fun <T> spawnTask(block: suspend () -> T): AsyncTaskHandle<T> {
        val g = guard
        return if (g != null) {
            g.spawnTask(block)
        } else {
            val deferred = scope.async { block() }
            object : AsyncTaskHandle<T> {
                override fun cancel() {
                    deferred.cancel()
                }

                override suspend fun join() {
                    deferred.join()
                }

                override suspend fun await(): T = deferred.await()
            }
        }
    }

    /**
     * Spawns a coroutine job on the executor.
     *
     * If a [ShutdownGuard] is registered, the job is tracked gracefully.
     */
    public fun spawn(block: suspend () -> Unit): TaskHandle {
        val g = guard
        return if (g != null) {
            g.spawn(block)
        } else {
            val job = scope.launch { block() }
            object : TaskHandle {
                override fun cancel() {
                    job.cancel()
                }

                override suspend fun join() {
                    job.join()
                }
            }
        }
    }

    /**
     * Gets a reference to the [ShutdownGuard] if this executor was created with [graceful].
     */
    public fun guard(): ShutdownGuard? = guard

    public companion object {
        /**
         * Creates a new default [Executor].
         */
        public fun new(): Executor = Executor(guard = null)

        /**
         * Creates a new [Executor] with the given [ShutdownGuard].
         */
        public fun graceful(guard: ShutdownGuard): Executor = Executor(guard = guard)
    }
}
