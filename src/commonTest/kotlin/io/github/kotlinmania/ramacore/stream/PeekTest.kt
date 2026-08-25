// port-lint: tests stream/peek.rs
package io.github.kotlinmania.ramacore.stream

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PeekTest {
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
    fun testPeekStreamBasic() =
        runTest {
            val peekReader = HeapReader.new(byteArrayOf(1, 2))
            val innerReader = HeapReader.new(byteArrayOf(3, 4, 5))
            val stream = PeekStream.new(peekReader, innerReader)

            val buf = ByteArray(5)
            val n1 = stream.read(buf, 0, 2)
            assertEquals(2, n1)

            val n2 = stream.read(buf, 2, 3)
            assertEquals(3, n2)

            assertContentEquals(byteArrayOf(1, 2, 3, 4, 5), buf)
        }

    @Test
    fun testPeekStreamReadCases() =
        runTest {
            // Case 1: N = 10, peek="hello", inner=" world"
            run {
                val peek = HeapReader.new("hello".encodeToByteArray())
                val inner = HeapReader.new(" world".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 10, listOf("hello", " world", ""))
            }

            // Case 2: N = 5, peek="hello world", inner="next data"
            run {
                val peek = HeapReader.new("hello world".encodeToByteArray())
                val inner = HeapReader.new("next data".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 5, listOf("hello", " worl", "d", "next ", "data", ""))
            }

            // Case 3: N = 2, peek="peek", inner="inner"
            run {
                val peek = HeapReader.new("peek".encodeToByteArray())
                val inner = HeapReader.new("inner".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 2, listOf("pe", "ek", "in", "ne", "r", ""))
            }

            // Case 4: N = 8, peek="", inner="inner data"
            run {
                val peek = HeapReader.new("".encodeToByteArray())
                val inner = HeapReader.new("inner data".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 8, listOf("inner da", "ta", ""))
            }

            // Case 5: N = 10, peek="", inner="inner data"
            run {
                val peek = HeapReader.new("".encodeToByteArray())
                val inner = HeapReader.new("inner data".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 10, listOf("inner data", ""))
            }

            // Case 6: N = 12, peek="", inner="inner data"
            run {
                val peek = HeapReader.new("".encodeToByteArray())
                val inner = HeapReader.new("inner data".encodeToByteArray())
                val stream = PeekStream.new(peek, inner)
                testMultiReadAsync(stream, 12, listOf("inner data", ""))
            }
        }
}
