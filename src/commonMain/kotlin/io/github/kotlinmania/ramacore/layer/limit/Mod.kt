// port-lint: source layer/limit/mod.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentGuard
import io.github.kotlinmania.ramacore.layer.limit.policy.Policy
import io.github.kotlinmania.ramacore.layer.limit.policy.PolicyOutput
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.service.Service

/**
 * Mapper function for translating policy errors in [Limit].
 */
public fun interface PolicyErrorMapper<in PolicyError : Any, out Error : Any> {
    public operator fun invoke(error: PolicyError): Error
}

/**
 * Middleware that limits incoming inputs based on a [Policy].
 */
public class Limit<Input, Output : Any, Error : Any, Guard, PolicyError : Any>(
    public val inner: Service<Input, Output, Error>,
    public val policy: Policy<Input, Guard, PolicyError>,
    public val errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>? = null,
    private val policyErrorMapper: PolicyErrorMapper<PolicyError, Error>? = null,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        var currentInput = input
        while (true) {
            val result = policy.check(currentInput)
            currentInput = result.input
            when (val output = result.output) {
                is PolicyOutput.Ready -> {
                    val guard = output.guard
                    try {
                        return inner.serve(currentInput)
                    } finally {
                        if (guard is ConcurrentGuard) {
                            guard.releaseGuard()
                        }
                    }
                }
                is PolicyOutput.Abort -> {
                    val adapter = errorIntoOutput
                    if (adapter != null) {
                        return adapter.errorIntoOutput(output.error)
                    }
                    val mapper = policyErrorMapper
                    if (mapper != null) {
                        return RamaResult.err(mapper(output.error))
                    }
                    @Suppress("UNCHECKED_CAST")
                    return RamaResult.err(output.error as Error)
                }
                is PolicyOutput.Retry -> {
                    // Retry checking policy
                }
            }
        }
    }

    public fun withErrorIntoOutputFn(
        errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>,
    ): Limit<Input, Output, Error, Guard, PolicyError> =
        Limit(inner, policy, errorIntoOutput = errorIntoOutput, policyErrorMapper = policyErrorMapper)

    override fun toString(): String = "Limit($inner, $policy)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, Guard, PolicyError : Any> new(
            inner: Service<Input, Output, Error>,
            policy: Policy<Input, Guard, PolicyError>,
            policyErrorMapper: PolicyErrorMapper<PolicyError, Error>? = null,
        ): Limit<Input, Output, Error, Guard, PolicyError> =
            Limit(inner, policy, policyErrorMapper = policyErrorMapper)

        public fun <Input, Output : Any, Error : Any> unlimited(
            inner: Service<Input, Output, Error>,
        ): Limit<Input, Output, Error, Unit, Nothing> =
            Limit(inner, UnlimitedPolicy.instance())
    }
}
