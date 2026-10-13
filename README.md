# Sea Glass para Compose

Librería Kotlin Multiplatform con el design system **Sea Glass**: Material 3 y M3 Expressive reinterpretados como vidrio de mar, con superficies esmeriladas, cantos pulidos con luz y color que se ve a través.

- **Plataformas:** Android, iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`), escritorio (JVM) y web (Wasm).
- **Base técnica:** Compose Multiplatform 1.8.2, Kotlin 2.1.21 y Material 3 (`compose.material3`). La librería expone Material 3 como `api`, así que en tu proyecto puedes mezclar componentes `Sg*` con los de Material.
- **Fuentes incluidas:** Bricolage Grotesque, Figtree y JetBrains Mono, todas con licencia OFL. Las licencias están en `licenses/`.

## Instalación

### Opción A · Maven local (la más rápida)

```bash
cd sea-glass
./gradlew :seaglass-compose:publishToMavenLocal
```

Tu proyecto:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

// build.gradle.kts del módulo (KMP: en commonMain.dependencies; Android: en dependencies)
implementation("dev.seaglass:seaglass-compose:0.1.1")
```

En macOS se publican todas las plataformas. En Linux o Windows se omiten las de iOS.

### Opción B · Build compuesto (para editar la librería y la app a la vez)

```kotlin
// settings.gradle.kts de tu app
includeBuild("../sea-glass")
```

Y la misma dependencia `dev.seaglass:seaglass-compose:0.1.1`. Gradle la sustituye por el código fuente.

### Opción C · GitHub Packages

Sube el proyecto a un repositorio y ejecuta:

```bash
GITHUB_REPOSITORY=tu-usuario/sea-glass GITHUB_ACTOR=tu-usuario GITHUB_TOKEN=<token con write:packages> \
  ./gradlew :seaglass-compose:publishAllPublicationsToGitHubPackagesRepository
```

En los proyectos que la usen, añade el repositorio `https://maven.pkg.github.com/tu-usuario/sea-glass` con credenciales.

**Antes de publicar:** cambia `group` y `version` en `seaglass-compose/build.gradle.kts`.

## Uso

```kotlin
import dev.seaglass.compose.*

@Composable
fun App() {
    SeaGlassTheme {                       // sigue el modo del sistema; darkTheme = true fuerza Noche
        SeaGlassBackground {              // suelo de arena / agua nocturna con guijarros detrás
            Scaffold(
                containerColor = Color.Transparent,
                topBar = { SgTopAppBar("Colección", Modifier.statusBarsPadding().padding(8.dp)) },
                bottomBar = {
                    SgNavigationBar(Modifier.navigationBarsPadding().padding(12.dp)) {
                        SgNavigationBarItem(selected = true, onClick = {}, icon = Icons.Filled.Home, label = "Inicio")
                        SgNavigationBarItem(selected = false, onClick = {}, icon = Icons.Filled.Search, label = "Explorar")
                    }
                },
                floatingActionButton = { SgFab(onClick = {}, icon = Icons.Filled.Edit, contentDescription = "Nueva nota") },
            ) { padding ->
                Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SgButton("Guardar", onClick = {})
                    SgButton("Compartir", onClick = {}, style = SgButtonStyle.Tonal)
                }
            }
        }
    }
}
```

Para ver todos los componentes en los dos temas, llama a **`SeaGlassGallery()`** desde cualquier pantalla. Trae su propio tema y un interruptor Arena/Noche.

### Tokens

| Qué | Dónde |
|---|---|
| Roles de M3 (`primary`, `onPrimaryContainer`, `surfaceContainerHigh`…) | `MaterialTheme.colorScheme` |
| Suelo, vidrio, tintes de guijarro, estados, `inkMuted` | `SeaGlassTheme.colors` |
| Escala tipográfica M3 | `MaterialTheme.typography` |
| Emphasized y `captionMono` | `SeaGlassTheme.typography` |
| Esquinas `corner-xs` … `corner-xxl` | `SeaGlassCorners` |
| Espaciado de 4 dp | `SeaGlassSpacing` |
| Muelles spatial y effects de Expressive | `SeaGlassMotion` |
| Shape library (cookie, trébol, sunny, flor, burst, arco, gema) | `SeaGlassShapes` |
| Valores literales de color | `SeaGlassPalette` |

### Modificadores de superficie

- `Modifier.glass(shape, level)`: vidrio con borde de canto y sombra. Los niveles van de `GlassLevel.Level0` a `Level5`.
- `Modifier.glassPanel()`: panel esmerilado grande.
- `Modifier.pebble(color, shape)`: relleno sólido de guijarro, con luz arriba y sombra interior abajo.
- `Modifier.frost(color, shape)`: relleno tonal, con luz de canto y sin sombra exterior.

## Componentes

| Grupo | Componentes |
|---|---|
| Acciones | `SgButton` (5 estilos, 5 tamaños, forma redonda o cuadrada), `SgToggleButton`, `SgIconButton`, `SgIconToggleButton`, `SgButtonGroup`, `SgConnectedButtonGroup` (también `Multi`), `SgSegmentedButtons`, `SgSplitButton`, `SgFab`, `SgExtendedFab`, `SgFabMenu` |
| Entradas | `SgTextField` (Filled / Outlined), `SgCheckbox`, `SgTriStateCheckbox`, `SgRadioButton`, `SgSwitch`, `SgSlider`, `SgRangeSlider`, `SgAssistChip`, `SgFilterChip`, `SgInputChip`, `SgSuggestionChip`, `SgAvatar` |
| Navegación | `SgTopAppBar` (Small, CenterAligned, Medium, Large), `SgSearchAppBar`, `SgSearchBar`, `SgSearchView` (pantalla completa / docked), `SgToolbar` + `SgToolbarItem`, `SgNavigationBar` + `SgNavigationBarItem`, `SgNavigationRail` + `SgNavigationRailItem`, `SgModalNavigationDrawer`, `SgTabRow` |
| Feedback | `SgLinearProgress` (plano / ondulado), `SgLinearProgressIndeterminate`, `SgCircularProgress`, `SgLoadingIndicator`, `SgSnackbar` + `SgSnackbarHost`, `SgBadge` + `SgBadgedBox`, `SgTooltipBox`, `SgRichTooltipBox`, `SgDropdownMenu` + `SgMenuGroup` + `SgMenuItem` |
| Contenedores | `SgCard` + `SgMedia` + `SgCardBody` + `SgCardActions`, `SgListItem`, `SgListIcon`, `SgDivider`, `SgSegmentedListItem`, `SgMultiBrowseCarousel`, `SgUncontainedCarousel`, `SgAlertDialog`, `SgFullScreenDialog`, `SgModalBottomSheet` + `SgSheetAction`, `SgModalSideSheet` |
| Selectores | `SgDatePicker`, `SgDatePickerDialog`, `SgDateRangePickerDialog`, `SgTimePickerDialog` (más `sgDatePickerColors()` y `sgTimePickerColors()` si usas los de M3 directamente) |

## Desenfoque real del vidrio

Compose no trae de serie un desenfoque del fondo que funcione en todas las plataformas. Por eso, sin configuración, el vidrio sube su opacidad para seguir siendo legible. Si quieres el esmerilado real, conecta una librería de backdrop blur como [Haze](https://github.com/chrisbanes/haze) (Kotlin Multiplatform) mediante `GlassBackdrop`:

```kotlin
val hazeState = remember { HazeState() }
SeaGlassTheme(
    backdrop = GlassBackdrop { _, level ->
        Modifier.hazeEffect(state = hazeState) { blurRadius = level.blur }
    },
) {
    SeaGlassBackground(Modifier.hazeSource(hazeState)) { /* … */ }
}
```

Los nombres `hazeSource` y `hazeEffect` son los de Haze 1.x; comprueba la API de la versión que instales. Sea Glass no depende de Haze.

## Notas

- Los componentes de navegación, sheets y dialogs no aplican los insets del sistema: añade `statusBarsPadding()`, `navigationBarsPadding()` o `safeDrawingPadding()` donde los coloques. El botón atrás del drawer y de la side sheet también lo gestiona tu app (`BackHandler`).
- Algunos componentes envuelven APIs experimentales de Material 3 y las marcan con `@OptIn` dentro de la librería: Slider con pistas personalizadas, carrusel, tooltips, bottom sheet y selectores de fecha y hora. Si al subir de versión de Compose Multiplatform cambia alguna firma, el error estará en `Inputs.kt`, `Containers.kt`, `Feedback.kt`, `Dialogs.kt` o `Pickers.kt`.
- Para cambiar los colores, edita `Color.kt`. Los nombres de los roles coinciden con el design system Sea Glass.
