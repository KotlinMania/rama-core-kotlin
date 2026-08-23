// port-lint: source layer/limit/policy/concurrent.rs
package io.github.kotlinmania.ramacore.layer.limit.policy

import kotlinx.coroutines.sync.Semaphore

/**
 * Error returned when concurrency limit is reached.
 */
public class LimitReached : Exception("serve aborted due to exhausted concurrency limit") {
    override fun toString(): String = "LimitReached: $message"

    override fun equals(other: Any?): Boolean = other is LimitReached

    override fun hashCode(): Int = "LimitReached".hashCode()
}

/**
 * Guard that releases the acquired concurrency permit.
 */
public fun interface ConcurrentGuard {
    public fun release()
}

/**
 * Tracker trait for tracking concurrent requests.
 */
public interface ConcurrentTracker<Guard : ConcurrentGuard, Error : Any> {
    public fun tryAccess(): Result<Guard>
}

/**
 * Concurrency counter that limits active requests up to [max].
 */
public class ConcurrentCounter(
    public val max: Int,
) : ConcurrentTracker<ConcurrentCounter.Guard, LimitReached> {
    private val semaphore = if (max > 0) Semaphore(max) else null

    public class Guard internal constructor(
        private val counter: ConcurrentCounter,
    ) : ConcurrentGuard {
        private var released = false

        override fun release() {
            if (!released) {
                released = true
                counter.semaphore?.release()
            }
        }
    }

    override fun tryAccess(): Result<Guard> {
        val sem = semaphore
        if (sem == null || !sem.tryAcquire()) {
            return Result.failure(LimitReached())
        }
        return Result.success(Guard(this))
    }

    override fun toString(): String = "ConcurrentCounter(max=$max)"

    public companion object {
        public fun new(max: Int): ConcurrentCounter = ConcurrentCounter(max)
    }
}

/**
 * A [Policy] that limits the number of concurrent requests.
 */
public class ConcurrentPolicy<Input, Guard : ConcurrentGuard, Error : Any, C : ConcurrentTracker<Guard, Error>>(
    public val tracker: C,
) : Policy<Input, Guard, Error> {
    override suspend fun check(input: Input): PolicyResult<Input, Guard, Error> {
        val accessResult = tracker.tryAccess()
        return accessResult.fold(
            onSuccess = { guard ->
                PolicyResult(input, PolicyOutput.Ready(guard))
            },
            onFailure = { err ->
                @Suppress("UNCHECKED_CAST")
                val error = (err as? LimitReached ?: LimitReached()) as Error
                PolicyResult(input, PolicyOutput.Abort(error))
            },
        )
    }

    override fun toString(): String = "ConcurrentPolicy($tracker)"

    public companion object {
        public fun <Input> max(max: Int): ConcurrentPolicy<Input, ConcurrentCounter.Guard, LimitReached, ConcurrentCounter> =
            ConcurrentPolicy(ConcurrentCounter(max))

        public fun <Input, Guard : ConcurrentGuard, Error : Any, C : ConcurrentTracker<Guard, Error>> new(
            tracker: C,
        ): ConcurrentPolicy<Input, Guard, Error, C> = ConcurrentPolicy(tracker)
    }
}
