// port-lint: source rama-core/src/username/parse.rs
package io.github.kotlinmania.ramacore.username

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.RamaResult

/**
 * The parse state of a username label.
 */
public enum class UsernameLabelState {
    /**
     * The label was used by this parser.
     */
    Used,

    /**
     * The label was ignored by this parser.
     */
    Ignored,

    /**
     * Abort the parsing as a state has been reached from which cannot be recovered.
     */
    Abort,
}

/**
 * A parser which can parse labels from a username.
 */
public interface UsernameLabelParser {
    /**
     * Interpret the label and return whether or not the label was recognised and valid.
     */
    public fun parseLabel(label: String): UsernameLabelState

    /**
     * Consume parser state and store or use any of the relevant information seen.
     */
    public fun build(ext: Extensions): RamaResult<Unit, String>
}

/**
 * Wrapper type that can be used with multiple [UsernameLabelParser]s
 * in order for it to stop iterating over the parsers once there was one that consumed the label.
 */
public class ExclusiveUsernameParsers(
    public val parsers: List<UsernameLabelParser>,
) : UsernameLabelParser {
    public constructor(vararg parsers: UsernameLabelParser) : this(parsers.toList())

    override fun parseLabel(label: String): UsernameLabelState {
        for (parser in parsers) {
            when (parser.parseLabel(label)) {
                UsernameLabelState.Ignored -> Unit
                UsernameLabelState.Used -> return UsernameLabelState.Used
                UsernameLabelState.Abort -> return UsernameLabelState.Abort
            }
        }
        return UsernameLabelState.Ignored
    }

    override fun build(ext: Extensions): RamaResult<Unit, String> {
        for (parser in parsers) {
            val res = parser.build(ext)
            if (res.isFailure()) return res
        }
        return RamaResult.ok(Unit)
    }

    override fun toString(): String = "ExclusiveUsernameParsers($parsers)"
}

/**
 * Composite parser running multiple [UsernameLabelParser]s where all parsers receive all labels.
 */
public class CompositeUsernameParser(
    public val parsers: List<UsernameLabelParser>,
) : UsernameLabelParser {
    public constructor(vararg parsers: UsernameLabelParser) : this(parsers.toList())

    override fun parseLabel(label: String): UsernameLabelState {
        var state = UsernameLabelState.Ignored
        for (parser in parsers) {
            when (parser.parseLabel(label)) {
                UsernameLabelState.Ignored -> Unit
                UsernameLabelState.Used -> state = UsernameLabelState.Used
                UsernameLabelState.Abort -> return UsernameLabelState.Abort
            }
        }
        return state
    }

    override fun build(ext: Extensions): RamaResult<Unit, String> {
        for (parser in parsers) {
            val res = parser.build(ext)
            if (res.isFailure()) return res
        }
        return RamaResult.ok(Unit)
    }

    override fun toString(): String = "CompositeUsernameParser($parsers)"
}

/**
 * No-op parser that accepts everything.
 */
public class UnitUsernameLabelParser : UsernameLabelParser {
    override fun parseLabel(label: String): UsernameLabelState = UsernameLabelState.Used

    override fun build(ext: Extensions): RamaResult<Unit, String> = RamaResult.ok(Unit)
}

/**
 * Opaque string labels collected using the [UsernameOpaqueLabelParser].
 */
public class UsernameLabels(
    public val labels: List<String>,
) {
    override fun toString(): String = "UsernameLabels($labels)"

    override fun equals(other: Any?): Boolean = other is UsernameLabels && other.labels == labels

    override fun hashCode(): Int = labels.hashCode()
}

/**
 * A [UsernameLabelParser] which collects all labels from the username without specific parsing logic.
 */
public class UsernameOpaqueLabelParser : UsernameLabelParser {
    private val labels = mutableListOf<String>()

    override fun parseLabel(label: String): UsernameLabelState {
        labels.add(label)
        return UsernameLabelState.Used
    }

    override fun build(ext: Extensions): RamaResult<Unit, String> {
        if (labels.isNotEmpty()) {
            ext.insert(UsernameLabels(labels.toList()))
        }
        return RamaResult.ok(Unit)
    }

    public companion object {
        public fun new(): UsernameOpaqueLabelParser = UsernameOpaqueLabelParser()
    }
}

/**
 * Parse a username, extracting the username (first part)
 * and passing everything else to the [UsernameLabelParser].
 */
public fun parseUsername(
    ext: Extensions,
    parser: UsernameLabelParser,
    usernameRef: String,
): RamaResult<String, String> =
    parseUsernameWithSeparator(ext, parser, usernameRef, DEFAULT_USERNAME_LABEL_SEPARATOR)

/**
 * Parse a username with a custom separator, extracting the username (first part)
 * and passing everything else to the [UsernameLabelParser].
 */
public fun parseUsernameWithSeparator(
    ext: Extensions,
    parser: UsernameLabelParser,
    usernameRef: String,
    separator: Char,
): RamaResult<String, String> {
    val labels = mutableListOf<String>()
    var start = 0
    for (i in 0 until usernameRef.length) {
        if (usernameRef[i] == separator) {
            labels.add(usernameRef.substring(start, i))
            start = i + 1
        }
    }
    labels.add(usernameRef.substring(start))

    val username = labels.firstOrNull() ?: return RamaResult.err("missing username")
    if (username.isEmpty()) {
        return RamaResult.err("empty username")
    }

    for (index in 0 until labels.size - 1) {
        val label = labels[index + 1]
        when (parser.parseLabel(label)) {
            UsernameLabelState.Used -> Unit
            UsernameLabelState.Ignored -> return RamaResult.err("ignored username label #$index: $label")
            UsernameLabelState.Abort -> return RamaResult.err("invalid username label #$index: $label")
        }
    }

    val buildResult = parser.build(ext)
    if (buildResult.isFailure()) {
        return RamaResult.err(buildResult.errorOrNull() ?: "parser build failed")
    }

    return RamaResult.ok(username)
}
