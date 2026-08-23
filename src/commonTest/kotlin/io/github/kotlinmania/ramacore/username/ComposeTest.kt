// port-lint: tests username/compose.rs
package io.github.kotlinmania.ramacore.username

import io.github.kotlinmania.ramacore.Extensions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComposeTest {
    @Test
    fun testParseComposeUsernameLabels() {
        val composedUsername = "john-foo-bar-baz"

        val ext = Extensions()
        val usernameRes =
            parseUsername(
                ext,
                UsernameOpaqueLabelParser.new(),
                composedUsername,
            )
        assertTrue(usernameRes.isSuccess())
        assertEquals("john", usernameRes.getOrNull())

        val labels = ext.get<UsernameLabels>()!!
        val composedRes = composeUsername("john", labels.asWriter())
        assertTrue(composedRes.isSuccess())
        assertEquals(composedUsername, composedRes.getOrNull())
    }

    @Test
    fun testComposeUsernameCustomSeparator() {
        val writer =
            listLabelWriter(
                listOf(
                    stringLabelWriter("alpha"),
                    stringLabelWriter("beta"),
                ),
            )
        val res = composeUsernameWithSeparator("user", writer, '_')
        assertTrue(res.isSuccess())
        assertEquals("user_alpha_beta", res.getOrNull())
    }

    @Test
    fun testComposeEmptyLabelFails() {
        val writer = stringLabelWriter("")
        val res = composeUsername("user", writer)
        assertTrue(res.isFailure())
    }
}
