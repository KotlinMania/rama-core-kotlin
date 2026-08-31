// port-lint: source rama-core/src/layer/trace_err.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

public enum class TraceLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

/**
 * Callback for handling errors in [TraceErr].
 */
public interface TraceErrCallback<in Error> {
    public operator fun invoke(error: Error, level: TraceLevel)
}

/**
 * Service that traces / logs errors of the inner [Service].
 */
public class TraceErr<Input, Output : Any, Error : Any>(
    public val inner: Service<Input, Output, Error>,
    public val level: TraceLevel = TraceLevel.ERROR,
    private val onErr: TraceErrCallback<Error>? = null,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        val res = inner.serve(input)
        if (res.isFailure()) {
            onErr?.invoke(res.error!!, level)
        }
        return res
    }

    override fun toString(): String = "TraceErr($inner, $level)"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            inner: Service<Input, Output, Error>,
            level: TraceLevel = TraceLevel.ERROR,
            onErr: TraceErrCallback<Error>? = null,
        ): TraceErr<Input, Output, Error> = TraceErr(inner, level, onErr)

        public inline fun <Input, Output : Any, Error : Any> of(
            inner: Service<Input, Output, Error>,
            level: TraceLevel = TraceLevel.ERROR,
            crossinline onErr: (Error, TraceLevel) -> Unit,
        ): TraceErr<Input, Output, Error> =
            TraceErr(
                inner,
                level,
                object : TraceErrCallback<Error> {
                    override fun invoke(error: Error, level: TraceLevel) = onErr(error, level)
                },
            )
    }
}

/**
 * [Layer] that produces [TraceErr] services.
 */
public class TraceErrLayer<Input, Output : Any, Error : Any>(
    public val level: TraceLevel = TraceLevel.ERROR,
    private val onErr: TraceErrCallback<Error>? = null,
) : Layer<Service<Input, Output, Error>, TraceErr<Input, Output, Error>> {
    override fun layer(inner: Service<Input, Output, Error>): TraceErr<Input, Output, Error> =
        TraceErr(inner, level, onErr)

    override fun toString(): String = "TraceErrLayer($level)"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            level: TraceLevel = TraceLevel.ERROR,
            onErr: TraceErrCallback<Error>? = null,
        ): TraceErrLayer<Input, Output, Error> = TraceErrLayer(level, onErr)

        public inline fun <Input, Output : Any, Error : Any> of(
            level: TraceLevel = TraceLevel.ERROR,
            crossinline onErr: (Error, TraceLevel) -> Unit,
        ): TraceErrLayer<Input, Output, Error> =
            TraceErrLayer(
                level,
                object : TraceErrCallback<Error> {
                    override fun invoke(error: Error, level: TraceLevel) = onErr(error, level)
                },
            )
    }
}
