// port-lint: source stream/read.rs
package io.github.kotlinmania.ramacore.stream

import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.Context
import io.github.kotlinmania.tokio.io.Poll
import io.github.kotlinmania.tokio.io.ReadBuf
import kotlin.math.min

/**
 * Reads bytes from an [AsyncRead] into [dest] starting at [offset] up to [length] bytes.
 * Returns the number of bytes read, or 0 if EOF.
 */
public suspend fun AsyncRead.read(
    dest: ByteArray,
    offset: Int = 0,
    length: Int = dest.size - offset,
): Int {
    if (length <= 0) return 0
    val readBuf = ReadBuf(UByteArray(length))
    return when (val poll = pollRead(Context(), readBuf)) {
        is Poll.Ready -> {
            poll.value.getOrThrow()
            val filled = readBuf.filled()
            for (i in filled.indices) {
                dest[offset + i] = filled[i].toByte()
            }
            filled.size
        }
        else -> 0
    }
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

    override fun pollRead(
        cx: Context,
        buf: ReadBuf,
    ): Poll<Result<Unit>> {
        if (position >= data.size) return Poll.ready(Result.success(Unit))
        val toRead = min(buf.remaining(), remaining())
        if (toRead > 0) {
            val slice = UByteArray(toRead) { idx -> data[position + idx].toUByte() }
            buf.putSlice(slice)
            position += toRead
        }
        return Poll.ready(Result.success(Unit))
    }

    public suspend fun read(
        dest: ByteArray,
        offset: Int = 0,
        length: Int = dest.size - offset,
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

    override fun pollRead(
        cx: Context,
        buf: ReadBuf,
    ): Poll<Result<Unit>> {
        if (this.offset >= data.size) return Poll.ready(Result.success(Unit))
        val toCopy = min(buf.remaining(), remaining())
        if (toCopy > 0) {
            val slice = UByteArray(toCopy) { idx -> data[this.offset + idx].toUByte() }
            buf.putSlice(slice)
            this.offset += toCopy
        }
        return Poll.ready(Result.success(Unit))
    }

    public suspend fun read(
        dest: ByteArray,
        offset: Int = 0,
        length: Int = dest.size - offset,
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

    override fun pollRead(
        cx: Context,
        buf: ReadBuf,
    ): Poll<Result<Unit>> {
        if (!doneFirst) {
            val before = buf.filled().size
            when (val res = first.pollRead(cx, buf)) {
                is Poll.Ready -> {
                    if (res.value.isFailure) return res
                    if (buf.filled().size == before) {
                        doneFirst = true
                    } else {
                        return Poll.ready(Result.success(Unit))
                    }
                }
                else -> return Poll.pending()
            }
        }
        return second.pollRead(cx, buf)
    }

    public suspend fun read(
        dest: ByteArray,
        offset: Int = 0,
        length: Int = dest.size - offset,
    ): Int {
        if (!doneFirst) {
            val n = (first as AsyncRead).read(dest, offset, length)
            if (n == 0) {
                doneFirst = true
            } else {
                return n
            }
        }
        return (second as AsyncRead).read(dest, offset, length)
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
