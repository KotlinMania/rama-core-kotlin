package io.github.kotlinmania.ramacore.stream.json

import io.github.kotlinmania.ramacore.stream.json.stream.JsonReadStream
import io.github.kotlinmania.serdejson.fromStr
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private data class Data(
    val bar: String,
) {
    companion object {
        fun deserialize(str: String): Data {
            val v = fromStr(str).getOrThrow()
            val obj = v.asObject() ?: throw IllegalArgumentException("Expected object")
            val bar = obj.get("bar")?.asStr() ?: throw IllegalArgumentException("Missing bar")
            return Data(bar)
        }
    }
}

class JsonStreamTest {
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
                val readStream = JsonReadStream.fromStringFlow(flowOf(input)) { Data.deserialize(it) }
                val results = readStream.toFlow().toList().map { it.getOrThrow() }
                assertEquals(listOf(Data("foo"), Data("qux"), Data("baz")), results)
            }
        }
}
