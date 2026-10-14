# Desarrollo de Sea Glass

Notas para quien compila, prueba o publica la librería. La documentación de uso está en el [README](../README.md).

## Instalación local

```sh
./gradlew :seaglass-compose:publishToMavenLocal :seaglass-haze:publishToMavenLocal
```

Añade `mavenLocal()` al consumidor, o usa `includeBuild("../sea-glass")` con las mismas coordenadas.
Windows/Linux omiten los artefactos iOS: para un conjunto completo usa macOS.

## Demo, tests y documentación API

```sh
./gradlew :sample:installDebug
./gradlew :sample:run
./gradlew :sample:wasmJsBrowserDevelopmentRun
./gradlew :sample:wasmJsBrowserDistribution
./gradlew :sample:linkDebugFrameworkIosSimulatorArm64 # macOS
./gradlew :seaglass-compose:desktopTest apiCheck
./gradlew :seaglass-compose:dokkaHtml :seaglass-haze:dokkaHtml
```

Si hay varios Android, instala el APK con `adb -s SERIAL install -r sample/build/outputs/apk/debug/sample-debug.apk`. La galería está en `sample`, fuera del artefacto de producción. Incluye catálogo, formulario local, modales, 200 tarjetas y controles para Noche, marca, RTL, fuente al 200%, movimiento reducido y tres modos de superficie. No necesita backend.

iOS: integra `SeaGlassSample.framework` en tu app Xcode y presenta `MainViewControllerKt.MainViewController()` mediante el wrapper Swift de `sample/iosApp/SeaGlassView.swift`. Requiere macOS, Xcode, un host y firma propios; no se incluye un proyecto Xcode generado.


## Compatibilidad de build

| Pieza | Versión |
|---|---|
| AGP / Gradle | 8.7.3 / 8.14.3; JDK 17+ (Gradle 8.14 no soporta JDK 25) |
| Android demo | compile/target 35 |
| API / documentación | Binary Compatibility Validator 0.17.0 / Dokka 1.9.20 |
| Validación | escritorio en Windows; Apple requiere macOS |

La compatibilidad de compilación no sustituye la validación visual de cada renderer. No se han medido mejoras de rendimiento. Se conservan los modificadores `composed` para resolver el tema y construir modificadores de terceros; no hay evidencia que justifique sustituirlos por Node. Se almacenan pinceles/paths y el progreso se lee en dibujo/semánticas, fuera de composición.

## Publicación

Ver [PUBLISHING.md](PUBLISHING.md): los tags `v*` publican en Maven Central desde GitHub Actions.
Más contexto: [validación y matriz visual](VALIDATION.md).
