// port-lint: source layer/map_result.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Transform function for mapping results in [MapResult].
 */
public interface MapResultTransform<InOutput : Any, OutOutput : Any, InError : Any, OutError : Any> {
    public operator fun invoke(result: RamaResult<InOutput, InError>): RamaResult<OutOutput, OutError>
}

/**
 * Maps this service's result type to a different result.
 */
public class MapResult<Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any>(
    public val inner: Service<Input, InOutput, InError>,
    private val transform: MapResultTransform<InOutput, OutOutput, InError, OutError>,
) : Service<Input, OutOutput, OutError> {
    override suspend fun serve(input: Input): RamaResult<OutOutput, OutError> {
        val result = inner.serve(input)
        return transform(result)
    }

    override fun toString(): String = "MapResult($inner)"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any> new(
            inner: Service<Input, InOutput, InError>,
            transform: MapResultTransform<InOutput, OutOutput, InError, OutError>,
        ): MapResult<Input, InOutput, OutOutput, InError, OutError> = MapResult(inner, transform)

        public inline fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any> of(
            inner: Service<Input, InOutput, InError>,
            crossinline transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
        ): MapResult<Input, InOutput, OutOutput, InError, OutError> =
            MapResult(
                inner,
                object : MapResultTransform<InOutput, OutOutput, InError, OutError> {
                    override fun invoke(result: RamaResult<InOutput, InError>): RamaResult<OutOutput, OutError> =
                        transform(result)
                },
            )
    }
}

/**
 * A [Layer] that produces a [MapResult] service.
 */
public class MapResultLayer<Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any>(
    private val transform: MapResultTransform<InOutput, OutOutput, InError, OutError>,
) : Layer<Service<Input, InOutput, InError>, MapResult<Input, InOutput, OutOutput, InError, OutError>> {
    override fun layer(inner: Service<Input, InOutput, InError>): MapResult<Input, InOutput, OutOutput, InError, OutError> =
        MapResult(inner, transform)

    override fun toString(): String = "MapResultLayer"

    public companion object {
        public fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any> new(
            transform: MapResultTransform<InOutput, OutOutput, InError, OutError>,
        ): MapResultLayer<Input, InOutput, OutOutput, InError, OutError> = MapResultLayer(transform)

        public inline fun <Input, InOutput : Any, OutOutput : Any, InError : Any, OutError : Any> of(
            crossinline transform: (RamaResult<InOutput, InError>) -> RamaResult<OutOutput, OutError>,
        ): MapResultLayer<Input, InOutput, OutOutput, InError, OutError> =
            MapResultLayer(
                object : MapResultTransform<InOutput, OutOutput, InError, OutError> {
                    override fun invoke(result: RamaResult<InOutput, InError>): RamaResult<OutOutput, OutError> =
                        transform(result)
                },
            )
    }
}
