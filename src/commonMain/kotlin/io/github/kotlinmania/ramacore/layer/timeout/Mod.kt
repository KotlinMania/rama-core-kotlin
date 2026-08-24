// port-lint: source layer/timeout/mod.rs
package io.github.kotlinmania.ramacore.layer.timeout

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.LayerErrorStatic
import io.github.kotlinmania.ramacore.layer.MakeLayerError
import io.github.kotlinmania.ramacore.layer.MapErrTransform
import io.github.kotlinmania.ramacore.mapErr
import io.github.kotlinmania.ramacore.service.Service
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

/**
 * Applies a timeout to requests.
 */
public class Timeout<Input, Output : Any, InError : Any, OutError : Any>(
    public val inner: Service<Input, Output, InError>,
    public val timeout: Duration?,
    public val intoError: MakeLayerError<OutError>,
    public val errorMapper: MapErrTransform<InError, OutError>,
) : Service<Input, Output, OutError> {
    override suspend fun serve(input: Input): RamaResult<Output, OutError> {
        val dur = timeout
        return if (dur != null) {
            val res =
                withTimeoutOrNull(dur) {
                    inner.serve(input)
                }
            if (res == null) {
                RamaResult.err(intoError.makeLayerError())
            } else {
                res.mapErr { errorMapper(it) }
            }
        } else {
            inner.serve(input).mapErr { errorMapper(it) }
        }
    }

    override fun toString(): String = "Timeout($inner, timeout=$timeout)"

    public companion object {
        public fun <Input, Output : Any> new(
            inner: Service<Input, Output, Elapsed>,
            timeout: Duration,
        ): Timeout<Input, Output, Elapsed, Elapsed> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = LayerErrorStatic(Elapsed.new(timeout)),
                errorMapper =
                    io.github.kotlinmania.ramacore.layer
                        .IdentityMapErrTransform(),
            )

        public fun <Input, Output : Any> never(
            inner: Service<Input, Output, Elapsed>,
        ): Timeout<Input, Output, Elapsed, Elapsed> =
            Timeout(
                inner = inner,
                timeout = null,
                intoError = LayerErrorStatic(Elapsed.new(null)),
                errorMapper =
                    io.github.kotlinmania.ramacore.layer
                        .IdentityMapErrTransform(),
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any> withError(
            inner: Service<Input, Output, InError>,
            timeout: Duration,
            error: OutError,
            errorMapper: MapErrTransform<InError, OutError>,
        ): Timeout<Input, Output, InError, OutError> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = LayerErrorStatic(error),
                errorMapper = errorMapper,
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any> withErrorFn(
            inner: Service<Input, Output, InError>,
            timeout: Duration,
            errorFn: MakeLayerError<OutError>,
            errorMapper: MapErrTransform<InError, OutError>,
        ): Timeout<Input, Output, InError, OutError> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = errorFn,
                errorMapper = errorMapper,
            )
    }
}
