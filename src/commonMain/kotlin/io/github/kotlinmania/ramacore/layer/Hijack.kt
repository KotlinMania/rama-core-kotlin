// port-lint: source layer/hijack.rs
package io.github.kotlinmania.ramacore.layer

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.matcher.Matcher
import io.github.kotlinmania.ramacore.service.Service

/**
 * Middleware to hijack requests to a service when they match a [Matcher].
 */
public class HijackService<Input, Output : Any, Error : Any, S : Service<Input, Output, Error>, H : Service<Input, Output, Error>, M : Matcher<Input>>(
    public val inner: S,
    public val hijack: H,
    public val matcher: M,
) : Service<Input, Output, Error> {
    override suspend fun serve(input: Input): RamaResult<Output, Error> {
        val ext = Extensions()
        if (matcher.matches(ext, input)) {
            if (input is ExtensionsMut) {
                input.extensionsMut().extend(ext)
            }
            return hijack.serve(input)
        }
        return inner.serve(input)
    }

    override fun toString(): String = "HijackService($inner, $hijack, $matcher)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, S : Service<Input, Output, Error>, H : Service<Input, Output, Error>, M : Matcher<Input>> new(
            inner: S,
            hijack: H,
            matcher: M,
        ): HijackService<Input, Output, Error, S, H, M> =
            HijackService(inner, hijack, matcher)
    }
}

/**
 * Layer to hijack requests when matching a [Matcher].
 */
public class HijackLayer<Input, Output : Any, Error : Any, H : Service<Input, Output, Error>, M : Matcher<Input>>(
    public val matcher: M,
    public val hijack: H,
) : Layer<Service<Input, Output, Error>, HijackService<Input, Output, Error, Service<Input, Output, Error>, H, M>> {
    override fun layer(
        inner: Service<Input, Output, Error>,
    ): HijackService<Input, Output, Error, Service<Input, Output, Error>, H, M> =
        HijackService(inner, hijack, matcher)

    override fun toString(): String = "HijackLayer($matcher, $hijack)"

    public companion object {
        public fun <Input, Output : Any, Error : Any, H : Service<Input, Output, Error>, M : Matcher<Input>> new(
            matcher: M,
            hijack: H,
        ): HijackLayer<Input, Output, Error, H, M> = HijackLayer(matcher, hijack)
    }
}
