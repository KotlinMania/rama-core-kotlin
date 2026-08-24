// port-lint: tests matcher/test.rs
package io.github.kotlinmania.ramacore.matcher

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsRef
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private object Marker {
    data object Odd

    data object Even

    data object Const
}

private class OddMatcher : Matcher<Int> {
    override fun matches(ext: Extensions?, input: Int): Boolean {
        if (input % 2 != 0) {
            ext?.insert(Marker.Odd)
            return true
        }
        return false
    }
}

private class EvenMatcher : Matcher<Int> {
    override fun matches(ext: Extensions?, input: Int): Boolean {
        if (input % 2 == 0) {
            ext?.insert(Marker.Even)
            return true
        }
        return false
    }
}

private class NumberConstMatcher(
    val value: Int,
) : Matcher<Int> {
    override fun matches(ext: Extensions?, input: Int): Boolean {
        if (input == value) {
            ext?.insert(Marker.Const)
            return true
        }
        return false
    }
}

private sealed interface TestMatchers : Matcher<Int> {
    data class Const(val m: NumberConstMatcher) : TestMatchers, Matcher<Int> by m
    data class Even(val m: EvenMatcher) : TestMatchers, Matcher<Int> by m
    data class Odd(val m: OddMatcher) : TestMatchers, Matcher<Int> by m
}

private class SimpleExtHolder(
    private val ext: Extensions,
) : ExtensionsRef {
    override fun extensions(): Extensions = ext
}

class MatcherTest {
    @Test
    fun testNot() {
        assertFalse(Not.new(true).matches(null, Unit))
        assertFalse(Not.new(ConstMatcher.TRUE).matches(null, Unit))
        assertTrue(Not.new(false).matches(null, Unit))
    }

    @Test
    fun testNotBuilder() {
        assertFalse(true.asNotMatcher().matches(null, Unit))
        assertFalse(true.asNotMatcher().matches(null, 0))
        assertFalse(true.asNotMatcher().matches(null, false))
        assertFalse(true.asNotMatcher().matches(null, "foo"))
    }

    @Test
    fun testOption() {
        val noneMatcher: Matcher<Int>? = null
        assertFalse(noneMatcher.matches(null, 0))
        val someMatcher: Matcher<Int>? = NumberConstMatcher(0)
        assertTrue(someMatcher.matches(null, 0))
        val otherMatcher: Matcher<Int>? = NumberConstMatcher(1)
        assertFalse(otherMatcher.matches(null, 0))
    }

    @Test
    fun testAnd() {
        val matcher = And.new(NumberConstMatcher(1), OddMatcher())
        assertFalse(matcher.matches(null, 0))
        assertTrue(matcher.matches(null, 1))
        assertFalse(matcher.matches(null, 2))
        for (i in 3..255) {
            assertFalse(matcher.matches(null, i), "i = $i")
        }
    }

    @Test
    fun testAndBuilder() {
        val matcher = NumberConstMatcher(1).and(OddMatcher())
        assertFalse(matcher.matches(null, 0))
        assertTrue(matcher.matches(null, 1))
        assertFalse(matcher.matches(null, 2))
        for (i in 3..255) {
            assertFalse(matcher.matches(null, i), "i = $i")
        }
    }

    @Test
    fun testOr() {
        val matcher = Or.new(NumberConstMatcher(1), EvenMatcher())
        assertTrue(matcher.matches(null, 0))
        assertTrue(matcher.matches(null, 1))
        assertTrue(matcher.matches(null, 2))
        for (i in 3..255) {
            if (i % 2 == 0) {
                assertTrue(matcher.matches(null, i), "i = $i")
            } else {
                assertFalse(matcher.matches(null, i), "i = $i")
            }
        }
    }

    @Test
    fun testOrBuilder() {
        var matcher: Matcher<Int> = NumberConstMatcher(1)
        for (i in 2..12) {
            matcher = matcher.or(NumberConstMatcher(i))
        }

        assertFalse(matcher.matches(null, 0))
        for (i in 1..12) {
            assertTrue(matcher.matches(null, i), "i = $i")
        }
        for (i in 13..255) {
            assertFalse(matcher.matches(null, i), "i = $i")
        }
    }

    @Test
    fun testAndNever() {
        for (i in 0..255) {
            assertFalse(
                And.new(OddMatcher(), EvenMatcher()).matches(null, i),
                "i = $i",
            )
        }
    }

    @Test
    fun testOrNever() {
        for (i in 0..255) {
            assertTrue(
                Or.new(OddMatcher(), EvenMatcher()).matches(null, i),
                "i = $i",
            )
        }
    }

    @Test
    fun testAndOr() {
        val matcher =
            NumberConstMatcher(1)
                .or(NumberConstMatcher(2))
                .and(OddMatcher().or(EvenMatcher()))
        assertTrue(matcher.matches(null, 1))
        assertTrue(matcher.matches(null, 2))
        for (i in 3..255) {
            assertFalse(matcher.matches(null, i), "i = $i")
        }
    }

    @Test
    fun testMatchFnAlways() {
        assertTrue(matchFn<Unit> { true }.matches(null, Unit))
        assertTrue(matchFn<Int> { true }.matches(null, 0))
        assertTrue(matchFn<Boolean> { true }.matches(null, false))
        assertTrue(matchFn<String> { true }.matches(null, "foo"))
    }

    @Test
    fun testMatchFn() {
        val matcher = matchFn<Int> { input -> input % 2 != 0 }
        for (i in 0..255) {
            if (i % 2 != 0) {
                assertTrue(matcher.matches(null, i), "i = $i")
            } else {
                assertFalse(matcher.matches(null, i), "i = $i")
            }
        }
    }

    @Test
    fun testEnumMatcher() {
        assertFalse(TestMatchers.Const(NumberConstMatcher(1)).matches(null, 0))
        assertTrue(TestMatchers.Const(NumberConstMatcher(1)).matches(null, 1))
        assertFalse(TestMatchers.Even(EvenMatcher()).matches(null, 1))
        assertTrue(TestMatchers.Even(EvenMatcher()).matches(null, 2))
        assertFalse(TestMatchers.Odd(OddMatcher()).matches(null, 2))
        assertTrue(TestMatchers.Odd(OddMatcher()).matches(null, 3))
    }

    @Test
    fun testIterEnumAnd() {
        val matchers: List<TestMatchers> = listOf(
            TestMatchers.Const(NumberConstMatcher(1)),
            TestMatchers.Odd(OddMatcher()),
        )
        assertTrue(matchers[0].matches(null, 1))
        assertTrue(matchers[1].matches(null, 1))
        for (matcher in matchers) {
            assertTrue(matcher.matches(null, 1))
        }
        assertTrue(matchers.matchesAnd(null, 1))
        assertFalse(matchers.matchesAnd(null, 3))
        assertFalse(matchers.matchesAnd(null, 4))
    }

    @Test
    fun testIterEmpty() {
        val matchers: List<NumberConstMatcher> = emptyList()
        for (i in 0..255) {
            assertTrue(matchers.matchesAnd(null, i))
            assertTrue(matchers.matchesOr(null, i))
        }
    }

    @Test
    fun testIterEnumOr() {
        val matchers: List<TestMatchers> = listOf(
            TestMatchers.Const(NumberConstMatcher(0)),
            TestMatchers.Const(NumberConstMatcher(2)),
            TestMatchers.Odd(OddMatcher()),
        )
        assertTrue(matchers[0].matches(null, 0))
        assertTrue(matchers[1].matches(null, 2))
        assertTrue(matchers[2].matches(null, 1))
        for (i in 0..2) {
            assertTrue(matchers.matchesOr(null, i), "i = $i")
        }
        for (i in 3..255) {
            if (i % 2 == 1) {
                assertTrue(matchers.matchesOr(null, i), "i = $i")
            } else {
                assertFalse(matchers.matchesOr(null, i), "i = $i")
            }
        }
    }

    @Test
    fun testIterBoxAnd() {
        val matchers: List<Matcher<Int>> = listOf(NumberConstMatcher(1), OddMatcher())
        assertTrue(matchers[0].matches(null, 1))
        assertTrue(matchers[1].matches(null, 1))
        for (matcher in matchers) {
            assertTrue(matcher.matches(null, 1))
        }
        assertTrue(matchers.matchesAnd(null, 1))
        assertFalse(matchers.matchesAnd(null, 3))
        assertFalse(matchers.matchesAnd(null, 4))
    }

    @Test
    fun testIterBoxOr() {
        val matchers: List<Matcher<Int>> = listOf(
            NumberConstMatcher(0),
            NumberConstMatcher(2),
            OddMatcher(),
        )
        assertTrue(matchers[0].matches(null, 0))
        assertTrue(matchers[1].matches(null, 2))
        assertTrue(matchers[2].matches(null, 1))
        for (i in 0..2) {
            assertTrue(matchers.matchesOr(null, i), "i = $i")
        }
        for (i in 3..255) {
            if (i % 2 == 1) {
                assertTrue(matchers.matchesOr(null, i), "i = $i")
            } else {
                assertFalse(matchers.matchesOr(null, i), "i = $i")
            }
        }
    }

    @Test
    fun testExtInsertAndRevertOpOr() {
        val matcher =
            EvenMatcher()
                .and(NumberConstMatcher(2))
                .or(OddMatcher().and(NumberConstMatcher(3)))

        val ext1 = Extensions()
        assertTrue(matcher.matches(ext1, 2))
        assertNotNull(ext1.get<Marker.Even>())
        assertNotNull(ext1.get<Marker.Const>())
        assertNull(ext1.get<Marker.Odd>())

        val ext2 = Extensions()
        assertTrue(matcher.matches(ext2, 3))
        assertNull(ext2.get<Marker.Even>())
        assertNotNull(ext2.get<Marker.Const>())
        assertNotNull(ext2.get<Marker.Odd>())

        val ext3 = Extensions()
        assertFalse(matcher.matches(ext3, 4))
        assertNull(ext3.get<Marker.Even>())
        assertNull(ext3.get<Marker.Const>())
        assertNull(ext3.get<Marker.Odd>())
    }

    @Test
    fun testExtInsertAndRevertIterOr() {
        val matchers: List<Matcher<Int>> = listOf(
            EvenMatcher().and(NumberConstMatcher(2)),
            OddMatcher().and(NumberConstMatcher(3)),
        )

        val ext1 = Extensions()
        assertTrue(matchers.matchesOr(ext1, 2))
        assertNotNull(ext1.get<Marker.Even>())
        assertNotNull(ext1.get<Marker.Const>())
        assertNull(ext1.get<Marker.Odd>())

        val ext2 = Extensions()
        assertTrue(matchers.matchesOr(ext2, 3))
        assertNull(ext2.get<Marker.Even>())
        assertNotNull(ext2.get<Marker.Const>())
        assertNotNull(ext2.get<Marker.Odd>())

        val ext3 = Extensions()
        assertFalse(matchers.matchesOr(ext3, 4))
        assertNull(ext3.get<Marker.Even>())
        assertNull(ext3.get<Marker.Const>())
        assertNull(ext3.get<Marker.Odd>())
    }

    @Test
    fun testExtInsertAndRevertIterAnd() {
        val matchers: List<Matcher<Int>> = listOf(
            NumberConstMatcher(2).or(NumberConstMatcher(3)),
            OddMatcher().or(EvenMatcher()),
        )

        val ext1 = Extensions()
        assertTrue(matchers.matchesAnd(ext1, 3))
        assertNull(ext1.get<Marker.Even>())
        assertNotNull(ext1.get<Marker.Const>())
        assertNotNull(ext1.get<Marker.Odd>())

        val ext2 = Extensions()
        assertTrue(matchers.matchesAnd(ext2, 2))
        assertNotNull(ext2.get<Marker.Even>())
        assertNotNull(ext2.get<Marker.Const>())
        assertNull(ext2.get<Marker.Odd>())

        val ext3 = Extensions()
        assertFalse(matchers.matchesAnd(ext3, 1))
        assertNull(ext3.get<Marker.Even>())
        assertNull(ext3.get<Marker.Const>())
        assertNull(ext3.get<Marker.Odd>())
    }

    @Test
    fun testExtInsertAndRevertOpAnd() {
        val matcher =
            NumberConstMatcher(2)
                .or(NumberConstMatcher(3))
                .and(OddMatcher().or(EvenMatcher()))

        val ext1 = Extensions()
        assertTrue(matcher.matches(ext1, 3))
        assertNull(ext1.get<Marker.Even>())
        assertNotNull(ext1.get<Marker.Const>())
        assertNotNull(ext1.get<Marker.Odd>())

        val ext2 = Extensions()
        assertTrue(matcher.matches(ext2, 2))
        assertNotNull(ext2.get<Marker.Even>())
        assertNotNull(ext2.get<Marker.Const>())
        assertNull(ext2.get<Marker.Odd>())

        val ext3 = Extensions()
        assertFalse(matcher.matches(ext3, 1))
        assertNull(ext3.get<Marker.Even>())
        assertNull(ext3.get<Marker.Const>())
        assertNull(ext3.get<Marker.Odd>())
    }

    data class MyMarker(
        val id: Int,
    )

    @Test
    fun testExtensionMatcher() {
        val constMatcher = ExtensionMatcher.withConst(MyMarker(42))
        val fnMatcher = ExtensionMatcher.withFn<MyMarker> { it.id > 10 }

        val ext1 = Extensions().insert(MyMarker(42))
        val holder1 = SimpleExtHolder(ext1)
        assertTrue(constMatcher.matches(null, holder1))
        assertTrue(fnMatcher.matches(null, holder1))

        val ext2 = Extensions().insert(MyMarker(5))
        val holder2 = SimpleExtHolder(ext2)
        assertFalse(constMatcher.matches(null, holder2))
        assertFalse(fnMatcher.matches(null, holder2))

        val extEmpty = Extensions()
        val holderEmpty = SimpleExtHolder(extEmpty)
        assertFalse(constMatcher.matches(null, holderEmpty))
        assertFalse(fnMatcher.matches(null, holderEmpty))
    }
}
