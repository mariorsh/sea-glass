# 0.2.0 → 0.3.0

Platform-only release; no theme, token or component changes. Three incompatible
changes:

- **Haze 2.** If your app uses Haze directly (not only through `seaglass-haze`),
  add `dev.chrisbanes.haze:haze-blur:2.1.0` alongside `haze:2.1.0`: blur moved
  out of the core `haze` artifact in Haze 2.0. The mutable `Modifier.hazeEffect {
  blurEnabled = ...; backgroundColor = ...; blurRadius = ...; noiseFactor = ... }`
  DSL is replaced by the typed `Modifier.hazeBlur(input = HazeInput.Sources(state),
  style = HazeBlurStyle { blurEnabled(...); backgroundColor(...); blurRadius(...);
  noiseFactor(...) })`, and `HazeDefaults.blurEnabled()` is replaced by
  `HazeBlurDefaults.isBlurEnabledByDefault()`. `seaglass-haze`'s own public API
  (`rememberHazeBackdrop`, `Modifier.seaGlassHazeSource`) is unchanged.
- **No `iosX64`.** The library and sample no longer build for the Intel iOS
  simulator target. `iosArm64` and `iosSimulatorArm64` (Apple Silicon simulator
  and device) are unaffected.
- **No transitive `material-icons-core`.** `seaglass-compose` no longer exposes
  `org.jetbrains.compose.material:material-icons-core` as an `api` dependency
  (it never used more than six glyphs from it). Apps that relied on getting
  Material icons transitively through `seaglass-compose` must add
  `material-icons-core` themselves.

# 0.1.1 → 0.2.0

Publication group and source packages move from `dev.seaglass` to
`io.github.mariorsh.seaglass`. Use
`io.github.mariorsh.seaglass:seaglass-compose:0.2.0` (and the optional
`seaglass-haze` artifact), and replace `import dev.seaglass.compose.*` with
`import io.github.mariorsh.seaglass.compose.*`. Haze helpers now live in
`io.github.mariorsh.seaglass.haze`. The core Android namespace is exactly
`io.github.mariorsh.seaglass`; Haze and the sample use `.haze` and `.sample`.
The sample application ID uses the new prefix too. No aliases are provided for the old packages.

Recompile consumers when upgrading: this minor release evolves Compose function
signatures and is source compatible for ordinary trailing-lambda calls, not binary
compatible with an already compiled 0.1.1 client.

- `SeaGlassTheme { }` and Arena/Noche presets continue to work. Pass
  `config = SeaGlassThemeConfig(...)` to customize. Material `colorScheme` and
  `SeaGlassColors` are independent. Nested themes reset to their own preset/config,
  including motion, strings, surfaces and backdrop; explicitly reuse configuration
  and backdrop to inherit them.
- `SeaGlassTheme.motion` now reads composition-local configuration and must be
  accessed from composition. Outside composition the legacy standalone
  `SeaGlassMotion` spring helpers remain available.
- Set `fonts = SeaGlassFonts(...)` to prevent default font resource loading. If you
  supply both `typography` and `extraTypography`, no bundled fonts are loaded either.
- `GlassBackdrop` remains a SAM. Its new `isBlurAvailable` defaults to false. Existing
  adapters must override this property or use `GlassBackdrop.available(adapter)`
  only after checking real provider/platform support. An adapter returning Modifier
  must report false. Haze consumers should use `rememberHazeBackdrop`.
- `SeaGlassGallery` moved from the production artifact to `sample`, under the new
  `io.github.mariorsh.seaglass.compose` package. Run the sample or copy the gallery
  into your development app; it is no longer a production library API.
- Bottom sheets retain `sheetState` and also accept `skipPartiallyExpanded`.
  An explicit state takes precedence and owns its expansion policy.
- Group buttons morph without increasing horizontal padding. Use RowScope `weight`
  for equal distribution in constrained rows. Connected and segmented groups
  distribute their available width; labels may wrap and heights grow.
- Defaults for internal accessibility labels are English. Use
  `SeaGlassThemeConfig(strings = SeaGlassStrings.Spanish)` for the former language.
- Tiny button variants retain their visual minimum height and reserve at least
  48dp in layout for interaction. This may increase spacing in compact layouts.
- Drawer and side sheet use modal windows and inherit platform dismissal/focus
  behavior. They open immediately (no decorative slide) and support logical RTL edges.
  Cross-window backdrop capture is disabled. No application BackHandler is required.
