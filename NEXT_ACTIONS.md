# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 59/60 (98.3%)
- **Function parity:** 239/385 matched (target 750) — 62.1%
- **Class/type parity:** 90/186 matched (target 205) — 48.4%
- **Combined symbol parity:** 329/571 matched (target 955) — 57.6%
- **Average inline-code cosine:** 0.33 (function body across 45 matched files)
- **Average documentation cosine:** 0.57 (doc text across 45 matched files)
- **Cheat-zeroed Files:** 18
- **Critical Issues:** 52 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. extensions

- **Target:** `ramacore.Extensions`
- **Similarity:** 0.24
- **Dependents:** 9
- **Priority Score:** 9052408.0
- **Functions:** 13/16 matched (target 37)
- **Missing functions:** `clone_box`, `as_any`, `clone`
- **Types:** 6/8 matched (target 11)
- **Missing types:** `Extension`, `ExtensionType`
- **Tests:** 4/4 matched

### 2. policy.matcher

- **Target:** `policy.Matcher`
- **Similarity:** 0.64
- **Dependents:** 5
- **Priority Score:** 5061103.5
- **Functions:** 4/7 matched (target 13)
- **Missing functions:** `assert_ready`, `assert_abort`, `matches`
- **Types:** 1/4 matched (target 6)
- **Missing types:** `Guard`, `Error`, `NumberedRequest`
- **Tests:** 3/6 matched

### 3. limit.layer

- **Target:** `limit.Layer`
- **Similarity:** 0.51
- **Dependents:** 2
- **Priority Score:** 2010705.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Service`

### 4. matcher.iter

- **Target:** `matcher.Iter`
- **Similarity:** 0.86
- **Dependents:** 1
- **Priority Score:** 1010301.4
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `IteratorMatcherExt`

### 5. rt.executor

- **Target:** `rt.Executor`
- **Similarity:** 0.62
- **Dependents:** 1
- **Priority Score:** 1000503.8
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 6. stream.peek

- **Target:** `stream.Peek`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 192607.0
- **Functions:** 6/25 matched (target 13)
- **Missing functions:** `poll_read`, `poll_fill_buf`, `consume`, `poll_write`, `poll_flush`, `poll_shutdown`, `poll_write_vectored`, `is_write_vectored`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`, `test_multi_read_async`, `test_multi_read_sync`, `test_sync_and_async`, `new_peek_write_stream`, `test_multi_write_async`, `test_multi_write_sync`
- **Types:** 1/1 matched (target 3)
- **Missing types:** _none_
- **Tests:** 2/8 matched

### 7. stream.read

- **Target:** `stream.Read`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 162607.3
- **Functions:** 8/24 matched (target 20)
- **Missing functions:** `from`, `default`, `poll_read`, `poll_fill_buf`, `consume`, `read_exact`, `read_to_end`, `read_to_string`, `read_vectored`, `get_ref`, `get_mut`, `get_pin_mut`, `into_inner`, `test_multi_read_async`, `test_multi_read_sync`, `test_sync_and_async`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 3/6 matched

### 8. svc_input

- **Target:** `ramacore.SvcInput`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 161909.4
- **Functions:** 3/19 matched (target 7)
- **Missing functions:** `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`, `read`, `read_vectored`, `read_to_end`, `read_to_string`, `read_exact`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 9. layer.consume_err

- **Target:** `layer.ConsumeErr`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 121708.7
- **Functions:** 3/9 matched (target 12)
- **Missing functions:** `fmt`, `default`, `with_output`, `trace`, `with_response`, `into_layer`
- **Types:** 2/8 matched (target 4)
- **Missing types:** `Output`, `Error`, `Service`, `Trace`, `DefaultOutput`, `StaticOutput`

### 10. stream.rewind

- **Target:** `stream.Rewind`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 101606.6
- **Functions:** 5/15 matched (target 8)
- **Missing functions:** `new`, `rewind`, `into_inner`, `get_mut`, `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/4 matched

### 11. service.handler

- **Target:** `service.Handler`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 91507.5
- **Functions:** 5/10 matched (target 14)
- **Missing functions:** `call`, `fmt`, `clone`, `from_input`, `assert_send_sync`
- **Types:** 1/5 matched (target 3)
- **Missing types:** `Factory`, `Output`, `Error`, `FromInput`
- **Tests:** 2/3 matched

### 12. layer.layer_fn

- **Target:** `layer.LayerFn`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 91306.9
- **Functions:** 3/7 matched (target 5)
- **Missing functions:** `fmt`, `test_layer_fn`, `serve`, `layer_fn_has_useful_debug_impl`
- **Types:** 1/6 matched (target 1)
- **Missing types:** `Service`, `ToUpper`, `Output`, `Error`, `WrappedService`
- **Tests:** 0/3 matched

### 13. conversion

- **Target:** `ramacore.Conversion`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 91107.9
- **Functions:** 2/5 matched (target 11)
- **Missing functions:** `rama_from`, `rama_try_from`, `from_ref`
- **Types:** 0/6 matched (target 1)
- **Missing types:** `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`

### 14. service.svc

- **Target:** `service.Svc [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 82410.0
- **Functions:** 10/15 matched (target 38)
- **Missing functions:** `serve_box`, `clone`, `fmt`, `assert_send`, `assert_sync`
- **Types:** 6/9 matched (target 8)
- **Missing types:** `Output`, `Error`, `DynService`
- **Tests:** 6/8 matched

### 15. layer.get_extension

- **Target:** `layer.GetExtension`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 81606.9
- **Functions:** 4/8 matched (target 18)
- **Missing functions:** `fmt`, `clone`, `get_extension_basic`, `get_extension_output`
- **Types:** 4/8 matched (target 4)
- **Missing types:** `Service`, `Output`, `Error`, `State`
- **Tests:** 0/2 matched

### 16. matcher.ext

- **Target:** `matcher.Ext`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 81207.4
- **Functions:** 3/6 matched (target 4)
- **Missing functions:** `call`, `test_extension_matcher`, `test_fn_extension_matcher`
- **Types:** 1/6 matched (target 1)
- **Missing types:** `ExtensionPredicate`, `PredicateConst`, `PredicateFn`, `MyMarker`, `MyOtherMarker`
- **Tests:** 0/2 matched
- **Lint issues:** 1

### 17. stream.json.stream.read

- **Target:** `commonMain.kotlin.io.github.kotlinmania.ramacore.stream.json.stream.Read`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 71907.4
- **Functions:** 11/16 matched
- **Missing functions:** `new`, `new_with_config`, `into_inner`, `poll_next`, `next`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `Item`, `TestStruct`
- **Tests:** 11/12 matched

### 18. policy.concurrent

- **Target:** `policy.Concurrent`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 71804.5
- **Functions:** 7/12 matched (target 16)
- **Missing functions:** `with_backoff`, `max_with_backoff`, `drop`, `assert_ready`, `assert_abort`
- **Types:** 4/6 matched
- **Missing types:** `Error`, `ConcurrentCounterGuard`
- **Tests:** 3/5 matched

### 19. layer.mod

- **Target:** `layer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71510.0
- **Functions:** 7/9 matched (target 32)
- **Missing functions:** `fmt`, `clone`
- **Types:** 1/6 matched (target 9)
- **Missing types:** `Layer`, `Service`, `MaybeLayeredSvc`, `Error`, `Output`
- **Tests:** 4/4 matched

### 20. layer.add_extension

- **Target:** `layer.AddExtension`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 61404.0
- **Functions:** 4/6 matched (target 18)
- **Missing functions:** `basic_input`, `basic_output`
- **Types:** 4/8 matched (target 4)
- **Missing types:** `Service`, `Output`, `Error`, `Counter`
- **Tests:** 0/2 matched

### 21. layer.map_err

- **Target:** `layer.MapErr`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 51007.2
- **Functions:** 3/5 matched (target 12)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched
- **Missing types:** `Output`, `Error`, `Service`

### 22. layer.map_output

- **Target:** `layer.MapOutput`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 51007.2
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`

### 23. layer.trace_err

- **Target:** `layer.TraceErr`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 51006.9
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `with_level`, `default`
- **Types:** 2/5 matched (target 4)
- **Missing types:** `Output`, `Error`, `Service`

### 24. layer.map_input

- **Target:** `layer.MapInput`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 51006.9
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`

### 25. layer.map_result

- **Target:** `layer.MapResult`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 51006.8
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`

### 26. matcher.mfn

- **Target:** `matcher.Mfn`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 50809.1
- **Functions:** 2/5 matched (target 8)
- **Missing functions:** `clone`, `fmt`, `call`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `MatchFnBox`, `Sealed`

### 27. timeout.mod

- **Target:** `timeout.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 41010.0
- **Functions:** 5/6 matched
- **Missing functions:** `with`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `DefaultTimeout`, `Output`, `Error`

### 28. stream.write

- **Target:** `stream.Write`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 41007.6
- **Functions:** 6/8 matched
- **Missing functions:** `into_inner`, `poll_next`
- **Types:** 0/2 matched (target 3)
- **Missing types:** `Item`, `TestStruct`
- **Tests:** 4/4 matched

### 29. policy.mod

- **Target:** `policy.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40910.0
- **Functions:** 1/3 matched
- **Missing functions:** `fmt`, `new`
- **Types:** 4/6 matched (target 7)
- **Missing types:** `Guard`, `Error`

### 30. layer.hijack

- **Target:** `layer.Hijack`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 40904.5
- **Functions:** 3/4 matched (target 6)
- **Missing functions:** `into_layer`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Output`, `Error`, `Service`

### 31. json.engine

- **Target:** `json.Engine`
- **Similarity:** 0.72
- **Dependents:** 0
- **Priority Score:** 33802.8
- **Functions:** 33/36 matched (target 38)
- **Missing functions:** `collect_output`, `configured_engine`, `engine_with_empty_line_handling`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 25/28 matched

### 32. json.codec

- **Target:** `json.Codec`
- **Similarity:** 0.44
- **Dependents:** 0
- **Priority Score:** 32005.6
- **Functions:** 13/15 matched (target 17)
- **Missing functions:** `clone`, `default`
- **Types:** 4/5 matched
- **Missing types:** `Error`
- **Tests:** 7/7 matched

### 33. username.compose

- **Target:** `username.Compose`
- **Similarity:** 0.43
- **Dependents:** 0
- **Priority Score:** 31205.7
- **Functions:** 6/8 matched (target 20)
- **Missing functions:** `fmt`, `source`
- **Types:** 3/4 matched
- **Missing types:** `ComposeErrorKind`

### 34. limit.mod

- **Target:** `limit.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 31110.0
- **Functions:** 7/8 matched (target 16)
- **Missing functions:** `handle_request`
- **Types:** 1/3 matched (target 5)
- **Missing types:** `Output`, `Error`
- **Tests:** 3/4 matched

### 35. layer.into_error

- **Target:** `layer.IntoError`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 30705.9
- **Functions:** 2/2 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 2)
- **Missing types:** `MakeLayerError`, `Error`, `Sealed`

### 36. lib

- **Target:** `ramacore.Lib`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 30508.9
- **Functions:** 2/4 matched (target 5)
- **Missing functions:** `take_zip_from_parts`, `poll`
- **Types:** 0/1 matched (target 3)
- **Missing types:** `Output`

### 37. limit.into_output

- **Target:** `limit.IntoOutput`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 30507.3
- **Functions:** 1/1 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/4 matched (target 1)
- **Missing types:** `ErrorIntoOutput`, `Output`, `Error`

### 38. username.parse

- **Target:** `username.Parse`
- **Similarity:** 0.73
- **Dependents:** 0
- **Priority Score:** 22602.7
- **Functions:** 14/15 matched (target 33)
- **Missing functions:** `write_labels`
- **Types:** 10/11 matched (target 13)
- **Missing types:** `Error`
- **Tests:** 9/9 matched

### 39. timeout.layer

- **Target:** `timeout.Layer`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 20804.9
- **Functions:** 5/6 matched
- **Missing functions:** `into_layer`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Service`

### 40. opentelemetry.attributes

- **Target:** `opentelemetry.Attributes`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20210.0
- **Functions:** 0/1 matched (target 6)
- **Missing functions:** `attributes`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `AttributesFactory`

### 41. matcher.mod

- **Target:** `matcher.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10610.0
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Matcher`

### 42. stream.json.stream.mod

- **Target:** `commonMain.kotlin.io.github.kotlinmania.ramacore.stream.json.stream.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10410.0
- **Functions:** 3/4 matched (target 3)
- **Missing functions:** `write_read_pending`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/4 matched

### 43. timeout.error

- **Target:** `timeout.Error`
- **Similarity:** 0.42
- **Dependents:** 0
- **Priority Score:** 10305.8
- **Functions:** 1/2 matched (target 4)
- **Missing functions:** `fmt`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 44. username.mod

- **Target:** `username.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `parse_compose_username_labels`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 45. stream.mod

- **Target:** `stream.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `Stream`

### 46. matcher.op_not

- **Target:** `matcher.OpNot`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 306.0
- **Functions:** 2/2 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 47. json.config

- **Target:** `json.Config`
- **Similarity:** 0.74
- **Dependents:** 0
- **Priority Score:** 302.6
- **Functions:** 1/1 matched (target 3)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 48. json.mod

- **Target:** `json.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 49. opentelemetry.mod

- **Target:** `opentelemetry.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 50. matcher.op_and

- **Target:** `matcher.OpAnd`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 51. matcher.op_or

- **Target:** `matcher.OpOr`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 52. rt.future

- **Target:** `rt.Future`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 107.8
- **Functions:** 1/1 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 53. combinators.mod

- **Target:** `combinators.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 54. telemetry.mod

- **Target:** `telemetry.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_

### 55. telemetry.tracing

- **Target:** `telemetry.Tracing [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 11)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 56. graceful

- **Target:** `graceful.Mod [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_

### 57. combinators.either

- **Target:** `combinators.Either [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 140)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 18)
- **Missing types:** _none_

### 58. service.mod

- **Target:** `service.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 59. rt.mod

- **Target:** `rt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
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
| `matcher.test` | `matcher.Test` | 0 | `matcher/test.rs` | `matcher/Test.kt` |

