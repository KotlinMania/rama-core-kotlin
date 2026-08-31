// port-lint: source username/mod.rs
package io.github.kotlinmania.ramacore.username

/**
 * Utilities to work with usernames and pull information out of it.
 *
 * Provides username parsing ([parseUsername], [parseUsernameWithSeparator])
 * and username composing ([composeUsername], [composeUsernameWithSeparator]).
 */
public const val DEFAULT_USERNAME_LABEL_SEPARATOR: Char = '-'

/**
 * Username utilities module ledger.
 */
internal object UsernameModuleLedger {
    const val defaultSeparator: Char = DEFAULT_USERNAME_LABEL_SEPARATOR
}
