// port-lint: source rt/executor.rs
package io.github.kotlinmania.ramacore.rt

import io.github.kotlinmania.ramacore.graceful.ShutdownGuard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Future / task executor that utilises coroutine dispatchers.
 */
public class Executor(
    public val guard: ShutdownGuard? = null,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    /**
     * Spawns a task on the executor, returning a [Deferred] result.
     *
     * If a [ShutdownGuard] is registered, the task is tracked gracefully.
     */
    public fun <T> spawnTask(block: suspend () -> T): Deferred<T> {
        val g = guard
        return if (g != null) {
            g.spawnTask(block)
        } else {
            scope.async { block() }
        }
    }

    /**
     * Spawns a coroutine job on the executor.
     *
     * If a [ShutdownGuard] is registered, the job is tracked gracefully.
     */
    public fun spawn(block: suspend () -> Unit): Job {
        val g = guard
        return if (g != null) {
            g.spawn(block)
        } else {
            scope.launch { block() }
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
        public fun new(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)): Executor =
            Executor(guard = null, scope = scope)

        /**
         * Creates a new [Executor] with the given [ShutdownGuard].
         */
        public fun graceful(
            guard: ShutdownGuard,
            scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        ): Executor = Executor(guard = guard, scope = scope)
    }
}
