// port-lint: source service/handler.rs
package io.github.kotlinmania.ramacore.service

import io.github.kotlinmania.ramacore.RamaResult

/**
 * A parameterless handler function that serves without input.
 */
public interface ParameterlessHandler<out Output : Any, out Error : Any> {
    public suspend fun serve(): RamaResult<Output, Error>
}

/**
 * A [ServiceFn] is a [Service] implemented using a function / service.
 */
public class ServiceFn<in Input, out Output : Any, out Error : Any>(
    private val handler: Service<Input, Output, Error>,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> = handler.serve(input)

    override fun toString(): String = "ServiceFn"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            handler: Service<Input, Output, Error>,
        ): ServiceFn<Input, Output, Error> = ServiceFn(handler)
    }
}

/**
 * Create a [ServiceFn] from a suspending service function.
 */
public fun <Input, Output : Any, Error : Any> serviceFn(
    handler: Service<Input, Output, Error>,
): ServiceFn<Input, Output, Error> = ServiceFn(handler)

/**
 * Create a parameterless [ServiceFn] from a suspending handler.
 */
public fun <Output : Any, Error : Any> serviceFn(
    handler: ParameterlessHandler<Output, Error>,
): ServiceFn<Unit, Output, Error> =
    ServiceFn(
        object : Service<Unit, Output, Error> {
            override suspend fun serve(input: Unit): RamaResult<Output, Error> = handler.serve()
        },
    )

/**
 * Create a [ServiceFn] from a suspending lambda.
 */
public inline fun <Input, Output : Any, Error : Any> serviceFn(
    crossinline handler: suspend (Input) -> RamaResult<Output, Error>,
): ServiceFn<Input, Output, Error> =
    ServiceFn(
        object : Service<Input, Output, Error> {
            override suspend fun serve(input: Input): RamaResult<Output, Error> = handler(input)
        },
    )

/**
 * Create a parameterless [ServiceFn] from a suspending lambda.
 */
public inline fun <Output : Any, Error : Any> serviceFn(
    crossinline handler: suspend () -> RamaResult<Output, Error>,
): ServiceFn<Unit, Output, Error> =
    ServiceFn(
        object : Service<Unit, Output, Error> {
            override suspend fun serve(input: Unit): RamaResult<Output, Error> = handler()
        },
    )

