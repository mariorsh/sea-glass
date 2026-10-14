# Sea Glass 0.2.0: configurable themes, optional blur and executable samples

Sea Glass 0.1.1 fixed its theme tokens internally and treated an installed backdrop
adapter as proof of blur support. This change introduces `SeaGlassThemeConfig`,
explicit provider capability and shared surface policies, with an optional Haze
module that leaves the core independent of the provider.

The sample is now a separate application, including Android, desktop, Wasm and an
iOS framework entry point. It demonstrates theme overrides, local form validation,
modal interactions, font scaling, RTL, reduced motion and a repeatable scrolling
workload. Small custom buttons reserve touch targets and grow with their text;
button-group press animation no longer increases horizontal layout size.

The release adds English/Spanish overridable labels, progress draw-phase reads,
updated progress semantics, cached paths/brushes, native modal windows, API baselines,
Dokka, CI and staged Maven publication with external signing configuration.

Follow-up rendering fix: glass surfaces use a separate exterior-only shadow caster
to prevent Android elevation artifacts inside translucent cards. The gallery uses
its own decorative background as the Haze source, excluding cards and controls.
The desktop test count is now 13, including uniform-fill and exterior-shadow checks.

## Compatibility

- Ordinary `SeaGlassTheme { }` calls continue to work; positional legacy theme
  calls have a deprecated forwarding overload. Recompile clients for 0.2.0.
- `SeaGlassGallery` moved to sample sources; it is removed from the production API.
- Legacy backdrop adapters must explicitly declare actual support; otherwise the
  conservative fallback is used. Haze users can use `rememberHazeBackdrop`.
- Themes nested without explicit configuration reset their presets rather than
  inheriting parent configuration. Material/Sea Glass colors remain independent.
- Bottom sheets accept an explicit `SheetState` and the existing workspace change
  for `skipPartiallyExpanded`; explicit state wins.

## Validation

Local Windows validation includes Android APK assembly, desktop and Wasm compilation,
11 desktop tests (including keyboard, semantics and an opaque-pixel assertion),
API comparison, Dokka, Wasm production distribution and local staged publications.
The final command results are recorded in [VALIDATION.md](VALIDATION.md).

Nothing A015 exploration used ARTEMIS observations and ADB actions. Verified states
include empty-form validation, dialog/side-sheet content, drawer Back dismissal,
200% text wrapping, RTL and scrolling under Blur/Fallback/Opaque. Autonomous ARTEMIS
tasks are blocked by the configured Planner credential; this is not presented as a
successful autonomous run. No performance improvement has been measured.

## Pending before release

- Run the macOS CI job for all Apple KLIBs and framework linking.
- Run the documented visual/accessibility matrix on Apple, desktop and Web hosts,
  including screen readers, complete focus restoration and reduced platform motion.
- The owner must choose a code license and verify the Maven namespace in Central
  Portal. `io.github.mariorsh.seaglass` ownership is not established by this PR.
- Supply signing secrets, stage all targets on macOS, review the complete bundle
  and submit manually to Central. No remote packages, tags, merge or deployment
  have been performed.
