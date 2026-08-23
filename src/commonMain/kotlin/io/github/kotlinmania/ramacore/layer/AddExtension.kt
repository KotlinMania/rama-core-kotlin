// port-lint: source layer/add_extension.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import kotlin.reflect.KClass

/**
 * [Layer] for adding a value to incoming input's extensions.
 */
public class AddInputExtensionLayer<T : Any>(
    public val valueType: KClass<T>,
    public val value: T,
) {
    public fun <Input : ExtensionsMut, Output : Any, Error : Any, S : Service<Input, Output, Error>> layer(
        inner: S,
    ): AddInputExtension<Input, Output, Error, S, T> =
        AddInputExtension(inner, valueType, value)

    public fun <Input : ExtensionsMut, Output : Any, Error : Any, S : Service<Input, Output, Error>> intoLayer(
        inner: S,
    ): AddInputExtension<Input, Output, Error, S, T> = layer(inner)

    override fun toString(): String = "AddInputExtensionLayer($value)"

    public companion object {
        public inline fun <reified T : Any> new(value: T): AddInputExtensionLayer<T> =
            AddInputExtensionLayer(T::class, value)
    }
}

/**
 * Middleware for adding a value to incoming input's extensions.
 */
public class AddInputExtension<Input : ExtensionsMut, Output : Any, Error : Any, S : Service<Input, Output, Error>, T : Any>(
    public val inner: S,
    public val valueType: KClass<T>,
    public val value: T,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        input.extensionsMut().insertErased(valueType, value)
        return inner.serve(input)
    }

    override fun toString(): String = "AddInputExtension($inner, $value)"

    public companion object {
        public inline fun <Input : ExtensionsMut, Output : Any, Error : Any, S : Service<Input, Output, Error>, reified T : Any> new(
            inner: S,
            value: T,
        ): AddInputExtension<Input, Output, Error, S, T> =
            AddInputExtension(inner, T::class, value)
    }
}

/**
 * [Layer] for adding a value to an output's extensions.
 */
public class AddOutputExtensionLayer<T : Any>(
    public val valueType: KClass<T>,
    public val value: T,
) {
    public fun <Input, Output : ExtensionsMut, Error : Any, S : Service<Input, Output, Error>> layer(
        inner: S,
    ): AddOutputExtension<Input, Output, Error, S, T> =
        AddOutputExtension(inner, valueType, value)

    public fun <Input, Output : ExtensionsMut, Error : Any, S : Service<Input, Output, Error>> intoLayer(
        inner: S,
    ): AddOutputExtension<Input, Output, Error, S, T> = layer(inner)

    override fun toString(): String = "AddOutputExtensionLayer($value)"

    public companion object {
        public inline fun <reified T : Any> new(value: T): AddOutputExtensionLayer<T> =
            AddOutputExtensionLayer(T::class, value)
    }
}

/**
 * Middleware for adding a value to an output's extensions.
 */
public class AddOutputExtension<Input, Output : ExtensionsMut, Error : Any, S : Service<Input, Output, Error>, T : Any>(
    public val inner: S,
    public val valueType: KClass<T>,
    public val value: T,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        val res = inner.serve(input)
        if (res.isSuccess()) {
            res.value!!.extensionsMut().insertErased(valueType, value)
        }
        return res
    }

    override fun toString(): String = "AddOutputExtension($inner, $value)"

    public companion object {
        public inline fun <Input, Output : ExtensionsMut, Error : Any, S : Service<Input, Output, Error>, reified T : Any> new(
            inner: S,
            value: T,
        ): AddOutputExtension<Input, Output, Error, S, T> =
            AddOutputExtension(inner, T::class, value)
    }
}
