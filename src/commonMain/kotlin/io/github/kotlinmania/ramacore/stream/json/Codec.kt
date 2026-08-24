// port-lint: source stream/json/codec.rs
package io.github.kotlinmania.ramacore.stream.json

/**
 * NDJson encoder.
 */
public class JsonEncoder<T>(
    private var written: Boolean = false,
    private val serializer: (T) -> String,
) {
    /**
     * Encodes [data] to an NDJSON line.
     * If prior data was written, prepends a newline character.
     */
    public fun encode(data: T): String {
        val serialized = serializer(data)
        val result = if (written) "\n$serialized" else serialized
        written = true
        return result
    }

    /**
     * Encodes [data] to an NDJSON byte array.
     */
    public fun encodeToByteArray(data: T): ByteArray =
        encode(data).encodeToByteArray()

    public companion object {
        /**
         * Creates a new [JsonEncoder] for new streams.
         */
        public fun <T> new(serializer: (T) -> String): JsonEncoder<T> =
            JsonEncoder(written = false, serializer = serializer)

        /**
         * Creates a new [JsonEncoder] starting with a leading newline for continuing an existing stream.
         */
        public fun <T> newContinued(serializer: (T) -> String): JsonEncoder<T> =
            JsonEncoder(written = true, serializer = serializer)
    }
}

/**
 * NDJson decoder decoding ndjson stream into objects.
 */
public class JsonDecoder<T>(
    private val engine: NdjsonEngine<T>,
) {
    /**
     * Feeds [src] data into the decoder and attempts to pop the next decoded item.
     */
    public fun decode(src: String): Result<T>? {
        val existing = engine.pop()
        if (existing != null) return existing

        if (src.isNotEmpty()) {
            engine.input(src)
        }
        return engine.pop()
    }

    /**
     * Feeds [src] byte array data into the decoder and attempts to pop the next decoded item.
     */
    public fun decode(src: ByteArray): Result<T>? = decode(src.decodeToString())

    /**
     * Signals EOF to the decoder, finalizes remaining buffered data, and returns the next decoded item.
     */
    public fun decodeEof(src: String = ""): Result<T>? {
        if (src.isNotEmpty()) {
            engine.input(src)
        }
        engine.finish()
        return engine.pop()
    }

    /**
     * Signals EOF to the decoder with byte array data.
     */
    public fun decodeEof(src: ByteArray): Result<T>? = decodeEof(src.decodeToString())

    public companion object {
        /**
         * Creates a new [JsonDecoder] with default config.
         */
        public fun <T> new(deserializer: (String) -> T): JsonDecoder<T> =
            JsonDecoder(NdjsonEngine.new(deserializer))

        /**
         * Creates a new [JsonDecoder] with custom config.
         */
        public fun <T> newWithConfig(config: ParseConfig, deserializer: (String) -> T): JsonDecoder<T> =
            JsonDecoder(NdjsonEngine.withConfig(config, deserializer))
    }
}
