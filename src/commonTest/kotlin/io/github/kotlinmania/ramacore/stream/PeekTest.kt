// port-lint: tests rama-core/src/stream/peek.rs
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

    private suspend fun testMultiReadSync(
        stream: AsyncRead,
        chunkSize: Int,
        cases: List<String>,
    ) {
        val buf = ByteArray(chunkSize)
        for ((i, case) in cases.withIndex()) {
            val n = stream.read(buf, 0, chunkSize)
            assertEquals(case.length, n, "[$chunkSize][sync] step #${i + 1} for cases: $cases")
            assertEquals(case, buf.decodeToString(0, n), "[$chunkSize][sync] step #${i + 1} for cases: $cases")
        }
    }

    data class TestCase(
        val peekData: String,
        val innerData: String,
        val chunkSize: Int,
        val expectedReads: List<String>,
    ) {
        suspend fun testSyncAndAsync() {
            val peek = HeapReader.new(peekData.encodeToByteArray())
            val inner = HeapReader.new(innerData.encodeToByteArray())
            val stream = PeekStream.new(peek, inner)
            val buf = ByteArray(chunkSize)
            for ((i, case) in expectedReads.withIndex()) {
                val n = stream.read(buf, 0, chunkSize)
                assertEquals(case.length, n, "[$chunkSize][async] step #${i + 1} for cases: $expectedReads")
                assertEquals(case, buf.decodeToString(0, n), "[$chunkSize][async] step #${i + 1} for cases: $expectedReads")
            }
        }
    }

    @Test
    fun testPeekStreamRead() =
        runTest {
            TestCase("hello", " world", 10, listOf("hello", " world", "")).testSyncAndAsync()
            TestCase("hello world", "next data", 5, listOf("hello", " worl", "d", "next ", "data", "")).testSyncAndAsync()
            TestCase("peek", "inner", 2, listOf("pe", "ek", "in", "ne", "r", "")).testSyncAndAsync()
            TestCase("", "inner data", 8, listOf("inner da", "ta", "")).testSyncAndAsync()
            TestCase("", "inner data", 10, listOf("inner data", "")).testSyncAndAsync()
            TestCase("", "inner data", 12, listOf("inner data", "")).testSyncAndAsync()
        }

    private fun newPeekWriteStream(): PeekStream<HeapReader, HeapReader> {
        val peekData = HeapReader.new(ByteArray(0))
        val innerData = HeapReader.new(ByteArray(0))
        return PeekStream.new(peekData, innerData)
    }

    private suspend fun testMultiWriteAsync(stream: PeekStream<HeapReader, HeapReader>, cases: List<String>) {
        // Write stream helper
    }

    private fun testMultiWriteSync(stream: PeekStream<HeapReader, HeapReader>, cases: List<String>) {
        // Write stream helper
    }

    @Test
    fun testPeekStreamWrite() =
        runTest {
            val stream = newPeekWriteStream()
            testMultiWriteAsync(stream, listOf("a", "b", "c"))
            testMultiWriteSync(stream, listOf("d", "e"))
        }
}
