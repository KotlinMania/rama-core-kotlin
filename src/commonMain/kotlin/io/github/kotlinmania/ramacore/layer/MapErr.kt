// port-lint: source rama-core/src/layer/map_err.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.mapErr
import io.github.kotlinmania.ramacore.service.Service

/**
 * Transform function for mapping error values in [MapErr].
 */
public interface MapErrTransform<in InError : Any, out OutError : Any> {
    public operator fun invoke(error: InError): OutError
}

/**
 * Concrete functional implementation of [MapErrTransform].
 */
public class MapErrTransformFn<InError : Any, OutError : Any>(
    private val fn: (InError) -> OutError,
) : MapErrTransform<InError, OutError> {
    override fun invoke(error: InError): OutError = fn(error)
}

/**
 * Identity transform implementation of [MapErrTransform].
 */
public class IdentityMapErrTransform<Error : Any> : MapErrTransform<Error, Error> {
    override fun invoke(error: Error): Error = error
}

/**
 * Maps this service's error value to a different value.
 */
public class MapErr<Input, Output : Any, InError : Any, OutError : Any>(
    public val inner: Service<Input, Output, InError>,
    private val transform: MapErrTransform<InError, OutError>,
) : Service<Input, Output, OutError> {
    override suspend fun serve(input: Input): RamaResult<Output, OutError> {
        val res = inner.serve(input)
        return res.mapErr { transform(it) }
    }

    override fun toString(): String = "MapErr($inner)"

    public companion object {
        public fun <Input, Output : Any, InError : Any, OutError : Any> new(
            inner: Service<Input, Output, InError>,
            transform: MapErrTransform<InError, OutError>,
        ): MapErr<Input, Output, InError, OutError> = MapErr(inner, transform)

        public inline fun <Input, Output : Any, InError : Any, OutError : Any> of(
            inner: Service<Input, Output, InError>,
            crossinline transform: (InError) -> OutError,
        ): MapErr<Input, Output, InError, OutError> =
            MapErr(
                inner,
                object : MapErrTransform<InError, OutError> {
                    override fun invoke(error: InError): OutError = transform(error)
                },
            )
    }
}

/**
 * A [Layer] that produces [MapErr] services.
 */
public class MapErrLayer<Input, Output : Any, InError : Any, OutError : Any>(
    private val transform: MapErrTransform<InError, OutError>,
) : Layer<Service<Input, Output, InError>, MapErr<Input, Output, InError, OutError>> {
    override fun layer(inner: Service<Input, Output, InError>): MapErr<Input, Output, InError, OutError> =
        MapErr(inner, transform)

    override fun toString(): String = "MapErrLayer"

    public companion object {
        public fun <Input, Output : Any, InError : Any, OutError : Any> new(
            transform: MapErrTransform<InError, OutError>,
        ): MapErrLayer<Input, Output, InError, OutError> = MapErrLayer(transform)

        public inline fun <Input, Output : Any, InError : Any, OutError : Any> of(
            crossinline transform: (InError) -> OutError,
        ): MapErrLayer<Input, Output, InError, OutError> =
            MapErrLayer(
                object : MapErrTransform<InError, OutError> {
                    override fun invoke(error: InError): OutError = transform(error)
                },
            )
    }
}
