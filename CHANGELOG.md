# Changelog

## 0.3.0-alpha01 — 2026-11-10

Platform-only release: no theme, token, component or module changes.

- Kotlin 2.4.21, Compose Multiplatform 1.12.2, AGP 9.5.1, Gradle 9.9.0, Dokka
  2.3.0 (Gradle plugin v2) and Binary Compatibility Validator 0.18.2. AGP 9's
  built-in Kotlin/new DSL temporarily bypassed via `android.builtInKotlin` /
  `android.newDsl` to keep the current module layout; see
  [DEVELOPMENT.md](docs/DEVELOPMENT.md).
- Haze moves to 2.1.0: blur lives in the new `haze-blur` artifact and
  `seaglass-haze` is rewritten on `Modifier.hazeBlur`/`HazeBlurStyle`.
  `rememberHazeBackdrop(state, enabled)` and `Modifier.seaGlassHazeSource(state)`
  keep their signatures and behavior.
- Drop `iosX64`; `iosArm64` and `iosSimulatorArm64` remain.
- Replace the `material-icons-core` dependency in `seaglass-compose` with six
  internal `ImageVector`s (check, close, search, add, back arrow, dropdown
  chevron); the sample depends on `material-icons-core` directly for its
  broader icon set. No visible change to the gallery.
- `seaglass-compose` and `seaglass-haze` build with `explicitApi()`.
- Public ABI is now also validated for iOS/Wasm klibs, alongside the existing
  JVM baselines.
- Repo cleanup: remove `docs/PR.md`, drop device-serial/local-tooling notes
  from `docs/VALIDATION.md`, remove the unused GitHub Packages publishing
  block, add Dependabot for Gradle and GitHub Actions.

See [migration](docs/MIGRATION.md) for the three breaking changes (Haze 2,
no iosX64, no transitive material-icons-core).

## 0.2.0 — 2026-10-14

- Move the Maven group and core Android namespace to `io.github.mariorsh.seaglass`.
  Kotlin packages use `.compose`/`.haze`, and the sample uses `.sample`.

- Fix Android elevation artifacts inside translucent cards/panels by rendering an
  independent shadow caster, clipped to the exterior. Connect the gallery backdrop
  to its decorative background alone so catalogue cards receive actual blur.

- Configurable Material and Sea Glass colors, fonts, typography, shapes, motion,
  surface appearance and accessible strings; Arena/Noche defaults retained.
- Explicit backdrop support and shared Auto/Blur/Opaque policy; optional Haze 1.5.3.
- Small visual button variants reserve 48dp interaction space; heights grow with
  text and buttons show focus/hover. Group morphing stays within available width.
- Modal drawer and side sheet use dialog isolation and logical RTL edges.
- RTL menu positioning and window clamping; progress draw reads and semantics.
- English/Spanish overridable labels and reduced motion configuration.
- Gallery moved to Android, desktop, Wasm sample and iOS framework integration.
- Tests, public API baselines, Dokka, CI and local publication validation.
- Central staging/signing prepared; release requires owner license, namespace
  verification and complete Apple validation.

See [migration](docs/MIGRATION.md) for compatibility details.
