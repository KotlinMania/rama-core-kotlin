// port-lint: tests rama-core/src/telemetry/opentelemetry/attributes.rs
// port-lint: tests rama-core/src/telemetry/tracing.rs
package io.github.kotlinmania.ramacore.telemetry

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.AttributesFactory
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.KeyValue
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.MeterOptions
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.ServiceInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TelemetryTest {
    @Test
    fun testAttributesFactory() {
        val empty = AttributesFactory.EMPTY
        assertEquals(emptyList(), empty.attributes(10, Extensions()))

        val fixed = AttributesFactory.of(listOf(KeyValue("service.name", "test")))
        val attrs = fixed.attributes(5, Extensions())
        assertEquals(1, attrs.size)
        assertEquals("service.name", attrs[0].key)
        assertEquals("test", attrs[0].value)
    }

    @Test
    fun testMeterOptions() {
        val options =
            MeterOptions(
                service = ServiceInfo("my-service", "1.0.0"),
                attributes = listOf(KeyValue("env", "prod")),
                metricPrefix = "rama",
            )
        assertEquals("my-service", options.service?.name)
        assertEquals("1.0.0", options.service?.version)
        assertEquals("rama", options.metricPrefix)
        assertEquals(1, options.attributes?.size)
    }

    @Test
    fun testTracingSpans() {
        val root = rootSpan(TracingLevel.INFO, "server.request", "http")
        assertEquals("server.request", root.name)
        assertEquals(TracingLevel.INFO, root.level)
        assertNull(root.parent)

        val child = traceSpan("parse.body").record("content_length", "1024")
        assertEquals("parse.body", child.name)
        assertEquals(TracingLevel.TRACE, child.level)
        assertEquals("1024", child.fields["content_length"])

        val linked = child.followsFrom(root)
        assertEquals("server.request", linked.fields["follows_from"])
    }
}
