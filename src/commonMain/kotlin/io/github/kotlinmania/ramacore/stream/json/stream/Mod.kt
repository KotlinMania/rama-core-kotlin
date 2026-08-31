// port-lint: source rama-core/src/stream/json/stream/mod.rs
package io.github.kotlinmania.ramacore.stream.json.stream

/**
 * Re-exports for stream json streaming module.
 */
public typealias ReadStream<T, S> = JsonReadStream<T, S>
public typealias WriteStream<T> = JsonWriteStream<T>
