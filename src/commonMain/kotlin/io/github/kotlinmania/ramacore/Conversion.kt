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
 * Mirrors Rust's `Result<T, E>` with [Ok] for success and [Err] for failure.
 * Defined as a repo-local sealed type instead of using [kotlin.Result] so the
 * Kotlin Multiplatform Swift Export plugin does not need to bridge
 * [kotlin.Throwable] (and the surrounding `KotlinStdlib.kt` Any?/Array<Any?>
 * cast scaffolding) into the generated `.swiftmodule`. See http-kotlin
 * a179143 for the precedent.
 */
public sealed interface RamaResult<out T, out E> {
    public class Ok<out T>(public val value: T) : RamaResult<T, Nothing>
    public class Err<out E>(public val error: E) : RamaResult<Nothing, E>
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
