// port-lint: source layer/map_input.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Composes a function in front of the service.
 */
public class MapInput<InInput, OutInput, Output : Any, Error : Any, S : Service<OutInput, Output, Error>>(
    public val inner: S,
    public val transform: (InInput) -> OutInput,
) : Service<InInput, Output, Error> {
    override suspend fun serve(input: InInput): RamaResult<Output, Error> =
        inner.serve(transform(input))

    override fun toString(): String = "MapInput($inner)"

    public companion object {
        public fun <InInput, OutInput, Output : Any, Error : Any, S : Service<OutInput, Output, Error>> new(
            inner: S,
            transform: (InInput) -> OutInput,
        ): MapInput<InInput, OutInput, Output, Error, S> = MapInput(inner, transform)
    }
}

/**
 * A [Layer] that produces [MapInput] services.
 */
public class MapInputLayer<InInput, OutInput, Output : Any, Error : Any, S : Service<OutInput, Output, Error>>(
    public val transform: (InInput) -> OutInput,
) : Layer<S, MapInput<InInput, OutInput, Output, Error, S>> {
    override fun layer(inner: S): MapInput<InInput, OutInput, Output, Error, S> =
        MapInput(inner, transform)

    override fun toString(): String = "MapInputLayer"

    public companion object {
        public fun <InInput, OutInput, Output : Any, Error : Any, S : Service<OutInput, Output, Error>> new(
            transform: (InInput) -> OutInput,
        ): MapInputLayer<InInput, OutInput, Output, Error, S> = MapInputLayer(transform)
    }
}
