// port-lint: tests rama-core/src/combinators/either.rs
package io.github.kotlinmania.ramacore.combinators

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EitherTest {
    @Test
    fun testEither2() {
        val a: Either<String, Int> = Either.A("hello")
        val b: Either<String, Int> = Either.B(42)

        assertEquals("hello", a.toString())
        assertEquals("42", b.toString())

        assertEquals(Either.A("hello"), a)
        assertEquals(Either.B(42), b)
        assertNotEquals<Either<String, Int>>(a, b)

        val unwrappedA =
            when (a) {
                is Either.A -> a.value
                is Either.B -> a.value.toString()
            }
        assertEquals("hello", unwrappedA)

        val unwrappedB =
            when (b) {
                is Either.A -> b.value
                is Either.B -> b.value.toString()
            }
        assertEquals("42", unwrappedB)
    }

    @Test
    fun testEither3() {
        val a: Either3<String, Int, Boolean> = Either3.A("alpha")
        val b: Either3<String, Int, Boolean> = Either3.B(100)
        val c: Either3<String, Int, Boolean> = Either3.C(true)

        assertEquals("alpha", a.toString())
        assertEquals("100", b.toString())
        assertEquals("true", c.toString())

        assertEquals(Either3.A("alpha"), a)
        assertEquals(Either3.B(100), b)
        assertEquals(Either3.C(true), c)

        val matchedC =
            when (c) {
                is Either3.A -> "a"
                is Either3.B -> "b"
                is Either3.C -> "c"
            }
        assertEquals("c", matchedC)
    }

    @Test
    fun testEither4() {
        val a: Either4<Int, Int, Int, Int> = Either4.A(1)
        val b: Either4<Int, Int, Int, Int> = Either4.B(2)
        val c: Either4<Int, Int, Int, Int> = Either4.C(3)
        val d: Either4<Int, Int, Int, Int> = Either4.D(4)

        assertEquals("1", a.toString())
        assertEquals("2", b.toString())
        assertEquals("3", c.toString())
        assertEquals("4", d.toString())

        val matched =
            listOf(a, b, c, d).map { item ->
                when (item) {
                    is Either4.A -> item.value
                    is Either4.B -> item.value
                    is Either4.C -> item.value
                    is Either4.D -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4), matched)
    }

    @Test
    fun testEither5() {
        val items: List<Either5<Int, Int, Int, Int, Int>> =
            listOf(
                Either5.A(1),
                Either5.B(2),
                Either5.C(3),
                Either5.D(4),
                Either5.E(5),
            )

        val values =
            items.map { item ->
                when (item) {
                    is Either5.A -> item.value
                    is Either5.B -> item.value
                    is Either5.C -> item.value
                    is Either5.D -> item.value
                    is Either5.E -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4, 5), values)
    }

    @Test
    fun testEither6() {
        val items: List<Either6<Int, Int, Int, Int, Int, Int>> =
            listOf(
                Either6.A(1),
                Either6.B(2),
                Either6.C(3),
                Either6.D(4),
                Either6.E(5),
                Either6.F(6),
            )

        val values =
            items.map { item ->
                when (item) {
                    is Either6.A -> item.value
                    is Either6.B -> item.value
                    is Either6.C -> item.value
                    is Either6.D -> item.value
                    is Either6.E -> item.value
                    is Either6.F -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4, 5, 6), values)
    }

    @Test
    fun testEither7() {
        val items: List<Either7<Int, Int, Int, Int, Int, Int, Int>> =
            listOf(
                Either7.A(1),
                Either7.B(2),
                Either7.C(3),
                Either7.D(4),
                Either7.E(5),
                Either7.F(6),
                Either7.G(7),
            )

        val values =
            items.map { item ->
                when (item) {
                    is Either7.A -> item.value
                    is Either7.B -> item.value
                    is Either7.C -> item.value
                    is Either7.D -> item.value
                    is Either7.E -> item.value
                    is Either7.F -> item.value
                    is Either7.G -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), values)
    }

    @Test
    fun testEither8() {
        val items: List<Either8<Int, Int, Int, Int, Int, Int, Int, Int>> =
            listOf(
                Either8.A(1),
                Either8.B(2),
                Either8.C(3),
                Either8.D(4),
                Either8.E(5),
                Either8.F(6),
                Either8.G(7),
                Either8.H(8),
            )

        val values =
            items.map { item ->
                when (item) {
                    is Either8.A -> item.value
                    is Either8.B -> item.value
                    is Either8.C -> item.value
                    is Either8.D -> item.value
                    is Either8.E -> item.value
                    is Either8.F -> item.value
                    is Either8.G -> item.value
                    is Either8.H -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8), values)
    }

    @Test
    fun testEither9() {
        val items: List<Either9<Int, Int, Int, Int, Int, Int, Int, Int, Int>> =
            listOf(
                Either9.A(1),
                Either9.B(2),
                Either9.C(3),
                Either9.D(4),
                Either9.E(5),
                Either9.F(6),
                Either9.G(7),
                Either9.H(8),
                Either9.I(9),
            )

        val values =
            items.map { item ->
                when (item) {
                    is Either9.A -> item.value
                    is Either9.B -> item.value
                    is Either9.C -> item.value
                    is Either9.D -> item.value
                    is Either9.E -> item.value
                    is Either9.F -> item.value
                    is Either9.G -> item.value
                    is Either9.H -> item.value
                    is Either9.I -> item.value
                }
            }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), values)
        assertEquals("9", Either9.I(9).toString())
        assertEquals(Either9.I(9).hashCode(), Either9.I(9).hashCode())
    }
}
