// port-lint: source rama-core/src/stream/peek.rs
package io.github.kotlinmania.ramacore.stream

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef

/**
 * A stream which has peeked some data of the inner stream,
 * to be read first prior to any other reading.
 */
public class PeekStream<P : AsyncRead, S : AsyncRead>(
    public val peek: P,
    public val inner: S,
) : AsyncRead,
    ExtensionsRef,
    ExtensionsMut {
    private var donePeek: Boolean = false

    override fun extensions(): Extensions =
        if (inner is ExtensionsRef) inner.extensions() else Extensions()

    override fun extensionsMut(): Extensions =
        if (inner is ExtensionsMut) inner.extensionsMut() else extensions()

    override suspend fun read(
        dest: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        if (!donePeek) {
            val n = peek.read(dest, offset, length)
            if (n == 0) {
                donePeek = true
            } else {
                return n
            }
        }
        return inner.read(dest, offset, length)
    }

    public companion object {
        /**
         * Creates a new [PeekStream] wrapping [peek] and [inner].
         */
        public fun <P : AsyncRead, S : AsyncRead> new(
            peek: P,
            inner: S,
        ): PeekStream<P, S> = PeekStream(peek, inner)
    }
}
