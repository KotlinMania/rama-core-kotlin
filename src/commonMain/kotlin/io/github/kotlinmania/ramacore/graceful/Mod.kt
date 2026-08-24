// port-lint: source graceful.rs
package io.github.kotlinmania.ramacore.graceful

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * Handle representing a background task.
 */
public interface TaskHandle {
    /**
     * Cancels the task execution.
     */
    public fun cancel()

    /**
     * Awaits completion of the task.
     */
    public suspend fun join()
}

/**
 * Handle representing a background task that produces a result.
 */
public interface AsyncTaskHandle<out T> : TaskHandle {
    /**
     * Awaits and returns the result of the task.
     */
    public suspend fun await(): T
}

/**
 * Shutdown manager for graceful shutdown of async-first applications.
 */
public class Shutdown private constructor(
    private val signal: CompletableDeferred<Unit>,
) {
    public constructor() : this(CompletableDeferred())

    /**
     * Creates a new [ShutdownGuard] associated with this shutdown manager.
     */
    public fun guard(): ShutdownGuard =
        ShutdownGuard(signal, CoroutineScope(Dispatchers.Default))

    /**
     * Triggers graceful shutdown.
     */
    public fun shutdown() {
        signal.complete(Unit)
    }

    /**
     * Returns `true` if shutdown has been triggered.
     */
    public val isShuttingDown: Boolean get() = signal.isCompleted

    /**
     * Awaits the shutdown signal.
     */
    public suspend fun waitShutdown(): Unit = signal.await()

    public companion object {
        /**
         * Creates a new [ShutdownBuilder].
         */
        public fun builder(): ShutdownBuilder = ShutdownBuilder()

        /**
         * Creates a new default [Shutdown] instance.
         */
        public fun new(): Shutdown = Shutdown()
    }
}

/**
 * Builder for configuring and creating a [Shutdown] instance.
 */
public class ShutdownBuilder(
    private var timeout: Duration? = null,
) {
    /**
     * Sets an optional graceful shutdown timeout duration.
     */
    public fun withTimeout(timeout: Duration?): ShutdownBuilder {
        this.timeout = timeout
        return this
    }

    /**
     * Builds the [Shutdown] instance.
     */
    public fun build(): Shutdown = Shutdown()
}

/**
 * Guard that tracks tasks and coordinates graceful shutdown.
 */
public class ShutdownGuard internal constructor(
    private val signal: Deferred<Unit>,
    private val scope: CoroutineScope,
) {
    internal constructor(signal: Deferred<Unit>) : this(signal, CoroutineScope(Dispatchers.Default))

    private val activeJobs = mutableListOf<Job>()
    private val mutex = Mutex()

    /**
     * Spawns a task that is tracked by this shutdown guard.
     */
    public fun <T> spawnTask(block: suspend () -> T): AsyncTaskHandle<T> {
        val deferred = scope.async { block() }
        trackJob(deferred)
        return object : AsyncTaskHandle<T> {
            override fun cancel() {
                deferred.cancel()
            }

            override suspend fun join() {
                deferred.join()
            }

            override suspend fun await(): T = deferred.await()
        }
    }

    /**
     * Spawns a coroutine job tracked by this shutdown guard.
     */
    public fun spawn(block: suspend () -> Unit): TaskHandle {
        val job = scope.launch { block() }
        trackJob(job)
        return object : TaskHandle {
            override fun cancel() {
                job.cancel()
            }

            override suspend fun join() {
                job.join()
            }
        }
    }

    private fun trackJob(job: Job) {
        scope.launch {
            mutex.withLock { activeJobs.add(job) }
            try {
                job.join()
            } finally {
                mutex.withLock { activeJobs.remove(job) }
            }
        }
    }

    /**
     * Awaits until shutdown signal is received.
     */
    public suspend fun cancelled(): Unit = signal.await()

    /**
     * Returns `true` if shutdown has been triggered.
     */
    public val isCancelled: Boolean get() = signal.isCompleted

    /**
     * Returns the count of currently tracked active jobs.
     */
    public suspend fun activeCount(): Int = mutex.withLock { activeJobs.size }
}
