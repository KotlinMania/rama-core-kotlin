# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 13/60 (21.7%)
- **Function parity:** 49/446 matched (target 289) — 11.0%
- **Class/type parity:** 26/173 matched (target 66) — 15.0%
- **Combined symbol parity:** 75/619 matched (target 355) — 12.1%
- **Average inline-code cosine:** 0.25 (function body across 9 matched files)
- **Average documentation cosine:** 0.63 (doc text across 9 matched files)
- **Cheat-zeroed Files:** 6
- **Critical Issues:** 12 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. extensions

- **Target:** `ramacore.Extensions`
- **Similarity:** 0.21
- **Dependents:** 9
- **Priority Score:** 9062408.0
- **Functions:** 12/16 matched (target 36)
- **Missing functions:** `new`, `clone_box`, `as_any`, `clone`
- **Types:** 6/8 matched (target 11)
- **Missing types:** `Extension`, `ExtensionType`
- **Tests:** 4/4 matched

### 2. svc_input

- **Target:** `ramacore.SvcInput`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 161908.7
- **Functions:** 3/19 matched (target 7)
- **Missing functions:** `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`, `read`, `read_vectored`, `read_to_end`, `read_to_string`, `read_exact`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 3. service.svc

- **Target:** `service.Svc [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 152410.0
- **Functions:** 4/15 matched (target 32)
- **Missing functions:** `serve_box`, `clone`, `fmt`, `assert_send`, `assert_sync`, `add_svc`, `static_dispatch`, `dynamic_dispatch`, `service_arc`, `box_service_arc`, `reject_svc`
- **Types:** 5/9 matched (target 7)
- **Missing types:** `Service`, `Output`, `Error`, `DynService`
- **Tests:** 0/8 matched

### 4. layer.mod

- **Target:** `layer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 111510.0
- **Functions:** 3/9 matched (target 8)
- **Missing functions:** `fmt`, `clone`, `simple_input_layer`, `simple_optional_input_layer`, `simple_output_layer`, `simple_optional_output_layer`
- **Types:** 1/6 matched (target 3)
- **Missing types:** `Layer`, `Service`, `MaybeLayeredSvc`, `Error`, `Output`
- **Tests:** 0/4 matched

### 5. service.handler

- **Target:** `service.Handler`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 101508.4
- **Functions:** 4/10 matched (target 7)
- **Missing functions:** `call`, `fmt`, `clone`, `from_input`, `assert_send_sync`, `test_service_fn_without_usage`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `Factory`, `Output`, `Error`, `FromInput`
- **Tests:** 1/3 matched

### 6. conversion

- **Target:** `ramacore.Conversion`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 91107.9
- **Functions:** 2/5 matched (target 9)
- **Missing functions:** `rama_from`, `rama_try_from`, `from_ref`
- **Types:** 0/6 matched (target 1)
- **Missing types:** `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`

### 7. layer.layer_fn

- **Target:** `layer.LayerFn`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 81304.7
- **Functions:** 4/7 matched (target 9)
- **Missing functions:** `fmt`, `serve`, `layer_fn_has_useful_debug_impl`
- **Types:** 1/6 matched (target 3)
- **Missing types:** `Service`, `ToUpper`, `Output`, `Error`, `WrappedService`
- **Tests:** 1/3 matched

### 8. username.compose

- **Target:** `username.Compose`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 51207.3
- **Functions:** 5/8 matched (target 17)
- **Missing functions:** `fmt`, `source`, `write_labels`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `ComposeErrorKind`, `UsernameLabelWriter`

### 9. username.parse

- **Target:** `username.Parse`
- **Similarity:** 0.69
- **Dependents:** 0
- **Priority Score:** 42603.1
- **Functions:** 12/15 matched (target 32)
- **Missing functions:** `write_labels`, `test_username_label_parser_abort_tuple`, `test_username_label_parser_abort_exclusive_tuple`
- **Types:** 10/11 matched (target 13)
- **Missing types:** `Error`
- **Tests:** 7/9 matched

### 10. username.mod

- **Target:** `username.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `parse_compose_username_labels`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 11. service.mod

- **Target:** `service.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 12. combinators.mod

- **Target:** `combinators.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 13. combinators.either

- **Target:** `combinators.Either [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 132)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 17)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `limit.mod` | `layer.limit.Mod` | 0 | `layer/limit/mod.rs` | `layer/limit/Mod.kt` |
| `policy.mod` | `layer.limit.policy.Mod` | 0 | `layer/limit/policy/mod.rs` | `layer/limit/policy/Mod.kt` |
| `timeout.mod` | `layer.timeout.Mod` | 0 | `layer/timeout/mod.rs` | `layer/timeout/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `matcher.mod` | `matcher.Mod` | 0 | `matcher/mod.rs` | `matcher/Mod.kt` |
| `rt.mod` | `rt.Mod` | 0 | `rt/mod.rs` | `rt/Mod.kt` |
| `json.mod` | `stream.json.Mod` | 0 | `stream/json/mod.rs` | `stream/json/Mod.kt` |
| `stream.json.stream.mod` | `stream.json.stream.Mod` | 0 | `stream/json/stream/mod.rs` | `stream/json/stream/Mod.kt` |
| `stream.mod` | `stream.Mod` | 0 | `stream/mod.rs` | `stream/Mod.kt` |
| `telemetry.mod` | `telemetry.Mod` | 0 | `telemetry/mod.rs` | `telemetry/Mod.kt` |
| `opentelemetry.mod` | `telemetry.opentelemetry.Mod` | 0 | `telemetry/opentelemetry/mod.rs` | `telemetry/opentelemetry/Mod.kt` |

