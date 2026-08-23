// port-lint: source matcher/iter.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions

/**
 * Matches in case all [Matcher] elements match for the given [input] within the specified [ext].
 */
public fun <Input> Iterable<Matcher<Input>>.matchesAnd(ext: Extensions?, input: Input): Boolean {
    if (ext != null) {
        val innerExt = Extensions()
        for (matcher in this) {
            if (!matcher.matches(innerExt, input)) {
                return false
            }
        }
        ext.extend(innerExt)
        return true
    } else {
        for (matcher in this) {
            if (!matcher.matches(null, input)) {
                return false
            }
        }
        return true
    }
}

/**
 * Matches in case any of the [Matcher] elements match for the given [input] within the specified [ext].
 * An empty iterable returns true.
 */
public fun <Input> Iterable<Matcher<Input>>.matchesOr(ext: Extensions?, input: Input): Boolean {
    val iter = this.iterator()
    if (!iter.hasNext()) {
        return true
    }

    if (ext != null) {
        while (iter.hasNext()) {
            val matcher = iter.next()
            val innerExt = Extensions()
            if (matcher.matches(innerExt, input)) {
                ext.extend(innerExt)
                return true
            }
        }
        return false
    } else {
        while (iter.hasNext()) {
            val matcher = iter.next()
            if (matcher.matches(null, input)) {
                return true
            }
        }
        return false
    }
}
