// port-lint: source rama-core/src/service/svc.rs
package io.github.kotlinmania.ramacore.service

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.combinators.Either
import io.github.kotlinmania.ramacore.combinators.Either3
import io.github.kotlinmania.ramacore.combinators.Either4
import io.github.kotlinmania.ramacore.combinators.Either5
import io.github.kotlinmania.ramacore.combinators.Either6
import io.github.kotlinmania.ramacore.combinators.Either7
import io.github.kotlinmania.ramacore.combinators.Either8
import io.github.kotlinmania.ramacore.combinators.Either9

/**
 * A [Service] that produces rama services to serve given an input.
 */
public interface Service<in Input, out Output : Any, out Error : Any> {
    /**
     * Serve an output or error for the given input.
     */
    public suspend fun serve(input: Input): RamaResult<Output, Error>

    /**
     * Box this service to allow for dynamic dispatch.
     */
    public fun boxed(): BoxService<Input, Output, Error> = BoxService(this)
}

/**
 * A boxed [Service], to serve inputs with dynamic dispatch.
 */
public class BoxService<in Input, out Output : Any, out Error : Any>(
    private val inner: Service<Input, Output, Error>,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> = inner.serve(input)

    override fun boxed(): BoxService<Input, Output, Error> = this

    override fun toString(): String = "BoxService"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            service: Service<Input, Output, Error>,
        ): BoxService<Input, Output, Error> = BoxService(service)
    }
}

/**
 * A [Service] which will simply return the given input as success.
 */
public class MirrorService<Input : Any> : Service<Input, Input, Nothing> {
    override suspend fun serve(input: Input): RamaResult<Input, Nothing> = RamaResult.ok(input)

    override fun toString(): String = "MirrorService"

    public companion object {
        public fun <Input : Any> new(): MirrorService<Input> = MirrorService()
    }
}

/**
 * Error indicating an input was rejected.
 */
public class RejectError(
    public val message: String = "Input rejected",
) {
    override fun toString(): String = message

    override fun equals(other: Any?): Boolean = other is RejectError && other.message == message

    override fun hashCode(): Int = message.hashCode()

    public companion object {
        public fun new(): RejectError = RejectError()
    }
}

/**
 * A [Service] which always rejects with an error.
 */
public class RejectService<out Output : Any, out Error : Any>(
    public val error: Error,
) : Service<Any?, Output, Error> {
    override suspend fun serve(input: Any?): RamaResult<Output, Error> = RamaResult.err(error)

    override fun toString(): String = "RejectService(error=$error)"

    public companion object {
        public fun <Output : Any, Error : Any> new(error: Error): RejectService<Output, Error> =
            RejectService(error)

        public fun <Output : Any> default(): RejectService<Output, RejectError> =
            RejectService(RejectError())
    }
}

/**
 * Serve an input using a 2-variant [Either] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either<Service<Input, Output, Error>, Service<Input, Output, Error>>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either.A -> value.serve(input)
        is Either.B -> value.serve(input)
    }

/**
 * Serve an input using a 3-variant [Either3] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either3<Service<Input, Output, Error>, Service<Input, Output, Error>, Service<Input, Output, Error>>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either3.A -> value.serve(input)
        is Either3.B -> value.serve(input)
        is Either3.C -> value.serve(input)
    }

/**
 * Serve an input using a 4-variant [Either4] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either4<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either4.A -> value.serve(input)
        is Either4.B -> value.serve(input)
        is Either4.C -> value.serve(input)
        is Either4.D -> value.serve(input)
    }

/**
 * Serve an input using a 5-variant [Either5] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either5<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either5.A -> value.serve(input)
        is Either5.B -> value.serve(input)
        is Either5.C -> value.serve(input)
        is Either5.D -> value.serve(input)
        is Either5.E -> value.serve(input)
    }

/**
 * Serve an input using a 6-variant [Either6] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either6<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either6.A -> value.serve(input)
        is Either6.B -> value.serve(input)
        is Either6.C -> value.serve(input)
        is Either6.D -> value.serve(input)
        is Either6.E -> value.serve(input)
        is Either6.F -> value.serve(input)
    }

/**
 * Serve an input using a 7-variant [Either7] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either7<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either7.A -> value.serve(input)
        is Either7.B -> value.serve(input)
        is Either7.C -> value.serve(input)
        is Either7.D -> value.serve(input)
        is Either7.E -> value.serve(input)
        is Either7.F -> value.serve(input)
        is Either7.G -> value.serve(input)
    }

/**
 * Serve an input using a 8-variant [Either8] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either8<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either8.A -> value.serve(input)
        is Either8.B -> value.serve(input)
        is Either8.C -> value.serve(input)
        is Either8.D -> value.serve(input)
        is Either8.E -> value.serve(input)
        is Either8.F -> value.serve(input)
        is Either8.G -> value.serve(input)
        is Either8.H -> value.serve(input)
    }

/**
 * Serve an input using a 9-variant [Either9] service.
 */
public suspend fun <Input, Output : Any, Error : Any> Either9<
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
    Service<Input, Output, Error>,
>.serve(
    input: Input,
): RamaResult<Output, Error> =
    when (this) {
        is Either9.A -> value.serve(input)
        is Either9.B -> value.serve(input)
        is Either9.C -> value.serve(input)
        is Either9.D -> value.serve(input)
        is Either9.E -> value.serve(input)
        is Either9.F -> value.serve(input)
        is Either9.G -> value.serve(input)
        is Either9.H -> value.serve(input)
        is Either9.I -> value.serve(input)
    }
