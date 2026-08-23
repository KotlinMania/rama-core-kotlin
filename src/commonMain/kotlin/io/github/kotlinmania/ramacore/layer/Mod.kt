// port-lint: source layer/mod.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * A layer that produces a layered service (middleware wrapping an inner service).
 */
public fun interface Layer<in S, out OutS> {
    /**
     * Wrap the given service with the middleware, returning a new service.
     */
    public fun layer(inner: S): OutS

    /**
     * Wrap the given service with the middleware.
     */
    public fun intoLayer(inner: S): OutS = layer(inner)
}

/**
 * Two layers composed together into a stack.
 */
public class Stack<Inner : Layer<S, Mid>, Outer : Layer<Mid, Out>, S, Mid, Out>(
    public val inner: Inner,
    public val outer: Outer,
) : Layer<S, Out> {
    override fun layer(inner: S): Out = outer.layer(this.inner.layer(inner))

    override fun intoLayer(inner: S): Out = outer.intoLayer(this.inner.intoLayer(inner))

    override fun toString(): String = "Stack($inner, $outer)"
}

/**
 * Compose this layer with an [outer] layer.
 */
public fun <Inner : Layer<S, Mid>, Outer : Layer<Mid, Out>, S, Mid, Out> Inner.andThen(
    outer: Outer,
): Stack<Inner, Outer, S, Mid, Out> = Stack(this, outer)

/**
 * A [Service] created by wrapping a service with an optional [Layer].
 */
public class MaybeLayeredService<Input, Output : Any, Error : Any>(
    private val service: Service<Input, Output, Error>,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> = service.serve(input)

    override fun toString(): String = "MaybeLayeredService($service)"
}

/**
 * Apply this optional layer to [inner] service.
 */
public fun <S : Service<Input, Output, Error>, OutS : Service<Input, Output, Error>, Input, Output : Any, Error : Any> Layer<S, OutS>?.layerOptional(
    inner: S,
): Service<Input, Output, Error> =
    if (this != null) {
        MaybeLayeredService(this.layer(inner))
    } else {
        MaybeLayeredService(inner)
    }

/**
 * Layer type and utilities module ledger.
 */
internal object LayerModuleLedger
