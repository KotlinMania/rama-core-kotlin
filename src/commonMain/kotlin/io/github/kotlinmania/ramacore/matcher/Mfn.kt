// port-lint: source matcher/mfn.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * Predicate operating on input for matching.
 */
public fun interface InputPredicate<in Input> {
    public operator fun invoke(input: Input): Boolean
}

/**
 * Predicate operating on extensions for matching.
 */
public fun interface ExtPredicate {
    public operator fun invoke(ext: Extensions?): Boolean
}

/**
 * Constant predicate returning a boolean.
 */
public fun interface ConstPredicate {
    public operator fun invoke(): Boolean
}

/**
 * A matcher created from a function / lambda.
 */
public class MatchFn<Input>(
    private val matcher: Matcher<Input>,
) : Matcher<Input> {
    override fun matches(ext: Extensions?, input: Input): Boolean = matcher.matches(ext, input)

    override fun toString(): String = "MatchFn"

    public companion object {
        public fun <Input> from(matcher: Matcher<Input>): MatchFn<Input> =
            MatchFn(matcher)

        public fun <Input> fromInput(predicate: InputPredicate<Input>): MatchFn<Input> =
            MatchFn { _, input -> predicate(input) }

        public fun <Input> fromExt(predicate: ExtPredicate): MatchFn<Input> =
            MatchFn { ext, _ -> predicate(ext) }

        public fun <Input> fromConst(predicate: ConstPredicate): MatchFn<Input> =
            MatchFn { _, _ -> predicate() }
    }
}

/**
 * Create a [MatchFn] from a [Matcher].
 */
public fun <Input> matchFn(matcher: Matcher<Input>): MatchFn<Input> =
    MatchFn.from(matcher)

/**
 * Create a [MatchFn] from an [InputPredicate].
 */
public fun <Input> matchFn(predicate: InputPredicate<Input>): MatchFn<Input> =
    MatchFn.fromInput(predicate)

