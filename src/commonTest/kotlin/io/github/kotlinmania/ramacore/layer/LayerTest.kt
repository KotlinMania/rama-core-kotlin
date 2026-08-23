// port-lint: tests layer/mod.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.timeout.Elapsed
import io.github.kotlinmania.ramacore.layer.timeout.Timeout
import io.github.kotlinmania.ramacore.layer.timeout.TimeoutLayer
import io.github.kotlinmania.ramacore.matcher.Matcher
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramacore.service.serviceFn
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

private class TestContext(
    val id: String,
    private val ext: Extensions = Extensions(),
) : ExtensionsMut {
    override fun extensions(): Extensions = ext

    override fun extensionsMut(): Extensions = ext
}

data class Counter(
    val count: Int,
)

class LayerTest {
    @Test
    fun testMapInput() =
        runTest {
            val baseSvc =
                serviceFn<Int, String, String> { input ->
                    RamaResult.ok("Result: $input")
                }
            val mappedSvc = MapInput.new(baseSvc) { str: String -> str.toInt() }

            val res = mappedSvc.serve("42")
            assertTrue(res.isSuccess())
            assertEquals("Result: 42", res.value)
        }

    @Test
    fun testMapOutput() =
        runTest {
            val baseSvc =
                serviceFn<Int, Int, String> { input ->
                    RamaResult.ok(input * 2)
                }
            val mappedSvc = MapOutput.new(baseSvc) { num: Int -> "Doubled: $num" }

            val res = mappedSvc.serve(21)
            assertTrue(res.isSuccess())
            assertEquals("Doubled: 42", res.value)
        }

    @Test
    fun testMapErr() =
        runTest {
            val baseSvc =
                serviceFn<Int, String, Int> { input ->
                    if (input == 0) RamaResult.err(404) else RamaResult.ok("OK")
                }
            val mappedSvc = MapErr.new(baseSvc) { code: Int -> "Error code: $code" }

            val res = mappedSvc.serve(0)
            assertTrue(res.isFailure())
            assertEquals("Error code: 404", res.error)
        }

    @Test
    fun testMapResult() =
        runTest {
            val baseSvc =
                serviceFn<Int, String, String> { input ->
                    if (input > 0) RamaResult.ok("Positive") else RamaResult.err("Non-positive")
                }
            val mappedSvc =
                MapResult.new(baseSvc) { res ->
                    if (res.isSuccess()) {
                        RamaResult.ok("Success: ${res.value}")
                    } else {
                        RamaResult.ok("Recovered: ${res.error}")
                    }
                }

            val res1 = mappedSvc.serve(5)
            assertEquals("Success: Positive", res1.value)
            val res2 = mappedSvc.serve(-1)
            assertEquals("Recovered: Non-positive", res2.value)
        }

    @Test
    fun testAddInputExtension() =
        runTest {
            val svc =
                AddInputExtensionLayer.new(Counter(42)).intoLayer(
                    serviceFn<TestContext, String, String> { ctx ->
                        val counter = ctx.extensions().get<Counter>()
                        assertNotNull(counter)
                        assertEquals(42, counter.count)
                        RamaResult.ok("Success")
                    },
                )

            val ctx = TestContext("test")
            val res = svc.serve(ctx)
            assertTrue(res.isSuccess())
        }

    @Test
    fun testAddOutputExtension() =
        runTest {
            val svc =
                AddOutputExtensionLayer.new(Counter(99)).intoLayer(
                    serviceFn<String, TestContext, String> { id ->
                        RamaResult.ok(TestContext(id))
                    },
                )

            val res = svc.serve("req1")
            assertTrue(res.isSuccess())
            val counter = res.value!!.extensions().get<Counter>()
            assertNotNull(counter)
            assertEquals(99, counter.count)
        }

    @Test
    fun testGetInputExtension() =
        runTest {
            var observedCount = 0
            val svc =
                GetInputExtensionLayer
                    .new<Counter> { counter ->
                        observedCount = counter.count
                    }.intoLayer(
                        serviceFn<TestContext, String, String> {
                            RamaResult.ok("Done")
                        },
                    )

            val ctx = TestContext("test")
            ctx.extensionsMut().insert(Counter(77))
            svc.serve(ctx)
            assertEquals(77, observedCount)
        }

    @Test
    fun testHijackService() =
        runTest {
            val normalSvc =
                serviceFn<TestContext, String, String> {
                    RamaResult.ok("Normal")
                }
            val hijackSvc =
                serviceFn<TestContext, String, String> {
                    RamaResult.ok("Hijacked")
                }
            val matcher = Matcher<TestContext> { _, ctx -> ctx.id == "admin" }

            val layered = HijackService.new(normalSvc, hijackSvc, matcher)

            val res1 = layered.serve(TestContext("user"))
            assertEquals("Normal", res1.value)

            val res2 = layered.serve(TestContext("admin"))
            assertEquals("Hijacked", res2.value)
        }

    @Test
    fun testConsumeErr() =
        runTest {
            var errConsumed: String? = null
            val failingSvc =
                serviceFn<Int, String, String> {
                    RamaResult.err("Boom")
                }
            val consumedSvc =
                ConsumeErr.new(
                    inner = failingSvc,
                    fallback = { "Fallback Output" },
                    consumer = { err -> errConsumed = err },
                )

            val res = consumedSvc.serve(1)
            assertTrue(res.isSuccess())
            assertEquals("Fallback Output", res.value)
            assertEquals("Boom", errConsumed)
        }

    @Test
    fun testTimeoutSuccess() =
        runTest {
            val slowSvc =
                serviceFn<Int, String, Elapsed> {
                    RamaResult.ok("Quick Result")
                }
            val timeoutSvc = TimeoutLayer.new<Int, String, Service<Int, String, Elapsed>>(500.milliseconds).layer(slowSvc)

            val res = timeoutSvc.serve(1)
            assertTrue(res.isSuccess())
            assertEquals("Quick Result", res.value)
        }

    @Test
    fun testTimeoutElapse() =
        runTest {
            val slowSvc =
                serviceFn<Int, String, Elapsed> {
                    delay(200.milliseconds)
                    RamaResult.ok("Slow Result")
                }
            val timeoutSvc = Timeout.new(slowSvc, 20.milliseconds)

            val res = timeoutSvc.serve(1)
            assertTrue(res.isFailure())
        }
}
