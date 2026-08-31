// port-lint: source rama-core/src/layer/limit/into_output.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.RamaResult

/**
 * Adapter to transform a limit policy error into a service result.
 */
public fun interface ErrorIntoOutput<in PolicyError : Any, out Output : Any, out Error : Any> {
    public fun errorIntoOutput(error: PolicyError): RamaResult<Output, Error>
}

/**
 * Function wrapper for [ErrorIntoOutput].
 */
public class ErrorIntoOutputFn<PolicyError : Any, out Output : Any, out Error : Any>(
    private val delegate: ErrorIntoOutput<PolicyError, Output, Error>,
) : ErrorIntoOutput<PolicyError, Output, Error> {
    override fun errorIntoOutput(error: PolicyError): RamaResult<Output, Error> =
        delegate.errorIntoOutput(error)

    override fun toString(): String = "ErrorIntoOutputFn"

    public companion object {
        public fun <PolicyError : Any, Output : Any, Error : Any> of(
            delegate: ErrorIntoOutput<PolicyError, Output, Error>,
        ): ErrorIntoOutputFn<PolicyError, Output, Error> = ErrorIntoOutputFn(delegate)

        public fun <PolicyError : Any, Output : Any, Error : Any> new(
            delegate: ErrorIntoOutput<PolicyError, Output, Error>,
        ): ErrorIntoOutputFn<PolicyError, Output, Error> = ErrorIntoOutputFn(delegate)
    }
}
