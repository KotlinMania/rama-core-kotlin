// port-lint: tests stream/rewind.rs
package io.github.kotlinmania.ramacore.stream

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class RewindTest {
    @Test
    fun testRewindPartial() =
        runTest {
            val underlying = byteArrayOf(104, 101, 108, 108, 111)
            val mock = HeapReader.new(underlying)
            val stream = Rewind.new(mock)

            val buf1 = ByteArray(2)
            val n1 = stream.read(buf1)
            assertEquals(2, n1)
            assertContentEquals(byteArrayOf(104, 101), buf1)

            stream.rewind(buf1)

            val fullBuf = ByteArray(5)
            val n2 = stream.read(fullBuf)
            assertEquals(2, n2)

            val n3 = stream.read(fullBuf, offset = 2, length = 3)
            assertEquals(3, n3)

            assertContentEquals(underlying, fullBuf)
        }

    @Test
    fun testRewindFull() =
        runTest {
            val underlying = byteArrayOf(104, 101, 108, 108, 111)
            val mock = HeapReader.new(underlying)
            val stream = Rewind.new(mock)

            val buf = ByteArray(5)
            val n = stream.read(buf)
            assertEquals(5, n)
            assertContentEquals(underlying, buf)

            stream.rewind(buf)

            val buf2 = ByteArray(5)
            val n2 = stream.read(buf2)
            assertEquals(5, n2)
            assertContentEquals(underlying, buf2)
        }
}
