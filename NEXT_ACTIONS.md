# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 39/60 (65.0%)
- **Function parity:** 119/424 matched (target 509) — 28.1%
- **Class/type parity:** 73/188 matched (target 134) — 38.8%
- **Combined symbol parity:** 192/612 matched (target 643) — 31.4%
- **Average inline-code cosine:** 0.30 (function body across 31 matched files)
- **Average documentation cosine:** 0.59 (doc text across 31 matched files)
- **Cheat-zeroed Files:** 10
- **Critical Issues:** 36 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. extensions

- **Target:** `ramacore.Extensions [PROVENANCE-FALLBACK]`
- **Similarity:** 0.21
- **Dependents:** 9
- **Priority Score:** 9062408.0
- **Functions:** 12/16 matched (target 36)
- **Missing functions:** `new`, `clone_box`, `as_any`, `clone`
- **Types:** 6/8 matched (target 11)
- **Missing types:** `Extension`, `ExtensionType`
- **Tests:** 4/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions.rs` vs expected `extensions.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:extensions.rs` vs expected `extensions.rs`
- **Proposed provenance header:** `// port-lint: source extensions.rs` (current: `// port-lint: source extensions.rs`)
- **Proposed provenance header:** `// port-lint: tests extensions.rs` (current: `// port-lint: tests extensions.rs`)
- **Lint issues:** 2

### 2. policy.matcher

- **Target:** `policy.Matcher [PROVENANCE-FALLBACK]`
- **Similarity:** 0.10
- **Dependents:** 5
- **Priority Score:** 5101109.0
- **Functions:** 1/7 matched (target 3)
- **Missing functions:** `assert_ready`, `assert_abort`, `matcher_policy_empty`, `matcher_policy_always`, `matches`, `matcher_policy_scoped_limits`
- **Types:** 0/4 matched (target 2)
- **Missing types:** `Guard`, `Error`, `NumberedRequest`, `TestMatchers`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/policy/matcher.rs` vs expected `layer/limit/policy/matcher.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/policy/matcher.rs` (current: `// port-lint: source layer/limit/policy/matcher.rs`)
- **Lint issues:** 1

### 3. limit.layer

- **Target:** `limit.Layer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 2
- **Priority Score:** 2030706.9
- **Functions:** 3/5 matched (target 4)
- **Missing functions:** `with_error_into_response_fn`, `into_layer`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/layer.rs` vs expected `layer/limit/layer.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/layer.rs` (current: `// port-lint: source layer/limit/layer.rs`)
- **Lint issues:** 1

### 4. matcher.iter

- **Target:** `matcher.Iter [PROVENANCE-FALLBACK]`
- **Similarity:** 0.86
- **Dependents:** 1
- **Priority Score:** 1010301.4
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `IteratorMatcherExt`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/iter.rs` vs expected `matcher/iter.rs`
- **Proposed provenance header:** `// port-lint: source matcher/iter.rs` (current: `// port-lint: source matcher/iter.rs`)
- **Lint issues:** 1

### 5. svc_input

- **Target:** `ramacore.SvcInput [PROVENANCE-FALLBACK]`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 161909.4
- **Functions:** 3/19 matched (target 7)
- **Missing functions:** `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`, `read`, `read_vectored`, `read_to_end`, `read_to_string`, `read_exact`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `svc_input.rs` vs expected `svc_input.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:svc_input.rs` vs expected `svc_input.rs`
- **Proposed provenance header:** `// port-lint: source svc_input.rs` (current: `// port-lint: source svc_input.rs`)
- **Proposed provenance header:** `// port-lint: tests svc_input.rs` (current: `// port-lint: tests svc_input.rs`)
- **Lint issues:** 2

### 6. service.svc

- **Target:** `service.Svc [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 142410.0
- **Functions:** 4/15 matched (target 32)
- **Missing functions:** `serve_box`, `clone`, `fmt`, `assert_send`, `assert_sync`, `add_svc`, `static_dispatch`, `dynamic_dispatch`, `service_arc`, `box_service_arc`, `reject_svc`
- **Types:** 6/9 matched (target 8)
- **Missing types:** `Output`, `Error`, `DynService`
- **Tests:** 0/8 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `service/svc.rs` vs expected `service/svc.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:service/svc.rs` vs expected `service/svc.rs`
- **Proposed provenance header:** `// port-lint: source service/svc.rs` (current: `// port-lint: source service/svc.rs`)
- **Proposed provenance header:** `// port-lint: tests service/svc.rs` (current: `// port-lint: tests service/svc.rs`)
- **Lint issues:** 2

### 7. layer.consume_err

- **Target:** `layer.ConsumeErr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 121708.7
- **Functions:** 3/9 matched (target 12)
- **Missing functions:** `fmt`, `default`, `with_output`, `trace`, `with_response`, `into_layer`
- **Types:** 2/8 matched (target 4)
- **Missing types:** `Output`, `Error`, `Service`, `Trace`, `DefaultOutput`, `StaticOutput`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/consume_err.rs` vs expected `layer/consume_err.rs`
- **Proposed provenance header:** `// port-lint: source layer/consume_err.rs` (current: `// port-lint: source layer/consume_err.rs`)
- **Lint issues:** 1

### 8. layer.mod

- **Target:** `layer.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 111510.0
- **Functions:** 3/9 matched (target 21)
- **Missing functions:** `fmt`, `clone`, `simple_input_layer`, `simple_optional_input_layer`, `simple_output_layer`, `simple_optional_output_layer`
- **Types:** 1/6 matched
- **Missing types:** `Layer`, `Service`, `MaybeLayeredSvc`, `Error`, `Output`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/mod.rs` vs expected `layer/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:layer/mod.rs` vs expected `layer/mod.rs`
- **Proposed provenance header:** `// port-lint: source layer/mod.rs` (current: `// port-lint: source layer/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests layer/mod.rs` (current: `// port-lint: tests layer/mod.rs`)
- **Lint issues:** 2

### 9. policy.concurrent

- **Target:** `policy.Concurrent [PROVENANCE-FALLBACK]`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 101807.6
- **Functions:** 4/12 matched (target 11)
- **Missing functions:** `with_backoff`, `max_with_backoff`, `drop`, `assert_ready`, `assert_abort`, `concurrent_policy_zero`, `concurrent_policy`, `concurrent_policy_clone`
- **Types:** 4/6 matched (target 5)
- **Missing types:** `Error`, `ConcurrentCounterGuard`
- **Tests:** 0/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/policy/concurrent.rs` vs expected `layer/limit/policy/concurrent.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/policy/concurrent.rs` (current: `// port-lint: source layer/limit/policy/concurrent.rs`)
- **Lint issues:** 1

### 10. service.handler

- **Target:** `service.Handler [PROVENANCE-FALLBACK]`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 101508.4
- **Functions:** 4/10 matched (target 12)
- **Missing functions:** `call`, `fmt`, `clone`, `from_input`, `assert_send_sync`, `test_service_fn_without_usage`
- **Types:** 1/5 matched (target 3)
- **Missing types:** `Factory`, `Output`, `Error`, `FromInput`
- **Tests:** 1/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `service/handler.rs` vs expected `service/handler.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:service/handler.rs` vs expected `service/handler.rs`
- **Proposed provenance header:** `// port-lint: source service/handler.rs` (current: `// port-lint: source service/handler.rs`)
- **Proposed provenance header:** `// port-lint: tests service/handler.rs` (current: `// port-lint: tests service/handler.rs`)
- **Lint issues:** 2

### 11. layer.layer_fn

- **Target:** `layer.LayerFn [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 91306.9
- **Functions:** 3/7 matched (target 5)
- **Missing functions:** `fmt`, `test_layer_fn`, `serve`, `layer_fn_has_useful_debug_impl`
- **Types:** 1/6 matched (target 1)
- **Missing types:** `Service`, `ToUpper`, `Output`, `Error`, `WrappedService`
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/layer_fn.rs` vs expected `layer/layer_fn.rs`
- **Proposed provenance header:** `// port-lint: source layer/layer_fn.rs` (current: `// port-lint: source layer/layer_fn.rs`)
- **Lint issues:** 1

### 12. conversion

- **Target:** `ramacore.Conversion [PROVENANCE-FALLBACK]`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 91107.9
- **Functions:** 2/5 matched (target 11)
- **Missing functions:** `rama_from`, `rama_try_from`, `from_ref`
- **Types:** 0/6 matched (target 1)
- **Missing types:** `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `conversion.rs` vs expected `conversion.rs`
- **Proposed provenance header:** `// port-lint: source conversion.rs` (current: `// port-lint: source conversion.rs`)
- **Lint issues:** 1

### 13. layer.get_extension

- **Target:** `layer.GetExtension [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 81606.9
- **Functions:** 4/8 matched (target 18)
- **Missing functions:** `fmt`, `clone`, `get_extension_basic`, `get_extension_output`
- **Types:** 4/8 matched (target 4)
- **Missing types:** `Service`, `Output`, `Error`, `State`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/get_extension.rs` vs expected `layer/get_extension.rs`
- **Proposed provenance header:** `// port-lint: source layer/get_extension.rs` (current: `// port-lint: source layer/get_extension.rs`)
- **Lint issues:** 1

### 14. matcher.ext

- **Target:** `matcher.Ext [PROVENANCE-FALLBACK]`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 81207.4
- **Functions:** 3/6 matched (target 4)
- **Missing functions:** `call`, `test_extension_matcher`, `test_fn_extension_matcher`
- **Types:** 1/6 matched (target 1)
- **Missing types:** `ExtensionPredicate`, `PredicateConst`, `PredicateFn`, `MyMarker`, `MyOtherMarker`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/ext.rs` vs expected `matcher/ext.rs`
- **Proposed provenance header:** `// port-lint: source matcher/ext.rs` (current: `// port-lint: source matcher/ext.rs`)
- **Lint issues:** 2

### 15. limit.mod

- **Target:** `limit.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71110.0
- **Functions:** 3/8 matched (target 7)
- **Missing functions:** `with_error_into_output_fn`, `test_limit`, `handle_request`, `test_with_error_into_response_fn`, `test_zero_limit`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Output`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/mod.rs` (current: `// port-lint: source layer/limit/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests layer/limit/mod.rs` (current: `// port-lint: tests layer/limit/mod.rs`)
- **Lint issues:** 2

### 16. layer.add_extension

- **Target:** `layer.AddExtension [PROVENANCE-FALLBACK]`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 61404.0
- **Functions:** 4/6 matched (target 18)
- **Missing functions:** `basic_input`, `basic_output`
- **Types:** 4/8 matched (target 4)
- **Missing types:** `Service`, `Output`, `Error`, `Counter`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/add_extension.rs` vs expected `layer/add_extension.rs`
- **Proposed provenance header:** `// port-lint: source layer/add_extension.rs` (current: `// port-lint: source layer/add_extension.rs`)
- **Lint issues:** 1

### 17. layer.map_err

- **Target:** `layer.MapErr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 51007.2
- **Functions:** 3/5 matched (target 12)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/map_err.rs` vs expected `layer/map_err.rs`
- **Proposed provenance header:** `// port-lint: source layer/map_err.rs` (current: `// port-lint: source layer/map_err.rs`)
- **Lint issues:** 1

### 18. layer.map_output

- **Target:** `layer.MapOutput [PROVENANCE-FALLBACK]`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 51007.2
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/map_output.rs` vs expected `layer/map_output.rs`
- **Proposed provenance header:** `// port-lint: source layer/map_output.rs` (current: `// port-lint: source layer/map_output.rs`)
- **Lint issues:** 1

### 19. layer.trace_err

- **Target:** `layer.TraceErr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 51006.9
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `with_level`, `default`
- **Types:** 2/5 matched (target 4)
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/trace_err.rs` vs expected `layer/trace_err.rs`
- **Proposed provenance header:** `// port-lint: source layer/trace_err.rs` (current: `// port-lint: source layer/trace_err.rs`)
- **Lint issues:** 1

### 20. layer.map_input

- **Target:** `layer.MapInput [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 51006.9
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/map_input.rs` vs expected `layer/map_input.rs`
- **Proposed provenance header:** `// port-lint: source layer/map_input.rs` (current: `// port-lint: source layer/map_input.rs`)
- **Lint issues:** 1

### 21. layer.map_result

- **Target:** `layer.MapResult [PROVENANCE-FALLBACK]`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 51006.8
- **Functions:** 3/5 matched (target 10)
- **Missing functions:** `fmt`, `into_layer`
- **Types:** 2/5 matched (target 3)
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/map_result.rs` vs expected `layer/map_result.rs`
- **Proposed provenance header:** `// port-lint: source layer/map_result.rs` (current: `// port-lint: source layer/map_result.rs`)
- **Lint issues:** 1

### 22. matcher.mfn

- **Target:** `matcher.Mfn [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 50809.1
- **Functions:** 2/5 matched (target 8)
- **Missing functions:** `clone`, `fmt`, `call`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `MatchFnBox`, `Sealed`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/mfn.rs` vs expected `matcher/mfn.rs`
- **Proposed provenance header:** `// port-lint: source matcher/mfn.rs` (current: `// port-lint: source matcher/mfn.rs`)
- **Lint issues:** 1

### 23. username.parse

- **Target:** `username.Parse [PROVENANCE-FALLBACK]`
- **Similarity:** 0.69
- **Dependents:** 0
- **Priority Score:** 42603.1
- **Functions:** 12/15 matched (target 32)
- **Missing functions:** `write_labels`, `test_username_label_parser_abort_tuple`, `test_username_label_parser_abort_exclusive_tuple`
- **Types:** 10/11 matched (target 13)
- **Missing types:** `Error`
- **Tests:** 7/9 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `username/parse.rs` vs expected `username/parse.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:username/parse.rs` vs expected `username/parse.rs`
- **Proposed provenance header:** `// port-lint: source username/parse.rs` (current: `// port-lint: source username/parse.rs`)
- **Proposed provenance header:** `// port-lint: tests username/parse.rs` (current: `// port-lint: tests username/parse.rs`)
- **Lint issues:** 2

### 24. timeout.mod

- **Target:** `timeout.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 41010.0
- **Functions:** 5/6 matched
- **Missing functions:** `with`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `DefaultTimeout`, `Output`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/timeout/mod.rs` vs expected `layer/timeout/mod.rs`
- **Proposed provenance header:** `// port-lint: source layer/timeout/mod.rs` (current: `// port-lint: source layer/timeout/mod.rs`)
- **Lint issues:** 1

### 25. policy.mod

- **Target:** `policy.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40910.0
- **Functions:** 1/3 matched
- **Missing functions:** `fmt`, `new`
- **Types:** 4/6 matched (target 7)
- **Missing types:** `Guard`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/policy/mod.rs` vs expected `layer/limit/policy/mod.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/policy/mod.rs` (current: `// port-lint: source layer/limit/policy/mod.rs`)
- **Lint issues:** 1

### 26. layer.hijack

- **Target:** `layer.Hijack [PROVENANCE-FALLBACK]`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 40904.5
- **Functions:** 3/4 matched (target 6)
- **Missing functions:** `into_layer`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Output`, `Error`, `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/hijack.rs` vs expected `layer/hijack.rs`
- **Proposed provenance header:** `// port-lint: source layer/hijack.rs` (current: `// port-lint: source layer/hijack.rs`)
- **Lint issues:** 1

### 27. username.compose

- **Target:** `username.Compose [PROVENANCE-FALLBACK]`
- **Similarity:** 0.43
- **Dependents:** 0
- **Priority Score:** 31205.7
- **Functions:** 6/8 matched (target 20)
- **Missing functions:** `fmt`, `source`
- **Types:** 3/4 matched
- **Missing types:** `ComposeErrorKind`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `username/compose.rs` vs expected `username/compose.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:username/compose.rs` vs expected `username/compose.rs`
- **Proposed provenance header:** `// port-lint: source username/compose.rs` (current: `// port-lint: source username/compose.rs`)
- **Proposed provenance header:** `// port-lint: tests username/compose.rs` (current: `// port-lint: tests username/compose.rs`)
- **Lint issues:** 2

### 28. layer.into_error

- **Target:** `layer.IntoError [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 30705.9
- **Functions:** 2/2 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 2)
- **Missing types:** `MakeLayerError`, `Error`, `Sealed`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/into_error.rs` vs expected `layer/into_error.rs`
- **Proposed provenance header:** `// port-lint: source layer/into_error.rs` (current: `// port-lint: source layer/into_error.rs`)
- **Lint issues:** 1

### 29. timeout.layer

- **Target:** `timeout.Layer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 20804.9
- **Functions:** 5/6 matched
- **Missing functions:** `into_layer`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Service`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/timeout/layer.rs` vs expected `layer/timeout/layer.rs`
- **Proposed provenance header:** `// port-lint: source layer/timeout/layer.rs` (current: `// port-lint: source layer/timeout/layer.rs`)
- **Lint issues:** 1

### 30. limit.into_output

- **Target:** `limit.IntoOutput [PROVENANCE-FALLBACK]`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 20507.3
- **Functions:** 1/1 matched (target 3)
- **Missing functions:** _none_
- **Types:** 2/4 matched (target 2)
- **Missing types:** `Output`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/limit/into_output.rs` vs expected `layer/limit/into_output.rs`
- **Proposed provenance header:** `// port-lint: source layer/limit/into_output.rs` (current: `// port-lint: source layer/limit/into_output.rs`)
- **Lint issues:** 1

### 31. matcher.mod

- **Target:** `matcher.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10610.0
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Matcher`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/mod.rs` vs expected `matcher/mod.rs`
- **Proposed provenance header:** `// port-lint: source matcher/mod.rs` (current: `// port-lint: source matcher/mod.rs`)
- **Lint issues:** 1

### 32. timeout.error

- **Target:** `timeout.Error [PROVENANCE-FALLBACK]`
- **Similarity:** 0.42
- **Dependents:** 0
- **Priority Score:** 10305.8
- **Functions:** 1/2 matched (target 4)
- **Missing functions:** `fmt`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `layer/timeout/error.rs` vs expected `layer/timeout/error.rs`
- **Proposed provenance header:** `// port-lint: source layer/timeout/error.rs` (current: `// port-lint: source layer/timeout/error.rs`)
- **Lint issues:** 1

### 33. username.mod

- **Target:** `username.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `parse_compose_username_labels`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `username/mod.rs` vs expected `username/mod.rs`
- **Proposed provenance header:** `// port-lint: source username/mod.rs` (current: `// port-lint: source username/mod.rs`)
- **Lint issues:** 1

### 34. matcher.op_not

- **Target:** `matcher.OpNot [PROVENANCE-FALLBACK]`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 306.0
- **Functions:** 2/2 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/op_not.rs` vs expected `matcher/op_not.rs`
- **Proposed provenance header:** `// port-lint: source matcher/op_not.rs` (current: `// port-lint: source matcher/op_not.rs`)
- **Lint issues:** 1

### 35. matcher.op_and

- **Target:** `matcher.OpAnd [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/op_and.rs` vs expected `matcher/op_and.rs`
- **Proposed provenance header:** `// port-lint: source matcher/op_and.rs` (current: `// port-lint: source matcher/op_and.rs`)
- **Lint issues:** 1

### 36. matcher.op_or

- **Target:** `matcher.OpOr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `matcher/op_or.rs` vs expected `matcher/op_or.rs`
- **Proposed provenance header:** `// port-lint: source matcher/op_or.rs` (current: `// port-lint: source matcher/op_or.rs`)
- **Lint issues:** 1

### 37. combinators.mod

- **Target:** `combinators.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `combinators/mod.rs` vs expected `combinators/mod.rs`
- **Proposed provenance header:** `// port-lint: source combinators/mod.rs` (current: `// port-lint: source combinators/mod.rs`)
- **Lint issues:** 1

### 38. service.mod

- **Target:** `service.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `service/mod.rs` vs expected `service/mod.rs`
- **Proposed provenance header:** `// port-lint: source service/mod.rs` (current: `// port-lint: source service/mod.rs`)
- **Lint issues:** 1

### 39. combinators.either

- **Target:** `combinators.Either [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 132)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 17)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `combinators/either.rs` vs expected `combinators/either.rs`
- **Proposed provenance header:** `// port-lint: source combinators/either.rs` (current: `// port-lint: source combinators/either.rs`)
- **Lint issues:** 1

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
| `lib` | `Lib` | 0 | `src/lib.rs` | `Lib.kt` |
| `rt.mod` | `rt.Mod` | 0 | `src/rt/mod.rs` | `rt/Mod.kt` |
| `json.mod` | `stream.json.Mod` | 0 | `src/stream/json/mod.rs` | `stream/json/Mod.kt` |
| `stream.json.stream.mod` | `stream.json.stream.Mod` | 0 | `src/stream/json/stream/mod.rs` | `stream/json/stream/Mod.kt` |
| `stream.mod` | `stream.Mod` | 0 | `src/stream/mod.rs` | `stream/Mod.kt` |
| `telemetry.mod` | `telemetry.Mod` | 0 | `src/telemetry/mod.rs` | `telemetry/Mod.kt` |
| `opentelemetry.mod` | `telemetry.opentelemetry.Mod` | 0 | `src/telemetry/opentelemetry/mod.rs` | `telemetry/opentelemetry/Mod.kt` |

