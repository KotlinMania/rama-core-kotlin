// port-lint: tests stream/json/stream/read.rs
package io.github.kotlinmania.ramacore.stream.json.stream

import io.github.kotlinmania.ramacore.stream.json.EmptyLineHandling
import io.github.kotlinmania.ramacore.stream.json.ParseConfig
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
private data class ReadTestStruct(
    val key: Long,
    val value: Long,
)

private class SingleThenPanicIter(
    private var data: String?,
) : Iterator<String> {
    override fun hasNext(): Boolean = data != null

    override fun next(): String {
        val res = data ?: throw IllegalStateException("iterator queried twice")
        data = null
        return res
    }
}

class ReadTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun pendingStreamResultsInPendingItem() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}\n")
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2)), results)
        }

    @Test
    fun emptyStreamResultsInEmptyResults() =
        runTest {
            val flow = emptyFlow<String>()
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList()
            assertTrue(results.isEmpty())
        }

    @Test
    fun singletonIterWithSingleJsonLine() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}\n")
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2)), results)
        }

    @Test
    fun multipleIterItemsComposeSingleJsonLine() =
        runTest {
            val flow = flowOf("{\"key\":", "1,\"value\":", "2}\n")
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2)), results)
        }

    @Test
    fun wrappedStreamNotQueriedWhileSufficientDataRemains() =
        runTest {
            val iter = SingleThenPanicIter("{\"key\":1,\"value\":2}\n{\"key\":3,\"value\":4}\n")
            val flow = iter.asSequence().asFlow()
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2), ReadTestStruct(3, 4)), results)
        }

    @Test
    fun streamWithParseAlwaysConfigRespectsConfig() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
            val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.ParseAlways)
            val readStream = JsonReadStream.fromStringFlow(flow, config) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList()
            assertEquals(3, results.size)
            assertEquals(ReadTestStruct(1, 2), results[0].getOrThrow())
            assertTrue(results[1].isFailure)
            assertEquals(ReadTestStruct(3, 4), results[2].getOrThrow())
        }

    @Test
    fun streamWithIgnoreEmptyConfigRespectsConfig() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
            val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
            val readStream = JsonReadStream.fromStringFlow(flow, config) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2), ReadTestStruct(3, 4)), results)
        }

    @Test
    fun streamWithParseRestHandlesValidFinalization() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}")
            val config = ParseConfig.DEFAULT.withParseRest(true)
            val readStream = JsonReadStream.fromStringFlow(flow, config) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(ReadTestStruct(1, 2)), results)
        }

    @Test
    fun streamWithParseRestHandlesInvalidFinalization() =
        runTest {
            val flow = flowOf("invalid json")
            val config = ParseConfig.DEFAULT.withParseRest(true)
            val readStream = JsonReadStream.fromStringFlow(flow, config) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList()
            assertEquals(1, results.size)
            assertTrue(results[0].isFailure)
        }

    @Test
    fun streamWithoutParseRestDoesNotHandleFinalization() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}")
            val config = ParseConfig.DEFAULT.withParseRest(false)
            val readStream = JsonReadStream.fromStringFlow(flow, config) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList()
            assertTrue(results.isEmpty())
        }

    @Test
    fun fallibleStreamOperatesCorrectlyWithInterspersedErrors() =
        runTest {
            val flow = flowOf("{\"key\":1,\"value\":2}\n", "bad json\n", "{\"key\":3,\"value\":4}\n")
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<ReadTestStruct>(it) }
            val results = readStream.toFlow().toList()
            assertEquals(3, results.size)
            assertEquals(ReadTestStruct(1, 2), results[0].getOrThrow())
            assertTrue(results[1].isFailure)
            assertEquals(ReadTestStruct(3, 4), results[2].getOrThrow())
        }
}
