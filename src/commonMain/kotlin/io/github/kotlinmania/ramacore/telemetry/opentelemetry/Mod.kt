// port-lint: source rama-core/src/telemetry/opentelemetry/mod.rs
package io.github.kotlinmania.ramacore.telemetry.opentelemetry

/**
 * Information about the service producing the metrics.
 */
public data class ServiceInfo(
    public val name: String,
    public val version: String,
)

/**
 * Options that can be used to customize a meter (middleware) provided by rama.
 */
public data class MeterOptions(
    public val service: ServiceInfo? = null,
    public val attributes: List<KeyValue>? = null,
    public val metricPrefix: String? = null,
) {
    public companion object {
        public val DEFAULT: MeterOptions = MeterOptions()
    }
}
