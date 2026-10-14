// port-lint: source stream/peek.rs
package io.github.kotlinmania.ramacore.stream

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.Context
import io.github.kotlinmania.tokio.io.Poll
import io.github.kotlinmania.tokio.io.ReadBuf

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

    override fun pollRead(
        cx: Context,
        buf: ReadBuf,
    ): Poll<Result<Unit>> {
        if (!donePeek) {
            val before = buf.filled().size
            when (val res = peek.pollRead(cx, buf)) {
                is Poll.Ready -> {
                    if (res.value.isFailure) return res
                    if (buf.filled().size == before) {
                        donePeek = true
                    } else {
                        return Poll.ready(Result.success(Unit))
                    }
                }
                else -> return Poll.pending()
            }
        }
        return inner.pollRead(cx, buf)
    }

    public suspend fun read(
        dest: ByteArray,
        offset: Int = 0,
        length: Int = dest.size - offset,
    ): Int {
        if (!donePeek) {
            val n = (peek as AsyncRead).read(dest, offset, length)
            if (n == 0) {
                donePeek = true
            } else {
                return n
            }
        }
        return (inner as AsyncRead).read(dest, offset, length)
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
