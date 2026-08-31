// port-lint: tests stream/json/mod.rs
package io.github.kotlinmania.ramacore.stream.json

import io.github.kotlinmania.ramacore.stream.json.stream.JsonReadStream
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class Data(
    val bar: String,
)

class ModTest {
    private val json = Json { ignoreUnknownKeys = true }

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
}
