// port-lint: tests service/svc.rs
package io.github.kotlinmania.ramacore.service

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.combinators.Either
import io.github.kotlinmania.ramacore.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SvcTest {
    private class AddSvc(
        val addend: Int,
    ) : Service<Int, Int, Nothing> {
        override suspend fun serve(input: Int): RamaResult<Int, Nothing> =
            RamaResult.ok(addend + input)
    }

    private class MulSvc(
        val factor: Int,
    ) : Service<Int, Int, Nothing> {
        override suspend fun serve(input: Int): RamaResult<Int, Nothing> =
            RamaResult.ok(factor * input)
    }

    @Test
    fun testAddSvc() =
        runSync {
            val svc = AddSvc(1)
            val res = svc.serve(1)
            assertTrue(res.isSuccess())
            assertEquals(2, res.getOrNull())
        }

    @Test
    fun testStaticDispatch() =
        runSync {
            val services = listOf(AddSvc(1), AddSvc(2), AddSvc(3))
            for ((i, svc) in services.withIndex()) {
                val res = svc.serve(i)
                assertTrue(res.isSuccess())
                assertEquals(i * 2 + 1, res.getOrNull())
            }
        }

    @Test
    fun testDynamicDispatch() =
        runSync {
            val services: List<BoxService<Int, Int, Nothing>> =
                listOf(
                    AddSvc(1).boxed(),
                    AddSvc(2).boxed(),
                    AddSvc(3).boxed(),
                    MulSvc(4).boxed(),
                    MulSvc(5).boxed(),
                )

            for ((i, svc) in services.withIndex()) {
                val res = svc.serve(i)
                assertTrue(res.isSuccess())
                if (i < 3) {
                    assertEquals(i * 2 + 1, res.getOrNull())
                } else {
                    assertEquals(i * (i + 1), res.getOrNull())
                }
            }
        }

    @Test
    fun testMirrorService() =
        runSync {
            val svc = MirrorService.new<String>()
            val res = svc.serve("hello")
            assertTrue(res.isSuccess())
            assertEquals("hello", res.getOrNull())
        }

    @Test
    fun testRejectService() =
        runSync {
            val svc = RejectService.default<Int>()
            val res = svc.serve(1)
            assertTrue(res.isFailure())
            assertEquals("Input rejected", res.errorOrNull()?.toString())
        }

    @Test
    fun testEitherService() =
        runSync {
            val svc1: Either<Service<Int, Int, Nothing>, Service<Int, Int, Nothing>> =
                Either.A(AddSvc(10))
            val svc2: Either<Service<Int, Int, Nothing>, Service<Int, Int, Nothing>> =
                Either.B(MulSvc(10))

            assertEquals(15, svc1.serve(5).getOrNull())
            assertEquals(50, svc2.serve(5).getOrNull())
        }
}
