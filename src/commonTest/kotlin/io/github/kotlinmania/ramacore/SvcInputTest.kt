// port-lint: tests rama-core/src/svc_input.rs
package io.github.kotlinmania.ramacore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SvcInputTest {
    private class Tag(
        val name: String,
    )

    @Test
    fun testServiceInput() {
        val input = ServiceInput.new("test-input")
        assertEquals("test-input", input.input)

        input.extensionsMut().insert(Tag("custom-tag"))
        assertTrue(input.extensions().contains<Tag>())
        assertEquals("custom-tag", input.extensions().get<Tag>()?.name)
    }
}
