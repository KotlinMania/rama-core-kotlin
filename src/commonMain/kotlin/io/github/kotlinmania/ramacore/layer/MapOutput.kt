// port-lint: source layer/map_output.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.map
import io.github.kotlinmania.ramacore.service.Service

/**
 * Transform function for mapping output values in [MapOutput].
 */
public interface MapOutputTransform<in InOutput : Any, out OutOutput : Any> {
    public operator fun invoke(output: InOutput): OutOutput
}

/**
 * Maps this service's output value to a different value.
 */
public class MapOutput<Input, InOutput : Any, OutOutput : Any, Error : Any>(
    public val inner: Service<Input, InOutput, Error>,
    private val transform: MapOutputTransform<InOutput, OutOutput>,
) : Service<Input, OutOutput, Error> {
    override suspend fun serve(input: Input): RamaResult<OutOutput, Error> {
        val res = inner.serve(input)
        return res.map { transform(it) }
    }

    override fun toString(): String = "MapOutput($inner)"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, Error : Any> new(
            inner: Service<Input, InOutput, Error>,
            transform: MapOutputTransform<InOutput, OutOutput>,
        ): MapOutput<Input, InOutput, OutOutput, Error> = MapOutput(inner, transform)

        public inline fun <Input, InOutput : Any, OutOutput : Any, Error : Any> of(
            inner: Service<Input, InOutput, Error>,
            crossinline transform: (InOutput) -> OutOutput,
        ): MapOutput<Input, InOutput, OutOutput, Error> =
            MapOutput(
                inner,
                object : MapOutputTransform<InOutput, OutOutput> {
                    override fun invoke(output: InOutput): OutOutput = transform(output)
                },
            )
    }
}

/**
 * A [Layer] that produces a [MapOutput] service.
 */
public class MapOutputLayer<Input, InOutput : Any, OutOutput : Any, Error : Any>(
    private val transform: MapOutputTransform<InOutput, OutOutput>,
) : Layer<Service<Input, InOutput, Error>, MapOutput<Input, InOutput, OutOutput, Error>> {
    override fun layer(inner: Service<Input, InOutput, Error>): MapOutput<Input, InOutput, OutOutput, Error> =
        MapOutput(inner, transform)

    override fun toString(): String = "MapOutputLayer"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, Error : Any> new(
            transform: MapOutputTransform<InOutput, OutOutput>,
        ): MapOutputLayer<Input, InOutput, OutOutput, Error> = MapOutputLayer(transform)

        public inline fun <Input, InOutput : Any, OutOutput : Any, Error : Any> of(
            crossinline transform: (InOutput) -> OutOutput,
        ): MapOutputLayer<Input, InOutput, OutOutput, Error> =
            MapOutputLayer(
                object : MapOutputTransform<InOutput, OutOutput> {
                    override fun invoke(output: InOutput): OutOutput = transform(output)
                },
            )
    }
}
