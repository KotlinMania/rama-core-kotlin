package io.github.kotlinmania.ramacore.stream

import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.AsyncWrite

/**
 * A stream is a type that implements [AsyncRead] and [AsyncWrite].
 * This is specific to Rama and is directly linked to the supertraits of Tokio.
 */
public interface Stream :
    AsyncRead,
    AsyncWrite
