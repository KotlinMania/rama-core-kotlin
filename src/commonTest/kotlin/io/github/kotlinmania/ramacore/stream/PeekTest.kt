// port-lint: tests stream/peek.rs
package io.github.kotlinmania.ramacore.stream

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PeekTest {
    @Test
    fun testPeekStream() =
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
}
