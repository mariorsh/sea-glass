# Validation and reproducible review

## Local results (2026-10-14)

Namespace follow-up: a clean rebuild with `--no-build-cache` passed Android sample
assembly, desktop/Wasm sample compilation, `apiCheck`, all 13 desktop tests and
`validateLocalPublication` (6m21s; `build/namespace-clean-validation.log`). Staged
POMs use `io.github.mariorsh.seaglass`; the rebuilt APK and desktop library JAR
contain no entries under the old namespace. API baselines and documentation use
the new packages. The Wasm production distribution must be regenerated after this
clean build; its earlier successful build predates the namespace migration.

Follow-up glass fix: 13 desktop tests pass, including uniform transparent content
and a pixel check for a retained exterior shadow with no interior shadow tint.
Android sample assembly, desktop/Wasm compilation and `apiCheck` also pass
(`build/glass-rectangle-validation.log`, `build/exterior-shadow-tests.log`). On
Nothing A015 the reported rectangle was reproduced with native panel elevation,
then removed with an independent exterior-only shadow caster. Desktop did not
reproduce the original Android artifact; its tests validate the intended rendering
contract. The gallery now captures only its decorative background for Haze.

| Check | Result |
|---|---|
| Android sample APK / desktop sample compile | Passed |
| Wasm compile and production distribution | Passed; webpack reports bundle-size warnings |
| Desktop tests | 11 passed, 0 failures/errors |
| Android + desktop API comparison | Passed against the new 0.2 baselines |
| Dokka and local staged publications | Passed for Android, desktop, Wasm and root metadata |
| Local metadata / sources / documentation / OFL validation | Passed |
| Release prerequisite guard on Windows | Rejected as expected: complete release requires macOS |
| Apple compilation, linking and complete publication | Not executed here; macOS CI prepared |
| Browser/iOS visual checks and full assistive-technology matrix | Pending |

The final full invocation finished successfully in 4m45s. Logs are local build
artifacts (`build/final-validation.log`, `build/release-guard.log`), not committed.
The standalone `validateLocalPublication apiCheck` invocation also passed (2m58s),
confirming publication validation waits for all staged publications; its log is
`build/publication-final.log`.
No packages were uploaded remotely. The generated static demo is under
`sample/build/dist/wasmJs/productionExecutable/`; the Android APK is under
`sample/build/outputs/apk/debug/`.

## Automated gates

Use JDK 17+, Android SDK 35. Public API dumps for Android and desktop live in each
library's `api/` folder. `apiCheck` detects changes to these baselines; intentional
changes require reviewing `apiDump` output. This is a 0.2 baseline, not a claim of
binary compatibility with 0.1.1. Native/Wasm compilation is checked separately.

```sh
./gradlew :seaglass-compose:desktopTest apiCheck
./gradlew :sample:assembleDebug :sample:compileKotlinDesktop :sample:compileKotlinWasmJs
./gradlew :sample:wasmJsBrowserDistribution
./gradlew :seaglass-compose:publishAllPublicationsToStagingRepository :seaglass-haze:publishAllPublicationsToStagingRepository validateLocalPublication
```

Linux UI tests require a display (`xvfb-run -a` in CI). Apple compilation/framework
linking and complete staged publications require macOS/Xcode. CI includes both jobs;
the existence of a workflow is not evidence that a hosted run passed.

Tests cover surface support/policy combinations, invalid appearance tokens, menu
alignment and clamping, independent theme tokens, nested theme resets, touch targets,
disabled controls, growing text and updated progress semantics. UI tests use native
Compose semantics rather than resolution-dependent coordinates and synchronize with
`waitForIdle`/test assertions.

The desktop suite also verifies Enter activation, toggle semantics and the rendered
center pixel of an Opaque surface with an explicitly translucent input color.

Regenerate baselines with `apiDump` in a separate Gradle invocation before running
`apiCheck`. Scheduling both together triggers Gradle's implicit-output dependency
validation in Binary Compatibility Validator 0.17.0. CI only runs `apiCheck` and
never regenerates baselines automatically.

## Device exploration prerequisite

Device chosen by owner: Nothing A015, serial `00101347R001053`, Android 17, 1080×2400.
ARTEMIS diagnosis and hierarchy observations were performed before authoring tests.
The autonomous Pro and Flash runners both fail before execution because the configured
Planner requires GOOGLE_API_KEY; do not interpret their task-start acknowledgements
as successful exploration. No credentials were changed. Direct ARTEMIS hierarchy
and screenshot observation, together with ADB actions, were used to verify visible
states. The empty form produced `Name is required`; opening Dialog exposed
`Discard changes?`, `Cancel`, `Confirm`; opening Sheet exposed `Filters`, `Apply`.
These observations do not verify screen reader behavior or desktop focus confinement.

A subsequent bounded ADB sequence opened the drawer and dismissed it with Back,
returning to the Checks screen. On Scroll / perf, scrolling under the overlay was
inspected in Blur, Fallback and Opaque: the overlay text stayed crisp, the underlying
text was blurred in Blur, visible through the fallback and hidden by Opaque. RTL
mirrored logical text and control alignment. These are visual checks on this device,
not frame-time measurements or coverage of other renderers.

At the sample's 200% font setting the long ExtraSmall button wrapped to two lines
and grew vertically. The catalogue tabs retain single-line labels (which may be
visually truncated in narrow windows); their full labels remain available in semantics.

ARTEMIS also reports an optional port warning:
“Another program is listening on 127.0.0.1:8000 (often a dev server). Stop it, or set
ARTEMIS_DAEMON_PORT to a free port in the MCP server's env block and restart the MCP
server. Tasks still run in standalone mode meanwhile, without the shared scheduler.”
To restore autonomous runs, configure the provider/Planner credentials in the
ARTEMIS `.env` or MCP environment, then reload its MCP server; never paste keys in chat.

## Visual matrix (manual, no golden-image claim)

For each supported host: Android API 24/31 (fallback), API 32+ (blur), JVM desktop,
Safari/iOS, Chromium/Wasm. Use Arena, Noche, and Brand with the following cases:

| Case | Checks |
|---|---|
| Small buttons and icons | 48dp layout targets, no overlap, visible focus/hover |
| 200% font + long labels | No clipped text; growing height; group width respected |
| Keyboard | Tab/Shift+Tab order; Enter/Space activation; disabled skipped |
| Modal | Initial focus; Tab stays inside; background cannot receive clicks; Back/Escape dismiss; launcher focus returns |
| Small window + IME | Form stays reachable by scrolling; safe insets |
| RTL | Logical drawer/side-sheet edge, menu start alignment, edge clamping |
| Reduced motion | No morphing/decorative spring; loading remains described |
| Screen reader | Roles, checked/selected/disabled, progress and pane announcement |
| Scroll + blur | Source moves behind crisp overlay text; no self capture |
| Fallback / Opaque | Legible fill; Opaque never invokes effect |
| Popup / dialog | No attempt to sample an independent window |

Record host, OS, renderer, resolution, density and font scale with screenshots.
Do not approve iOS/Web blur or focus behavior solely from Android results.

## Performance scenario

Run `sample` → **Scroll / perf**. It contains 200 keyed cards and a separate glass
overlay. Compare **Blur**, **Fallback**, **Opaque** using the same theme, device,
window size, density, font scale, scroll speed and content. Warm up, perform at least
five identical scroll passes per mode, and use Android Studio System Trace/Perfetto
frame timings or the platform profiler. Report median and p95 frame duration,
missed frames, CPU/GPU load and allocations with the device/renderer configuration.
Use a release/profileable build for measurements, not the debug exploration APK.
Keep decorative/background content and capture area identical across comparisons.

No benchmark results or measured speedups are claimed. Deferred progress reads and
cached brushes/paths remove demonstrated composition/allocation sites; a Modifier.Node
rewrite is deferred because there is no measured justification for its complexity.
