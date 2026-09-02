// port-lint: tests stream/json/engine.rs
package io.github.kotlinmania.ramacore.stream.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Serializable
private data class TestStruct(
    val key: Long,
    val value: Long,
)

class EngineTest {
    private val json = Json { ignoreUnknownKeys = true }

    private fun collectOutput(engine: NdjsonEngine<TestStruct>): List<Result<TestStruct>> {
        val list = mutableListOf<Result<TestStruct>>()
        while (true) {
            val item = engine.pop() ?: break
            list.add(item)
        }
        return list
    }

    private fun configuredEngine(
        configure: (ParseConfig) -> ParseConfig,
    ): NdjsonEngine<TestStruct> {
        val config = configure(ParseConfig.DEFAULT)
        return NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
    }

    private fun engineWithEmptyLineHandling(
        emptyLineHandling: EmptyLineHandling,
    ): NdjsonEngine<TestStruct> =
        configuredEngine { it.withEmptyLineHandling(emptyLineHandling) }

    @Test
    fun noInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun incompleteInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"val")
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun singleExactInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"value\":4}\n")
        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(3, 4), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun singleItemSplitIntoTwoInputs() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":42,")
        engine.input("\"value\":24}\n")
        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(42, 24), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun twoItemsInSingleInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":1}\n{\"key\":2,\"value\":2}\n")
        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(1, 1), result.next().getOrThrow())
        assertEquals(TestStruct(2, 2), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun twoItemsInManyInputsWithRest() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":12,\"v")
        engine.input("alue\":3")
        engine.input("4}\n{\"key")
        engine.input("\":56,\"valu")
        engine.input("e\":78}\n{\"key\":")

        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(12, 34), result.next().getOrThrow())
        assertEquals(TestStruct(56, 78), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun inputCompletingPreviousRestThenMultipleCompleteItemsAndMoreRest() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":9,\"value\":")
        engine.input("8}\n{\"key\":7,\"value\":6}\n{\"key\":5,\"value\":4}\n{\"key\":")
        engine.input("3,\"value\":2}\n{")

        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(9, 8), result.next().getOrThrow())
        assertEquals(TestStruct(7, 6), result.next().getOrThrow())
        assertEquals(TestStruct(5, 4), result.next().getOrThrow())
        assertEquals(TestStruct(3, 2), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun carriageReturnHandledGracefully() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\r\n{\"key\":3,\"value\":4}\r\n")
        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(1, 2), result.next().getOrThrow())
        assertEquals(TestStruct(3, 4), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun whitespaceHandledGracefully() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("\t{ \"key\":\t13,  \"value\":   37 } \r\n")
        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(13, 37), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun erroneousEntryEmittedAsJsonError() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1}\n{\"key\":1,\"value\":1}\n")
        val result = collectOutput(engine).iterator()
        assertTrue(result.next().isFailure)
        assertTrue(result.next().isSuccess)
        assertFalse(result.hasNext())
    }

    @Test
    fun errorFromSplitEntry() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":100,\"value\":200}\n{\"key\":")
        engine.input("\"should be a number\",\"value\":0}\n{\"key\":300,\"value\":400}\n")

        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(100, 200), result.next().getOrThrow())
        assertTrue(result.next().isFailure)
        assertEquals(TestStruct(300, 400), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }

    @Test
    fun oldDataIsDiscarded() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        val count = 20
        engine.input("{ \"key\": 1, ")
        for (i in 0 until count - 1) {
            engine.input("\"value\": 2 }\r\n{ \"key\": 1, ")
        }
        engine.input("\"value\": 2 }\r\n")

        assertFalse(engine.hasInputPending)
        assertEquals(count, engine.queuedCount)
    }

    @Test
    fun raisesErrorWhenParsingEmptyLineInParseAlwaysMode() {
        val engine = engineWithEmptyLineHandling(EmptyLineHandling.ParseAlways)
        engine.input("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
        assertTrue(collectOutput(engine).any { it.isFailure })
    }

    @Test
    fun doesNotRaiseErrorWhenParsingEmptyLineInIgnoreEmptyMode() {
        val engine = engineWithEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        engine.input("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
        assertTrue(collectOutput(engine).all { it.isSuccess })
    }

    @Test
    fun doesNotRaiseErrorWhenParsingEmptyLineWithCarriageReturnInIgnoreEmptyMode() {
        val engine = engineWithEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        engine.input("{\"key\":1,\"value\":2}\r\n\r\n{\"key\":3,\"value\":4}\n")
        assertTrue(collectOutput(engine).all { it.isSuccess })
    }

    @Test
    fun raisesErrorWhenParsingNonEmptyBlankLineInIgnoreEmptyMode() {
        val engine = engineWithEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        engine.input("{\"key\":1,\"value\":2}\n \t\r\n{\"key\":3,\"value\":4}\n")
        assertTrue(collectOutput(engine).any { it.isFailure })
    }

    @Test
    fun doesNotRaiseErrorWhenParsingNonEmptyBlankLineInIgnoreBlankMode() {
        val engine = engineWithEmptyLineHandling(EmptyLineHandling.IgnoreBlank)
        engine.input("{\"key\":1,\"value\":2}\n \t\r\n{\"key\":3,\"value\":4}\n")
        assertTrue(collectOutput(engine).all { it.isSuccess })
    }

    @Test
    fun finalizeIgnoresRestIfParseRestIsFalse() {
        val engine = configuredEngine { it.withParseRest(false) }
        engine.input("{\"key\":1,\"value\":2}")
        engine.finish()
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun finalizeParsesValidRest() {
        val emptyLineHandlings = listOf(
            EmptyLineHandling.ParseAlways,
            EmptyLineHandling.IgnoreEmpty,
            EmptyLineHandling.IgnoreBlank,
        )

        for (emptyLineHandling in emptyLineHandlings) {
            val engine = configuredEngine {
                it.withEmptyLineHandling(emptyLineHandling).withParseRest(true)
            }
            engine.input("{\"key\":1,\"value\":2}")
            engine.finish()

            val result = collectOutput(engine).iterator()
            assertEquals(TestStruct(1, 2), result.next().getOrThrow())
            assertFalse(result.hasNext())
        }
    }

    @Test
    fun finalizeRaisesErrorOnInvalidRest() {
        val engine = configuredEngine { it.withParseRest(true) }
        engine.input("invalid json")
        engine.finish()

        val result = collectOutput(engine).iterator()
        assertTrue(result.next().isFailure)
        assertFalse(result.hasNext())
    }

    @Test
    fun finalizeIgnoresEmptyRestEvenIfEmptyLineHandlingIsParseAlways() {
        val engine = configuredEngine {
            it.withEmptyLineHandling(EmptyLineHandling.ParseAlways).withParseRest(true)
        }
        engine.finish()
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun finalizeIgnoresEmptyRestIfEmptyLineHandlingIsIgnoreEmpty() {
        val engine = configuredEngine {
            it.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty).withParseRest(true)
        }
        engine.finish()
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun finalizeDoesNotIgnoreNonEmptyBlankRestIfEmptyLineHandlingIsIgnoreEmpty() {
        val engine = configuredEngine {
            it.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty).withParseRest(true)
        }
        engine.input(" ")
        engine.finish()

        val result = collectOutput(engine).iterator()
        assertTrue(result.next().isFailure)
        assertFalse(result.hasNext())
    }

    @Test
    fun finalizeIgnoresNonEmptyBlankRestIfEmptyLineHandlingIsIgnoreBlank() {
        val engine = configuredEngine {
            it.withEmptyLineHandling(EmptyLineHandling.IgnoreBlank).withParseRest(true)
        }
        engine.input(" ")
        engine.finish()
        assertTrue(collectOutput(engine).isEmpty())
    }

    @Test
    fun finalizeIsIdempotent() {
        val engine = configuredEngine { it.withParseRest(true) }
        engine.input("{\"key\":13,\"value\":37}")
        engine.finish()
        engine.finish()

        val result = collectOutput(engine).iterator()
        assertEquals(TestStruct(13, 37), result.next().getOrThrow())
        assertFalse(result.hasNext())
    }
}
