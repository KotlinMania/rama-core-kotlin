// port-lint: source layer/limit/mod.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.layer.limit.policy.ConcurrentGuard
import io.github.kotlinmania.ramacore.layer.limit.policy.Policy
import io.github.kotlinmania.ramacore.layer.limit.policy.PolicyOutput
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.service.Service

/**
 * Middleware that limits incoming inputs based on a [Policy].
 */
public class Limit<Input, Output : Any, Error : Any, Guard, PolicyError : Any, S : Service<Input, Output, Error>, P : Policy<Input, Guard, PolicyError>>(
    public val inner: S,
    public val policy: P,
    public val errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>? = null,
    public val policyErrorMapper: ((PolicyError) -> Error)? = null,
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
                            guard.release()
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

    override fun toString(): String = "Limit($inner, $policy)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, Guard, PolicyError : Any, S : Service<Input, Output, Error>, P : Policy<Input, Guard, PolicyError>> new(
            inner: S,
            policy: P,
            policyErrorMapper: ((PolicyError) -> Error)? = null,
        ): Limit<Input, Output, Error, Guard, PolicyError, S, P> =
            Limit(inner, policy, policyErrorMapper = policyErrorMapper)

        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> unlimited(
            inner: S,
        ): Limit<Input, Output, Error, Unit, Nothing, S, UnlimitedPolicy<Input>> =
            Limit(inner, UnlimitedPolicy.instance())
    }
}
