// port-lint: tests layer/layer_fn.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.runSync
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramacore.service.serviceFn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LayerTest {
    private class ToUpperService(
        private val inner: Service<String, String, Nothing>,
    ) : Service<String, String, Nothing> {
        override suspend fun serve(input: String): RamaResult<String, Nothing> {
            val res = inner.serve(input)
            return RamaResult.ok(res.getOrNull()!!.uppercase())
        }
    }

    @Test
    fun testLayerFn() =
        runSync {
            val layer = layerFn { inner: Service<String, String, Nothing> -> ToUpperService(inner) }
            val svc = layer.layer(serviceFn { req: String -> RamaResult.ok(req) })

            val res = svc.serve("hello")
            assertTrue(res.isSuccess())
            assertEquals("HELLO", res.getOrNull())
        }

    @Test
    fun testLayerStack() =
        runSync {
            val l1 = layerFn { inner: Service<String, String, Nothing> -> ToUpperService(inner) }
            val l2 =
                layerFn { inner: Service<String, String, Nothing> ->
                    Service { input: String ->
                        val res = inner.serve(input)
                        RamaResult.ok("prefix-${res.getOrNull()!!}")
                    }
                }

            val stack = l1.andThen(l2)
            val svc = stack.layer(serviceFn { req: String -> RamaResult.ok(req) })

            val res = svc.serve("hello")
            assertTrue(res.isSuccess())
            assertEquals("prefix-HELLO", res.getOrNull())
        }

    @Test
    fun testOptionalLayer() =
        runSync {
            val nullLayer: Layer<Service<String, String, Nothing>, Service<String, String, Nothing>>? = null
            val svc = nullLayer.layerOptional(serviceFn { req: String -> RamaResult.ok(req) })
            val res = svc.serve("hello")
            assertEquals("hello", res.getOrNull())
        }
}
