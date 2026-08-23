// port-lint: source layer/timeout/mod.rs
package io.github.kotlinmania.ramacore.layer.timeout

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.LayerErrorFn
import io.github.kotlinmania.ramacore.layer.LayerErrorStatic
import io.github.kotlinmania.ramacore.layer.MakeLayerError
import io.github.kotlinmania.ramacore.mapErr
import io.github.kotlinmania.ramacore.service.Service
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

/**
 * Applies a timeout to requests.
 */
public class Timeout<Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>>(
    public val inner: S,
    public val timeout: Duration?,
    public val intoError: MakeLayerError<OutError>,
    public val errorMapper: (InError) -> OutError,
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
                res.mapErr(errorMapper)
            }
        } else {
            inner.serve(input).mapErr(errorMapper)
        }
    }

    override fun toString(): String = "Timeout($inner, timeout=$timeout)"

    public companion object {
        public fun <Input, Output : Any, S : Service<Input, Output, Elapsed>> new(
            inner: S,
            timeout: Duration,
        ): Timeout<Input, Output, Elapsed, Elapsed, S> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = LayerErrorStatic(Elapsed.new(timeout)),
                errorMapper = { it },
            )

        public fun <Input, Output : Any, S : Service<Input, Output, Elapsed>> never(
            inner: S,
        ): Timeout<Input, Output, Elapsed, Elapsed, S> =
            Timeout(
                inner = inner,
                timeout = null,
                intoError = LayerErrorStatic(Elapsed.new(null)),
                errorMapper = { it },
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> withError(
            inner: S,
            timeout: Duration,
            error: OutError,
            errorMapper: (InError) -> OutError,
        ): Timeout<Input, Output, InError, OutError, S> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = LayerErrorStatic(error),
                errorMapper = errorMapper,
            )

        public fun <Input, Output : Any, InError : Any, OutError : Any, S : Service<Input, Output, InError>> withErrorFn(
            inner: S,
            timeout: Duration,
            errorFn: () -> OutError,
            errorMapper: (InError) -> OutError,
        ): Timeout<Input, Output, InError, OutError, S> =
            Timeout(
                inner = inner,
                timeout = timeout,
                intoError = LayerErrorFn(errorFn),
                errorMapper = errorMapper,
            )
    }
}
