// port-lint: source extensions.rs
package io.github.kotlinmania.ramacore

import kotlin.reflect.KClass

/**
 * A type map of protocol extensions.
 *
 * `Extensions` can be used by `Request` and `Response` to store extra data
 * derived from the underlying protocol. Values are stored keyed by their
 * runtime class; the most recently inserted value of a given class wins for
 * [get], while [first] returns the oldest, and [iter] walks every value of
 * the given class in insertion order.
 */
public class Extensions {
    private val entries: MutableList<Entry> = mutableListOf()

    private class Entry(
        val typeId: KClass<*>,
        val value: Any,
    )

    /** Extend this store with the entries from [other]. */
    public fun extend(other: Extensions): Extensions {
        entries.addAll(other.entries)
        return this
    }

    /**
     * Make a shallow copy of this store. Entries refer to the same value
     * objects, but the underlying entry list is independent — adding to the
     * clone does not add to the original.
     */
    public fun copy(): Extensions {
        val out = Extensions()
        out.entries.addAll(entries)
        return out
    }

    /**
     * Insert [value] into this store under its runtime type [T].
     *
     * Upstream Rust bounds this on `T: Clone + Send + Sync + Debug +
     * 'static`. Send/Sync/'static guarantee thread-safety and lifetime in
     * Rust; Kotlin has no equivalent borrow-checked ownership system, so
     * the bound collapses to `T : Any`. Debug is satisfied by Kotlin's
     * universal [toString].
     */
    public inline fun <reified T : Any> insert(value: T): Extensions {
        insertErased(T::class, value)
        return this
    }

    @PublishedApi
    internal fun insertErased(typeId: KClass<*>, value: Any) {
        entries.add(Entry(typeId, value))
    }

    /** Returns true if this store contains any value of type [T]. */
    public inline fun <reified T : Any> contains(): Boolean = containsErased(T::class)

    @PublishedApi
    internal fun containsErased(typeId: KClass<*>): Boolean {
        for (i in entries.indices.reversed()) {
            if (entries[i].typeId == typeId) return true
        }
        return false
    }

    /**
     * Return the most recently inserted value of type [T], or null.
     *
     * In most cases this is what callers want; for the oldest inserted
     * value, see [first].
     */
    public inline fun <reified T : Any> get(): T? {
        val raw = getErased(T::class) ?: return null
        return raw as T
    }

    @PublishedApi
    internal fun getErased(typeId: KClass<*>): Any? {
        for (i in entries.indices.reversed()) {
            val e = entries[i]
            if (e.typeId == typeId) return e.value
        }
        return null
    }

    /**
     * Return the oldest inserted value of type [T], or null.
     *
     * In most cases callers want [get] instead; [first] is for the rare
     * case where insertion-order semantics matter.
     */
    public inline fun <reified T : Any> first(): T? {
        val raw = firstErased(T::class) ?: return null
        return raw as T
    }

    @PublishedApi
    internal fun firstErased(typeId: KClass<*>): Any? {
        for (e in entries) {
            if (e.typeId == typeId) return e.value
        }
        return null
    }

    /**
     * Iterate over every value of type [T], oldest first.
     */
    public inline fun <reified T : Any> iter(): Sequence<T> =
        iterErased(T::class).map { it as T }

    @PublishedApi
    internal fun iterErased(typeId: KClass<*>): Sequence<Any> =
        entries.asSequence().filter { it.typeId == typeId }.map { it.value }

    override fun toString(): String =
        entries.joinToString(prefix = "Extensions[", postfix = "]") {
            "${it.typeId.simpleName ?: "<anonymous>"}=${it.value}"
        }
}

/** Anything that can yield a read-only view of its [Extensions] store. */
public interface ExtensionsRef {
    public fun extensions(): Extensions
}

/**
 * Anything that can yield a mutable view of its [Extensions] store.
 *
 * Implementors of [ExtensionsMut] are also [ExtensionsRef]; the same
 * trait-hierarchy relation exists in upstream Rust.
 */
public interface ExtensionsMut : ExtensionsRef {
    public fun extensionsMut(): Extensions
}

internal class ExtensionsAsRef(
    private val store: Extensions,
) : ExtensionsMut {
    override fun extensions(): Extensions = store

    override fun extensionsMut(): Extensions = store
}

/**
 * Treat this [Extensions] store as an [ExtensionsMut]. Upstream Rust gets
 * this via blanket `impl ExtensionsRef for Extensions` / `impl
 * ExtensionsMut for Extensions`; Kotlin doesn't allow methods that
 * "implement an interface on a class from outside the class", so we
 * provide a thin wrapper view that delegates back to the same store.
 */
public fun Extensions.asExtensionsRef(): ExtensionsMut = ExtensionsAsRef(this)

// The upstream Rust file also defines blanket impls of ExtensionsRef and
// ExtensionsMut for `&T`, `&mut T`, `Box<T>`, `Pin<Box<T>>`, and `Arc<T>`
// wrappers. Those wrappers are part of Rust's ownership/borrow machinery
// (shared-borrow, exclusive-borrow, heap-pinning, atomic refcounting); none
// of them have a direct Kotlin equivalent, since Kotlin object references
// are already always shared, mutable, and heap-allocated. Callers that
// would have wrapped a value in Box/Arc/Pin in Rust simply use the value
// directly in Kotlin, and the trait is already implemented on the value.

// Upstream Rust also uses an `impl_extensions_either!` macro to derive
// `ExtensionsRef`/`ExtensionsMut` for `Either1`..`Either9` combinator
// variants whose payloads implement the trait. Kotlin's `Either*` sealed
// hierarchies already drop the upstream blanket trait impls (see
// combinators/Either.kt); the same applies here — code that needs an
// Extensions view from an Either variant can pattern-match on the variant
// and call `.extensions()` on the payload directly.

/**
 * Two stores chained for [contains] / [get] lookups: the first store wins
 * if both contain the requested type.
 *
 * Upstream Rust expresses this as `impl ChainableExtensions for (S, T)`
 * via the tuple type itself; Kotlin has no arbitrary-arity tuples and
 * exposing a `Pair<A, B>` in the public API would leak `kotlin.Pair`
 * (per workspace Swift Export rules a forbidden public surface), so the
 * pair is wrapped in a named class.
 */
public class ChainableExtensionsPair(
    public val first: ExtensionsRef,
    public val second: ExtensionsRef,
) {
    public inline fun <reified I : Any> contains(): Boolean =
        first.extensions().contains<I>() || second.extensions().contains<I>()

    public inline fun <reified I : Any> get(): I? =
        first.extensions().get<I>() ?: second.extensions().get<I>()
}

/**
 * Three stores chained for [contains] / [get] lookups: stores are
 * consulted left-to-right and the first match wins.
 */
public class ChainableExtensionsTriple(
    public val first: ExtensionsRef,
    public val second: ExtensionsRef,
    public val third: ExtensionsRef,
) {
    public inline fun <reified I : Any> contains(): Boolean =
        ChainableExtensionsPair(first, second).contains<I>() ||
            third.extensions().contains<I>()

    public inline fun <reified I : Any> get(): I? =
        first.extensions().get<I>()
            ?: second.extensions().get<I>()
            ?: third.extensions().get<I>()
}

/**
 * Wrapper that a leaf-like service can insert when returning an output
 * so that the input [Extensions] remain accessible to downstream callers.
 */
public class InputExtensions(
    public val extensions: Extensions,
) {
    override fun toString(): String = "InputExtensions($extensions)"

    override fun equals(other: Any?): Boolean =
        other is InputExtensions && other.extensions === extensions

    override fun hashCode(): Int = extensions.hashCode()
}
