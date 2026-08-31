// port-lint: tests rama-core/src/lib.rs
package io.github.kotlinmania.ramacore

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun testZip() =
        runTest {
            val result =
                RamaFutures.zip(
                    op1 = { 1 },
                    op2 = { 2 },
                )
            assertEquals(1, result.first)
            assertEquals(2, result.second)
        }

    @Test
    fun testTryZipSuccess() =
        runTest {
            val result =
                RamaFutures.tryZip(
                    op1 = { RamaResult.ok(1) },
                    op2 = { RamaResult.ok(2) },
                )
            assertTrue(result.isSuccess())
            assertEquals(1, result.getOrNull()?.first)
            assertEquals(2, result.getOrNull()?.second)
        }

    @Test
    fun testTryZipFailure() =
        runTest {
            val result =
                RamaFutures.tryZip(
                    op1 = { RamaResult.ok(1) },
                    op2 = { RamaResult.err("error in second") },
                )
            assertTrue(result.isFailure())
            assertEquals("error in second", result.errorOrNull())
        }
}
