// port-lint: source layer/map_result.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Maps this service's result type to a different result.
 */
public class MapResult<Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any, S : Service<Input, InOutput, InError>>(
    public val inner: S,
    public val transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
) : Service<Input, OutOutput, OutError> {
    override suspend fun serve(input: Input): RamaResult<OutOutput, OutError> {
        val result = inner.serve(input)
        return transform(result)
    }

    override fun toString(): String = "MapResult($inner)"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any, S : Service<Input, InOutput, InError>> new(
            inner: S,
            transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
        ): MapResult<Input, InOutput, OutOutput, InError, OutError, S> = MapResult(inner, transform)
    }
}

/**
 * A [Layer] that produces a [MapResult] service.
 */
public class MapResultLayer<Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any, S : Service<Input, InOutput, InError>>(
    public val transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
) : Layer<S, MapResult<Input, InOutput, OutOutput, InError, OutError, S>> {
    override fun layer(inner: S): MapResult<Input, InOutput, OutOutput, InError, OutError, S> =
        MapResult(inner, transform)

    override fun toString(): String = "MapResultLayer"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any, S : Service<Input, InOutput, InError>> new(
            transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
        ): MapResultLayer<Input, InOutput, OutOutput, InError, OutError, S> = MapResultLayer(transform)
    }
}
