// port-lint: source username/compose.rs
package io.github.kotlinmania.ramacore.username

import io.github.kotlinmania.ramacore.RamaResult

/**
 * Error returned in case composing of a username went wrong.
 */
public class ComposeError(
    public val message: String,
) {
    override fun toString(): String = message

    override fun equals(other: Any?): Boolean = other is ComposeError && other.message == message

    override fun hashCode(): Int = message.hashCode()

    public companion object {
        public fun emptyLabel(): ComposeError = ComposeError("empty label")

        public fun fmtError(error: String): ComposeError = ComposeError("fmt error: $error")
    }
}

/**
 * Composer used to compose a username into a string with labels, separated by [separator].
 */
public class Composer(
    public val separator: Char = DEFAULT_USERNAME_LABEL_SEPARATOR,
    username: String,
) {
    private val buffer: StringBuilder = StringBuilder(username)

    /**
     * Write a label into the composer.
     */
    public fun writeLabel(label: String): RamaResult<Unit, ComposeError> {
        if (label.isEmpty()) {
            return RamaResult.err(ComposeError.emptyLabel())
        }
        buffer.append(separator)
        buffer.append(label)
        return RamaResult.ok(Unit)
    }

    /**
     * Return the composed username string.
     */
    public fun compose(): String = buffer.toString()

    public companion object {
        public fun new(username: String): Composer =
            Composer(DEFAULT_USERNAME_LABEL_SEPARATOR, username)

        public fun withSeparator(separator: Char, username: String): Composer =
            Composer(separator, username)
    }
}

/**
 * A type that can write itself as label(s) to compose into a username with labels.
 */
public interface UsernameLabelWriter {
    /**
     * Write all labels into the given composer.
     */
    public fun writeLabels(composer: Composer): RamaResult<Unit, ComposeError>
}

/**
 * Compose a username into a username together with its labels.
 */
public fun composeUsername(
    username: String,
    labels: UsernameLabelWriter,
): RamaResult<String, ComposeError> =
    composeUsernameWithSeparator(username, labels, DEFAULT_USERNAME_LABEL_SEPARATOR)

/**
 * Compose a username into a username together with its labels using a custom separator.
 */
public fun composeUsernameWithSeparator(
    username: String,
    labels: UsernameLabelWriter,
    separator: Char,
): RamaResult<String, ComposeError> {
    val composer = Composer(separator, username)
    val res = labels.writeLabels(composer)
    if (res.isFailure()) {
        return RamaResult.err(res.errorOrNull() ?: ComposeError.emptyLabel())
    }
    return RamaResult.ok(composer.compose())
}

/**
 * Create a [UsernameLabelWriter] from a list of label writers.
 */
public fun listLabelWriter(writers: List<UsernameLabelWriter>): UsernameLabelWriter =
    object : UsernameLabelWriter {
        override fun writeLabels(composer: Composer): RamaResult<Unit, ComposeError> {
            for (writer in writers) {
                val res = writer.writeLabels(composer)
                if (res.isFailure()) return res
            }
            return RamaResult.ok(Unit)
        }
    }

/**
 * Create a [UsernameLabelWriter] from a single string label.
 */
public fun stringLabelWriter(label: String): UsernameLabelWriter =
    object : UsernameLabelWriter {
        override fun writeLabels(composer: Composer): RamaResult<Unit, ComposeError> =
            composer.writeLabel(label)
    }

/**
 * Create a [UsernameLabelWriter] from [UsernameLabels].
 */
public fun UsernameLabels.asWriter(): UsernameLabelWriter =
    object : UsernameLabelWriter {
        override fun writeLabels(composer: Composer): RamaResult<Unit, ComposeError> {
            for (label in labels) {
                val res = composer.writeLabel(label)
                if (res.isFailure()) return res
            }
            return RamaResult.ok(Unit)
        }
    }
