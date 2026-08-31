// port-lint: source layer/into_error.rs
package io.github.kotlinmania.ramacore.layer

/**
 * Factory for creating layer-specific errors.
 */
public fun interface MakeLayerError<out E : Any> {
    /**
     * Create a new error value.
     */
    public fun makeLayerError(): E
}

/**
 * A [MakeLayerError] implementation that invokes a lambda to produce a new error.
 */
public class LayerErrorFn<out E : Any>(
    private val fn: () -> E,
) : MakeLayerError<E> {
    override fun makeLayerError(): E = fn()

    override fun toString(): String = "LayerErrorFn"

    public companion object {
        public fun <E : Any> new(fn: () -> E): LayerErrorFn<E> = LayerErrorFn(fn)
    }
}

/**
 * A [MakeLayerError] implementation that always returns a static error value.
 */
public class LayerErrorStatic<out E : Any>(
    private val error: E,
) : MakeLayerError<E> {
    override fun makeLayerError(): E = error

    override fun toString(): String = "LayerErrorStatic($error)"

    public companion object {
        public fun <E : Any> new(error: E): LayerErrorStatic<E> = LayerErrorStatic(error)
    }
}
