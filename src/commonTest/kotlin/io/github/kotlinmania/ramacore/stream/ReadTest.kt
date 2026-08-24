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

    @Test
    fun testChainReader() =
        runTest {
            val r1 = HeapReader.new(byteArrayOf(1, 2))
            val r2 = HeapReader.new(byteArrayOf(3, 4))
            val chain = ChainReader.new(r1, r2)

            val buf = ByteArray(4)
            val n1 = chain.read(buf, 0, 2)
            assertEquals(2, n1)
            val n2 = chain.read(buf, 2, 2)
            assertEquals(2, n2)

            assertContentEquals(byteArrayOf(1, 2, 3, 4), buf)
        }
}
