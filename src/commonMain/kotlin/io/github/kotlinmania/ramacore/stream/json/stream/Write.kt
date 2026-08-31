// port-lint: source rama-core/src/stream/json/stream/write.rs
package io.github.kotlinmania.ramacore.stream.json.stream

import io.github.kotlinmania.ramacore.stream.json.JsonEncoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Wraps a [Flow] of serializable items and offers a [Flow] of NDJSON encoded string chunks.
 */
public class JsonWriteStream<T>(
    private val itemStream: Flow<T>,
    private val continued: Boolean = false,
    private val serializer: (T) -> String,
) {
    /**
     * Converts this stream into a Kotlin [Flow] of NDJSON lines.
     */
    public fun toFlow(): Flow<String> =
        flow {
            val encoder = if (continued) JsonEncoder.newContinued(serializer) else JsonEncoder.new(serializer)
            itemStream.collect { item ->
                emit(encoder.encode(item))
            }
        }

    /**
     * Converts this stream into a Kotlin [Flow] of NDJSON byte arrays.
     */
    public fun toByteArrayFlow(): Flow<ByteArray> =
        flow {
            toFlow().collect { line ->
                emit(line.encodeToByteArray())
            }
        }

    public companion object {
        /**
         * Creates a new [JsonWriteStream] for a new stream.
         */
        public fun <T> new(
            itemStream: Flow<T>,
            serializer: (T) -> String,
        ): JsonWriteStream<T> = JsonWriteStream(itemStream, continued = false, serializer = serializer)

        /**
         * Creates a new [JsonWriteStream] continuing an existing stream.
         */
        public fun <T> newContinued(
            itemStream: Flow<T>,
            serializer: (T) -> String,
        ): JsonWriteStream<T> = JsonWriteStream(itemStream, continued = true, serializer = serializer)
    }
}
