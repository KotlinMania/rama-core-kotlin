// port-lint: tests stream/json/codec.rs
package io.github.kotlinmania.ramacore.stream.json

import io.github.kotlinmania.serdejson.Value
import io.github.kotlinmania.serdejson.fromStr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private data class Item(
    val id: Int,
    val name: String,
) {
    fun toJson(): String = """{"id":$id,"name":"$name"}"""

    companion object {
        fun deserialize(str: String): Item {
            val v = fromStr(str).getOrThrow()
            val obj = v.asObject() ?: throw IllegalArgumentException("Expected object")
            val id =
                (obj.get("id")?.asI64() ?: obj.get("id")?.asU64()?.toLong())?.toInt()
                    ?: throw IllegalArgumentException("Missing id")
            val name = obj.get("name")?.asStr() ?: throw IllegalArgumentException("Missing name")
            return Item(id, name)
        }
    }
}

private data class OrderEvent(
    val item: String,
    val quantity: Int,
    val prepaid: Boolean,
) {
    companion object {
        fun deserialize(str: String): OrderEvent {
            val v = fromStr(str).getOrThrow()
            val obj = v.asObject() ?: throw IllegalArgumentException("Expected object")
            val item = obj.get("item")?.asStr() ?: throw IllegalArgumentException("Missing item")
            val quantity =
                (obj.get("quantity")?.asI64() ?: obj.get("quantity")?.asU64()?.toLong())?.toInt()
                    ?: throw IllegalArgumentException("Missing quantity")
            val prepaid =
                (obj.get("prepaid") as? Value.Bool)?.value
                    ?: throw IllegalArgumentException("Missing prepaid")
            return OrderEvent(item, quantity, prepaid)
        }
    }
}

class CodecTest {
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
        val encoder = JsonEncoder.new<Item> { it.toJson() }
        val input =
            listOf(
                Item(1, "alice"),
                Item(2, "bob"),
                Item(3, "carol"),
            )
        val encoded = input.joinToString("") { encoder.encode(it) }

        val decoder = JsonDecoder.new { Item.deserialize(it) }
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
        val encoder = JsonEncoder.new<Item> { it.toJson() }
        val items =
            listOf(
                Item(10, "ten"),
                Item(20, "twenty"),
                Item(30, "thirty"),
            )
        val full = items.joinToString("") { encoder.encode(it) }

        val decoder = JsonDecoder.new { Item.deserialize(it) }
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

    @Test
    fun decodeReportsErrorForMalformedJsonLine() {
        val decoder = JsonDecoder.new { fromStr(it).getOrThrow() }
        val input = "{not valid json}\n{\"ok\": true}\n"
        val res = decoder.decode(input)
        assertTrue(res != null && res.isFailure)
        val next = decoder.decode("")
        if (next != null) {
            assertTrue(next.isSuccess)
            val obj = next.getOrThrow().asObject()
            assertEquals(true, (obj?.get("ok") as? Value.Bool)?.value)
        }
    }

    @Test
    fun decodeOrderEvents() {
        val inputs =
            listOf(
                """{"item":"Apple Watch Series 9","quantity":2,"prepaid":true}""",
                "\n" + """{"item":"extra item","quantity":0,"prepaid":true}""",
                "\n" + """{"item":"Gaming Mousepad XL","quantity":1,"prepaid":false}""",
                "\n" + """{"item":"Noise Cancelling Headphones","quantity":3,"prepaid":true}""",
                "\n" + """{"item":"Ergonomic Chair","quantity":1,"prepaid":true}""",
                "\n" + """{"item":"extra item","quantity":6,"prepaid":false}""",
                "\n" + """{"item":"LED Monitor 27\"","quantity":4,"prepaid":false}""",
                "\n" + """{"item":"Smartphone Stand","quantity":6,"prepaid":false}""",
                "\n" + """{"item":"Mechanical Keyboard","quantity":2,"prepaid":true}""",
                "\n" + """{"item":"extra item","quantity":12,"prepaid":true}""",
                "\n" + """{"item":"Laptop Sleeve 15.6\"","quantity":3,"prepaid":false}""",
                "\n" + """{"item":"USB-C Docking Station","quantity":1,"prepaid":true}""",
                "\n" + """{"item":"Wireless Presenter","quantity":1,"prepaid":false}""",
                "\n" + """{"item":"extra item","quantity":18,"prepaid":false}""",
                "\n" + """{"item":"Foldable Desk Lamp","quantity":5,"prepaid":true}""",
                "\n" + """{"item":"Portable SSD 1TB","quantity":2,"prepaid":true}""",
                "\n" + """{"item":"Webcam Cover Slide","quantity":10,"prepaid":false}""",
                "\n" + """{"item":"extra item","quantity":24,"prepaid":true}""",
                "\n" + """{"item":"Bluetooth Speaker","quantity":2,"prepaid":false}""",
                "\n" + """{"item":"Fitness Tracker Band","quantity":4,"prepaid":true}""",
                "\n" + """{"item":"Laser Pointer","quantity":1,"prepaid":false}""",
                "\n" + """{"item":"extra item","quantity":30,"prepaid":false}""",
                "\n" + """{"item":"Conference Mic","quantity":2,"prepaid":true}""",
                "\n" + """{"item":"Noise-Absorbing Panels","quantity":12,"prepaid":false}""",
                "\n" + """{"item":"Desk Organizer Set","quantity":1,"prepaid":true}""",
                "\n" + """{"item":"extra item","quantity":36,"prepaid":true}""",
                "\n" + """{"item":"Whiteboard Eraser Pack","quantity":6,"prepaid":false}""",
                "\n" + """{"item":"Travel Power Adapter","quantity":2,"prepaid":true}""",
            )

        var eventCount = 0
        val uniqueEvents = mutableSetOf<String>()
        val decoder = JsonDecoder.new { OrderEvent.deserialize(it) }

        for (input in inputs) {
            var res = decoder.decode(input)
            while (res != null) {
                val event = res.getOrThrow()
                uniqueEvents.add(event.item)
                eventCount++
                res = decoder.decode("")
            }
        }
        var eofRes = decoder.decodeEof()
        while (eofRes != null) {
            val event = eofRes.getOrThrow()
            uniqueEvents.add(event.item)
            eventCount++
            eofRes = decoder.decodeEof()
        }

        assertEquals(28, eventCount)
        assertEquals(22, uniqueEvents.size)
    }

    @Test
    fun decodeOrderEventsRandomChunks() {
        val rawInput =
            listOf(
                """{"item":"Apple Watch Series 9","quantity":2,"prepaid":true}""",
                """{"item":"extra item","quantity":0,"prepaid":true}""",
                """{"item":"Gaming Mousepad XL","quantity":1,"prepaid":false}""",
                """{"item":"Noise Cancelling Headphones","quantity":3,"prepaid":true}""",
                """{"item":"Ergonomic Chair","quantity":1,"prepaid":true}""",
                """{"item":"extra item","quantity":6,"prepaid":false}""",
                """{"item":"LED Monitor 27\"","quantity":4,"prepaid":false}""",
                """{"item":"Smartphone Stand","quantity":6,"prepaid":false}""",
                """{"item":"Mechanical Keyboard","quantity":2,"prepaid":true}""",
                """{"item":"extra item","quantity":12,"prepaid":true}""",
                """{"item":"Laptop Sleeve 15.6\"","quantity":3,"prepaid":false}""",
                """{"item":"USB-C Docking Station","quantity":1,"prepaid":true}""",
                """{"item":"Wireless Presenter","quantity":1,"prepaid":false}""",
                """{"item":"extra item","quantity":18,"prepaid":false}""",
                """{"item":"Foldable Desk Lamp","quantity":5,"prepaid":true}""",
                """{"item":"Portable SSD 1TB","quantity":2,"prepaid":true}""",
                """{"item":"Webcam Cover Slide","quantity":10,"prepaid":false}""",
                """{"item":"extra item","quantity":24,"prepaid":true}""",
                """{"item":"Bluetooth Speaker","quantity":2,"prepaid":false}""",
                """{"item":"Fitness Tracker Band","quantity":4,"prepaid":true}""",
                """{"item":"Laser Pointer","quantity":1,"prepaid":false}""",
                """{"item":"extra item","quantity":30,"prepaid":false}""",
                """{"item":"Conference Mic","quantity":2,"prepaid":true}""",
                """{"item":"Noise-Absorbing Panels","quantity":12,"prepaid":false}""",
                """{"item":"Desk Organizer Set","quantity":1,"prepaid":true}""",
                """{"item":"extra item","quantity":36,"prepaid":true}""",
                """{"item":"Whiteboard Eraser Pack","quantity":6,"prepaid":false}""",
                """{"item":"Travel Power Adapter","quantity":2,"prepaid":true}""",
            ).joinToString("\n")

        val random = kotlin.random.Random(42)
        for (i in 0 until 32) {
            val max = rawInput.length
            var begin = 0
            var eventCount = 0
            val uniqueEvents = mutableSetOf<String>()
            val decoder = JsonDecoder.new { OrderEvent.deserialize(it) }

            while (begin < max) {
                val end = random.nextInt(begin, max + 1)
                val chunk = rawInput.substring(begin, end)
                var res = decoder.decode(chunk)
                while (res != null) {
                    val event = res.getOrThrow()
                    uniqueEvents.add(event.item)
                    eventCount++
                    res = decoder.decode("")
                }
                begin = end
            }

            var eofRes = decoder.decodeEof()
            while (eofRes != null) {
                val event = eofRes.getOrThrow()
                uniqueEvents.add(event.item)
                eventCount++
                eofRes = decoder.decodeEof()
            }

            assertEquals(28, eventCount)
            assertEquals(22, uniqueEvents.size)
        }
    }
}
