package io.github.kotlinmania.ramacore

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

internal fun <T> runSync(block: suspend () -> T): T {
    var out: Result<T>? = null
    block.startCoroutine(
        object : Continuation<T> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resumeWith(result: Result<T>) {
                out = result
            }
        },
    )
    val r = out ?: error("Coroutine suspended unexpectedly")
    return r.getOrThrow()
}
