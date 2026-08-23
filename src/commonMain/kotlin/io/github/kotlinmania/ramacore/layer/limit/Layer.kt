// port-lint: source layer/limit/layer.rs
package io.github.kotlinmania.ramacore.layer.limit

import io.github.kotlinmania.ramacore.layer.Layer
import io.github.kotlinmania.ramacore.layer.limit.policy.Policy
import io.github.kotlinmania.ramacore.layer.limit.policy.UnlimitedPolicy
import io.github.kotlinmania.ramacore.service.Service

/**
 * [Layer] for applying a limit [Policy].
 */
public class LimitLayer<Input, Output : Any, Error : Any, Guard, PolicyError : Any, S : Service<Input, Output, Error>, P : Policy<Input, Guard, PolicyError>>(
    public val policy: P,
    public val errorIntoOutput: ErrorIntoOutput<PolicyError, Output, Error>? = null,
    public val policyErrorMapper: ((PolicyError) -> Error)? = null,
) : Layer<S, Limit<Input, Output, Error, Guard, PolicyError, S, P>> {
    override fun layer(inner: S): Limit<Input, Output, Error, Guard, PolicyError, S, P> =
        Limit(inner, policy, errorIntoOutput, policyErrorMapper)

    override fun toString(): String = "LimitLayer($policy)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, Guard, PolicyError : Any, S : Service<Input, Output, Error>, P : Policy<Input, Guard, PolicyError>> new(
            policy: P,
            policyErrorMapper: ((PolicyError) -> Error)? = null,
        ): LimitLayer<Input, Output, Error, Guard, PolicyError, S, P> =
            LimitLayer(policy, policyErrorMapper = policyErrorMapper)

        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>> unlimited(): LimitLayer<Input, Output, Error, Unit, Nothing, S, UnlimitedPolicy<Input>> =
            LimitLayer(UnlimitedPolicy.instance())
    }
}
