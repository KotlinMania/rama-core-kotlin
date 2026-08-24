// port-lint: source stream/read.rs
package io.github.kotlinmania.ramacore.stream

import kotlin.math.min

/**
 * Common reader interface for byte streams.
 */
public interface AsyncRead {
    /**
     * Reads bytes into [dest] starting at [offset] up to [length] bytes.
     * Returns the number of bytes read, or 0 if EOF.
     */
    public suspend fun read(
        dest: ByteArray,
        offset: Int = 0,
        length: Int = dest.size - offset,
    ): Int
}

/**
 * Reader for reading from a heap-allocated byte buffer.
 */
public class HeapReader(
    private val data: ByteArray,
) : AsyncRead {
    private var position: Int = 0

    /**
     * Number of remaining bytes available to read.
     */
    public fun remaining(): Int = data.size - position

    /**
     * Returns `true` if there are any remaining bytes.
     */
    public fun hasRemaining(): Boolean = remaining() > 0

    /**
     * Skips up to [n] bytes.
     */
    public fun skip(n: Int) {
        position = min(position + n, data.size)
    }

    override suspend fun read(
        dest: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        if (position >= data.size) return 0
        val toRead = min(length, remaining())
        data.copyInto(dest, destinationOffset = offset, startIndex = position, endIndex = position + toRead)
        position += toRead
        return toRead
    }

    public companion object {
        /**
         * Creates a new [HeapReader] from the given [data].
         */
        public fun new(data: ByteArray): HeapReader = HeapReader(data.copyOf())

        /**
         * Creates an empty [HeapReader].
         */
        public fun empty(): HeapReader = HeapReader(ByteArray(0))

        /**
         * Creates a [HeapReader] from a UTF-8 string.
         */
        public fun fromString(text: String): HeapReader = HeapReader(text.encodeToByteArray())
    }
}

/**
 * Reader for reading from a fixed-size byte buffer.
 */
public class StackReader(
    private val data: ByteArray,
) : AsyncRead {
    private var offset: Int = 0

    /**
     * Skip up to [n] bytes.
     */
    public fun skip(n: Int) {
        offset = min(offset + n, data.size)
    }

    /**
     * Number of remaining bytes available to read.
     */
    public fun remaining(): Int = data.size - offset

    /**
     * Returns `true` if all bytes have been consumed.
     */
    public fun hasRemaining(): Boolean = remaining() > 0

    override suspend fun read(
        dest: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        if (this.offset >= data.size) return 0
        val toCopy = min(length, remaining())
        if (toCopy > 0) {
            data.copyInto(dest, destinationOffset = offset, startIndex = this.offset, endIndex = this.offset + toCopy)
            this.offset += toCopy
        }
        return toCopy
    }

    public companion object {
        /**
         * Creates a new [StackReader] with the specified byte data.
         */
        public fun new(data: ByteArray): StackReader = StackReader(data.copyOf())
    }
}

/**
 * Reader that chains two readers together in sequence.
 */
public class ChainReader<T : AsyncRead, U : AsyncRead>(
    public val first: T,
    public val second: U,
) : AsyncRead {
    private var doneFirst: Boolean = false

    override suspend fun read(
        dest: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        if (!doneFirst) {
            val n = first.read(dest, offset, length)
            if (n == 0) {
                doneFirst = true
            } else {
                return n
            }
        }
        return second.read(dest, offset, length)
    }

    public companion object {
        /**
         * Creates a new [ChainReader] with the specified readers.
         */
        public fun <T : AsyncRead, U : AsyncRead> new(
            first: T,
            second: U,
        ): ChainReader<T, U> = ChainReader(first, second)
    }
}
