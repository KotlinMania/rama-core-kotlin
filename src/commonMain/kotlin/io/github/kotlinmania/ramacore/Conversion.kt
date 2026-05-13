// port-lint: source src/conversion.rs
package io.github.kotlinmania.ramacore

/**
 * Alternative for direct construction/conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 */
fun interface RamaFrom<T, U, CrateMarker> {
    fun ramaFrom(value: T): U
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
fun interface RamaInto<T, CrateMarker> {
    fun ramaInto(): T
}

/** Convert [value] to [U] using the supplied [RamaFrom] implementation. */
fun <T, U, CrateMarker> ramaInto(value: T, from: RamaFrom<T, U, CrateMarker>): U =
    from.ramaFrom(value)

/**
 * Alternative for fallible construction/conversion which can be implemented by external
 * modules for external types.
 *
 * To implement this interface, use a module-local `CrateMarker` generic type.
 * More info: <https://ramaproxy.org/book/intro/patterns.html#working-around-the-orphan-rule-in-specific-cases>
 */
fun interface RamaTryFrom<T, U, CrateMarker> {
    fun ramaTryFrom(value: T): Result<U>
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
fun interface RamaTryInto<T, CrateMarker> {
    fun ramaTryInto(): Result<T>
}

/** Convert [value] to [U] using the supplied [RamaTryFrom] implementation. */
fun <T, U, CrateMarker> ramaTryInto(value: T, from: RamaTryFrom<T, U, CrateMarker>): Result<U> =
    from.ramaTryFrom(value)

/**
 * Create `Self` from a reference [T].
 *
 * This is mostly used for extractors, but it can be used for anything
 * that needs to create an owned type from a reference.
 */
fun interface FromRef<T, U> {
    /** Converts to this type from a reference to the input type. */
    fun fromRef(input: T): U
}

/** Identity [FromRef]: produces the input value unchanged. */
fun <T> identityFromRef(): FromRef<T, T> = FromRef { it }
