// port-lint: source combinators/mod.rs
package io.github.kotlinmania.ramacore.combinators

// Combinators for working with or in function of services.
//
// Tracking file for the upstream combinators module root. The upstream root
// consists of one submodule declaration and a re-export block, so per the
// workspace rule on root re-exports (CLAUDE.md), no Kotlin typealias is
// introduced. Callers reach the canonical symbols directly via
// [io.github.kotlinmania.ramacore.combinators.Either] (and Either3..Either9).
//
// Upstream submodules:
//   - The either submodule is translated to Either.kt in this same package.
//
// Upstream root re-exports:
//   - Either, Either3, Either4, Either5, Either6, Either7, Either8, Either9
//     are re-exported at the combinators module root from the either module.
//     The Kotlin translations live in Either.kt in this same package and are
//     accessible by name from any caller in
//     [io.github.kotlinmania.ramacore.combinators]; no synthetic alias is
//     introduced.
//   - define_either, impl_either, impl_async_read_write_either, and
//     impl_iterator_either are Rust `macro_rules!` macros re-exported through
//     the combinators module. Kotlin does not have user-defined macros, so
//     these symbols have no Kotlin counterpart; their generated blanket-impl
//     bodies must be expanded manually at each use site if the consumer trait
//     has a Kotlin analog (Iterator does; tokio AsyncRead and AsyncWrite do
//     not at commonMain).
//
// Callers migrated:
//   (none — Either has no Kotlin callers yet at the time this tracking file
//   landed; the upstream Service trait impls that consume `impl_either` are
//   not ported yet.)

internal object CombinatorsModuleLedger
