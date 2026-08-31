// port-lint: source rama-core/src/matcher/ext.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsRef
import kotlin.reflect.KClass

/**
 * A matcher which allows you to match based on an extension in the input.
 */
public class ExtensionMatcher<T : Any>(
    public val targetClass: KClass<T>,
    private val predicate: (T) -> Boolean,
) : Matcher<ExtensionsRef> {
    override fun matches(ext: Extensions?, input: ExtensionsRef): Boolean {
        val found = input.extensions().getErased(targetClass) ?: return false
        @Suppress("UNCHECKED_CAST")
        return predicate(found as T)
    }

    override fun toString(): String = "ExtensionMatcher(${targetClass.simpleName})"

    public companion object {
        /**
         * Create an [ExtensionMatcher] with a custom predicate.
         */
        public inline fun <reified T : Any> withFn(
            noinline predicate: (T) -> Boolean,
        ): ExtensionMatcher<T> = ExtensionMatcher(T::class, predicate)

        /**
         * Create an [ExtensionMatcher] that matches against a constant value.
         */
        public inline fun <reified T : Any> withConst(value: T): ExtensionMatcher<T> =
            ExtensionMatcher(T::class) { it == value }
    }
}
