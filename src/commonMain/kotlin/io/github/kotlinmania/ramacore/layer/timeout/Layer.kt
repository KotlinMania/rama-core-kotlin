// port-lint: source layer/timeout/layer.rs
package io.github.kotlinmania.ramacore.layer.timeout

import io.github.kotlinmania.ramacore.layer.Layer
import io.github.kotlinmania.ramacore.layer.LayerErrorFn
import io.github.kotlinmania.ramacore.layer.LayerErrorStatic
import io.github.kotlinmania.ramacore.layer.MakeLayerError
import io.github.kotlinmania.ramacore.service.Service
import kotlin.time.Duration

/**
 * Applies a timeout to requests via the supplied inner service.
 */
public class TimeoutLayer<Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>>(
    public val timeout: Duration?,
    public val intoError: MakeLayerError<OutError>,
    public val errorMapper: (InError) -> OutError,
) : Layer<S, Timeout<Input, Output, InError, OutError, S>> {
    override fun layer(inner: S): Timeout<Input, Output, InError, OutError, S> =
        Timeout(inner, timeout, intoError, errorMapper)

    override fun toString(): String = "TimeoutLayer(timeout=$timeout)"

    public companion object {
        public fun <Input, Output : Any, S : Service<Input, Output, Elapsed>> new(
            timeout: Duration,
        ): TimeoutLayer<Input, Output, Elapsed, Elapsed, S> =
            TimeoutLayer(
                timeout = timeout,
                intoError = LayerErrorStatic(Elapsed.new(timeout)),
                errorMapper = { it },
            )

        public fun <Input, Output : Any, S : Service<Input, Output, Elapsed>> never(): TimeoutLayer<Input, Output, Elapsed, Elapsed, S> =
            TimeoutLayer(
                timeout = null,
                intoError = LayerErrorStatic(Elapsed.new(null)),
                errorMapper = { it },
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> withError(
            timeout: Duration,
            error: OutError,
            errorMapper: (InError) -> OutError,
        ): TimeoutLayer<Input, Output, InError, OutError, S> =
            TimeoutLayer(
                timeout = timeout,
                intoError = LayerErrorStatic(error),
                errorMapper = errorMapper,
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> withErrorFn(
            timeout: Duration,
            errorFn: () -> OutError,
            errorMapper: (InError) -> OutError,
        ): TimeoutLayer<Input, Output, InError, OutError, S> =
            TimeoutLayer(
                timeout = timeout,
                intoError = LayerErrorFn(errorFn),
                errorMapper = errorMapper,
            )
    }
}
