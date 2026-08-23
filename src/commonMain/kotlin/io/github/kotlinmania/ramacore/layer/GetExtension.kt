// port-lint: source layer/get_extension.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import kotlin.reflect.KClass

/**
 * [Layer] for retrieving a value from input extensions.
 */
public class GetInputExtensionLayer<T : Any>(
    public val targetClass: KClass<T>,
    public val callback: suspend (T) -> Unit,
) {
    public fun <Input : ExtensionsRef, Output : Any, Error : Any, S : Service<Input, Output, Error>> layer(
        inner: S,
    ): GetInputExtension<Input, Output, Error, S, T> =
        GetInputExtension(inner, targetClass, callback)

    public fun <Input : ExtensionsRef, Output : Any, Error : Any, S : Service<Input, Output, Error>> intoLayer(
        inner: S,
    ): GetInputExtension<Input, Output, Error, S, T> = layer(inner)

    override fun toString(): String = "GetInputExtensionLayer(${targetClass.simpleName})"

    public companion object {
        public inline fun <reified T : Any> new(
            noinline callback: suspend (T) -> Unit,
        ): GetInputExtensionLayer<T> = GetInputExtensionLayer(T::class, callback)
    }
}

/**
 * Middleware for retrieving a value from input extensions.
 */
public class GetInputExtension<Input : ExtensionsRef, Output : Any, Error : Any, S : Service<Input, Output, Error>, T : Any>(
    public val inner: S,
    public val targetClass: KClass<T>,
    public val callback: suspend (T) -> Unit,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        val extValue = input.extensions().getErased(targetClass)
        if (extValue != null) {
            @Suppress("UNCHECKED_CAST")
            callback(extValue as T)
        }
        return inner.serve(input)
    }

    override fun toString(): String = "GetInputExtension($inner, ${targetClass.simpleName})"

    public companion object {
        public inline fun <Input : ExtensionsRef, Output : Any, Error : Any, S : Service<Input, Output, Error>, reified T : Any> new(
            inner: S,
            noinline callback: suspend (T) -> Unit,
        ): GetInputExtension<Input, Output, Error, S, T> =
            GetInputExtension(inner, T::class, callback)
    }
}

/**
 * [Layer] for retrieving a value from output extensions.
 */
public class GetOutputExtensionLayer<T : Any>(
    public val targetClass: KClass<T>,
    public val callback: suspend (T) -> Unit,
) {
    public fun <Input, Output : ExtensionsRef, Error : Any, S : Service<Input, Output, Error>> layer(
        inner: S,
    ): GetOutputExtension<Input, Output, Error, S, T> =
        GetOutputExtension(inner, targetClass, callback)

    public fun <Input, Output : ExtensionsRef, Error : Any, S : Service<Input, Output, Error>> intoLayer(
        inner: S,
    ): GetOutputExtension<Input, Output, Error, S, T> = layer(inner)

    override fun toString(): String = "GetOutputExtensionLayer(${targetClass.simpleName})"

    public companion object {
        public inline fun <reified T : Any> new(
            noinline callback: suspend (T) -> Unit,
        ): GetOutputExtensionLayer<T> = GetOutputExtensionLayer(T::class, callback)
    }
}

/**
 * Middleware for retrieving a value from output extensions.
 */
public class GetOutputExtension<Input, Output : ExtensionsRef, Error : Any, S : Service<Input, Output, Error>, T : Any>(
    public val inner: S,
    public val targetClass: KClass<T>,
    public val callback: suspend (T) -> Unit,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        val res = inner.serve(input)
        if (res.isSuccess()) {
            val extValue = res.value!!.extensions().getErased(targetClass)
            if (extValue != null) {
                @Suppress("UNCHECKED_CAST")
                callback(extValue as T)
            }
        }
        return res
    }

    override fun toString(): String = "GetOutputExtension($inner, ${targetClass.simpleName})"

    public companion object {
        public inline fun <Input, Output : ExtensionsRef, Error : Any, S : Service<Input, Output, Error>, reified T : Any> new(
            inner: S,
            noinline callback: suspend (T) -> Unit,
        ): GetOutputExtension<Input, Output, Error, S, T> =
            GetOutputExtension(inner, T::class, callback)
    }
}
