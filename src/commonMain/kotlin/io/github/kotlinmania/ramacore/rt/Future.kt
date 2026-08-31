// port-lint: source rama-core/src/rt/future.rs
package io.github.kotlinmania.ramacore.rt

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Poll the future/deferred once and return `T?` if it is ready, else `null`.
 *
 * In Kotlin coroutines, if the deferred is completed, returns its value; otherwise returns null.
 */
public object FutureExt {
    /**
     * Poll the deferred once and return the value if it is completed, else null.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    public fun <T> nowOrNever(deferred: Deferred<T>): T? =
        if (deferred.isCompleted) {
            try {
                deferred.getCompleted()
            } catch (_: Throwable) {
                null
            }
        } else {
            null
        }
}

/**
 * Poll the deferred once and return the value if it is completed, else null.
 */
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> Deferred<T>.nowOrNever(): T? = FutureExt.nowOrNever(this)
