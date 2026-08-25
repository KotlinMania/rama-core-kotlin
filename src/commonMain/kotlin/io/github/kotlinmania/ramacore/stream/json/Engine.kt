// port-lint: source stream/json/engine.rs
package io.github.kotlinmania.ramacore.stream.json

/**
 * The low-level engine parsing NDJSON data given as byte slices or strings into objects of type `T`.
 * Data is supplied in chunks and parsed objects can subsequently be read from a queue.
 */
public class NdjsonEngine<T>(
    private val config: ParseConfig = ParseConfig.DEFAULT,
    private val deserializer: (String) -> T,
) {
    private val inQueue: StringBuilder = StringBuilder()
    private val outQueue: ArrayDeque<Result<T>> = ArrayDeque()

    /**
     * Reads the next element from the queue of parsed items, if available.
     * Returns null if no element is available in the queue.
     */
    public fun pop(): Result<T>? = outQueue.removeFirstOrNull()

    /**
     * Parses the given data as NDJSON. In case the end does not match up with a newline, the rest
     * is stored in an internal cache.
     */
    public fun input(text: String) {
        var start = 0
        while (true) {
            val newlineIdx = text.indexOf('\n', startIndex = start)
            if (newlineIdx < 0) {
                inQueue.append(text.substring(start))
                break
            }
            val slice = text.substring(start, newlineIdx)
            val line =
                if (inQueue.isNotEmpty()) {
                    inQueue.append(slice)
                    val full = inQueue.toString()
                    inQueue.clear()
                    full
                } else {
                    slice
                }
            parseLine(line, config.emptyLineHandling)?.let { outQueue.addLast(it) }
            start = newlineIdx + 1
        }
    }

    /**
     * Parses the given byte array data as NDJSON UTF-8 string.
     */
    public fun input(data: ByteArray) {
        input(data.decodeToString())
    }

    private fun parseLine(line: String, emptyLineHandling: EmptyLineHandling): Result<T>? {
        val trimmedCr = if (line.endsWith('\r')) line.dropLast(1) else line
        val shouldIgnore =
            when (emptyLineHandling) {
                EmptyLineHandling.ParseAlways -> false
                EmptyLineHandling.IgnoreEmpty -> trimmedCr.isEmpty()
                EmptyLineHandling.IgnoreBlank -> trimmedCr.isBlank()
            }

        if (shouldIgnore) {
            return null
        }

        return try {
            Result.success(deserializer(trimmedCr))
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * Returns true if there is unprocessed input in the internal buffer.
     */
    public val hasInputPending: Boolean
        get() = inQueue.isNotEmpty()

    /**
     * Returns the number of parsed items currently queued.
     */
    public val queuedCount: Int
        get() = outQueue.size

    /**
     * Parses the rest leftover from previous calls to [input], if configured.
     * This function is idempotent.
     */
    public fun finish() {
        if (config.parseRest && inQueue.isNotEmpty()) {
            val emptyHandling =
                when (config.emptyLineHandling) {
                    EmptyLineHandling.ParseAlways -> EmptyLineHandling.IgnoreEmpty
                    EmptyLineHandling.IgnoreEmpty -> EmptyLineHandling.IgnoreEmpty
                    EmptyLineHandling.IgnoreBlank -> EmptyLineHandling.IgnoreBlank
                }
            val line = inQueue.toString()
            inQueue.clear()
            parseLine(line, emptyHandling)?.let { outQueue.addLast(it) }
        } else {
            inQueue.clear()
        }
    }

    public companion object {
        /**
         * Creates a new [NdjsonEngine] with default config.
         */
        public fun <T> new(deserializer: (String) -> T): NdjsonEngine<T> =
            NdjsonEngine(ParseConfig.DEFAULT, deserializer)

        /**
         * Creates a new [NdjsonEngine] with custom config.
         */
        public fun <T> withConfig(config: ParseConfig, deserializer: (String) -> T): NdjsonEngine<T> =
            NdjsonEngine(config, deserializer)
    }
}
