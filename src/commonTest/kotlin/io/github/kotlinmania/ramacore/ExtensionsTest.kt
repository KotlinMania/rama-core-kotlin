// port-lint: tests extensions.rs
package io.github.kotlinmania.ramacore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExtensionsTest {
    @Test
    fun getShouldReturnLastAddedExtension() {
        val ext = Extensions()
        ext.insert("first")
        ext.insert("second")

        assertEquals("second", ext.get<String>())

        val split = ext.copy()
        split.insert("split")

        assertEquals("second", ext.get<String>())
        assertEquals("split", split.get<String>())
    }

    @Test
    fun firstShouldReturnFirstAddedExtension() {
        val ext = Extensions()
        ext.insert("first")
        ext.insert("second")

        assertEquals("first", ext.first<String>())
    }

    @Test
    fun iterShouldWork() {
        val ext = Extensions()
        ext.insert("first")
        ext.insert(4)
        ext.insert(true)
        ext.insert("second")

        val output: List<String> = ext.iter<String>().toList()
        assertEquals("first", output[0])
        assertEquals("second", output[1])

        val firstBool = ext.iter<Boolean>().first()
        assertTrue(firstBool)
    }

    @Test
    fun testExtensions() {
        data class MyType(
            val value: Int,
        )

        val extensions = Extensions()

        extensions.insert(5)
        extensions.insert(MyType(10))

        assertEquals(5, extensions.get<Int>())

        val ext2 = extensions.copy()
        ext2.insert(true)

        assertEquals(5, ext2.get<Int>())
        assertEquals(MyType(10), ext2.get<MyType>())
        assertEquals(true, ext2.get<Boolean>())

        val extensions3 = Extensions()
        extensions3.insert(5)
        extensions3.insert(MyType(10))

        val extensions4 = Extensions()
        extensions4.extend(extensions3)
        assertEquals(5, extensions4.get<Int>())
        assertEquals(MyType(10), extensions4.get<MyType>())
    }

    @Test
    fun containsReflectsInsertedTypes() {
        val ext = Extensions()
        assertFalse(ext.contains<String>())
        ext.insert("hello")
        assertTrue(ext.contains<String>())
        assertFalse(ext.contains<Int>())
    }

    @Test
    fun getReturnsNullForAbsentType() {
        val ext = Extensions()
        assertNull(ext.get<Int>())
        assertNull(ext.first<Int>())
        assertTrue(ext.iter<Int>().toList().isEmpty())
    }

    @Test
    fun asExtensionsRefMutatesUnderlyingStore() {
        val store = Extensions()
        val view = store.asExtensionsRef()

        view.extensionsMut().insert(7)
        assertEquals(7, store.get<Int>())

        val ro: ExtensionsRef = view
        assertNotNull(ro.extensions().get<Int>())
    }

    @Test
    fun chainableExtensionsPairFirstWins() {
        val a = Extensions().also { it.insert("from-a") }
        val b = Extensions().also { it.insert("from-b") }

        val pair = ChainableExtensionsPair(a.asExtensionsRef(), b.asExtensionsRef())
        assertEquals("from-a", pair.get<String>())
        assertTrue(pair.contains<String>())
        assertFalse(pair.contains<Int>())
    }

    @Test
    fun chainableExtensionsTripleSearchesAllStores() {
        val a = Extensions()
        val b = Extensions().also { it.insert("from-b") }
        val c = Extensions().also { it.insert(42) }

        val triple =
            ChainableExtensionsTriple(
                a.asExtensionsRef(),
                b.asExtensionsRef(),
                c.asExtensionsRef(),
            )
        assertEquals("from-b", triple.get<String>())
        assertEquals(42, triple.get<Int>())
        assertFalse(triple.contains<Double>())
    }
}
