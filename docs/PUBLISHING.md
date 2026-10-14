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

## Automated release

Pushing a tag `vX.Y.Z` that matches `VERSION_NAME` runs `.github/workflows/release.yml`
on macOS, which tests, checks the API and runs `publishAndReleaseToMavenCentral`
(com.vanniktech.maven.publish). The job uses the `release` GitHub environment; add
required reviewers there for manual approval.

Required repository/environment secrets:

- `MAVEN_CENTRAL_USERNAME` / `MAVEN_CENTRAL_PASSWORD`: Central Portal user token.
- `SIGNING_KEY` (ASCII armored private key) / `SIGNING_PASSWORD`. Publish the public
  key to a public key server.

The namespace `io.github.mariorsh` must be verified in Central Portal. Central versions
are immutable: a failed release needs a new version. For a manual upload use
`./gradlew publishToMavenCentral` (without `AndRelease`) and release from the portal.
