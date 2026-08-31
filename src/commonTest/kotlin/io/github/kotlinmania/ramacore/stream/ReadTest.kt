// port-lint: tests rama-core/src/stream/read.rs
package io.github.kotlinmania.ramacore.stream

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadTest {
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

    inner class TestCase<R : AsyncRead>(
        val reader: () -> R,
        val chunkSize: Int,
        val expectedReads: List<String>,
    ) {
        suspend fun testSyncAndAsync() {
            val stream = reader()
            val buf = ByteArray(chunkSize)
            for ((i, case) in expectedReads.withIndex()) {
                val n = stream.read(buf, 0, chunkSize)
                assertEquals(case.length, n, "[$chunkSize][async] step #${i + 1} for cases: $expectedReads")
                assertEquals(case, buf.decodeToString(0, n), "[$chunkSize][async] step #${i + 1} for cases: $expectedReads")
            }
            testMultiReadSync(reader(), chunkSize, expectedReads)
        }
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
    fun testHeapReader() =
        runTest {
            TestCase({ HeapReader.new("".encodeToByteArray()) }, 5, listOf("")).testSyncAndAsync()
            TestCase({ HeapReader.new("hello world".encodeToByteArray()) }, 5, listOf("hello", " worl", "d", "")).testSyncAndAsync()
            TestCase({ HeapReader.new("hello world".encodeToByteArray()) }, 10, listOf("hello worl", "d", "")).testSyncAndAsync()
        }

    @Test
    fun testStackReader() =
        runTest {
            TestCase({ StackReader.new("".encodeToByteArray()) }, 5, listOf("")).testSyncAndAsync()
            TestCase({ StackReader.new("hello world".encodeToByteArray()) }, 5, listOf("hello", " worl", "d", "")).testSyncAndAsync()
            TestCase({ StackReader.new("hello world".encodeToByteArray()) }, 10, listOf("hello worl", "d", "")).testSyncAndAsync()
        }

    @Test
    fun testChainReader() =
        runTest {
            TestCase({
                ChainReader.new(
                    HeapReader.new("".encodeToByteArray()),
                    HeapReader.new("".encodeToByteArray()),
                )
            }, 5, listOf("")).testSyncAndAsync()

            TestCase({
                ChainReader.new(
                    HeapReader.new("hello world".encodeToByteArray()),
                    HeapReader.new("".encodeToByteArray()),
                )
            }, 5, listOf("hello", " worl", "d", "")).testSyncAndAsync()

            TestCase({
                ChainReader.new(
                    HeapReader.new("hello ".encodeToByteArray()),
                    HeapReader.new("world".encodeToByteArray()),
                )
            }, 5, listOf("hello", " ", "world", "")).testSyncAndAsync()

            TestCase({
                ChainReader.new(
                    HeapReader.new("".encodeToByteArray()),
                    HeapReader.new("hello world".encodeToByteArray()),
                )
            }, 5, listOf("hello", " worl", "d", "")).testSyncAndAsync()
        }
}
