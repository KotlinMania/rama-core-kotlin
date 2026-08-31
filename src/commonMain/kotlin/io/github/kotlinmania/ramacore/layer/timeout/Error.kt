// port-lint: source rama-core/src/layer/timeout/error.rs
package io.github.kotlinmania.ramacore.layer.timeout

import kotlin.time.Duration

/**
 * Default error indicating that a timeout elapsed.
 */
public class Elapsed(
    public val duration: Duration? = null,
) {
    override fun toString(): String =
        if (duration != null) {
            "timeout elapsed after $duration"
        } else {
            "timeout without duration"
        }

    override fun equals(other: Any?): Boolean =
        other is Elapsed && other.duration == duration

    override fun hashCode(): Int = duration.hashCode()

    public companion object {
        public fun new(duration: Duration? = null): Elapsed = Elapsed(duration)
    }
}
