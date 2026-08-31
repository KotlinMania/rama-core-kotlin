// port-lint: source rama-core/src/telemetry/tracing.rs
package io.github.kotlinmania.ramacore.telemetry

/**
 * Tracing level.
 */
public enum class TracingLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

/**
 * A tracing span representation for rama-core.
 */
public data class TracingSpan(
    public val name: String,
    public val level: TracingLevel = TracingLevel.INFO,
    public val target: String = "",
    public val parent: TracingSpan? = null,
    public val fields: Map<String, String> = emptyMap(),
) {
    /**
     * Records an attribute field onto this span.
     */
    public fun record(key: String, value: String): TracingSpan =
        copy(fields = fields + (key to value))

    /**
     * Associates a follows-from relationship with another span.
     */
    public fun followsFrom(span: TracingSpan): TracingSpan =
        copy(fields = fields + ("follows_from" to span.name))

    public companion object {
        public fun new(name: String, level: TracingLevel = TracingLevel.INFO): TracingSpan =
            TracingSpan(name = name, level = level)
    }
}

/**
 * Creates a new span with the given [level], [name], and optional [target].
 */
public fun span(
    level: TracingLevel,
    name: String,
    target: String = "",
    fields: Map<String, String> = emptyMap(),
): TracingSpan = TracingSpan(name = name, level = level, target = target, fields = fields)

/**
 * Creates a trace-level span.
 */
public fun traceSpan(name: String, target: String = ""): TracingSpan =
    span(TracingLevel.TRACE, name, target)

/**
 * Creates a debug-level span.
 */
public fun debugSpan(name: String, target: String = ""): TracingSpan =
    span(TracingLevel.DEBUG, name, target)

/**
 * Creates an info-level span.
 */
public fun infoSpan(name: String, target: String = ""): TracingSpan =
    span(TracingLevel.INFO, name, target)

/**
 * Creates a root span without a parent.
 */
public fun rootSpan(
    level: TracingLevel,
    name: String,
    target: String = "",
    fields: Map<String, String> = emptyMap(),
): TracingSpan = TracingSpan(name = name, level = level, target = target, parent = null, fields = fields)

/**
 * Creates a trace-level root span.
 */
public fun traceRootSpan(name: String, target: String = ""): TracingSpan =
    rootSpan(TracingLevel.TRACE, name, target)

/**
 * Creates a debug-level root span.
 */
public fun debugRootSpan(name: String, target: String = ""): TracingSpan =
    rootSpan(TracingLevel.DEBUG, name, target)

/**
 * Creates an info-level root span.
 */
public fun infoRootSpan(name: String, target: String = ""): TracingSpan =
    rootSpan(TracingLevel.INFO, name, target)
