// port-lint: source svc_input.rs
package io.github.kotlinmania.ramacore

/**
 * A generic service input that pairs an underlying [input] with an [Extensions] store.
 */
public class ServiceInput<T>(
    public var input: T,
    public val extensions: Extensions = Extensions(),
) : ExtensionsMut {
    override fun extensions(): Extensions = extensions

    override fun extensionsMut(): Extensions = extensions

    override fun toString(): String = "ServiceInput(input=$input, extensions=$extensions)"

    override fun equals(other: Any?): Boolean =
        other is ServiceInput<*> && other.input == input && other.extensions == extensions

    override fun hashCode(): Int = 31 * (input?.hashCode() ?: 0) + extensions.hashCode()

    public companion object {
        public fun <T> new(input: T): ServiceInput<T> = ServiceInput(input)
    }
}
