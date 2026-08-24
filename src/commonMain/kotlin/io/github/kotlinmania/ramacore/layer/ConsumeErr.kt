// port-lint: source layer/consume_err.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * Consumer callback for error values in [ConsumeErr].
 */
public interface ErrorConsumer<in Error> {
    public operator fun invoke(error: Error)
}

/**
 * Fallback supplier for output values in [ConsumeErr].
 */
public interface OutputFallback<out Output : Any> {
    public operator fun invoke(): Output
}

/**
 * Middleware that consumes a service's error value and returns a fallback output.
 */
public class ConsumeErr<Input, Output : Any, Error : Any>(
    public val inner: Service<Input, Output, Error>,
    private val fallback: OutputFallback<Output>,
    private val consumer: ErrorConsumer<Error>? = null,
) : Service<Input, Output, Nothing> {
    override suspend fun serve(input: Input): RamaResult<Output, Nothing> {
        val res = inner.serve(input)
        return if (res.isSuccess()) {
            RamaResult.ok(res.value!!)
        } else {
            consumer?.invoke(res.error!!)
            RamaResult.ok(fallback())
        }
    }

    override fun toString(): String = "ConsumeErr($inner)"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            inner: Service<Input, Output, Error>,
            fallback: OutputFallback<Output>,
            consumer: ErrorConsumer<Error>? = null,
        ): ConsumeErr<Input, Output, Error> = ConsumeErr(inner, fallback, consumer)

        public inline fun <Input, Output : Any, Error : Any> of(
            inner: Service<Input, Output, Error>,
            crossinline fallback: () -> Output,
            noinline consumer: ((Error) -> Unit)? = null,
        ): ConsumeErr<Input, Output, Error> =
            ConsumeErr(
                inner,
                object : OutputFallback<Output> {
                    override fun invoke(): Output = fallback()
                },
                consumer?.let {
                    object : ErrorConsumer<Error> {
                        override fun invoke(error: Error) = it(error)
                    }
                },
            )
    }
}

/**
 * [Layer] that produces [ConsumeErr] services.
 */
public class ConsumeErrLayer<Input, Output : Any, Error : Any>(
    private val fallback: OutputFallback<Output>,
    private val consumer: ErrorConsumer<Error>? = null,
) : Layer<Service<Input, Output, Error>, ConsumeErr<Input, Output, Error>> {
    override fun layer(inner: Service<Input, Output, Error>): ConsumeErr<Input, Output, Error> =
        ConsumeErr(inner, fallback, consumer)

    override fun toString(): String = "ConsumeErrLayer"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            fallback: OutputFallback<Output>,
            consumer: ErrorConsumer<Error>? = null,
        ): ConsumeErrLayer<Input, Output, Error> = ConsumeErrLayer(fallback, consumer)

        public inline fun <Input, Output : Any, Error : Any> of(
            crossinline fallback: () -> Output,
            noinline consumer: ((Error) -> Unit)? = null,
        ): ConsumeErrLayer<Input, Output, Error> =
            ConsumeErrLayer(
                object : OutputFallback<Output> {
                    override fun invoke(): Output = fallback()
                },
                consumer?.let {
                    object : ErrorConsumer<Error> {
                        override fun invoke(error: Error) = it(error)
                    }
                },
            )
    }
}
