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
public class HijackService<Input, Output : Any, Error : Any>(
    public val inner: Service<Input, Output, Error>,
    public val hijack: Service<Input, Output, Error>,
    public val matcher: Matcher<Input>,
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
        public fun <Input, Output : Any, Error : Any> new(
            inner: Service<Input, Output, Error>,
            hijack: Service<Input, Output, Error>,
            matcher: Matcher<Input>,
        ): HijackService<Input, Output, Error> =
            HijackService(inner, hijack, matcher)
    }
}

/**
 * Layer to hijack requests when matching a [Matcher].
 */
public class HijackLayer<Input, Output : Any, Error : Any>(
    public val matcher: Matcher<Input>,
    public val hijack: Service<Input, Output, Error>,
) : Layer<Service<Input, Output, Error>, HijackService<Input, Output, Error>> {
    override fun layer(
        inner: Service<Input, Output, Error>,
    ): HijackService<Input, Output, Error> =
        HijackService(inner, hijack, matcher)

    override fun toString(): String = "HijackLayer($matcher, $hijack)"

    public companion object {
        public fun <Input, Output : Any, Error : Any> new(
            matcher: Matcher<Input>,
            hijack: Service<Input, Output, Error>,
        ): HijackLayer<Input, Output, Error> = HijackLayer(matcher, hijack)
    }
}
