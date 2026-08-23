// port-lint: source service/handler.rs
package io.github.kotlinmania.ramacore.service

import io.github.kotlinmania.ramacore.RamaResult

/**
 * A [ServiceFn] is a [Service] implemented using a function.
 */
public class ServiceFn<in Input, out Output : Any, out Error : Any>(
    private val handler: suspend (Input) -> RamaResult<Output, Error>,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> = handler(input)

    override fun toString(): String = "ServiceFn"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            handler: suspend (Input) -> RamaResult<Output, Error>,
        ): ServiceFn<Input, Output, Error> = ServiceFn(handler)
    }
}

/**
 * Create a [ServiceFn] from a suspending function.
 */
public fun <Input, Output : Any, Error : Any> serviceFn(
    handler: suspend (Input) -> RamaResult<Output, Error>,
): ServiceFn<Input, Output, Error> = ServiceFn(handler)

/**
 * Create a parameterless [ServiceFn] from a suspending function.
 */
public fun <Output : Any, Error : Any> serviceFn(
    handler: suspend () -> RamaResult<Output, Error>,
): ServiceFn<Unit, Output, Error> = ServiceFn { handler() }
