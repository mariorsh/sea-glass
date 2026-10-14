# Release 0.2.0 (not published)

Coordinates are centralized in `gradle.properties`: `io.github.mariorsh.seaglass`, version `0.2.0`.
The owner must verify the GitHub-backed namespace `io.github.mariorsh` in Central
Portal and confirm it authorizes its child namespace `io.github.mariorsh.seaglass`.
The build does not establish account ownership or publishing permissions. Follow
the portal's GitHub ownership verification instructions; no DNS ownership of
`seaglass.dev` is required for these coordinates. Verification is still external.

The repository has no code LICENSE. The owner must choose one and supply
`-PreleaseLicenseName=... -PreleaseLicenseUrl=...`. The build deliberately does not
invent a license. Font OFL notices in `licenses/` must remain in distributed archives.

Official references:

- https://central.sonatype.org/register/namespace/
- https://central.sonatype.org/publish/requirements/
- https://central.sonatype.org/publish/publish-portal-upload/

## Local review

Use JDK 17 or newer, Android SDK 35, and macOS with Xcode for a complete release.
`publishToMavenLocal` writes only to the local Maven cache; Windows/Linux omit Apple
artifacts and must not be used to prepare a Central upload.

```sh
./gradlew :seaglass-compose:publishToMavenLocal :seaglass-haze:publishToMavenLocal
./gradlew :seaglass-compose:publishAllPublicationsToStagingRepository :seaglass-haze:publishAllPublicationsToStagingRepository
```

The staging repository is `build/staging-repository`. Review POMs, Gradle module
metadata, root metadata publication, Android release AAR, desktop JAR, Wasm KLIB,
all three iOS KLIBs, sources and Dokka documentation JARs.

The current group is staged under `io/github/mariorsh/seaglass/`. Validation and
the Central bundle select only the configured GROUP; older staged groups are not
included in the bundle.

## External steps before Central

1. Choose the code license and verify the namespace in the owner's Central account.
2. On macOS, configure `SIGNING_KEY` (ASCII armored private key), `SIGNING_PASSWORD`
   and `CENTRAL_NAMESPACE_VERIFIED` (the verified namespace, typically `io.github.mariorsh`,
   or the exact GROUP) in the environment or CI secrets.
   Publish the corresponding public signing key to a public key server.
3. Run `validateReleasePrerequisites` for both libraries, then stage every publication
   with the license properties. Signatures are created only when SIGNING_KEY is present.
4. Run `prepareCentralBundle` for a guarded zip including Maven checksums and signatures.
5. Upload that bundle manually to Central Portal for validation and inspection. This
   repository does not automatically upload, release, tag, or merge anything.

Central account credentials belong only in the portal or secret environment; they
are never embedded in source or POMs. Do not upload unsigned or partial staging output.
