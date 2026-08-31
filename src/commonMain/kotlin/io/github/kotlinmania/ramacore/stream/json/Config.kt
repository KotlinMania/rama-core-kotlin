// port-lint: source rama-core/src/stream/json/config.rs
package io.github.kotlinmania.ramacore.stream.json

/**
 * Controls how the parser deals with lines that contain no JSON values.
 */
public enum class EmptyLineHandling {
    /**
     * Parse every line, i.e. every segment between `\n` characters, even if it is empty. This will
     * result in errors for empty lines.
     */
    ParseAlways,

    /**
     * Ignore lines, i.e. segments between `\n` characters, which are empty, i.e. contain no
     * characters. For compatibility with `\r\n`-style linebreaks, this also ignores lines which
     * consist of only a single `\r` character.
     */
    IgnoreEmpty,

    /**
     * Ignore lines, i.e. segments between `\n` characters, which contain only whitespace
     * characters.
     */
    IgnoreBlank,
}

/**
 * Configuration for the NDJSON-parser which controls the behavior in various situations.
 *
 * By default, the parser will attempt to parse every line, i.e. every segment between `\n`
 * characters, even if it is empty. This will result in errors for empty lines.
 */
public data class ParseConfig(
    public val emptyLineHandling: EmptyLineHandling = EmptyLineHandling.ParseAlways,
    public val parseRest: Boolean = true,
) {
    /**
     * Creates a new config from this config which has a different handling for lines that contain
     * no JSON values.
     */
    public fun withEmptyLineHandling(emptyLineHandling: EmptyLineHandling): ParseConfig =
        copy(emptyLineHandling = emptyLineHandling)

    /**
     * Creates a new config from this config which has the given configuration on whether to parse
     * or ignore the rest, i.e. the part after the last newline character.
     */
    public fun withParseRest(parseRest: Boolean): ParseConfig =
        copy(parseRest = parseRest)

    public companion object {
        public val DEFAULT: ParseConfig = ParseConfig()

        public fun default(): ParseConfig = DEFAULT
    }
}
