// port-lint: source layer/layer_fn.rs
package io.github.kotlinmania.ramacore.layer

/**
 * A [Layer] implemented by a closure.
 */
public class LayerFn<in S, out OutS>(
    private val transform: (S) -> OutS,
) : Layer<S, OutS> {
    override fun layer(inner: S): OutS = transform(inner)

    override fun intoLayer(inner: S): OutS = transform(inner)

    override fun toString(): String = "LayerFn"

    public companion object {
        public fun <S, OutS> new(transform: (S) -> OutS): LayerFn<S, OutS> = LayerFn(transform)
    }
}

/**
 * Returns a new [LayerFn] that implements [Layer] by calling the given function.
 */
public fun <S, OutS> layerFn(transform: (S) -> OutS): LayerFn<S, OutS> = LayerFn(transform)
