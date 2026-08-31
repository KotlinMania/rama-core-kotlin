// port-lint: source rama-core/src/layer/limit/layer.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.layer.Layer
import io.github.kotlinmania.ramacore.layer.limit.policy.Policy
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.service.Service

/**
 * [Layer] for applying a limit [Policy].
 */
public class LimitLayer<Input, Output : Any, Error : Any, Guard, PolicyError : Any>(
    public val policy: Policy<Input, Guard, PolicyError>,
    public val errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>? = null,
    public val policyErrorMapper: PolicyErrorMapper<PolicyError, Error>? = null,
) : Layer<Service<Input, Output, Error>, Limit<Input, Output, Error, Guard, PolicyError>> {
    override fun layer(inner: Service<Input, Output, Error>): Limit<Input, Output, Error, Guard, PolicyError> =
        Limit(inner, policy, errorIntoOutput, policyErrorMapper)

    override fun intoLayer(inner: Service<Input, Output, Error>): Limit<Input, Output, Error, Guard, PolicyError> =
        layer(inner)

    public fun withErrorIntoResponseFn(
        errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>,
    ): LimitLayer<Input, Output, Error, Guard, PolicyError> =
        LimitLayer(policy, errorIntoOutput = errorIntoOutput, policyErrorMapper = policyErrorMapper)

    override fun toString(): String = "LimitLayer($policy)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, Guard, PolicyError : Any> new(
            policy: Policy<Input, Guard, PolicyError>,
            policyErrorMapper: PolicyErrorMapper<PolicyError, Error>? = null,
        ): LimitLayer<Input, Output, Error, Guard, PolicyError> =
            LimitLayer(policy, policyErrorMapper = policyErrorMapper)

        public fun <Input, Output : Any, Error : Any> unlimited(): LimitLayer<Input, Output, Error, Unit, Nothing> =
            LimitLayer(UnlimitedPolicy.instance())
    }
}
