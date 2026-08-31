// port-lint: tests stream/json/engine.rs
package io.github.kotlinmania.ramacore.stream.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Serializable
private data class TestStruct(
    val key: Long,
    val value: Long,
)

class EngineTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testNoInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        assertNull(engine.pop())
    }

    @Test
    fun testIncompleteInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"val")
        assertNull(engine.pop())
    }

    @Test
    fun testSingleExactInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"value\":4}\n")
        val item = engine.pop()?.getOrThrow()
        assertEquals(TestStruct(3, 4), item)
        assertNull(engine.pop())
    }

    @Test
    fun testSingleItemSplitIntoTwoInputs() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":42,")
        engine.input("\"value\":24}\n")
        val item = engine.pop()?.getOrThrow()
        assertEquals(TestStruct(42, 24), item)
        assertNull(engine.pop())
    }

    @Test
    fun testTwoItemsInSingleInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":1}\n{\"key\":2,\"value\":2}\n")
        assertEquals(TestStruct(1, 1), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(2, 2), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testCarriageReturnHandledGracefully() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\r\n{\"key\":3,\"value\":4}\r\n")
        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testWhitespaceHandledGracefully() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("\t{ \"key\":\t13,  \"value\":   37 } \r\n")
        assertEquals(TestStruct(13, 37), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testRaisesErrorWhenParsingEmptyLineInParseAlwaysMode() {
        val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.ParseAlways)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertTrue(engine.pop()!!.isFailure)
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
    }

    @Test
    fun testDoesNotRaiseErrorWhenParsingEmptyLineInIgnoreEmptyMode() {
        val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\n\n{\"key\":3,\"value\":4}\n")
        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testTwoItemsInManyInputsWithRest() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":12,\"v")
        engine.input("alue\":3")
        engine.input("4}\n{\"key")
        engine.input("\":56,\"valu")
        engine.input("e\":78}\n{\"key\":")

        assertEquals(TestStruct(12, 34), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(56, 78), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testInputCompletingPreviousRestThenMultipleCompleteItemsAndMoreRest() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":9,\"value\":")
        engine.input("8}\n{\"key\":7,\"value\":6}\n{\"key\":5,\"value\":4}\n{\"key\":")
        engine.input("3,\"value\":2}\n{")

        assertEquals(TestStruct(9, 8), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(7, 6), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(5, 4), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(3, 2), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testErroneousEntryEmittedAsJsonError() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1}\n{\"key\":1,\"value\":1}\n")

        val r1 = engine.pop()
        assertTrue(r1 != null && r1.isFailure)
        val r2 = engine.pop()
        assertTrue(r2 != null && r2.isSuccess)
        assertEquals(TestStruct(1, 1), r2.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testErrorFromSplitEntry() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":100,\"value\":200}\n{\"key\":")
        engine.input("\"should be a number\",\"value\":0}\n{\"key\":300,\"value\":400}\n")

        assertEquals(TestStruct(100, 200), engine.pop()?.getOrThrow())
        val r2 = engine.pop()
        assertTrue(r2 != null && r2.isFailure)
        assertEquals(TestStruct(300, 400), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testOldDataIsDiscarded() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        val count = 20
        engine.input("{ \"key\": 1, ")
        for (i in 0 until count - 1) {
            engine.input("\"value\": 2 }\r\n{ \"key\": 1, ")
        }
        engine.input("\"value\": 2 }\r\n")

        assertEquals(count, engine.queuedCount)
    }

    @Test
    fun testDoesNotRaiseErrorWhenParsingEmptyLineWithCarriageReturnInIgnoreEmptyMode() {
        val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\r\n\r\n{\"key\":3,\"value\":4}\n")

        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testRaisesErrorWhenParsingNonEmptyBlankLineInIgnoreEmptyMode() {
        val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\n \t\r\n{\"key\":3,\"value\":4}\n")

        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        val r2 = engine.pop()
        assertTrue(r2 != null && r2.isFailure)
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testDoesNotRaiseErrorWhenParsingNonEmptyBlankLineInIgnoreBlankMode() {
        val config = ParseConfig.DEFAULT.withEmptyLineHandling(EmptyLineHandling.IgnoreBlank)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}\n \t\r\n{\"key\":3,\"value\":4}\n")

        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertEquals(TestStruct(3, 4), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeRaisesErrorOnInvalidRest() {
        val config = ParseConfig.DEFAULT.withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("invalid json")
        engine.finish()

        val r = engine.pop()
        assertTrue(r != null && r.isFailure)
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeIgnoresEmptyRestEvenIfEmptyLineHandlingIsParseAlways() {
        val config =
            ParseConfig.DEFAULT
                .withEmptyLineHandling(EmptyLineHandling.ParseAlways)
                .withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.finish()
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeIgnoresEmptyRestIfEmptyLineHandlingIsIgnoreEmpty() {
        val config =
            ParseConfig.DEFAULT
                .withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
                .withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.finish()
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeDoesNotIgnoreNonEmptyBlankRestIfEmptyLineHandlingIsIgnoreEmpty() {
        val config =
            ParseConfig.DEFAULT
                .withEmptyLineHandling(EmptyLineHandling.IgnoreEmpty)
                .withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input(" ")
        engine.finish()

        val r = engine.pop()
        assertTrue(r != null && r.isFailure)
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeIgnoresNonEmptyBlankRestIfEmptyLineHandlingIsIgnoreBlank() {
        val config =
            ParseConfig.DEFAULT
                .withEmptyLineHandling(EmptyLineHandling.IgnoreBlank)
                .withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input(" ")
        engine.finish()
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeParsesValidRest() {
        val config = ParseConfig.DEFAULT.withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}")
        engine.finish()
        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeIgnoresRestIfParseRestIsFalse() {
        val config = ParseConfig.DEFAULT.withParseRest(false)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}")
        engine.finish()
        assertNull(engine.pop())
    }

    @Test
    fun testFinalizeIsIdempotent() {
        val config = ParseConfig.DEFAULT.withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":13,\"value\":37}")
        engine.finish()
        engine.finish()
        assertEquals(TestStruct(13, 37), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }
}
