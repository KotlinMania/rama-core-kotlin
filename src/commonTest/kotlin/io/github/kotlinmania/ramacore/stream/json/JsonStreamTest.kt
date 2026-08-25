// port-lint: tests stream/json/engine.rs stream/json/codec.rs stream/json/stream/read.rs stream/json/stream/write.rs
package io.github.kotlinmania.ramacore.stream.json

import io.github.kotlinmania.ramacore.stream.json.stream.JsonReadStream
import io.github.kotlinmania.ramacore.stream.json.stream.JsonWriteStream
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
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

@Serializable
private data class Item(
    val id: Int,
    val name: String,
)

@Serializable
private data class Data(
    val bar: String,
)

@Serializable
private data class OrderEvent(
    val item: String,
    val quantity: Int,
    val prepaid: Boolean,
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

class JsonStreamTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testEngineNoInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        assertNull(engine.pop())
    }

    @Test
    fun testEngineIncompleteInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"val")
        assertNull(engine.pop())
    }

    @Test
    fun testEngineSingleExactInput() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":3,\"value\":4}\n")
        val item = engine.pop()?.getOrThrow()
        assertEquals(TestStruct(3, 4), item)
        assertNull(engine.pop())
    }

    @Test
    fun testEngineSingleItemSplitIntoTwoInputs() {
        val engine = NdjsonEngine.new { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":42,")
        engine.input("\"value\":24}\n")
        val item = engine.pop()?.getOrThrow()
        assertEquals(TestStruct(42, 24), item)
        assertNull(engine.pop())
    }

    @Test
    fun testEngineTwoItemsInSingleInput() {
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
    fun testFinishParsesValidRest() {
        val config = ParseConfig.DEFAULT.withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}")
        engine.finish()
        assertEquals(TestStruct(1, 2), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testFinishIgnoresRestIfParseRestIsFalse() {
        val config = ParseConfig.DEFAULT.withParseRest(false)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":1,\"value\":2}")
        engine.finish()
        assertNull(engine.pop())
    }

    @Test
    fun testFinishIsIdempotent() {
        val config = ParseConfig.DEFAULT.withParseRest(true)
        val engine = NdjsonEngine.withConfig(config) { json.decodeFromString<TestStruct>(it) }
        engine.input("{\"key\":13,\"value\":37}")
        engine.finish()
        engine.finish()
        assertEquals(TestStruct(13, 37), engine.pop()?.getOrThrow())
        assertNull(engine.pop())
    }

    @Test
    fun testEncoderSingleAndMultipleValues() {
        val encoder = JsonEncoder.new<Int> { it.toString() }
        assertEquals("1", encoder.encode(1))
        assertEquals("\n2", encoder.encode(2))
        assertEquals("\n3", encoder.encode(3))
    }

    @Test
    fun testEncoderContinued() {
        val encoder = JsonEncoder.newContinued<Int> { it.toString() }
        assertEquals("\n1", encoder.encode(1))
        assertEquals("\n2", encoder.encode(2))
    }

    @Test
    fun testCodecRoundtrip() {
        val encoder = JsonEncoder.new<Item> { json.encodeToString(it) }
        val items =
            listOf(
                Item(1, "alice"),
                Item(2, "bob"),
                Item(3, "carol"),
            )
        val encoded = items.joinToString("") { encoder.encode(it) }

        val decoder = JsonDecoder.new { json.decodeFromString<Item>(it) }
        val out = mutableListOf<Item>()
        var res = decoder.decode(encoded)
        while (res != null) {
            out.add(res.getOrThrow())
            res = decoder.decode("")
        }
        res = decoder.decodeEof()
        if (res != null) {
            out.add(res.getOrThrow())
        }

        assertEquals(items, out)
    }

    @Test
    fun testReadStream() =
        runTest {
            val flow =
                flowOf(
                    "{\"key\":1,\"value\":2}\n",
                    "{\"key\":3,\"value\":4}\n",
                )
            val readStream = JsonReadStream.fromStringFlow(flow) { json.decodeFromString<TestStruct>(it) }
            val results = readStream.toFlow().toList().map { it.getOrThrow() }
            assertEquals(listOf(TestStruct(1, 2), TestStruct(3, 4)), results)
        }

    @Test
    fun testWriteStream() =
        runTest {
            val items = listOf(TestStruct(1, 2), TestStruct(3, 4))
            val writeStream = JsonWriteStream.new(items.asFlow()) { json.encodeToString(it) }
            val lines = writeStream.toFlow().toList()
            assertEquals(listOf("{\"key\":1,\"value\":2}", "\n{\"key\":3,\"value\":4}"), lines)
        }

    @Test
    fun testJsonStreamSimple() =
        runTest {
            val inputs =
                listOf(
                    "{\"bar\":\"foo\"}\n{\"bar\":\"qux\"}\n{\"bar\":\"baz\"}",
                    "{\"bar\": \"foo\"}\n{\"bar\": \"qux\"}\n{\"bar\": \"baz\"}",
                    "{\"bar\":\"foo\"}\n{\"bar\":\"qux\"}\n{\"bar\":\"baz\"}\n",
                    "{\"bar\": \"foo\"}\n{\"bar\": \"qux\"}\n{\"bar\": \"baz\"}\n",
                )

            for (input in inputs) {
                val readStream = JsonReadStream.fromStringFlow(flowOf(input)) { json.decodeFromString<Data>(it) }
                val results = readStream.toFlow().toList().map { it.getOrThrow() }
                assertEquals(listOf(Data("foo"), Data("qux"), Data("baz")), results)
            }
        }

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
