// port-lint: tests rama-core/src/stream/json/codec.rs
package io.github.kotlinmania.ramacore.stream.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class Item(
    val id: Int,
    val name: String,
)

class CodecTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun encodeSingleValueNoLeadingNewline() {
        val encoder = JsonEncoder.new<Int> { it.toString() }
        assertEquals("42", encoder.encode(42))
    }

    @Test
    fun encodeMultipleValuesSeparatedByNewlineWithoutTrailingNewline() {
        val encoder = JsonEncoder.new<Int> { it.toString() }
        assertEquals("1", encoder.encode(1))
        assertEquals("\n2", encoder.encode(2))
        assertEquals("\n3", encoder.encode(3))
    }

    @Test
    fun roundtripStructsEncodeThenDecodeAll() {
        val encoder = JsonEncoder.new<Item> { json.encodeToString(it) }
        val input =
            listOf(
                Item(1, "alice"),
                Item(2, "bob"),
                Item(3, "carol"),
            )
        val encoded = input.joinToString("") { encoder.encode(it) }

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

        assertEquals(input, out)
    }

    @Test
    fun decodeIncrementalStreamingChunks() {
        val encoder = JsonEncoder.new<Item> { json.encodeToString(it) }
        val items =
            listOf(
                Item(10, "ten"),
                Item(20, "twenty"),
                Item(30, "thirty"),
            )
        val full = items.joinToString("") { encoder.encode(it) }

        val decoder = JsonDecoder.new { json.decodeFromString<Item>(it) }
        val out = mutableListOf<Item>()

        val chunkSize = 7
        var pos = 0
        while (pos < full.length) {
            val end = minOf(pos + chunkSize, full.length)
            val chunk = full.substring(pos, end)
            var res = decoder.decode(chunk)
            while (res != null) {
                out.add(res.getOrThrow())
                res = decoder.decode("")
            }
            pos = end
        }

        val eofRes = decoder.decodeEof()
        if (eofRes != null) {
            out.add(eofRes.getOrThrow())
        }

        assertEquals(items, out)
    }
}
