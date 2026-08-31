// port-lint: source rama-core/src/telemetry/mod.rs
package io.github.kotlinmania.ramacore.telemetry

import io.github.kotlinmania.ramacore.telemetry.opentelemetry.AttributesFactory
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.KeyValue
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.MeterOptions
import io.github.kotlinmania.ramacore.telemetry.opentelemetry.ServiceInfo

/**
 * Re-exports and top-level definitions for rama-core telemetry.
 */
public typealias TelemetryKeyValue = KeyValue
public typealias TelemetryAttributesFactory = AttributesFactory
public typealias TelemetryMeterOptions = MeterOptions
public typealias TelemetryServiceInfo = ServiceInfo
