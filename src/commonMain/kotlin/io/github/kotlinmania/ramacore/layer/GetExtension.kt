// port-lint: source layer/get_extension.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import kotlin.reflect.KClass

/**
 * Suspending callback invoked when an extension value is found.
 */
public fun interface ExtensionCallback<in T> {
    public suspend operator fun invoke(value: T)
}

/**
 * [Layer] for retrieving a value from input extensions.
 */
public class GetInputExtensionLayer<T : Any>(
    public val targetClass: KClass<T>,
    private val callback: ExtensionCallback<T>,
) {
    public fun <Input : ExtensionsRef, Output : Any, Error : Any> layer(
        inner: Service<Input, Output, Error>,
    ): GetInputExtension<Input, Output, Error, T> =
        GetInputExtension(inner, targetClass, callback)

    public fun <Input : ExtensionsRef, Output : Any, Error : Any> intoLayer(
        inner: Service<Input, Output, Error>,
    ): GetInputExtension<Input, Output, Error, T> = layer(inner)

    override fun toString(): String = "GetInputExtensionLayer(${targetClass.simpleName})"

    public companion object {
        public fun <T : Any> of(
            targetClass: KClass<T>,
            callback: ExtensionCallback<T>,
        ): GetInputExtensionLayer<T> = GetInputExtensionLayer(targetClass, callback)

        public inline fun <reified T : Any> new(
            callback: ExtensionCallback<T>,
        ): GetInputExtensionLayer<T> = GetInputExtensionLayer(T::class, callback)
    }
}

/**
 * Middleware for retrieving a value from input extensions.
 */
public class GetInputExtension<Input : ExtensionsRef, Output : Any, Error : Any, T : Any>(
    public val inner: Service<Input, Output, Error>,
    public val targetClass: KClass<T>,
    private val callback: ExtensionCallback<T>,
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
        public fun <Input : ExtensionsRef, Output : Any, Error : Any, T : Any> of(
            inner: Service<Input, Output, Error>,
            targetClass: KClass<T>,
            callback: ExtensionCallback<T>,
        ): GetInputExtension<Input, Output, Error, T> =
            GetInputExtension(inner, targetClass, callback)

        public inline fun <Input : ExtensionsRef, Output : Any, Error : Any, reified T : Any> new(
            inner: Service<Input, Output, Error>,
            callback: ExtensionCallback<T>,
        ): GetInputExtension<Input, Output, Error, T> =
            GetInputExtension(inner, T::class, callback)
    }
}

/**
 * [Layer] for retrieving a value from output extensions.
 */
public class GetOutputExtensionLayer<T : Any>(
    public val targetClass: KClass<T>,
    private val callback: ExtensionCallback<T>,
) {
    public fun <Input, Output : ExtensionsRef, Error : Any> layer(
        inner: Service<Input, Output, Error>,
    ): GetOutputExtension<Input, Output, Error, T> =
        GetOutputExtension(inner, targetClass, callback)

    public fun <Input, Output : ExtensionsRef, Error : Any> intoLayer(
        inner: Service<Input, Output, Error>,
    ): GetOutputExtension<Input, Output, Error, T> = layer(inner)

    override fun toString(): String = "GetOutputExtensionLayer(${targetClass.simpleName})"

    public companion object {
        public fun <T : Any> of(
            targetClass: KClass<T>,
            callback: ExtensionCallback<T>,
        ): GetOutputExtensionLayer<T> = GetOutputExtensionLayer(targetClass, callback)

        public inline fun <reified T : Any> new(
            callback: ExtensionCallback<T>,
        ): GetOutputExtensionLayer<T> = GetOutputExtensionLayer(T::class, callback)
    }
}

/**
 * Middleware for retrieving a value from output extensions.
 */
public class GetOutputExtension<Input, Output : ExtensionsRef, Error : Any, T : Any>(
    public val inner: Service<Input, Output, Error>,
    public val targetClass: KClass<T>,
    private val callback: ExtensionCallback<T>,
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
        public fun <Input, Output : ExtensionsRef, Error : Any, T : Any> of(
            inner: Service<Input, Output, Error>,
            targetClass: KClass<T>,
            callback: ExtensionCallback<T>,
        ): GetOutputExtension<Input, Output, Error, T> =
            GetOutputExtension(inner, targetClass, callback)

        public inline fun <Input, Output : ExtensionsRef, Error : Any, reified T : Any> new(
            inner: Service<Input, Output, Error>,
            callback: ExtensionCallback<T>,
        ): GetOutputExtension<Input, Output, Error, T> =
            GetOutputExtension(inner, T::class, callback)
    }
}

