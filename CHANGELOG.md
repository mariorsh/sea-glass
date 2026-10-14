# Changelog

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
