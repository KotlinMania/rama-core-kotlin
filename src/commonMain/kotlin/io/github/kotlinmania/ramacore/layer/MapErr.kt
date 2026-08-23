// port-lint: source layer/map_err.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.mapErr
import io.github.kotlinmania.ramacore.service.Service

/**
 * Maps this service's error value to a different value.
 */
public class MapErr<Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>>(
    public val inner: S,
    public val transform: (InError) -> OutError,
) : Service<Input, Output, OutError> {
    override suspend fun serve(input: Input): RamaResult<Output, OutError> {
        val res = inner.serve(input)
        return res.mapErr(transform)
    }

    override fun toString(): String = "MapErr($inner)"

    public companion object {
        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> new(
            inner: S,
            transform: (InError) -> OutError,
        ): MapErr<Input, Output, InError, OutError, S> = MapErr(inner, transform)
    }
}

/**
 * A [Layer] that produces [MapErr] services.
 */
public class MapErrLayer<Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>>(
    public val transform: (InError) -> OutError,
) : Layer<S, MapErr<Input, Output, InError, OutError, S>> {
    override fun layer(inner: S): MapErr<Input, Output, InError, OutError, S> =
        MapErr(inner, transform)

    override fun toString(): String = "MapErrLayer"

    public companion object {
        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> new(
            transform: (InError) -> OutError,
        ): MapErrLayer<Input, Output, InError, OutError, S> = MapErrLayer(transform)
    }
}
