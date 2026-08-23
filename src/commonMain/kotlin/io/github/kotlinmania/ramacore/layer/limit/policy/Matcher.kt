// port-lint: source layer/limit/policy/matcher.rs
package io.github.kotlinmania.ramacore.layer.limit.policy

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.matcher.Matcher

/**
 * A policy map that applies a [Policy] based on a [Matcher].
 */
public class MatcherPolicyMap<Input, Guard, Error : Any>(
    public val routes: List<Pair<Matcher<Input>, Policy<Input, Guard, Error>>>,
    public val defaultPolicy: Policy<Input, Guard, Error>? = null,
) : Policy<Input, Guard?, Error> {
    override suspend fun check(input: Input): PolicyResult<Input, Guard?, Error> {
        for ((matcher, policy) in routes) {
            val ext = Extensions()
            if (matcher.matches(ext, input)) {
                if (input is ExtensionsMut) {
                    input.extensionsMut().extend(ext)
                }
                val res = policy.check(input)
                return when (val out = res.output) {
                    is PolicyOutput.Ready -> PolicyResult(res.input, PolicyOutput.Ready(out.guard))
                    is PolicyOutput.Abort -> PolicyResult(res.input, PolicyOutput.Abort(out.error))
                    is PolicyOutput.Retry -> PolicyResult(res.input, PolicyOutput.Retry)
                }
            }
        }
        val defaultPol = defaultPolicy
        if (defaultPol != null) {
            val res = defaultPol.check(input)
            return when (val out = res.output) {
                is PolicyOutput.Ready -> PolicyResult(res.input, PolicyOutput.Ready(out.guard))
                is PolicyOutput.Abort -> PolicyResult(res.input, PolicyOutput.Abort(out.error))
                is PolicyOutput.Retry -> PolicyResult(res.input, PolicyOutput.Retry)
            }
        }
        return PolicyResult(input, PolicyOutput.Ready(null))
    }

    override fun toString(): String = "MatcherPolicyMap(routes=${routes.size})"

    public companion object {
        public fun <Input, Guard, Error : Any> new(
            routes: List<Pair<Matcher<Input>, Policy<Input, Guard, Error>>>,
            defaultPolicy: Policy<Input, Guard, Error>? = null,
        ): MatcherPolicyMap<Input, Guard, Error> = MatcherPolicyMap(routes, defaultPolicy)
    }
}
