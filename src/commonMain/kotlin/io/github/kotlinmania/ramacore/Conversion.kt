// port-lint: source conversion.rs
package io.github.kotlinmania.ramacore

/**
 * Alternative for direct construction/conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 */
public fun interface RamaFrom<T, U, CrateMarker> {
    public fun ramaFrom(value: T): U
}

/**
 * Alternative for `into`-style conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 *
 * [RamaInto] is bridged from [RamaFrom] in the opposite direction by [ramaInto].
 */
public fun interface RamaInto<T, CrateMarker> {
    public fun ramaInto(): T
}

/** Convert [value] to [U] using the supplied [RamaFrom] implementation. */
public fun <T, U, CrateMarker> ramaInto(value: T, from: RamaFrom<T, U, CrateMarker>): U =
    from.ramaFrom(value)

/**
 * Result of a fallible conversion or construction performed by [RamaTryFrom] /
 * [RamaTryInto].
 *
 * Flat-class shape — not a sealed class with `Ok` / `Err` subclasses. The
 * Kotlin Multiplatform Swift Export plugin does not preserve sealed-subclass
 * casts: a Swift consumer who `import`s this type can read [value] /
 * [error] / [isSuccess] / [isFailure] but cannot pattern-match on a
 * `RamaResultOk` / `RamaResultErr` Swift type. See
 * `automation-artifacts/swift-export-rollout/SWIFT_EXPORT_ROLLOUT.md`
 * gap #4 and the recipe in § Recipe for replacing kotlin.Result<T>.
 *
 * Defined as a repo-local type instead of using [kotlin.Result] so the
 * Swift Export plugin does not need to bridge [kotlin.Throwable] (and the
 * surrounding `KotlinStdlib.kt` `Any?` / `Array<Any?>` cast scaffolding)
 * into the generated `.swiftmodule`. See `http-kotlin` `a179143`.
 *
 * The class invariant — exactly one of [value] / [error] is non-null — is
 * enforced at construction by [init]. The companion factories restrict the
 * payload to non-null `T & Any` / `E & Any` so the invariant cannot be
 * violated through the public surface.
 *
 * Mirrors the associated `Error` type on Rust's `TryFrom<T>` / `TryInto<T>`
 * traits via the [E] type parameter, which the previous `kotlin.Result<U>`
 * shape collapsed to `Throwable`.
 */
public class RamaResult<out T, out E> private constructor(
    public val value: T?,
    public val error: E?,
) {
    init {
        require((value == null) != (error == null)) {
            "RamaResult must carry exactly one of value or error (got value=$value, error=$error)"
        }
    }

    public fun isSuccess(): Boolean = value != null

    public fun isFailure(): Boolean = error != null

    public fun getOrNull(): T? = value

    public fun errorOrNull(): E? = error

    public companion object {
        public fun <T : Any> ok(value: T): RamaResult<T, Nothing> = RamaResult(value, null)

        public fun <E : Any> err(error: E): RamaResult<Nothing, E> = RamaResult(null, error)
    }
}

/**
 * Alternative for fallible construction/conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 */
public fun interface RamaTryFrom<T, U, E, CrateMarker> {
    public fun ramaTryFrom(value: T): RamaResult<U, E>
}

/**
 * Alternative for fallible `into`-style conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 *
 * [RamaTryInto] is bridged from [RamaTryFrom] in the opposite direction by [ramaTryInto].
 */
public fun interface RamaTryInto<T, E, CrateMarker> {
    public fun ramaTryInto(): RamaResult<T, E>
}

/** Convert [value] to [U] using the supplied [RamaTryFrom] implementation. */
public fun <T, U, E, CrateMarker> ramaTryInto(
    value: T,
    from: RamaTryFrom<T, U, E, CrateMarker>,
): RamaResult<U, E> = from.ramaTryFrom(value)

/**
 * Create `Self` from a reference [T].
 *
 * This is mostly used for extractors, but it can be used for anything
 * that needs to create an owned type from a reference.
 */
public fun interface FromRef<T, U> {
    /** Converts to this type from a reference to the input type. */
    public fun fromRef(input: T): U
}

/** Identity [FromRef]: produces the input value unchanged. */
public fun <T> identityFromRef(): FromRef<T, T> = FromRef { it }
