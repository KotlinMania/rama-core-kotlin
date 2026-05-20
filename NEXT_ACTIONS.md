# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/60 (1.7%)
- **Function parity:** 2/479 matched (target 3) — 0.4%
- **Class/type parity:** 0/167 matched — 0.0%
- **Combined symbol parity:** 2/646 matched (target 3) — 0.3%
- **Average inline-code cosine:** 0.21 (function body across 1 matched files)
- **Average documentation cosine:** 0.82 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. conversion

- **Target:** `ramacore.Conversion`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 91107.9
- **Functions:** 2/5 matched (target 3)
- **Missing functions:** `rama_from`, `rama_try_from`, `from_ref`
- **Types:** 0/6 matched (target 0)
- **Missing types:** `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../tmp/rama-core/src rust ../../src/commonMain/kotlin/io/github/kotlinmania/ramacore kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `combinators.mod` | `combinators.Mod` | 0 | `combinators/mod.rs` | `combinators/Mod.kt` |
| `limit.mod` | `layer.limit.Mod` | 0 | `layer/limit/mod.rs` | `layer/limit/Mod.kt` |
| `policy.mod` | `layer.limit.policy.Mod` | 0 | `layer/limit/policy/mod.rs` | `layer/limit/policy/Mod.kt` |
| `layer.mod` | `layer.Mod` | 0 | `layer/mod.rs` | `layer/Mod.kt` |
| `timeout.mod` | `layer.timeout.Mod` | 0 | `layer/timeout/mod.rs` | `layer/timeout/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `matcher.mod` | `matcher.Mod` | 0 | `matcher/mod.rs` | `matcher/Mod.kt` |
| `rt.mod` | `rt.Mod` | 0 | `rt/mod.rs` | `rt/Mod.kt` |
| `service.mod` | `service.Mod` | 0 | `service/mod.rs` | `service/Mod.kt` |
| `json.mod` | `stream.json.Mod` | 0 | `stream/json/mod.rs` | `stream/json/Mod.kt` |
| `stream.json.stream.mod` | `stream.json.stream.Mod` | 0 | `stream/json/stream/mod.rs` | `stream/json/stream/Mod.kt` |
| `stream.mod` | `stream.Mod` | 0 | `stream/mod.rs` | `stream/Mod.kt` |
| `telemetry.mod` | `telemetry.Mod` | 0 | `telemetry/mod.rs` | `telemetry/Mod.kt` |
| `opentelemetry.mod` | `telemetry.opentelemetry.Mod` | 0 | `telemetry/opentelemetry/mod.rs` | `telemetry/opentelemetry/Mod.kt` |
| `username.mod` | `username.Mod` | 0 | `username/mod.rs` | `username/Mod.kt` |

