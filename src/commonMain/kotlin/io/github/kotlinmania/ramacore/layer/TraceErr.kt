// port-lint: source layer/trace_err.rs
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
 * Service that traces / logs errors of the inner [Service].
 */
public class TraceErr<Input, Output : Any, Error : Any, S : Service<Input, Output, Error>>(
    public val inner: S,
    public val level: TraceLevel = TraceLevel.ERROR,
    public val onErr: ((Error, TraceLevel) -> Unit)? = null,
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
        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> new(
            inner: S,
            level: TraceLevel = TraceLevel.ERROR,
            onErr: ((Error, TraceLevel) -> Unit)? = null,
        ): TraceErr<Input, Output, Error, S> = TraceErr(inner, level, onErr)
    }
}

/**
 * [Layer] that produces [TraceErr] services.
 */
public class TraceErrLayer<Input, Output : Any, Error : Any, S : Service<Input, Output, Error>>(
    public val level: TraceLevel = TraceLevel.ERROR,
    public val onErr: ((Error, TraceLevel) -> Unit)? = null,
) : Layer<S, TraceErr<Input, Output, Error, S>> {
    override fun layer(inner: S): TraceErr<Input, Output, Error, S> =
        TraceErr(inner, level, onErr)

    override fun toString(): String = "TraceErrLayer($level)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> new(
            level: TraceLevel = TraceLevel.ERROR,
            onErr: ((Error, TraceLevel) -> Unit)? = null,
        ): TraceErrLayer<Input, Output, Error, S> = TraceErrLayer(level, onErr)
    }
}
