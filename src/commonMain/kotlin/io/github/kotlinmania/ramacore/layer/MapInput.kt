// port-lint: source layer/map_input.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Transform function for mapping input values in [MapInput].
 */
public interface MapInputTransform<in InInput, out OutInput> {
    public operator fun invoke(input: InInput): OutInput
}

/**
 * Composes a function in front of the service.
 */
public class MapInput<InInput, OutInput, Output : Any, Error : Any>(
    public val inner: Service<OutInput, Output, Error>,
    private val transform: MapInputTransform<InInput, OutInput>,
) : Service<InInput, Output, Error> {
    override suspend fun serve(input: InInput): RamaResult<Output, Error> =
        inner.serve(transform(input))

    override fun toString(): String = "MapInput($inner)"

    public companion object {
        public fun <InInput, OutInput, Output : Any, Error : Any> new(
            inner: Service<OutInput, Output, Error>,
            transform: MapInputTransform<InInput, OutInput>,
        ): MapInput<InInput, OutInput, Output, Error> = MapInput(inner, transform)

        public inline fun <InInput, OutInput, Output : Any, Error : Any> of(
            inner: Service<OutInput, Output, Error>,
            crossinline transform: (InInput) -> OutInput,
        ): MapInput<InInput, OutInput, Output, Error> =
            MapInput(
                inner,
                object : MapInputTransform<InInput, OutInput> {
                    override fun invoke(input: InInput): OutInput = transform(input)
                },
            )
    }
}

/**
 * A [Layer] that produces [MapInput] services.
 */
public class MapInputLayer<InInput, OutInput, Output : Any, Error : Any>(
    private val transform: MapInputTransform<InInput, OutInput>,
) : Layer<Service<OutInput, Output, Error>, MapInput<InInput, OutInput, Output, Error>> {
    override fun layer(inner: Service<OutInput, Output, Error>): MapInput<InInput, OutInput, Output, Error> =
        MapInput(inner, transform)

    override fun toString(): String = "MapInputLayer"

    public companion object {
        public fun <InInput, OutInput, Output : Any, Error : Any> new(
            transform: MapInputTransform<InInput, OutInput>,
        ): MapInputLayer<InInput, OutInput, Output, Error> = MapInputLayer(transform)

        public inline fun <InInput, OutInput, Output : Any, Error : Any> of(
            crossinline transform: (InInput) -> OutInput,
        ): MapInputLayer<InInput, OutInput, Output, Error> =
            MapInputLayer(
                object : MapInputTransform<InInput, OutInput> {
                    override fun invoke(input: InInput): OutInput = transform(input)
                },
            )
    }
}
