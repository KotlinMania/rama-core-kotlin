// port-lint: source matcher/op_not.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * A matcher that matches if the inner matcher does not match.
 */
public class Not<Input>(
    public val matcher: Matcher<Input>,
) : Matcher<Input> {
    override fun matches(ext: Extensions?, input: Input): Boolean =
        !matcher.matches(ext, input)

    override fun toString(): String = "Not($matcher)"

    public companion object {
        public fun <Input> new(matcher: Matcher<Input>): Not<Input> = Not(matcher)

        public fun new(constant: Boolean): Not<Any?> = Not(ConstMatcher(constant))
    }
}
