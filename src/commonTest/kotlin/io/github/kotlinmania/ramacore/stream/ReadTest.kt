// port-lint: tests stream/read.rs
package io.github.kotlinmania.ramacore.stream

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReadTest {
    @Test
    fun testHeapReader() =
        runTest {
            val data = byteArrayOf(1, 2, 3, 4, 5)
            val reader = HeapReader.new(data)

            assertEquals(5, reader.remaining())
            assertTrue(reader.hasRemaining())

            val buf = ByteArray(3)
            val read = reader.read(buf)
            assertEquals(3, read)
            assertContentEquals(byteArrayOf(1, 2, 3), buf)
            assertEquals(2, reader.remaining())

            val buf2 = ByteArray(5)
            val read2 = reader.read(buf2)
            assertEquals(2, read2)
            assertEquals(0, reader.remaining())
            assertFalse(reader.hasRemaining())
        }

    @Test
    fun testStackReader() =
        runTest {
            val data = byteArrayOf(10, 20, 30)
            val reader = StackReader.new(data)

            assertEquals(3, reader.remaining())
            val buf = ByteArray(2)
            val n = reader.read(buf)
            assertEquals(2, n)
            assertContentEquals(byteArrayOf(10, 20), buf)
        }

    private suspend fun testMultiReadAsync(
        stream: AsyncRead,
        chunkSize: Int,
        cases: List<String>,
    ) {
        val buf = ByteArray(chunkSize)
        for ((i, case) in cases.withIndex()) {
            val n = stream.read(buf, 0, chunkSize)
            assertEquals(case.length, n, "step #${i + 1} for cases: $cases")
            assertEquals(case, buf.decodeToString(0, n), "step #${i + 1} for cases: $cases")
        }
    }

    @Test
    fun testHeapReaderChunked() =
        runTest {
            // N = 5, data = "" -> [""]
            testMultiReadAsync(HeapReader.new("".encodeToByteArray()), 5, listOf(""))

            // N = 5, data = "hello world" -> ["hello", " worl", "d", ""]
            testMultiReadAsync(
                HeapReader.new("hello world".encodeToByteArray()),
                5,
                listOf("hello", " worl", "d", ""),
            )

            // N = 10, data = "hello world" -> ["hello worl", "d", ""]
            testMultiReadAsync(
                HeapReader.new("hello world".encodeToByteArray()),
                10,
                listOf("hello worl", "d", ""),
            )
        }

    @Test
    fun testStackReaderChunked() =
        runTest {
            // N = 5, data = "" -> [""]
            testMultiReadAsync(StackReader.new("".encodeToByteArray()), 5, listOf(""))

            // N = 5, data = "hello world" -> ["hello", " worl", "d", ""]
            testMultiReadAsync(
                StackReader.new("hello world".encodeToByteArray()),
                5,
                listOf("hello", " worl", "d", ""),
            )

            // N = 10, data = "hello world" -> ["hello worl", "d", ""]
            testMultiReadAsync(
                StackReader.new("hello world".encodeToByteArray()),
                10,
                listOf("hello worl", "d", ""),
            )
        }

    @Test
    fun testChainReaderChunked() =
        runTest {
            // N = 5, r1 = "", r2 = "" -> [""]
            testMultiReadAsync(
                ChainReader.new(
                    HeapReader.new("".encodeToByteArray()),
                    HeapReader.new("".encodeToByteArray()),
                ),
                5,
                listOf(""),
            )

            // N = 5, r1 = "hello world", r2 = "" -> ["hello", " worl", "d", ""]
            testMultiReadAsync(
                ChainReader.new(
                    HeapReader.new("hello world".encodeToByteArray()),
                    HeapReader.new("".encodeToByteArray()),
                ),
                5,
                listOf("hello", " worl", "d", ""),
            )

            // N = 5, r1 = "hello ", r2 = "world" -> ["hello", " ", "world", ""]
            testMultiReadAsync(
                ChainReader.new(
                    HeapReader.new("hello ".encodeToByteArray()),
                    HeapReader.new("world".encodeToByteArray()),
                ),
                5,
                listOf("hello", " ", "world", ""),
            )

            // N = 5, r1 = "", r2 = "hello world" -> ["hello", " worl", "d", ""]
            testMultiReadAsync(
                ChainReader.new(
                    HeapReader.new("".encodeToByteArray()),
                    HeapReader.new("hello world".encodeToByteArray()),
                ),
                5,
                listOf("hello", " worl", "d", ""),
            )
        }
}
