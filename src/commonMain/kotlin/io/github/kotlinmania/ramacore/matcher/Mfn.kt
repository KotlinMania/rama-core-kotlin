// port-lint: source matcher/mfn.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * A matcher created from a function / lambda.
 */
public class MatchFn<Input>(
    private val fn: (Extensions?, Input) -> Boolean,
) : Matcher<Input> {
    override fun matches(ext: Extensions?, input: Input): Boolean = fn(ext, input)

    override fun toString(): String = "MatchFn"

    public companion object {
        public fun <Input> from(fn: (Extensions?, Input) -> Boolean): MatchFn<Input> =
            MatchFn(fn)

        public fun <Input> fromInput(fn: (Input) -> Boolean): MatchFn<Input> =
            MatchFn { _, input -> fn(input) }

        public fun <Input> fromExt(fn: (Extensions?) -> Boolean): MatchFn<Input> =
            MatchFn { ext, _ -> fn(ext) }

        public fun <Input> fromConst(fn: () -> Boolean): MatchFn<Input> =
            MatchFn { _, _ -> fn() }
    }
}

/**
 * Create a [MatchFn] from a function taking `(Extensions?, Input)`.
 */
public fun <Input> matchFn(fn: (Extensions?, Input) -> Boolean): MatchFn<Input> =
    MatchFn.from(fn)

/**
 * Create a [MatchFn] from a function taking `(Input)`.
 */
public fun <Input> matchFn(fn: (Input) -> Boolean): MatchFn<Input> =
    MatchFn.fromInput(fn)
