// port-lint: source layer/timeout/layer.rs
package io.github.kotlinmania.ramacore.layer.timeout

import io.github.kotlinmania.ramacore.layer.Layer
import io.github.kotlinmania.ramacore.layer.LayerErrorFn
import io.github.kotlinmania.ramacore.layer.LayerErrorStatic
import io.github.kotlinmania.ramacore.layer.MakeLayerError
import io.github.kotlinmania.ramacore.layer.MapErrTransform
import io.github.kotlinmania.ramacore.service.Service
import kotlin.time.Duration

/**
 * Applies a timeout to requests via the supplied inner service.
 */
public class TimeoutLayer<Input, Output : Any, InError : Any, OutError : Any>(
    public val timeout: Duration?,
    public val intoError: MakeLayerError<OutError>,
    public val errorMapper: MapErrTransform<InError, OutError>,
) : Layer<Service<Input, Output, InError>, Timeout<Input, Output, InError, OutError>> {
    override fun layer(inner: Service<Input, Output, InError>): Timeout<Input, Output, InError, OutError> =
        Timeout(inner, timeout, intoError, errorMapper)

    override fun toString(): String = "TimeoutLayer(timeout=$timeout)"

    public companion object {
        public fun <Input, Output : Any> new(
            timeout: Duration,
        ): TimeoutLayer<Input, Output, Elapsed, Elapsed> =
            TimeoutLayer(
                timeout = timeout,
                intoError = LayerErrorStatic(Elapsed.new(timeout)),
                errorMapper = io.github.kotlinmania.ramacore.layer.IdentityMapErrTransform(),
            )

        public fun <Input, Output : Any> never(): TimeoutLayer<Input, Output, Elapsed, Elapsed> =
            TimeoutLayer(
                timeout = null,
                intoError = LayerErrorStatic(Elapsed.new(null)),
                errorMapper = io.github.kotlinmania.ramacore.layer.IdentityMapErrTransform(),
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any> withError(
            timeout: Duration,
            error: OutError,
            errorMapper: MapErrTransform<InError, OutError>,
        ): TimeoutLayer<Input, Output, InError, OutError> =
            TimeoutLayer(
                timeout = timeout,
                intoError = LayerErrorStatic(error),
                errorMapper = errorMapper,
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any> withErrorFn(
            timeout: Duration,
            errorFn: MakeLayerError<OutError>,
            errorMapper: MapErrTransform<InError, OutError>,
        ): TimeoutLayer<Input, Output, InError, OutError> =
            TimeoutLayer(
                timeout = timeout,
                intoError = errorFn,
                errorMapper = errorMapper,
            )
    }
}
