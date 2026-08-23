// port-lint: tests username/parse.rs
package io.github.kotlinmania.ramacore.username

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.RamaResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParseTest {
    private class UsernameNoLabelParser : UsernameLabelParser {
        override fun parseLabel(label: String): UsernameLabelState = UsernameLabelState.Ignored

        override fun build(ext: Extensions): RamaResult<Unit, String> = RamaResult.ok(Unit)
    }

    private class UsernameNoLabelPanicParser : UsernameLabelParser {
        override fun parseLabel(label: String): UsernameLabelState = throw IllegalStateException("this parser should not be called")

        override fun build(ext: Extensions): RamaResult<Unit, String> = RamaResult.ok(Unit)
    }

    private class UsernameLabelAbortParser : UsernameLabelParser {
        override fun parseLabel(label: String): UsernameLabelState = UsernameLabelState.Abort

        override fun build(ext: Extensions): RamaResult<Unit, String> = throw IllegalStateException("should not happen")
    }

    private class MyLabelParser : UsernameLabelParser {
        val labels = mutableListOf<String>()

        override fun parseLabel(label: String): UsernameLabelState {
            labels.add(label)
            return UsernameLabelState.Used
        }

        override fun build(ext: Extensions): RamaResult<Unit, String> {
            if (labels.isNotEmpty()) {
                ext.insert(MyLabels(labels.toList()))
            }
            return RamaResult.ok(Unit)
        }
    }

    private class MyLabels(
        val labels: List<String>,
    )

    @Test
    fun testParseUsernameEmpty() {
        val ext = Extensions()
        assertTrue(parseUsername(ext, UnitUsernameLabelParser(), "").isFailure())
        assertTrue(parseUsername(ext, UnitUsernameLabelParser(), "-").isFailure())
    }

    @Test
    fun testParseUsernameNoLabels() {
        val ext = Extensions()
        val res = parseUsername(ext, UsernameNoLabelParser(), "username")
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())
    }

    @Test
    fun testParseUsernameLabelCollector() {
        val ext = Extensions()
        val res =
            parseUsername(
                ext,
                UsernameOpaqueLabelParser.new(),
                "username-label1-label2",
            )
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())

        val labels = ext.get<UsernameLabels>()
        assertEquals(listOf("label1", "label2"), labels?.labels)
    }

    @Test
    fun testUsernameLabelsMultiParser() {
        val ext = Extensions()
        val parser =
            CompositeUsernameParser(
                UsernameOpaqueLabelParser.new(),
                UsernameNoLabelParser(),
            )

        val res = parseUsername(ext, parser, "username-label1-label2")
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())

        val labels = ext.get<UsernameLabels>()
        assertEquals(listOf("label1", "label2"), labels?.labels)
    }

    @Test
    fun testUsernameLabelsMultiConsumerParser() {
        val ext = Extensions()
        val parser =
            CompositeUsernameParser(
                UsernameNoLabelParser(),
                MyLabelParser(),
                UsernameOpaqueLabelParser.new(),
            )

        val res = parseUsername(ext, parser, "username-label1-label2")
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())

        val labels = ext.get<UsernameLabels>()
        assertEquals(listOf("label1", "label2"), labels?.labels)

        val myLabels = ext.get<MyLabels>()
        assertEquals(listOf("label1", "label2"), myLabels?.labels)
    }

    @Test
    fun testUsernameLabelsMultiConsumerExclusiveParsers() {
        val ext = Extensions()
        val parser =
            ExclusiveUsernameParsers(
                UsernameOpaqueLabelParser.new(),
                MyLabelParser(),
                UsernameNoLabelPanicParser(),
            )

        val res = parseUsername(ext, parser, "username-label1-label2")
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())

        val labels = ext.get<UsernameLabels>()
        assertEquals(listOf("label1", "label2"), labels?.labels)

        assertNull(ext.get<MyLabels>())
    }

    @Test
    fun testUsernameOpaqueLabelsNone() {
        val ext = Extensions()
        val parser = UsernameOpaqueLabelParser.new()
        val res = parseUsername(ext, parser, "username")
        assertTrue(res.isSuccess())
        assertEquals("username", res.getOrNull())
        assertNull(ext.get<UsernameLabels>())
    }

    @Test
    fun testUsernameLabelParserAbort() {
        val ext = Extensions()
        val p1 =
            CompositeUsernameParser(
                UsernameLabelAbortParser(),
                UsernameOpaqueLabelParser.new(),
            )
        assertTrue(parseUsername(ext, p1, "username-foo").isFailure())

        val p2 =
            ExclusiveUsernameParsers(
                UsernameLabelAbortParser(),
                UsernameOpaqueLabelParser.new(),
            )
        assertTrue(parseUsername(ext, p2, "username-foo").isFailure())
    }
}
