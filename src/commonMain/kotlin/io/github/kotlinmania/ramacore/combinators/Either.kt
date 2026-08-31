// port-lint: source rama-core/src/combinators/either.rs
package io.github.kotlinmania.ramacore.combinators

/**
 * A type to allow you to use multiple types as a single type,
 * delegating functionality to the wrapped type.
 */
public sealed interface Either<out A, out B> {
    public class A<T>(
        public val value: T,
    ) : Either<T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either<Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/**
 * Three-variant counterpart to [Either]. Same delegation rules apply: each
 * wrapped type is expected to work with the same inputs and outputs.
 */
public sealed interface Either3<out A, out B, out C> {
    public class A<T>(
        public val value: T,
    ) : Either3<T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either3<Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either3<Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Four-variant counterpart to [Either]. */
public sealed interface Either4<out A, out B, out C, out D> {
    public class A<T>(
        public val value: T,
    ) : Either4<T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either4<Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either4<Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either4<Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Five-variant counterpart to [Either]. */
public sealed interface Either5<out A, out B, out C, out D, out E> {
    public class A<T>(
        public val value: T,
    ) : Either5<T, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either5<Nothing, T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either5<Nothing, Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either5<Nothing, Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class E<T>(
        public val value: T,
    ) : Either5<Nothing, Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is E<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Six-variant counterpart to [Either]. */
public sealed interface Either6<out A, out B, out C, out D, out E, out F> {
    public class A<T>(
        public val value: T,
    ) : Either6<T, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either6<Nothing, T, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either6<Nothing, Nothing, T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either6<Nothing, Nothing, Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class E<T>(
        public val value: T,
    ) : Either6<Nothing, Nothing, Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is E<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class F<T>(
        public val value: T,
    ) : Either6<Nothing, Nothing, Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is F<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Seven-variant counterpart to [Either]. */
public sealed interface Either7<out A, out B, out C, out D, out E, out F, out G> {
    public class A<T>(
        public val value: T,
    ) : Either7<T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either7<Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either7<Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either7<Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class E<T>(
        public val value: T,
    ) : Either7<Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is E<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class F<T>(
        public val value: T,
    ) : Either7<Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is F<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class G<T>(
        public val value: T,
    ) : Either7<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is G<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Eight-variant counterpart to [Either]. */
public sealed interface Either8<out A, out B, out C, out D, out E, out F, out G, out H> {
    public class A<T>(
        public val value: T,
    ) : Either8<T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either8<Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class E<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is E<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class F<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is F<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class G<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is G<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class H<T>(
        public val value: T,
    ) : Either8<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is H<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}

/** Nine-variant counterpart to [Either]. */
public sealed interface Either9<out A, out B, out C, out D, out E, out F, out G, out H, out I> {
    public class A<T>(
        public val value: T,
    ) : Either9<T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is A<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class B<T>(
        public val value: T,
    ) : Either9<Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is B<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class C<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is C<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class D<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is D<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class E<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is E<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class F<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is F<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class G<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is G<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class H<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is H<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    public class I<T>(
        public val value: T,
    ) : Either9<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T> {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean = other is I<*> && other.value == value

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
}
