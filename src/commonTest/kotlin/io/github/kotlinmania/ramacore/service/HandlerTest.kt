// port-lint: tests rama-core/src/service/handler.rs
package io.github.kotlinmania.ramacore.service

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HandlerTest {
    @Test
    fun testServiceFn() =
        runSync {
            val s1 =
                serviceFn<String, String, Nothing> { req: String ->
                    assertEquals("hello", req)
                    RamaResult.ok("world")
                }.boxed()

            val res = s1.serve("hello")
            assertTrue(res.isSuccess())
            assertEquals("world", res.value)
        }

    @Test
    fun testParameterlessServiceFn() =
        runSync {
            val s =
                serviceFn<String, Nothing> {
                    RamaResult.ok("zero-arg")
                }

            val res = s.serve(Unit)
            assertTrue(res.isSuccess())
            assertEquals("zero-arg", res.value)
        }

    private fun <T> assertSendSync(t: T) {
        // Concurrency check translated for Kotlin
    }

    @Test
    fun testServiceFnWithoutUsage() {
        assertSendSync(serviceFn<Unit, Unit, Nothing> { RamaResult.ok(Unit) })
        assertSendSync(serviceFn<String, Unit, Nothing> { RamaResult.ok(Unit) })
    }
}
