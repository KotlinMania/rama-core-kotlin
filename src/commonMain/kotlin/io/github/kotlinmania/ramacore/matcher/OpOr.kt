// port-lint: source rama-core/src/matcher/op_or.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * A matcher that matches if any of the inner matchers match.
 */
public class Or<Input>(
    public val matchers: List<Matcher<Input>>,
) : Matcher<Input> {
    override fun matches(ext: Extensions?, input: Input): Boolean {
        if (ext != null) {
            for (matcher in matchers) {
                val innerExt = Extensions()
                if (matcher.matches(innerExt, input)) {
                    ext.extend(innerExt)
                    return true
                }
            }
            return false
        } else {
            for (matcher in matchers) {
                if (matcher.matches(null, input)) {
                    return true
                }
            }
            return false
        }
    }

    override fun or(other: Matcher<Input>): Matcher<Input> =
        Or(matchers + other)

    override fun toString(): String = "Or($matchers)"

    public companion object {
        public fun <Input> new(matchers: List<Matcher<Input>>): Or<Input> = Or(matchers)

        public fun <Input> new(first: Matcher<Input>, second: Matcher<Input>): Or<Input> =
            Or(listOf(first, second))
    }
}
