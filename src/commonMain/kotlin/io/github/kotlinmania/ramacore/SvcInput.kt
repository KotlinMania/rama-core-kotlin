// port-lint: source svc_input.rs
package io.github.kotlinmania.ramacore

/**
 * A generic service input that pairs an underlying [input] with an [Extensions] store.
 */
public class ServiceInput<T>(
    public var input: T,
    private val extStore: Extensions = Extensions(),
) : ExtensionsMut {
    override fun extensions(): Extensions = extStore

    override fun extensionsMut(): Extensions = extStore

    override fun toString(): String = "ServiceInput(input=$input, extensions=$extStore)"

    override fun equals(other: Any?): Boolean =
        other is ServiceInput<*> && other.input == input && other.extensions() == extStore

    override fun hashCode(): Int = 31 * (input?.hashCode() ?: 0) + extStore.hashCode()

    public companion object {
        public fun <T> new(input: T): ServiceInput<T> = ServiceInput(input)
    }
}
