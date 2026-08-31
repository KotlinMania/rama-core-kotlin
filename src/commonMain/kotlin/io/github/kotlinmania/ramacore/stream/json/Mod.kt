// port-lint: source rama-core/src/stream/json/mod.rs
package io.github.kotlinmania.ramacore.stream.json

import io.github.kotlinmania.ramacore.stream.json.stream.JsonReadStream
import io.github.kotlinmania.ramacore.stream.json.stream.JsonWriteStream

/**
 * NDJSON support module.
 */
public typealias ReadStream<T, S> = JsonReadStream<T, S>
public typealias WriteStream<T> = JsonWriteStream<T>
