=== Deep Analysis: tmp/rama-core (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/rama-core (rust)
  Files: 60
  Total imports: 270
  Most depended: extensions (9 dependents)

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 49
  Total imports: 160
  Most depended: ramacore.Extensions (14 dependents)

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/rama-core (60 files)
Target: src/commonMain/kotlin (49 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   39 files
Unmatched: 21 source, 2 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
extensions                    ramacore.Extensions [PROVENANCE-FALLBACK]0.21      9          12/16         6/8         9062408.0 
policy.matcher                policy.Matcher [PROVENANCE-FALLBACK]0.10      5          1/7           0/4         5101109.0 
limit.layer                   limit.Layer [PROVENANCE-FALLBACK]0.31      2          3/5           1/2         2030706.9 
matcher.iter                  matcher.Iter [PROVENANCE-FALLBACK]0.86      1          2/2           0/1         1010301.4 
svc_input                     ramacore.SvcInput [PROVENANCE-FALLBACK]0.06      0          3/19          0/0         161909.4  
service.svc                   service.Svc [ZERO] [PROVENANCE-FALLBACK]0.00      0          4/15          6/9         142410.0  
layer.consume_err             layer.ConsumeErr [PROVENANCE-FALLBACK]0.13      0          3/9           2/8         121708.7  
layer.mod                     layer.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          3/9           1/6         111510.0  
policy.concurrent             policy.Concurrent [PROVENANCE-FALLBACK]0.23      0          4/12          4/6         101807.6  
service.handler               service.Handler [PROVENANCE-FALLBACK]0.16      0          4/10          1/5         101508.4  
layer.layer_fn                layer.LayerFn [PROVENANCE-FALLBACK]0.31      0          3/7           1/6         91306.9   
conversion                    ramacore.Conversion [PROVENANCE-FALLBACK]0.21      0          2/5           0/6         91107.9   
layer.get_extension           layer.GetExtension [PROVENANCE-FALLBACK]0.31      0          4/8           4/8         81606.9   
matcher.ext                   matcher.Ext [PROVENANCE-FALLBACK]0.26      0          3/6           1/6         81207.4   
limit.mod                     limit.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          3/8           1/3         71110.0   
layer.add_extension           layer.AddExtension [PROVENANCE-FALLBACK]0.60      0          4/6           4/8         61404.0   
layer.map_err                 layer.MapErr [PROVENANCE-FALLBACK]0.28      0          3/5           2/5         51007.2   
layer.map_output              layer.MapOutput [PROVENANCE-FALLBACK]0.28      0          3/5           2/5         51007.2   
layer.trace_err               layer.TraceErr [PROVENANCE-FALLBACK]0.31      0          3/5           2/5         51006.9   
layer.map_input               layer.MapInput [PROVENANCE-FALLBACK]0.31      0          3/5           2/5         51006.9   
layer.map_result              layer.MapResult [PROVENANCE-FALLBACK]0.32      0          3/5           2/5         51006.8   
matcher.mfn                   matcher.Mfn [PROVENANCE-FALLBACK]0.09      0          2/5           1/3         50809.1   
username.parse                username.Parse [PROVENANCE-FALLBACK]0.69      0          12/15         10/11       42603.1   
timeout.mod                   timeout.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          5/6           1/4         41010.0   
policy.mod                    policy.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          1/3           4/6         40910.0   
layer.hijack                  layer.Hijack [PROVENANCE-FALLBACK]0.55      0          3/4           2/5         40904.5   
username.compose              username.Compose [PROVENANCE-FALLBACK]0.43      0          6/8           3/4         31205.7   
layer.into_error              layer.IntoError [PROVENANCE-FALLBACK]0.41      0          2/2           2/5         30705.9   
timeout.layer                 timeout.Layer [PROVENANCE-FALLBACK]0.51      0          5/6           1/2         20804.9   
limit.into_output             limit.IntoOutput [PROVENANCE-FALLBACK]0.27      0          1/1           2/4         20507.3   
matcher.mod                   matcher.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          4/4           1/2         10610.0   
timeout.error                 timeout.Error [PROVENANCE-FALLBACK]0.42      0          1/2           1/1         10305.8   
username.mod                  username.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/1           0/0         10110.0   
matcher.op_not                matcher.OpNot [PROVENANCE-FALLBACK]0.40      0          2/2           1/1         306.0     
matcher.op_and                matcher.OpAnd [PROVENANCE-FALLBACK]0.19      0          1/1           1/1         208.1     
matcher.op_or                 matcher.OpOr [PROVENANCE-FALLBACK]0.19      0          1/1           1/1         208.1     
combinators.mod               combinators.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      
service.mod                   service.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      
combinators.either            combinators.Either [ZERO] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      

=== Function and Symbol Details ===

extensions -> ramacore.Extensions [PROVENANCE-FALLBACK]
  similarity: 0.21, priority: 9062408.0, dependents: 9
  provenance warning: port-lint provenance header matched only after fallback normalization: `extensions.rs` vs expected `extensions.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:extensions.rs` vs expected `extensions.rs`
  functions: 12/16 matched (target total: 36, required body score: 0.21)
  missing functions: new, clone_box, as_any, clone
  types: 6/8 matched (target total: 11)
  missing types: Extension, ExtensionType
  tests: 4/4 matched

policy.matcher -> policy.Matcher [PROVENANCE-FALLBACK]
  similarity: 0.10, priority: 5101109.0, dependents: 5
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/matcher.rs` vs expected `layer/limit/policy/matcher.rs`
  functions: 1/7 matched (target total: 3, required body score: 0.10)
  missing functions: assert_ready, assert_abort, matcher_policy_empty, matcher_policy_always, matches, matcher_policy_scoped_limits
  types: 0/4 matched (target total: 2)
  missing types: Guard, Error, NumberedRequest, TestMatchers
  tests: 0/6 matched

limit.layer -> limit.Layer [PROVENANCE-FALLBACK]
  similarity: 0.31, priority: 2030706.9, dependents: 2
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/layer.rs` vs expected `layer/limit/layer.rs`
  functions: 3/5 matched (target total: 4, required body score: 0.31)
  missing functions: with_error_into_response_fn, into_layer
  types: 1/2 matched (target total: 1)
  missing types: Service

matcher.iter -> matcher.Iter [PROVENANCE-FALLBACK]
  similarity: 0.86, priority: 1010301.4, dependents: 1
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/iter.rs` vs expected `matcher/iter.rs`
  functions: 2/2 matched (target total: 2, required body score: 0.86)
  missing functions: none
  types: 0/1 matched (target total: 0)
  missing types: IteratorMatcherExt

svc_input -> ramacore.SvcInput [PROVENANCE-FALLBACK]
  similarity: 0.06, priority: 161909.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `svc_input.rs` vs expected `svc_input.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:svc_input.rs` vs expected `svc_input.rs`
  functions: 3/19 matched (target total: 7, required body score: 0.06)
  missing functions: poll_read, poll_write, poll_write_vectored, poll_flush, poll_shutdown, is_write_vectored, read, read_vectored, read_to_end, read_to_string, read_exact, write, flush, write_all, write_fmt, write_vectored
  types: 0/0 matched (target total: 3)
  missing types: none

service.svc -> service.Svc [ZERO] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 142410.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `service/svc.rs` vs expected `service/svc.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:service/svc.rs` vs expected `service/svc.rs`
  functions: 4/15 matched (target total: 32, required body score: 0.00)
  missing functions: serve_box, clone, fmt, assert_send, assert_sync, add_svc, static_dispatch, dynamic_dispatch, service_arc, box_service_arc, reject_svc
  types: 6/9 matched (target total: 8)
  missing types: Output, Error, DynService
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: Svc.kt: Rust-only type/unsafe terminology in Kotlin comments
  tests: 0/8 matched

layer.consume_err -> layer.ConsumeErr [PROVENANCE-FALLBACK]
  similarity: 0.13, priority: 121708.7, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/consume_err.rs` vs expected `layer/consume_err.rs`
  functions: 3/9 matched (target total: 12, required body score: 0.13)
  missing functions: fmt, default, with_output, trace, with_response, into_layer
  types: 2/8 matched (target total: 4)
  missing types: Output, Error, Service, Trace, DefaultOutput, StaticOutput

layer.mod -> layer.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 111510.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/mod.rs` vs expected `layer/mod.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:layer/mod.rs` vs expected `layer/mod.rs`
  functions: 3/9 matched (target total: 21, required body score: 0.00)
  missing functions: fmt, clone, simple_input_layer, simple_optional_input_layer, simple_output_layer, simple_optional_output_layer
  types: 1/6 matched (target total: 6)
  missing types: Layer, Service, MaybeLayeredSvc, Error, Output
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies
  tests: 0/4 matched

policy.concurrent -> policy.Concurrent [PROVENANCE-FALLBACK]
  similarity: 0.23, priority: 101807.6, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/concurrent.rs` vs expected `layer/limit/policy/concurrent.rs`
  functions: 4/12 matched (target total: 11, required body score: 0.23)
  missing functions: with_backoff, max_with_backoff, drop, assert_ready, assert_abort, concurrent_policy_zero, concurrent_policy, concurrent_policy_clone
  types: 4/6 matched (target total: 5)
  missing types: Error, ConcurrentCounterGuard
  tests: 0/5 matched

service.handler -> service.Handler [PROVENANCE-FALLBACK]
  similarity: 0.16, priority: 101508.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `service/handler.rs` vs expected `service/handler.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:service/handler.rs` vs expected `service/handler.rs`
  functions: 4/10 matched (target total: 12, required body score: 0.16)
  missing functions: call, fmt, clone, from_input, assert_send_sync, test_service_fn_without_usage
  types: 1/5 matched (target total: 3)
  missing types: Factory, Output, Error, FromInput
  tests: 1/3 matched

layer.layer_fn -> layer.LayerFn [PROVENANCE-FALLBACK]
  similarity: 0.31, priority: 91306.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/layer_fn.rs` vs expected `layer/layer_fn.rs`
  functions: 3/7 matched (target total: 5, required body score: 0.31)
  missing functions: fmt, test_layer_fn, serve, layer_fn_has_useful_debug_impl
  types: 1/6 matched (target total: 1)
  missing types: Service, ToUpper, Output, Error, WrappedService
  tests: 0/3 matched

conversion -> ramacore.Conversion [PROVENANCE-FALLBACK]
  similarity: 0.21, priority: 91107.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `conversion.rs` vs expected `conversion.rs`
  functions: 2/5 matched (target total: 11, required body score: 0.21)
  missing functions: rama_from, rama_try_from, from_ref
  types: 0/6 matched (target total: 1)
  missing types: RamaFrom, RamaInto, RamaTryFrom, Error, RamaTryInto, FromRef

layer.get_extension -> layer.GetExtension [PROVENANCE-FALLBACK]
  similarity: 0.31, priority: 81606.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/get_extension.rs` vs expected `layer/get_extension.rs`
  functions: 4/8 matched (target total: 18, required body score: 0.31)
  missing functions: fmt, clone, get_extension_basic, get_extension_output
  types: 4/8 matched (target total: 4)
  missing types: Service, Output, Error, State
  tests: 0/2 matched

matcher.ext -> matcher.Ext [PROVENANCE-FALLBACK]
  similarity: 0.26, priority: 81207.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/ext.rs` vs expected `matcher/ext.rs`
  functions: 3/6 matched (target total: 4, required body score: 0.26)
  missing functions: call, test_extension_matcher, test_fn_extension_matcher
  types: 1/6 matched (target total: 1)
  missing types: ExtensionPredicate, PredicateConst, PredicateFn, MyMarker, MyOtherMarker
  tests: 0/2 matched

limit.mod -> limit.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 71110.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
  functions: 3/8 matched (target total: 7, required body score: 0.00)
  missing functions: with_error_into_output_fn, test_limit, handle_request, test_with_error_into_response_fn, test_zero_limit
  types: 1/3 matched (target total: 2)
  missing types: Output, Error
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies
  tests: 0/4 matched

layer.add_extension -> layer.AddExtension [PROVENANCE-FALLBACK]
  similarity: 0.60, priority: 61404.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/add_extension.rs` vs expected `layer/add_extension.rs`
  functions: 4/6 matched (target total: 18, required body score: 0.60)
  missing functions: basic_input, basic_output
  types: 4/8 matched (target total: 4)
  missing types: Service, Output, Error, Counter
  tests: 0/2 matched

layer.map_err -> layer.MapErr [PROVENANCE-FALLBACK]
  similarity: 0.28, priority: 51007.2, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/map_err.rs` vs expected `layer/map_err.rs`
  functions: 3/5 matched (target total: 12, required body score: 0.28)
  missing functions: fmt, into_layer
  types: 2/5 matched (target total: 5)
  missing types: Output, Error, Service

layer.map_output -> layer.MapOutput [PROVENANCE-FALLBACK]
  similarity: 0.28, priority: 51007.2, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/map_output.rs` vs expected `layer/map_output.rs`
  functions: 3/5 matched (target total: 10, required body score: 0.28)
  missing functions: fmt, into_layer
  types: 2/5 matched (target total: 3)
  missing types: Output, Error, Service

layer.trace_err -> layer.TraceErr [PROVENANCE-FALLBACK]
  similarity: 0.31, priority: 51006.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/trace_err.rs` vs expected `layer/trace_err.rs`
  functions: 3/5 matched (target total: 10, required body score: 0.31)
  missing functions: with_level, default
  types: 2/5 matched (target total: 4)
  missing types: Output, Error, Service

layer.map_input -> layer.MapInput [PROVENANCE-FALLBACK]
  similarity: 0.31, priority: 51006.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/map_input.rs` vs expected `layer/map_input.rs`
  functions: 3/5 matched (target total: 10, required body score: 0.31)
  missing functions: fmt, into_layer
  types: 2/5 matched (target total: 3)
  missing types: Output, Error, Service

layer.map_result -> layer.MapResult [PROVENANCE-FALLBACK]
  similarity: 0.32, priority: 51006.8, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/map_result.rs` vs expected `layer/map_result.rs`
  functions: 3/5 matched (target total: 10, required body score: 0.32)
  missing functions: fmt, into_layer
  types: 2/5 matched (target total: 3)
  missing types: Output, Error, Service

matcher.mfn -> matcher.Mfn [PROVENANCE-FALLBACK]
  similarity: 0.09, priority: 50809.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/mfn.rs` vs expected `matcher/mfn.rs`
  functions: 2/5 matched (target total: 8, required body score: 0.09)
  missing functions: clone, fmt, call
  types: 1/3 matched (target total: 1)
  missing types: MatchFnBox, Sealed

username.parse -> username.Parse [PROVENANCE-FALLBACK]
  similarity: 0.69, priority: 42603.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `username/parse.rs` vs expected `username/parse.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:username/parse.rs` vs expected `username/parse.rs`
  functions: 12/15 matched (target total: 32, required body score: 0.69)
  missing functions: write_labels, test_username_label_parser_abort_tuple, test_username_label_parser_abort_exclusive_tuple
  types: 10/11 matched (target total: 13)
  missing types: Error
  tests: 7/9 matched

timeout.mod -> timeout.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 41010.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/timeout/mod.rs` vs expected `layer/timeout/mod.rs`
  functions: 5/6 matched (target total: 6, required body score: 0.00)
  missing functions: with
  types: 1/4 matched (target total: 1)
  missing types: DefaultTimeout, Output, Error
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

policy.mod -> policy.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 40910.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/mod.rs` vs expected `layer/limit/policy/mod.rs`
  functions: 1/3 matched (target total: 3, required body score: 0.00)
  missing functions: fmt, new
  types: 4/6 matched (target total: 7)
  missing types: Guard, Error
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

layer.hijack -> layer.Hijack [PROVENANCE-FALLBACK]
  similarity: 0.55, priority: 40904.5, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/hijack.rs` vs expected `layer/hijack.rs`
  functions: 3/4 matched (target total: 6, required body score: 0.55)
  missing functions: into_layer
  types: 2/5 matched (target total: 2)
  missing types: Output, Error, Service

username.compose -> username.Compose [PROVENANCE-FALLBACK]
  similarity: 0.43, priority: 31205.7, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `username/compose.rs` vs expected `username/compose.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:username/compose.rs` vs expected `username/compose.rs`
  functions: 6/8 matched (target total: 20, required body score: 0.43)
  missing functions: fmt, source
  types: 3/4 matched (target total: 4)
  missing types: ComposeErrorKind

layer.into_error -> layer.IntoError [PROVENANCE-FALLBACK]
  similarity: 0.41, priority: 30705.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/into_error.rs` vs expected `layer/into_error.rs`
  functions: 2/2 matched (target total: 6, required body score: 0.41)
  missing functions: none
  types: 2/5 matched (target total: 2)
  missing types: MakeLayerError, Error, Sealed

timeout.layer -> timeout.Layer [PROVENANCE-FALLBACK]
  similarity: 0.51, priority: 20804.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/timeout/layer.rs` vs expected `layer/timeout/layer.rs`
  functions: 5/6 matched (target total: 6, required body score: 0.51)
  missing functions: into_layer
  types: 1/2 matched (target total: 1)
  missing types: Service

limit.into_output -> limit.IntoOutput [PROVENANCE-FALLBACK]
  similarity: 0.27, priority: 20507.3, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/limit/into_output.rs` vs expected `layer/limit/into_output.rs`
  functions: 1/1 matched (target total: 3, required body score: 0.27)
  missing functions: none
  types: 2/4 matched (target total: 2)
  missing types: Output, Error

matcher.mod -> matcher.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10610.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/mod.rs` vs expected `matcher/mod.rs`
  functions: 4/4 matched (target total: 14, required body score: 0.00)
  missing functions: none
  types: 1/2 matched (target total: 3)
  missing types: Matcher
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

timeout.error -> timeout.Error [PROVENANCE-FALLBACK]
  similarity: 0.42, priority: 10305.8, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `layer/timeout/error.rs` vs expected `layer/timeout/error.rs`
  functions: 1/2 matched (target total: 4, required body score: 0.42)
  missing functions: fmt
  types: 1/1 matched (target total: 1)
  missing types: none

username.mod -> username.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10110.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `username/mod.rs` vs expected `username/mod.rs`
  functions: 0/1 matched (target total: 0, required body score: 0.00)
  missing functions: parse_compose_username_labels
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies; no target functions found; report scoring is function-by-function only
  tests: 0/1 matched

matcher.op_not -> matcher.OpNot [PROVENANCE-FALLBACK]
  similarity: 0.40, priority: 306.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/op_not.rs` vs expected `matcher/op_not.rs`
  functions: 2/2 matched (target total: 4, required body score: 0.40)
  missing functions: none
  types: 1/1 matched (target total: 1)
  missing types: none

matcher.op_and -> matcher.OpAnd [PROVENANCE-FALLBACK]
  similarity: 0.19, priority: 208.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/op_and.rs` vs expected `matcher/op_and.rs`
  functions: 1/1 matched (target total: 5, required body score: 0.19)
  missing functions: none
  types: 1/1 matched (target total: 1)
  missing types: none

matcher.op_or -> matcher.OpOr [PROVENANCE-FALLBACK]
  similarity: 0.19, priority: 208.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `matcher/op_or.rs` vs expected `matcher/op_or.rs`
  functions: 1/1 matched (target total: 5, required body score: 0.19)
  missing functions: none
  types: 1/1 matched (target total: 1)
  missing types: none

combinators.mod -> combinators.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `combinators/mod.rs` vs expected `combinators/mod.rs`
  functions: 0/0 matched (target total: 0, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

service.mod -> service.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `service/mod.rs` vs expected `service/mod.rs`
  functions: 0/0 matched (target total: 0, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

combinators.either -> combinators.Either [ZERO] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `combinators/either.rs` vs expected `combinators/either.rs`
  functions: 0/0 matched (target total: 132, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 17)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: no source functions found; target defines functions; report scoring is function-by-function only


=== Scores Forced To 0 ===

  - service.svc -> service.Svc: Svc.kt: Rust-only type/unsafe terminology in Kotlin comments
  - layer.mod -> layer.Mod: target contains TODO/stub/placeholder markers in function bodies
  - limit.mod -> limit.Mod: target contains TODO/stub/placeholder markers in function bodies
  - timeout.mod -> timeout.Mod: target contains TODO/stub/placeholder markers in function bodies
  - policy.mod -> policy.Mod: target contains TODO/stub/placeholder markers in function bodies
  - matcher.mod -> matcher.Mod: target contains TODO/stub/placeholder markers in function bodies
  - username.mod -> username.Mod: target contains TODO/stub/placeholder markers in function bodies; no target functions found; report scoring is function-by-function only
  - combinators.mod -> combinators.Mod: target contains TODO/stub/placeholder markers in function bodies
  - service.mod -> service.Mod: target contains TODO/stub/placeholder markers in function bodies
  - combinators.either -> combinators.Either: no source functions found; target defines functions; report scoring is function-by-function only

=== Provenance Header Fallbacks ===

These files were paired only after normalization; fix the port-lint source header.
  - extensions -> ramacore.Extensions: port-lint provenance header matched only after fallback normalization: `extensions.rs` vs expected `extensions.rs`
    proposed: // port-lint: source extensions.rs
  - extensions -> ramacore.Extensions: port-lint provenance header matched only after fallback normalization: `tests:extensions.rs` vs expected `extensions.rs`
    proposed: // port-lint: tests extensions.rs
  - policy.matcher -> policy.Matcher: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/matcher.rs` vs expected `layer/limit/policy/matcher.rs`
    proposed: // port-lint: source layer/limit/policy/matcher.rs
  - limit.layer -> limit.Layer: port-lint provenance header matched only after fallback normalization: `layer/limit/layer.rs` vs expected `layer/limit/layer.rs`
    proposed: // port-lint: source layer/limit/layer.rs
  - matcher.iter -> matcher.Iter: port-lint provenance header matched only after fallback normalization: `matcher/iter.rs` vs expected `matcher/iter.rs`
    proposed: // port-lint: source matcher/iter.rs
  - svc_input -> ramacore.SvcInput: port-lint provenance header matched only after fallback normalization: `svc_input.rs` vs expected `svc_input.rs`
    proposed: // port-lint: source svc_input.rs
  - svc_input -> ramacore.SvcInput: port-lint provenance header matched only after fallback normalization: `tests:svc_input.rs` vs expected `svc_input.rs`
    proposed: // port-lint: tests svc_input.rs
  - service.svc -> service.Svc: port-lint provenance header matched only after fallback normalization: `service/svc.rs` vs expected `service/svc.rs`
    proposed: // port-lint: source service/svc.rs
  - service.svc -> service.Svc: port-lint provenance header matched only after fallback normalization: `tests:service/svc.rs` vs expected `service/svc.rs`
    proposed: // port-lint: tests service/svc.rs
  - layer.consume_err -> layer.ConsumeErr: port-lint provenance header matched only after fallback normalization: `layer/consume_err.rs` vs expected `layer/consume_err.rs`
    proposed: // port-lint: source layer/consume_err.rs
  - layer.mod -> layer.Mod: port-lint provenance header matched only after fallback normalization: `layer/mod.rs` vs expected `layer/mod.rs`
    proposed: // port-lint: source layer/mod.rs
  - layer.mod -> layer.Mod: port-lint provenance header matched only after fallback normalization: `tests:layer/mod.rs` vs expected `layer/mod.rs`
    proposed: // port-lint: tests layer/mod.rs
  - policy.concurrent -> policy.Concurrent: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/concurrent.rs` vs expected `layer/limit/policy/concurrent.rs`
    proposed: // port-lint: source layer/limit/policy/concurrent.rs
  - service.handler -> service.Handler: port-lint provenance header matched only after fallback normalization: `service/handler.rs` vs expected `service/handler.rs`
    proposed: // port-lint: source service/handler.rs
  - service.handler -> service.Handler: port-lint provenance header matched only after fallback normalization: `tests:service/handler.rs` vs expected `service/handler.rs`
    proposed: // port-lint: tests service/handler.rs
  - layer.layer_fn -> layer.LayerFn: port-lint provenance header matched only after fallback normalization: `layer/layer_fn.rs` vs expected `layer/layer_fn.rs`
    proposed: // port-lint: source layer/layer_fn.rs
  - conversion -> ramacore.Conversion: port-lint provenance header matched only after fallback normalization: `conversion.rs` vs expected `conversion.rs`
    proposed: // port-lint: source conversion.rs
  - layer.get_extension -> layer.GetExtension: port-lint provenance header matched only after fallback normalization: `layer/get_extension.rs` vs expected `layer/get_extension.rs`
    proposed: // port-lint: source layer/get_extension.rs
  - matcher.ext -> matcher.Ext: port-lint provenance header matched only after fallback normalization: `matcher/ext.rs` vs expected `matcher/ext.rs`
    proposed: // port-lint: source matcher/ext.rs
  - limit.mod -> limit.Mod: port-lint provenance header matched only after fallback normalization: `layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
    proposed: // port-lint: source layer/limit/mod.rs
  - limit.mod -> limit.Mod: port-lint provenance header matched only after fallback normalization: `tests:layer/limit/mod.rs` vs expected `layer/limit/mod.rs`
    proposed: // port-lint: tests layer/limit/mod.rs
  - layer.add_extension -> layer.AddExtension: port-lint provenance header matched only after fallback normalization: `layer/add_extension.rs` vs expected `layer/add_extension.rs`
    proposed: // port-lint: source layer/add_extension.rs
  - layer.map_err -> layer.MapErr: port-lint provenance header matched only after fallback normalization: `layer/map_err.rs` vs expected `layer/map_err.rs`
    proposed: // port-lint: source layer/map_err.rs
  - layer.map_output -> layer.MapOutput: port-lint provenance header matched only after fallback normalization: `layer/map_output.rs` vs expected `layer/map_output.rs`
    proposed: // port-lint: source layer/map_output.rs
  - layer.trace_err -> layer.TraceErr: port-lint provenance header matched only after fallback normalization: `layer/trace_err.rs` vs expected `layer/trace_err.rs`
    proposed: // port-lint: source layer/trace_err.rs
  - layer.map_input -> layer.MapInput: port-lint provenance header matched only after fallback normalization: `layer/map_input.rs` vs expected `layer/map_input.rs`
    proposed: // port-lint: source layer/map_input.rs
  - layer.map_result -> layer.MapResult: port-lint provenance header matched only after fallback normalization: `layer/map_result.rs` vs expected `layer/map_result.rs`
    proposed: // port-lint: source layer/map_result.rs
  - matcher.mfn -> matcher.Mfn: port-lint provenance header matched only after fallback normalization: `matcher/mfn.rs` vs expected `matcher/mfn.rs`
    proposed: // port-lint: source matcher/mfn.rs
  - username.parse -> username.Parse: port-lint provenance header matched only after fallback normalization: `username/parse.rs` vs expected `username/parse.rs`
    proposed: // port-lint: source username/parse.rs
  - username.parse -> username.Parse: port-lint provenance header matched only after fallback normalization: `tests:username/parse.rs` vs expected `username/parse.rs`
    proposed: // port-lint: tests username/parse.rs
  - timeout.mod -> timeout.Mod: port-lint provenance header matched only after fallback normalization: `layer/timeout/mod.rs` vs expected `layer/timeout/mod.rs`
    proposed: // port-lint: source layer/timeout/mod.rs
  - policy.mod -> policy.Mod: port-lint provenance header matched only after fallback normalization: `layer/limit/policy/mod.rs` vs expected `layer/limit/policy/mod.rs`
    proposed: // port-lint: source layer/limit/policy/mod.rs
  - layer.hijack -> layer.Hijack: port-lint provenance header matched only after fallback normalization: `layer/hijack.rs` vs expected `layer/hijack.rs`
    proposed: // port-lint: source layer/hijack.rs
  - username.compose -> username.Compose: port-lint provenance header matched only after fallback normalization: `username/compose.rs` vs expected `username/compose.rs`
    proposed: // port-lint: source username/compose.rs
  - username.compose -> username.Compose: port-lint provenance header matched only after fallback normalization: `tests:username/compose.rs` vs expected `username/compose.rs`
    proposed: // port-lint: tests username/compose.rs
  - layer.into_error -> layer.IntoError: port-lint provenance header matched only after fallback normalization: `layer/into_error.rs` vs expected `layer/into_error.rs`
    proposed: // port-lint: source layer/into_error.rs
  - timeout.layer -> timeout.Layer: port-lint provenance header matched only after fallback normalization: `layer/timeout/layer.rs` vs expected `layer/timeout/layer.rs`
    proposed: // port-lint: source layer/timeout/layer.rs
  - limit.into_output -> limit.IntoOutput: port-lint provenance header matched only after fallback normalization: `layer/limit/into_output.rs` vs expected `layer/limit/into_output.rs`
    proposed: // port-lint: source layer/limit/into_output.rs
  - matcher.mod -> matcher.Mod: port-lint provenance header matched only after fallback normalization: `matcher/mod.rs` vs expected `matcher/mod.rs`
    proposed: // port-lint: source matcher/mod.rs
  - timeout.error -> timeout.Error: port-lint provenance header matched only after fallback normalization: `layer/timeout/error.rs` vs expected `layer/timeout/error.rs`
    proposed: // port-lint: source layer/timeout/error.rs
  - username.mod -> username.Mod: port-lint provenance header matched only after fallback normalization: `username/mod.rs` vs expected `username/mod.rs`
    proposed: // port-lint: source username/mod.rs
  - matcher.op_not -> matcher.OpNot: port-lint provenance header matched only after fallback normalization: `matcher/op_not.rs` vs expected `matcher/op_not.rs`
    proposed: // port-lint: source matcher/op_not.rs
  - matcher.op_and -> matcher.OpAnd: port-lint provenance header matched only after fallback normalization: `matcher/op_and.rs` vs expected `matcher/op_and.rs`
    proposed: // port-lint: source matcher/op_and.rs
  - matcher.op_or -> matcher.OpOr: port-lint provenance header matched only after fallback normalization: `matcher/op_or.rs` vs expected `matcher/op_or.rs`
    proposed: // port-lint: source matcher/op_or.rs
  - combinators.mod -> combinators.Mod: port-lint provenance header matched only after fallback normalization: `combinators/mod.rs` vs expected `combinators/mod.rs`
    proposed: // port-lint: source combinators/mod.rs
  - service.mod -> service.Mod: port-lint provenance header matched only after fallback normalization: `service/mod.rs` vs expected `service/mod.rs`
    proposed: // port-lint: source service/mod.rs
  - combinators.either -> combinators.Either: port-lint provenance header matched only after fallback normalization: `combinators/either.rs` vs expected `combinators/either.rs`
    proposed: // port-lint: source combinators/either.rs

=== Missing from Target (need to port) ===

File                          Deps    Path
------------------------------------------------------------------------------
rt.executor                   1       src/rt/executor.rs
graceful                      0       src/graceful.rs
lib                           0       src/lib.rs
matcher.test                  0       src/matcher/test.rs
rt.future                     0       src/rt/future.rs
rt.mod                        0       src/rt/mod.rs
json.codec                    0       src/stream/json/codec.rs
json.config                   0       src/stream/json/config.rs
json.engine                   0       src/stream/json/engine.rs
json.mod                      0       src/stream/json/mod.rs
stream.json.stream.mod        0       src/stream/json/stream/mod.rs
stream.json.stream.read       0       src/stream/json/stream/read.rs
stream.write                  0       src/stream/json/stream/write.rs
stream.mod                    0       src/stream/mod.rs
stream.peek                   0       src/stream/peek.rs
stream.read                   0       src/stream/read.rs
stream.rewind                 0       src/stream/rewind.rs
telemetry.mod                 0       src/telemetry/mod.rs
opentelemetry.attributes      0       src/telemetry/opentelemetry/attributes.rs
opentelemetry.mod             0       src/telemetry/opentelemetry/mod.rs
telemetry.tracing             0       src/telemetry/tracing.rs

=== Porting Quality Summary ===

Matched by exact header:          0 / 39
Matched by provenance fallback:   39 / 39
Matched by name:                  0 / 39
Total TODOs in target: 0
Total lint errors:    48
Stub files:           8

=== Big Picture ===

- Missing files: 21
- Incomplete ports (similarity < 60%): 36
- Stub files: 8
- Files missing functions: 29 (total deficit: 111 functions)
- Type definitions missing: 88
- Files missing tests: 12 (total deficit: 41 unported `#[test]` functions)
- Documentation coverage: 593 / 1230 lines (48%)

Primary focus: create missing files (highest deps first)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
ramacore.Extensions [PROVENA  0.21       0.00       12/16         4/4       0     2     LOW_SIM
  missing functions: `new`, `clone_box`, `as_any`, `clone`
  missing types: `Extension`, `ExtensionType`
policy.Matcher [PROVENANCE-F  0.10       0.00       1/7           0/6       0     1     LOW_SIM
  missing functions: `assert_ready`, `assert_abort`, `matcher_policy_empty`, `matcher_policy_always`, `matches`, `matcher_policy_scoped_limits`
  missing types: `Guard`, `Error`, `NumberedRequest`, `TestMatchers`
limit.Layer [PROVENANCE-FALL  0.31       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `with_error_into_response_fn`, `into_layer`
  missing types: `Service`
matcher.Iter [PROVENANCE-FAL  0.86       0.00       2/2           -         0     1     MISSING_TYPES
  missing types: `IteratorMatcherExt`
ramacore.SvcInput [PROVENANC  0.06       0.00       3/19          -         0     2     LOW_SIM
  missing functions: `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`, `read`, `read_vectored`, `read_to_end`, `read_to_string`, `read_exact`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`
service.Svc [ZERO] [PROVENAN  0.00       0.00       4/15          0/8       0     2     LOW_SIM
  missing functions: `serve_box`, `clone`, `fmt`, `assert_send`, `assert_sync`, `add_svc`, `static_dispatch`, `dynamic_dispatch`, `service_arc`, `box_service_arc`, `reject_svc`
  missing types: `Output`, `Error`, `DynService`
layer.ConsumeErr [PROVENANCE  0.13       0.00       3/9           -         0     1     LOW_SIM
  missing functions: `fmt`, `default`, `with_output`, `trace`, `with_response`, `into_layer`
  missing types: `Output`, `Error`, `Service`, `Trace`, `DefaultOutput`, `StaticOutput`
layer.Mod [STUB] [PROVENANCE  0.00       0.00       3/9           0/4       0     2     STUB
  missing functions: `fmt`, `clone`, `simple_input_layer`, `simple_optional_input_layer`, `simple_output_layer`, `simple_optional_output_layer`
  missing types: `Layer`, `Service`, `MaybeLayeredSvc`, `Error`, `Output`
policy.Concurrent [PROVENANC  0.23       0.00       4/12          0/5       0     1     LOW_SIM
  missing functions: `with_backoff`, `max_with_backoff`, `drop`, `assert_ready`, `assert_abort`, `concurrent_policy_zero`, `concurrent_policy`, `concurrent_policy_clone`
  missing types: `Error`, `ConcurrentCounterGuard`
service.Handler [PROVENANCE-  0.16       0.00       4/10          1/3       0     2     LOW_SIM
  missing functions: `call`, `fmt`, `clone`, `from_input`, `assert_send_sync`, `test_service_fn_without_usage`
  missing types: `Factory`, `Output`, `Error`, `FromInput`
layer.LayerFn [PROVENANCE-FA  0.31       0.00       3/7           0/3       0     1     LOW_SIM
  missing functions: `fmt`, `test_layer_fn`, `serve`, `layer_fn_has_useful_debug_impl`
  missing types: `Service`, `ToUpper`, `Output`, `Error`, `WrappedService`
ramacore.Conversion [PROVENA  0.21       0.00       2/5           -         0     1     LOW_SIM
  missing functions: `rama_from`, `rama_try_from`, `from_ref`
  missing types: `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`
layer.GetExtension [PROVENAN  0.31       0.00       4/8           0/2       0     1     LOW_SIM
  missing functions: `fmt`, `clone`, `get_extension_basic`, `get_extension_output`
  missing types: `Service`, `Output`, `Error`, `State`
matcher.Ext [PROVENANCE-FALL  0.26       0.00       3/6           0/2       0     2     LOW_SIM
  missing functions: `call`, `test_extension_matcher`, `test_fn_extension_matcher`
  missing types: `ExtensionPredicate`, `PredicateConst`, `PredicateFn`, `MyMarker`, `MyOtherMarker`
limit.Mod [STUB] [PROVENANCE  0.00       0.00       3/8           0/4       0     2     STUB
  missing functions: `with_error_into_output_fn`, `test_limit`, `handle_request`, `test_with_error_into_response_fn`, `test_zero_limit`
  missing types: `Output`, `Error`
layer.AddExtension [PROVENAN  0.60       0.00       4/6           0/2       0     1     MISSING_FUNCS
  missing functions: `basic_input`, `basic_output`
  missing types: `Service`, `Output`, `Error`, `Counter`
layer.MapErr [PROVENANCE-FAL  0.28       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `fmt`, `into_layer`
  missing types: `Output`, `Error`, `Service`
layer.MapOutput [PROVENANCE-  0.28       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `fmt`, `into_layer`
  missing types: `Output`, `Error`, `Service`
layer.TraceErr [PROVENANCE-F  0.31       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `with_level`, `default`
  missing types: `Output`, `Error`, `Service`
layer.MapInput [PROVENANCE-F  0.31       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `fmt`, `into_layer`
  missing types: `Output`, `Error`, `Service`
layer.MapResult [PROVENANCE-  0.32       0.00       3/5           -         0     1     LOW_SIM
  missing functions: `fmt`, `into_layer`
  missing types: `Output`, `Error`, `Service`
matcher.Mfn [PROVENANCE-FALL  0.09       0.00       2/5           -         0     1     LOW_SIM
  missing functions: `clone`, `fmt`, `call`
  missing types: `MatchFnBox`, `Sealed`
username.Parse [PROVENANCE-F  0.69       0.00       12/15         7/9       0     2     MISSING_FUNCS
  missing functions: `write_labels`, `test_username_label_parser_abort_tuple`, `test_username_label_parser_abort_exclusive_tuple`
  missing types: `Error`
timeout.Mod [STUB] [PROVENAN  0.00       0.00       5/6           -         0     1     STUB
  missing functions: `with`
  missing types: `DefaultTimeout`, `Output`, `Error`
policy.Mod [STUB] [PROVENANC  0.00       0.00       1/3           -         0     1     STUB
  missing functions: `fmt`, `new`
  missing types: `Guard`, `Error`
layer.Hijack [PROVENANCE-FAL  0.55       0.00       3/4           -         0     1     MISSING_FUNCS
  missing functions: `into_layer`
  missing types: `Output`, `Error`, `Service`
username.Compose [PROVENANCE  0.43       0.00       6/8           -         0     2     MISSING_FUNCS
  missing functions: `fmt`, `source`
  missing types: `ComposeErrorKind`
layer.IntoError [PROVENANCE-  0.41       0.00       2/2           -         0     1     MISSING_TYPES
  missing types: `MakeLayerError`, `Error`, `Sealed`
timeout.Layer [PROVENANCE-FA  0.51       0.00       5/6           -         0     1     MISSING_FUNCS
  missing functions: `into_layer`
  missing types: `Service`
limit.IntoOutput [PROVENANCE  0.27       0.00       1/1           -         0     1     LOW_SIM
  missing types: `Output`, `Error`
matcher.Mod [STUB] [PROVENAN  0.00       0.00       4/4           -         0     1     STUB
  missing types: `Matcher`
timeout.Error [PROVENANCE-FA  0.42       0.00       1/2           -         0     1     MISSING_FUNCS
  missing functions: `fmt`
username.Mod [STUB] [PROVENA  0.00       0.00       0/1           0/1       0     1     STUB
  missing functions: `parse_compose_username_labels`
matcher.OpNot [PROVENANCE-FA  0.40       0.00       2/2           -         0     1     LOW_SIM
matcher.OpAnd [PROVENANCE-FA  0.19       0.00       1/1           -         0     1     LOW_SIM
matcher.OpOr [PROVENANCE-FAL  0.19       0.00       1/1           -         0     1     LOW_SIM
combinators.Mod [STUB] [PROV  0.00       0.00       -             -         0     1     STUB
service.Mod [STUB] [PROVENAN  0.00       0.00       -             -         0     1     STUB
combinators.Either [ZERO] [P  0.00       0.00       -             -         0     1     LOW_SIM

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 36
Missing files: 21

Incomplete ports to complete:
  extensions                     similarity=0.21 function_parity=12/16 dependents=9
    missing functions: `new`, `clone_box`, `as_any`, `clone`
    missing types: `Extension`, `ExtensionType`
  policy.matcher                 similarity=0.10 function_parity=1/7 dependents=5
    missing functions: `assert_ready`, `assert_abort`, `matcher_policy_empty`, `matcher_policy_always`, `matches`, `matcher_policy_scoped_limits`
    missing types: `Guard`, `Error`, `NumberedRequest`, `TestMatchers`
  limit.layer                    similarity=0.31 function_parity=3/5 dependents=2
    missing functions: `with_error_into_response_fn`, `into_layer`
    missing types: `Service`
  svc_input                      similarity=0.06 function_parity=3/19 dependents=0
    missing functions: `poll_read`, `poll_write`, `poll_write_vectored`, `poll_flush`, `poll_shutdown`, `is_write_vectored`, `read`, `read_vectored`, `read_to_end`, `read_to_string`, `read_exact`, `write`, `flush`, `write_all`, `write_fmt`, `write_vectored`
  service.svc                    similarity=0.00 function_parity=4/15 dependents=0
    missing functions: `serve_box`, `clone`, `fmt`, `assert_send`, `assert_sync`, `add_svc`, `static_dispatch`, `dynamic_dispatch`, `service_arc`, `box_service_arc`, `reject_svc`
    missing types: `Output`, `Error`, `DynService`
  layer.consume_err              similarity=0.13 function_parity=3/9 dependents=0
    missing functions: `fmt`, `default`, `with_output`, `trace`, `with_response`, `into_layer`
    missing types: `Output`, `Error`, `Service`, `Trace`, `DefaultOutput`, `StaticOutput`
  layer.mod                      similarity=0.00 function_parity=3/9 dependents=0 [STUB]
    missing functions: `fmt`, `clone`, `simple_input_layer`, `simple_optional_input_layer`, `simple_output_layer`, `simple_optional_output_layer`
    missing types: `Layer`, `Service`, `MaybeLayeredSvc`, `Error`, `Output`
  policy.concurrent              similarity=0.23 function_parity=4/12 dependents=0
    missing functions: `with_backoff`, `max_with_backoff`, `drop`, `assert_ready`, `assert_abort`, `concurrent_policy_zero`, `concurrent_policy`, `concurrent_policy_clone`
    missing types: `Error`, `ConcurrentCounterGuard`
  service.handler                similarity=0.16 function_parity=4/10 dependents=0
    missing functions: `call`, `fmt`, `clone`, `from_input`, `assert_send_sync`, `test_service_fn_without_usage`
    missing types: `Factory`, `Output`, `Error`, `FromInput`
  layer.layer_fn                 similarity=0.31 function_parity=3/7 dependents=0
    missing functions: `fmt`, `test_layer_fn`, `serve`, `layer_fn_has_useful_debug_impl`
    missing types: `Service`, `ToUpper`, `Output`, `Error`, `WrappedService`
  conversion                     similarity=0.21 function_parity=2/5 dependents=0
    missing functions: `rama_from`, `rama_try_from`, `from_ref`
    missing types: `RamaFrom`, `RamaInto`, `RamaTryFrom`, `Error`, `RamaTryInto`, `FromRef`
  layer.get_extension            similarity=0.31 function_parity=4/8 dependents=0
    missing functions: `fmt`, `clone`, `get_extension_basic`, `get_extension_output`
    missing types: `Service`, `Output`, `Error`, `State`
  matcher.ext                    similarity=0.26 function_parity=3/6 dependents=0
    missing functions: `call`, `test_extension_matcher`, `test_fn_extension_matcher`
    missing types: `ExtensionPredicate`, `PredicateConst`, `PredicateFn`, `MyMarker`, `MyOtherMarker`
  limit.mod                      similarity=0.00 function_parity=3/8 dependents=0 [STUB]
    missing functions: `with_error_into_output_fn`, `test_limit`, `handle_request`, `test_with_error_into_response_fn`, `test_zero_limit`
    missing types: `Output`, `Error`
  layer.map_err                  similarity=0.28 function_parity=3/5 dependents=0
    missing functions: `fmt`, `into_layer`
    missing types: `Output`, `Error`, `Service`
  layer.map_output               similarity=0.28 function_parity=3/5 dependents=0
    missing functions: `fmt`, `into_layer`
    missing types: `Output`, `Error`, `Service`
  layer.trace_err                similarity=0.31 function_parity=3/5 dependents=0
    missing functions: `with_level`, `default`
    missing types: `Output`, `Error`, `Service`
  layer.map_input                similarity=0.31 function_parity=3/5 dependents=0
    missing functions: `fmt`, `into_layer`
    missing types: `Output`, `Error`, `Service`
  layer.map_result               similarity=0.32 function_parity=3/5 dependents=0
    missing functions: `fmt`, `into_layer`
    missing types: `Output`, `Error`, `Service`
  matcher.mfn                    similarity=0.09 function_parity=2/5 dependents=0
    missing functions: `clone`, `fmt`, `call`
    missing types: `MatchFnBox`, `Sealed`
  timeout.mod                    similarity=0.00 function_parity=5/6 dependents=0 [STUB]
    missing functions: `with`
    missing types: `DefaultTimeout`, `Output`, `Error`
  policy.mod                     similarity=0.00 function_parity=1/3 dependents=0 [STUB]
    missing functions: `fmt`, `new`
    missing types: `Guard`, `Error`
  layer.hijack                   similarity=0.55 function_parity=3/4 dependents=0
    missing functions: `into_layer`
    missing types: `Output`, `Error`, `Service`
  username.compose               similarity=0.43 function_parity=6/8 dependents=0
    missing functions: `fmt`, `source`
    missing types: `ComposeErrorKind`
  layer.into_error               similarity=0.41 function_parity=2/2 dependents=0
    missing types: `MakeLayerError`, `Error`, `Sealed`
  timeout.layer                  similarity=0.51 function_parity=5/6 dependents=0
    missing functions: `into_layer`
    missing types: `Service`
  limit.into_output              similarity=0.27 function_parity=1/1 dependents=0
    missing types: `Output`, `Error`
  matcher.mod                    similarity=0.00 function_parity=4/4 dependents=0 [STUB]
    missing types: `Matcher`
  timeout.error                  similarity=0.42 function_parity=1/2 dependents=0
    missing functions: `fmt`
  username.mod                   similarity=0.00 function_parity=0/1 dependents=0 [STUB]
    missing functions: `parse_compose_username_labels`
  matcher.op_not                 similarity=0.40 function_parity=2/2 dependents=0
  matcher.op_and                 similarity=0.19 function_parity=1/1 dependents=0
  matcher.op_or                  similarity=0.19 function_parity=1/1 dependents=0
  combinators.mod                similarity=0.00 function_parity=- dependents=0 [STUB]
  service.mod                    similarity=0.00 function_parity=- dependents=0 [STUB]
  combinators.either             similarity=0.00 function_parity=- dependents=0

=== Missing Files (by Dependents) ===

Source File                   Expected Target                       Dependents Path
-----------------------------------------------------------------------------------------------------------------------
rt.executor                   rt.Executor                           1          src/rt/executor.rs
graceful                      Graceful                              0          src/graceful.rs
matcher.test                  matcher.Test                          0          src/matcher/test.rs
rt.future                     rt.Future                             0          src/rt/future.rs
json.codec                    stream.json.Codec                     0          src/stream/json/codec.rs
json.config                   stream.json.Config                    0          src/stream/json/config.rs
json.engine                   stream.json.Engine                    0          src/stream/json/engine.rs
stream.json.stream.read       stream.json.stream.Read               0          src/stream/json/stream/read.rs
stream.write                  stream.json.stream.Write              0          src/stream/json/stream/write.rs
stream.peek                   stream.Peek                           0          src/stream/peek.rs
stream.read                   stream.Read                           0          src/stream/read.rs
stream.rewind                 stream.Rewind                         0          src/stream/rewind.rs
opentelemetry.attributes      telemetry.opentelemetry.Attributes    0          src/telemetry/opentelemetry/attributes.rs
telemetry.tracing             telemetry.Tracing                     0          src/telemetry/tracing.rs

=== Reexport / Wiring Modules (consult, don't transliterate) ===

lib                           Lib                                   0          src/lib.rs
rt.mod                        rt.Mod                                0          src/rt/mod.rs
json.mod                      stream.json.Mod                       0          src/stream/json/mod.rs
stream.json.stream.mod        stream.json.stream.Mod                0          src/stream/json/stream/mod.rs
stream.mod                    stream.Mod                            0          src/stream/mod.rs
telemetry.mod                 telemetry.Mod                         0          src/telemetry/mod.rs
opentelemetry.mod             telemetry.opentelemetry.Mod           0          src/telemetry/opentelemetry/mod.rs

=== Documentation Gaps ===

There is missing documentation that is hurting overall scoring.
Documentation coverage: 593 / 1230 lines (48%)
Files with >20% doc gap: 27

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
policy.mod                    106         15          85%       0.76      0.14      0.45      
policy.concurrent             102         15          85%       0.43      0.15      0.29      
layer.map_result              60          9           85%       0.55      0.15      0.35      
layer.hijack                  52          6           88%       0.57      0.12      0.34      
username.mod                  54          9           83%       0.41      0.17      0.29      
extensions                    100         57          43%       0.56      0.57      0.56      
layer.layer_fn                40          6           85%       0.63      0.15      0.39      
username.parse                74          45          39%       0.86      0.61      0.74      
timeout.mod                   28          3           89%       0.52      0.11      0.31      
layer.consume_err             40          15          62%       0.39      0.38      0.38      
layer.into_error              36          12          66%       0.64      0.33      0.48      
limit.mod                     26          6           76%       0.56      0.23      0.39      
limit.layer                   18          3           83%       0.56      0.17      0.36      
svc_input                     18          3           83%       0.22      0.17      0.19      
layer.add_extension           26          12          53%       0.73      0.46      0.60      
layer.map_output              22          9           59%       0.63      0.41      0.52      
combinators.either            26          14          46%       0.55      0.54      0.55      
timeout.layer                 14          3           78%       0.46      0.21      0.34      
layer.map_input               18          9           50%       0.59      0.50      0.55      
layer.trace_err               18          9           50%       0.50      0.50      0.50      
layer.mod                     32          24          25%       0.76      0.75      0.76      
layer.map_err                 22          15          31%       0.51      0.68      0.59      
matcher.ext                   16          9           43%       0.66      0.56      0.61      
service.handler               24          18          25%       0.60      0.75      0.68      
layer.get_extension           20          15          25%       0.77      0.75      0.76      
matcher.iter                  10          7           30%       0.81      0.70      0.76      
timeout.error                 6           3           50%       0.76      0.50      0.63      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
