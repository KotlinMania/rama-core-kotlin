// port-lint: tests stream/json/stream/write.rs
package io.github.kotlinmania.ramacore.stream.json.stream

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
private data class WriteTestStruct(
    val key: Long,
    val value: Long,
)

class WriteTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun pendingStreamResultsInPendingItem() =
        runTest {
            val items = listOf(WriteTestStruct(1, 2))
            val writeStream = JsonWriteStream.new(items.asFlow()) { json.encodeToString(it) }
            val lines = writeStream.toFlow().toList()
            assertEquals(listOf("{\"key\":1,\"value\":2}"), lines)
        }

    @Test
    fun emptyStreamResultsInEmptyResults() =
        runTest {
            val writeStream = JsonWriteStream.new(emptyFlow<WriteTestStruct>()) { json.encodeToString(it) }
            val lines = writeStream.toFlow().toList()
            assertTrue(lines.isEmpty())
        }

    @Test
    fun iterWithSingleJsonLine() =
        runTest {
            val items = listOf(WriteTestStruct(1, 2))
            val writeStream = JsonWriteStream.new(items.asFlow()) { json.encodeToString(it) }
            val lines = writeStream.toFlow().toList()
            assertEquals(listOf("{\"key\":1,\"value\":2}"), lines)
        }

    @Test
    fun iterWithTwoJsonLines() =
        runTest {
            val items = listOf(WriteTestStruct(1, 2), WriteTestStruct(3, 4))
            val writeStream = JsonWriteStream.new(items.asFlow()) { json.encodeToString(it) }
            val lines = writeStream.toFlow().toList()
            assertEquals(listOf("{\"key\":1,\"value\":2}", "\n{\"key\":3,\"value\":4}"), lines)
        }
}
