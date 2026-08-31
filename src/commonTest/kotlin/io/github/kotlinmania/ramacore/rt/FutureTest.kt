// port-lint: tests rt/future.rs
package io.github.kotlinmania.ramacore.rt

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FutureTest {
    @Test
    fun testNowOrNeverPending() =
        runTest {
            val deferred = CompletableDeferred<Int>()
            assertNull(deferred.nowOrNever())
        }

    @Test
    fun testNowOrNeverCompleted() =
        runTest {
            val deferred = CompletableDeferred<Int>()
            deferred.complete(42)
            assertEquals(42, deferred.nowOrNever())
        }
}
