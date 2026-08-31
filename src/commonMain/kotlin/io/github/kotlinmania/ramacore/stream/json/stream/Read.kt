// port-lint: source rama-core/src/stream/json/stream/read.rs
package io.github.kotlinmania.ramacore.stream.json.stream

import io.github.kotlinmania.ramacore.stream.json.NdjsonEngine
import io.github.kotlinmania.ramacore.stream.json.ParseConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Wraps a [Flow] of data chunks (Strings or ByteArrays) and offers a [Flow] of parsed NDJSON records
 * according to the provided [deserializer].
 */
public class JsonReadStream<T, S>(
    private val bytesStream: Flow<S>,
    private val config: ParseConfig = ParseConfig.DEFAULT,
    private val chunkToString: (S) -> String,
    private val deserializer: (String) -> T,
) {
    /**
     * Converts this stream into a Kotlin [Flow] of parsed item results.
     */
    public fun toFlow(): Flow<Result<T>> =
        flow {
            val engine = NdjsonEngine.withConfig(config, deserializer)
            bytesStream.collect { chunk ->
                engine.input(chunkToString(chunk))
                while (true) {
                    val item = engine.pop() ?: break
                    emit(item)
                }
            }
            engine.finish()
            while (true) {
                val item = engine.pop() ?: break
                emit(item)
            }
        }

    public companion object {
        /**
         * Creates a new [JsonReadStream] wrapping a String [Flow].
         */
        public fun <T> fromStringFlow(
            flow: Flow<String>,
            config: ParseConfig = ParseConfig.DEFAULT,
            deserializer: (String) -> T,
        ): JsonReadStream<T, String> =
            JsonReadStream(flow, config, { it }, deserializer)

        /**
         * Creates a new [JsonReadStream] wrapping a ByteArray [Flow].
         */
        public fun <T> fromByteArrayFlow(
            flow: Flow<ByteArray>,
            config: ParseConfig = ParseConfig.DEFAULT,
            deserializer: (String) -> T,
        ): JsonReadStream<T, ByteArray> =
            JsonReadStream(flow, config, { it.decodeToString() }, deserializer)
    }
}
