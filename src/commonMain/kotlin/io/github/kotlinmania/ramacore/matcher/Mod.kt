// port-lint: source matcher/mod.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.combinators.Either
import io.github.kotlinmania.ramacore.combinators.Either3
import io.github.kotlinmania.ramacore.combinators.Either4
import io.github.kotlinmania.ramacore.service.Service

/**
 * A condition to decide whether [Input] matches for router or other middleware purposes.
 */
public fun interface Matcher<Input> {
    /**
     * Returns true on a match, false otherwise.
     *
     * [ext] is null in case the callee is not interested in collecting potential
     * match metadata gathered during the matching process.
     */
    public fun matches(ext: Extensions?, input: Input): Boolean

    /**
     * Provide an alternative matcher to match if the current one does not match.
     */
    public fun or(other: Matcher<Input>): Matcher<Input> = Or.new(listOf(this, other))

    /**
     * Add another condition to match on top of the current one.
     */
    public fun and(other: Matcher<Input>): Matcher<Input> = And.new(listOf(this, other))

    /**
     * Negate the current condition.
     */
    public fun not(): Matcher<Input> = Not.new(this)
}

/**
 * Constant boolean matcher.
 */
public class ConstMatcher(
    public val value: Boolean,
) : Matcher<Any?> {
    override fun matches(ext: Extensions?, input: Any?): Boolean = value

    override fun toString(): String = "ConstMatcher($value)"

    public companion object {
        public val TRUE: ConstMatcher = ConstMatcher(true)
        public val FALSE: ConstMatcher = ConstMatcher(false)
    }
}

/**
 * Convert a [Boolean] to a [Matcher].
 */
public fun Boolean.asMatcher(): Matcher<Any?> = if (this) ConstMatcher.TRUE else ConstMatcher.FALSE

/**
 * Negate a boolean as a matcher.
 */
public fun Boolean.asNotMatcher(): Matcher<Any?> = if (this) ConstMatcher.FALSE else ConstMatcher.TRUE

/**
 * Matches an optional matcher, returning false if null.
 */
public fun <Input> Matcher<Input>?.matches(ext: Extensions?, input: Input): Boolean =
    this?.matches(ext, input) ?: false

/**
 * Matcher implementation for [Either].
 */
public fun <Input> Either<Matcher<Input>, Matcher<Input>>.matches(
    ext: Extensions?,
    input: Input,
): Boolean =
    when (this) {
        is Either.A -> value.matches(ext, input)
        is Either.B -> value.matches(ext, input)
    }

/**
 * Matcher implementation for [Either3].
 */
public fun <Input> Either3<Matcher<Input>, Matcher<Input>, Matcher<Input>>.matches(
    ext: Extensions?,
    input: Input,
): Boolean =
    when (this) {
        is Either3.A -> value.matches(ext, input)
        is Either3.B -> value.matches(ext, input)
        is Either3.C -> value.matches(ext, input)
    }

/**
 * Matcher implementation for [Either4].
 */
public fun <Input> Either4<Matcher<Input>, Matcher<Input>, Matcher<Input>, Matcher<Input>>.matches(
    ext: Extensions?,
    input: Input,
): Boolean =
    when (this) {
        is Either4.A -> value.matches(ext, input)
        is Either4.B -> value.matches(ext, input)
        is Either4.C -> value.matches(ext, input)
        is Either4.D -> value.matches(ext, input)
    }

/**
 * A route mapping a [Matcher] to a target [Service].
 */
public data class MatchRoute<Input, Output : Any, Error : Any>(
    public val matcher: Matcher<Input>,
    public val service: Service<Input, Output, Error>,
)

/**
 * Router that routes incoming inputs to services based on matcher predicates.
 */
public class MatcherRouter<Input, Output : Any, Error : Any>(
    public val routes: List<MatchRoute<Input, Output, Error>>,
    public val fallback: Service<Input, Output, Error>,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        for (route in routes) {
            val innerExt = Extensions()
            if (route.matcher.matches(innerExt, input)) {
                if (input is ExtensionsMut) {
                    input.extensionsMut().extend(innerExt)
                }
                return route.service.serve(input)
            }
        }
        return fallback.serve(input)
    }

    override fun toString(): String = "MatcherRouter(routes=${routes.size})"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            routes: List<MatchRoute<Input, Output, Error>>,
            fallback: Service<Input, Output, Error>,
        ): MatcherRouter<Input, Output, Error> = MatcherRouter(routes, fallback)
    }
}
