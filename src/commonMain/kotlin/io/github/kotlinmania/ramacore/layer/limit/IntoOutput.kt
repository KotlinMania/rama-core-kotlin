// port-lint: source layer/limit/into_output.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.RamaResult

/**
 * Adapter to transform a limit policy error into a service result.
 */
public fun interface ErrorIntoOutput<in PolicyError : Any, Output : Any, Error : Any> {
    public fun errorIntoOutput(error: PolicyError): RamaResult<Output, Error>
}

/**
 * Function wrapper for [ErrorIntoOutput].
 */
public class ErrorIntoOutputFn<PolicyError : Any, Output : Any, Error : Any>(
    private val fn: (PolicyError) -> RamaResult<Output, Error>,
) : ErrorIntoOutput<PolicyError, Output, Error> {
    override fun errorIntoOutput(error: PolicyError): RamaResult<Output, Error> = fn(error)

    override fun toString(): String = "ErrorIntoOutputFn"

    public companion object {
        public fun <PolicyError : Any, Output : Any, Error : Any> new(
            fn: (PolicyError) -> RamaResult<Output, Error>,
        ): ErrorIntoOutputFn<PolicyError, Output, Error> = ErrorIntoOutputFn(fn)
    }
}
