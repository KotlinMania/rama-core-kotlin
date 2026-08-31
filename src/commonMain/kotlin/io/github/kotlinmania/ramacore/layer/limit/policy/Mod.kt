// port-lint: source rama-core/src/layer/limit/policy/mod.rs
package io.github.kotlinmania.ramacore.layer.limit.policy

/**
 * Output of a limit policy check.
 */
public sealed class PolicyOutput<out Guard, out Error : Any> {
    /** The input is allowed to proceed, returning a guard to release when done. */
    public data class Ready<Guard>(
        val guard: Guard,
    ) : PolicyOutput<Guard, Nothing>()

    /** The input is rejected and aborted with [error]. */
    public data class Abort<Error : Any>(
        val error: Error,
    ) : PolicyOutput<Nothing, Error>()

    /** The input is not allowed yet, but should be retried. */
    public data object Retry : PolicyOutput<Nothing, Nothing>()
}

/**
 * Result of checking a limit policy on an input.
 */
public data class PolicyResult<Input, out Guard, out Error : Any>(
    public val input: Input,
    public val output: PolicyOutput<Guard, Error>,
)

/**
 * A limit policy that determines whether an input is allowed to proceed.
 */
public interface Policy<Input, Guard, Error : Any> {
    public suspend fun check(input: Input): PolicyResult<Input, Guard, Error>
}

/**
 * An unlimited policy that allows all requests to proceed unconditionally.
 */
public class UnlimitedPolicy<Input> : Policy<Input, Unit, Nothing> {
    override suspend fun check(input: Input): PolicyResult<Input, Unit, Nothing> =
        PolicyResult(input, PolicyOutput.Ready(Unit))

    override fun toString(): String = "UnlimitedPolicy"

    public companion object {
        private val INSTANCE = UnlimitedPolicy<Any?>()

        @Suppress("UNCHECKED_CAST")
        public fun <Input> instance(): UnlimitedPolicy<Input> = INSTANCE as UnlimitedPolicy<Input>
    }
}
