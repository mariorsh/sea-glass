# Sea Glass

Componentes Compose Multiplatform sobre Material 3: superficies esmeriladas, bordes iluminados, guijarros y presets Arena/Noche.

[Changelog](CHANGELOG.md) · [Migración desde 0.1.1](docs/MIGRATION.md)

## Instalación

Sea Glass está en Maven Central; no necesitas añadir repositorios extra (`google()` y `mavenCentral()` bastan).

```kotlin
// build.gradle.kts del módulo (KMP: commonMain.dependencies; Android: dependencies)
implementation("io.github.mariorsh.seaglass:seaglass-compose:0.2.0")
implementation("io.github.mariorsh.seaglass:seaglass-haze:0.2.0") // opcional: blur real con Haze
```

Plataformas: Android (minSdk 24), escritorio (JVM), web (Wasm) e iOS (`iosArm64`, `iosX64`, `iosSimulatorArm64`).

## Inicio rápido

```kotlin
import io.github.mariorsh.seaglass.compose.*

@Composable
fun App() {
    SeaGlassTheme {
        SeaGlassBackground {
            SgButton("Guardar", onClick = {})
        }
    }
}
```

## Personalización

`SeaGlassThemeConfig` reúne la configuración del tema. Arena/Noche siguen siendo los presets de `darkTheme`; los campos nulos seleccionan sus valores.

```kotlin
val brandFonts = SeaGlassFonts(FontFamily.SansSerif, FontFamily.SansSerif, FontFamily.Monospace)
val brand = SeaGlassThemeConfig(
    colorScheme = seaGlassLightColorScheme().copy(primary = Color(0xff8050ab)),
    colors = seaGlassLightColors().copy(aqua = Color(0xffad80d0)),
    fonts = brandFonts,
    shapes = Shapes(small = RoundedCornerShape(2.dp)),
    motion = SeaGlassMotionConfig(reduced = true),
    surfaces = SeaGlassSurfaceStyle(borderWidth = 2.dp, shadowMultiplier = 0.5f),
    strings = SeaGlassStrings.Spanish,
)
SeaGlassTheme(config = brand) { /* componentes */ }
```

Los roles Material (`MaterialTheme.colorScheme`) y los tokens Sea Glass (`SeaGlassTheme.colors`) son **independientes**. Cambiar uno no deriva el otro. `fonts` evita cargar las fuentes incluidas; también puedes proporcionar ambas `typography` y `extraTypography`. Si proporcionas solo una, la otra conserva el preset y puede cargar sus fuentes.

Las formas Material se consumen en tarjetas, campos y diálogos. Los botones mantienen su geometría expresiva por variante (`square`, `size`); las formas decorativas de `SeaGlassShapes` son independientes de Material Shapes.

Los temas anidados inician un preset/configuración nuevo, incluidos backdrop, strings, movimiento y superficies. Para heredar explícitamente reutiliza `config` y `backdrop`.

## Superficies y blur

`glass(shape, level, color)` y `glassPanel(shape)` comparten `SeaGlassSurfaceStyle`:

| Política | Proveedor disponible | Sin soporte / fallback forzado |
|---|---|---|
| Auto | Blur | Relleno legible |
| Blur | Solicita blur | Mismo fallback legible |
| Opaque | No invoca el backdrop | Opacidad 1 |

`forceFallback = true` fuerza el fallback. `GlassBackdrop.isBlurAvailable` declara soporte real y por defecto es `false`; instalar un adaptador no prueba disponibilidad. `GlassBackdrop.available(adapter)` sirve para adaptadores SAM cuyo soporte haya comprobado el consumidor.

Precedencia: Opaque fuerza alpha 1; después prevalece el color explícito (incluido su alpha); finalmente se usan el color del tema y la opacidad del nivel. Un color explícito translúcido seguirá siéndolo en fallback: el consumidor es responsable de su contraste. Sin color explícito, el mínimo de fallback es 0.9 en vidrio y 0.85 en panel. En blur `opacityMultiplier` modula los tokens; `shadowMultiplier`, `borderWidth` y `highlightMultiplier` controlan el acabado. Los paneles mantienen su relleno y sombra de 10dp; los niveles conservan su elevación, opacidad y blur.

### Haze opcional

El módulo principal no depende de Haze. `seaglass-haze` usa Haze **1.5.3**.

```kotlin
@Composable
fun BlurExample() {
    SeaGlassTheme {
        val haze = remember { HazeState() }
        val backdrop = rememberHazeBackdrop(haze)
        SeaGlassTheme(backdrop = backdrop) {
            Box {
                LazyColumn(Modifier.seaGlassHazeSource(haze)) {
                    items(100) { Text("Contenido detrás: $it", Modifier.padding(24.dp)) }
                }
                Box(Modifier.glassPanel().padding(24.dp)) { Text("Texto nítido") }
            }
        }
    }
}
```

Imports: `dev.chrisbanes.haze.HazeState` y `io.github.mariorsh.seaglass.haze.*`. Hay un ejemplo completo en la app `sample` del repositorio.

Fuente y efecto son **hermanos**: nunca captures el contenedor que contiene la propia superficie. La lista puede incluir sus tarjetas, pero el panel superpuesto queda fuera de la captura y su texto sigue nítido.

`rememberHazeBackdrop` usa `HazeDefaults.blurEnabled()` como disponibilidad conservadora. Android API 31 y anteriores usan fallback. Fuente y efecto deben estar en la misma ventana. Los diálogos y popups no capturan otra ventana: los wrappers de diálogo/menú desactivan el backdrop; el bottom sheet conserva un relleno legible. Para un diálogo personalizado crea una fuente dentro de su propia ventana.

## Accesibilidad y movimiento

Los botones pequeños reservan al menos 48dp en layout para interacción; su superficie visual puede ser menor. No fuerces restricciones inferiores al mínimo. Las alturas admiten texto creciente. Roles, selección, activación de teclado y deshabilitado usan primitivas Compose; los botones muestran foco y hover.

`SgButtonGroup` hace morphing sin crecimiento horizontal; usa `Modifier.weight(1f)` si necesitas repartir el ancho. Los grupos conectados/segmentados reparten el ancho disponible y permiten etiquetas multilínea.

Drawer y side sheet usan `Dialog`, aislando el fondo y delegando al host foco modal, Atrás y Escape. Sus extremos son lógicos en RTL. `SgAlertDialog` y `SgFullScreenDialog` exponen `DialogProperties`. La restauración de un control lanzador específico y tecnologías de asistencia requieren comprobación por host: consulta la matriz visual.

`SeaGlassStrings` usa inglés por defecto, ofrece `Spanish` y campos sobrescribibles. El consumidor selecciona el idioma. Los indicadores de carga exponen semántica de progreso indeterminado además de su etiqueta.

`SeaGlassMotionConfig(reduced = true)` elimina los muelles/morphing decorativos y deja una forma de carga estática identificable. Compose aplica la escala de duración del host cuando está disponible; usa el control explícito para una preferencia uniforme entre plataformas.

## Compatibilidad

| Pieza | Versión / destino |
|---|---|
| Kotlin / Compose Multiplatform | 2.1.21 / 1.8.2 |
| Android | minSdk 24; blur a partir de API 32 (en versiones anteriores se usa el relleno legible) |
| Escritorio | JVM |
| Web | Wasm; el blur depende del renderer y del navegador, con relleno de reserva |
| Apple | iosX64, iosArm64, iosSimulatorArm64 |
| Haze (opcional) | 1.5.3 |

Consulta también la [guía de migración](docs/MIGRATION.md) y el [changelog](CHANGELOG.md). Si quieres contribuir o compilar la librería, mira [DEVELOPMENT.md](docs/DEVELOPMENT.md).

## Licencia

El código se distribuye bajo [Apache License 2.0](LICENSE). Las fuentes incluidas (Bricolage Grotesque, Figtree y JetBrains Mono) se distribuyen bajo SIL Open Font License; consulta sus textos en [`licenses/`](licenses/).
