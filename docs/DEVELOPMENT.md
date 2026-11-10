# Desarrollo de Sea Glass

Notas para quien compila, prueba o publica la librería. La documentación de uso está en el [README](../README.md).

## Instalación local

```sh
./gradlew :seaglass-compose:publishToMavenLocal :seaglass-haze:publishToMavenLocal
```

Añade `mavenLocal()` al consumidor, o usa `includeBuild("../sea-glass")` con las mismas coordenadas.
Con este toolchain, Windows produce y publica los klibs de `iosArm64`/`iosSimulatorArm64`
(verificado contra el staging repository); el enlazado final de framework para `sample`
(`linkDebugFrameworkIosSimulatorArm64`, Xcode) sigue requiriendo macOS.

## Demo, tests y documentación API

```sh
./gradlew :sample:installDebug
./gradlew :sample:run
./gradlew :sample:wasmJsBrowserDevelopmentRun
./gradlew :sample:wasmJsBrowserDistribution
./gradlew :sample:linkDebugFrameworkIosSimulatorArm64 # macOS
./gradlew :seaglass-compose:desktopTest apiCheck
./gradlew :seaglass-compose:dokkaGeneratePublicationHtml :seaglass-haze:dokkaGeneratePublicationHtml
```

Si hay varios Android, instala el APK con `adb -s SERIAL install -r sample/build/outputs/apk/debug/sample-debug.apk`. La galería está en `sample`, fuera del artefacto de producción. Incluye catálogo, formulario local, modales, 200 tarjetas y controles para Noche, marca, RTL, fuente al 200%, movimiento reducido y tres modos de superficie. No necesita backend.

iOS: integra `SeaGlassSample.framework` en tu app Xcode y presenta `MainViewControllerKt.MainViewController()` mediante el wrapper Swift de `sample/iosApp/SeaGlassView.swift`. Requiere macOS, Xcode, un host y firma propios; no se incluye un proyecto Xcode generado.


## Compatibilidad de build

| Pieza | Versión |
|---|---|
| Kotlin / Compose Multiplatform | 2.4.21 / 1.12.2 |
| AGP / Gradle | 9.5.1 / 9.9.0; JDK 17+ |
| Android demo | compile/target 37 |
| API / documentación | Binary Compatibility Validator 0.18.2 (JVM + klib) / Dokka 2.3.0 |
| Haze, opcional | 2.1.0 (`haze` + `haze-blur`) |
| Validación | escritorio en Windows; Apple requiere macOS |

AGP 9's built-in Kotlin/new DSL reject the legacy `com.android.library`/`application`
plugins combined with `org.jetbrains.kotlin.multiplatform`; `android.builtInKotlin=false`
and `android.newDsl=false` in `gradle.properties` (AGP's own recommended bypass) keep
the current module layout for this platform-only release. Migrating to
`com.android.kotlin.multiplatform.library` is a separate, larger change, not done here.

La compatibilidad de compilación no sustituye la validación visual de cada renderer. No se han medido mejoras de rendimiento. Se conservan los modificadores `composed` para resolver el tema y construir modificadores de terceros; no hay evidencia que justifique sustituirlos por Node. Se almacenan pinceles/paths y el progreso se lee en dibujo/semánticas, fuera de composición.

## Publicación

Ver [PUBLISHING.md](PUBLISHING.md): los tags `v*` publican en Maven Central desde GitHub Actions.
Más contexto: [validación y matriz visual](VALIDATION.md).
