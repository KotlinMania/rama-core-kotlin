// port-lint: source rama-core/src/lib.rs
package io.github.kotlinmania.ramacore

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Re-exports and future utilities matching Rama core.
 *
 * Learn more about Rama:
 * - Github: https://github.com/plabayo/rama
 * - Book: https://ramaproxy.org/book/
 */

/**
 * A pair result from combining two asynchronous operations via [RamaFutures.zip].
 */
public data class ZipResult<out A, out B>(
    public val first: A,
    public val second: B,
)

/**
 * Future utilities matching Rama futures.
 */
public object RamaFutures {
    /**
     * Joins two asynchronous computations, waiting for both to complete.
     */
    public suspend fun <T1, T2> zip(
        op1: suspend () -> T1,
        op2: suspend () -> T2,
    ): ZipResult<T1, T2> =
        coroutineScope {
            val f1 = async { op1() }
            val f2 = async { op2() }
            ZipResult(f1.await(), f2.await())
        }

    /**
     * Joins two fallible operations, waiting for both to complete or one of them to error.
     */
    public suspend fun <T1 : Any, T2 : Any, E : Any> tryZip(
        op1: suspend () -> RamaResult<T1, E>,
        op2: suspend () -> RamaResult<T2, E>,
    ): RamaResult<ZipResult<T1, T2>, E> =
        coroutineScope {
            val f1 = async { op1() }
            val f2 = async { op2() }
            val r1 = f1.await()
            if (r1.isFailure()) {
                return@coroutineScope RamaResult.err(r1.errorOrNull()!!)
            }
            val r2 = f2.await()
            if (r2.isFailure()) {
                return@coroutineScope RamaResult.err(r2.errorOrNull()!!)
            }
            RamaResult.ok(ZipResult(r1.getOrNull()!!, r2.getOrNull()!!))
        }
}
