// port-lint: source rama-core/src/telemetry/opentelemetry/attributes.rs
package io.github.kotlinmania.ramacore.telemetry.opentelemetry

import io.github.kotlinmania.ramacore.Extensions

/**
 * A key-value pair representing a telemetry attribute.
 */
public data class KeyValue(
    public val key: String,
    public val value: String,
) {
    public companion object {
        public fun of(key: String, value: String): KeyValue = KeyValue(key, value)
    }
}

/**
 * Interface that can be used to implement your own attributes creator.
 * It is used by layers as a starting point for attributes, and they will add their own attributes on top.
 */
public fun interface AttributesFactory {
    /**
     * Create an attributes list with the given [sizeHint] and [ext] context.
     */
    public fun attributes(sizeHint: Int, ext: Extensions): List<KeyValue>

    public companion object {
        public val EMPTY: AttributesFactory = AttributesFactory { _, _ -> emptyList() }

        public fun of(attributes: List<KeyValue>): AttributesFactory =
            AttributesFactory { _, _ -> attributes }

        public fun from(fn: (Int, Extensions) -> List<KeyValue>): AttributesFactory =
            AttributesFactory { sizeHint, ext -> fn(sizeHint, ext) }
    }
}
