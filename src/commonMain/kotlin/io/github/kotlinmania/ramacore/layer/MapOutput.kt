// port-lint: source layer/map_output.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.map
import io.github.kotlinmania.ramacore.service.Service

/**
 * Maps this service's output value to a different value.
 */
public class MapOutput<Input, InOutput : Any, OutOutput : Any, Error : Any, S : Service<Input, InOutput, Error>>(
    public val inner: S,
    public val transform: (InOutput) -> OutOutput,
) : Service<Input, OutOutput, Error> {
    override suspend fun serve(input: Input): RamaResult<OutOutput, Error> {
        val res = inner.serve(input)
        return res.map(transform)
    }

    override fun toString(): String = "MapOutput($inner)"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, Error : Any, S : Service<Input, InOutput, Error>> new(
            inner: S,
            transform: (InOutput) -> OutOutput,
        ): MapOutput<Input, InOutput, OutOutput, Error, S> = MapOutput(inner, transform)
    }
}

/**
 * A [Layer] that produces a [MapOutput] service.
 */
public class MapOutputLayer<Input, InOutput : Any, OutOutput : Any, Error : Any, S : Service<Input, InOutput, Error>>(
    public val transform: (InOutput) -> OutOutput,
) : Layer<S, MapOutput<Input, InOutput, OutOutput, Error, S>> {
    override fun layer(inner: S): MapOutput<Input, InOutput, OutOutput, Error, S> =
        MapOutput(inner, transform)

    override fun toString(): String = "MapOutputLayer"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, Error : Any, S : Service<Input, InOutput, Error>> new(
            transform: (InOutput) -> OutOutput,
        ): MapOutputLayer<Input, InOutput, OutOutput, Error, S> = MapOutputLayer(transform)
    }
}
