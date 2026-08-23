// port-lint: source layer/consume_err.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Middleware that consumes a service's error value and returns a fallback output.
 */
public class ConsumeErr<Input, Output : Any, Error : Any, S : Service<Input, Output, Error>>(
    public val inner: S,
    public val consumer: (Error) -> Unit,
    public val fallback: () -> Output,
) : Service<Input, Output, Nothing> {
    override suspend fun serve(input: Input): RamaResult<Output, Nothing> {
        val res = inner.serve(input)
        return if (res.isSuccess()) {
            RamaResult.ok(res.value!!)
        } else {
            consumer(res.error!!)
            RamaResult.ok(fallback())
        }
    }

    override fun toString(): String = "ConsumeErr($inner)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> new(
            inner: S,
            fallback: () -> Output,
            consumer: (Error) -> Unit = {},
        ): ConsumeErr<Input, Output, Error, S> = ConsumeErr(inner, consumer, fallback)
    }
}

/**
 * [Layer] that produces [ConsumeErr] services.
 */
public class ConsumeErrLayer<Input, Output : Any, Error : Any, S : Service<Input, Output, Error>>(
    public val fallback: () -> Output,
    public val consumer: (Error) -> Unit = {},
) : Layer<S, ConsumeErr<Input, Output, Error, S>> {
    override fun layer(inner: S): ConsumeErr<Input, Output, Error, S> =
        ConsumeErr(inner, consumer, fallback)

    override fun toString(): String = "ConsumeErrLayer"

    public companion object {
        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> new(
            fallback: () -> Output,
            consumer: (Error) -> Unit = {},
        ): ConsumeErrLayer<Input, Output, Error, S> = ConsumeErrLayer(fallback, consumer)
    }
}
