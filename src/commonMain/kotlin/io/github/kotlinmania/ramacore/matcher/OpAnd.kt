// port-lint: source matcher/op_and.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * A matcher that matches if all of the inner matchers match.
 */
public class And<Input>(
    public val matchers: List<Matcher<Input>>,
) : Matcher<Input> {
    override fun matches(ext: Extensions?, input: Input): Boolean {
        if (ext != null) {
            val innerExt = Extensions()
            for (matcher in matchers) {
                if (!matcher.matches(innerExt, input)) {
                    return false
                }
            }
            ext.extend(innerExt)
            return true
        } else {
            for (matcher in matchers) {
                if (!matcher.matches(null, input)) {
                    return false
                }
            }
            return true
        }
    }

    override fun and(other: Matcher<Input>): Matcher<Input> =
        And(matchers + other)

    override fun toString(): String = "And($matchers)"

    public companion object {
        public fun <Input> new(matchers: List<Matcher<Input>>): And<Input> = And(matchers)

        public fun <Input> new(first: Matcher<Input>, second: Matcher<Input>): And<Input> =
            And(listOf(first, second))
    }
}
