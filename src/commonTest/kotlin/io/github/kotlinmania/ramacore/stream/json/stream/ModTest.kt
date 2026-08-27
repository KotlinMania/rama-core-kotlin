// port-lint: tests stream/json/stream/mod.rs
package io.github.kotlinmania.ramacore.stream.json.stream

import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ModTest {
    @Test
    fun writeReadPendingEmpty() =
        runTest {
            val writeStream = JsonWriteStream.new(emptyFlow<Int>()) { it.toString() }
            val readStream = JsonReadStream.fromStringFlow(writeStream.toFlow()) { it.toInt() }
            val collected = readStream.toFlow().toList()
            assertTrue(collected.isEmpty())
        }

    @Test
    fun writeReadOnce() =
        runTest {
            val writeStream = JsonWriteStream.new(flowOf(1)) { it.toString() }
            val readStream = JsonReadStream.fromStringFlow(writeStream.toFlow()) { it.toInt() }
            val collected = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(1), collected)
        }

    @Test
    fun writeReadTwice() =
        runTest {
            val writeStream = JsonWriteStream.new(flowOf(4, 2)) { it.toString() }
            val readStream = JsonReadStream.fromStringFlow(writeStream.toFlow()) { it.toInt() }
            val collected = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(4, 2), collected)
        }
}
