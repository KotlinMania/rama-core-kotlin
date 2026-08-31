// port-lint: source rama-core/src/stream/rewind.rs
package io.github.kotlinmania.ramacore.stream

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef
import kotlin.math.min

/**
 * Combine a buffer with an IO stream, rewinding reads to use the buffer.
 */
public class Rewind<T : AsyncRead>(
    public val inner: T,
    private var pre: ByteArray? = null,
) : AsyncRead,
    ExtensionsRef,
    ExtensionsMut {
    private var preOffset: Int = 0

    override fun extensions(): Extensions =
        if (inner is ExtensionsRef) inner.extensions() else Extensions()

    override fun extensionsMut(): Extensions =
        if (inner is ExtensionsMut) inner.extensionsMut() else extensions()

    /**
     * Rewinds the stream with the provided byte array prefix.
     */
    public fun rewind(bytes: ByteArray) {
        this.pre = bytes.copyOf()
        this.preOffset = 0
    }

    override suspend fun read(
        dest: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        val prefix = pre
        if (prefix != null && preOffset < prefix.size) {
            val available = prefix.size - preOffset
            val toCopy = min(length, available)
            prefix.copyInto(dest, destinationOffset = offset, startIndex = preOffset, endIndex = preOffset + toCopy)
            preOffset += toCopy
            if (preOffset >= prefix.size) {
                pre = null
                preOffset = 0
            }
            return toCopy
        }
        return inner.read(dest, offset, length)
    }

    public companion object {
        /**
         * Creates a new [Rewind] wrapping the given [io] stream.
         */
        public fun <T : AsyncRead> new(io: T): Rewind<T> = Rewind(io, null)

        /**
         * Creates a new [Rewind] with an initial buffered prefix.
         */
        public fun <T : AsyncRead> newBuffered(
            io: T,
            buf: ByteArray,
        ): Rewind<T> = Rewind(io, buf.copyOf())
    }
}
