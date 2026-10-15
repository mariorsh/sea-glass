<div align="center">

<a href="https://seaglass-web.vercel.app/">
  <img src="https://seaglass-web.vercel.app/favicon.svg" alt="Sea Glass" width="88" height="88" />
</a>

# Sea Glass

### El carácter del vidrio de mar. La flexibilidad de Compose.

Un sistema de diseño para **Compose Multiplatform**, sobre Material 3.<br />
Superficies esmeriladas, cantos iluminados y formas pulidas por el mar.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mariorsh.seaglass/seaglass-compose?style=flat-square&label=Maven%20Central&color=2E6B63)](https://central.sonatype.com/artifact/io.github.mariorsh.seaglass/seaglass-compose)
[![CI](https://img.shields.io/github/actions/workflow/status/mariorsh/sea-glass/ci.yml?branch=master&style=flat-square&label=checks&color=2E6B63)](https://github.com/mariorsh/sea-glass/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/mariorsh/sea-glass?style=flat-square&color=2E6B63)](LICENSE)

**[Explorar la web ↗](https://seaglass-web.vercel.app/)** &nbsp; · &nbsp;
**[Catálogo interactivo ↗](https://seaglass-web.vercel.app/catalogo)** &nbsp; · &nbsp;
[Inicio rápido](#inicio-rápido) &nbsp; · &nbsp;
[Changelog](CHANGELOG.md)

**Android · iOS · Desktop · Web**

</div>

---

## Una misma identidad, dos ambientes

| Arena | Noche |
| :--- | :--- |
| Fondos cálidos, verde mar y reflejos claros. | Fondos profundos, aguamarina y luz suave. |
| `#E8E4D8` · `#2E6B63` · `#7FBFAE` | `#0C171B` · `#8FD3C1` · `#276E61` |

Sea Glass combina los roles de Material 3 con tokens propios de vidrio, color y movimiento. Sus presets siguen el modo claro u oscuro del sistema, y puedes adaptar el resultado a la identidad de tu app.

| | Qué aporta |
| :--- | :--- |
| **Superficies con profundidad** | Vidrio, escarcha y guijarros con bordes iluminados y sombras configurables. |
| **Componentes para tu interfaz** | Acciones, entradas, navegación, feedback, contenedores y selectores. |
| **Tu marca, tu tema** | Colores, fuentes, formas, movimiento y acabado de las superficies. |
| **Blur opcional** | Integración Haze separada del núcleo, con fallback y modo opaco. |
| **Interacción cuidada** | Foco y hover en botones, objetivos de interacción de 48 dp y movimiento reducido. |
| **Tipografía propia** | Bricolage Grotesque, Figtree y JetBrains Mono, con opción de usar tus fuentes. |

> **Descubre el sistema de diseño en la [web de Sea Glass](https://seaglass-web.vercel.app/).** El catálogo interactivo está implementado en React/CSS; para probar los componentes Compose y su renderizado utiliza la aplicación [`sample`](sample).

## Instalación

Disponible en **Maven Central**. Con `google()` y `mavenCentral()` en tus repositorios es suficiente.

```kotlin
// commonMain.dependencies en KMP; dependencies en Android.
implementation("io.github.mariorsh.seaglass:seaglass-compose:0.2.0")
```

Para añadir blur real con Haze:

```kotlin
implementation("io.github.mariorsh.seaglass:seaglass-haze:0.2.0")
```

**¿Vienes de la 0.1.1?** Han cambiado las coordenadas y los paquetes. Consulta la [guía de migración](docs/MIGRATION.md) antes de actualizar.

## Inicio rápido

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mariorsh.seaglass.compose.*

@Composable
fun App() {
    SeaGlassTheme {
        SeaGlassBackground {
            SgCard(modifier = Modifier.padding(24.dp)) {
                SgCardBody(
                    title = "Una interfaz con carácter",
                    text = "Material 3, reinterpretado como vidrio de mar.",
                )
                SgCardActions {
                    SgButton("Explorar", onClick = { /* Tu acción */ })
                }
            }
        }
    }
}
```

El tema sigue el aspecto del sistema. Usa `darkTheme = false` para Arena o `darkTheme = true` para Noche. Puedes mezclar componentes `Sg*` con Material 3: la librería expone Material 3 como dependencia `api`.

## Explora los componentes

| Familia | Algunos componentes |
| :--- | :--- |
| **Acciones** | `SgButton`, `SgIconButton`, `SgToggleButton`, `SgSplitButton`, `SgFab` y grupos de botones. |
| **Entradas** | `SgTextField`, `SgCheckbox`, `SgRadioButton`, `SgSwitch`, `SgSlider` y chips. |
| **Navegación** | App bars, búsqueda, navigation bar, rail, drawer, tabs y toolbars. |
| **Feedback** | Progreso lineal y circular, loading indicator, snackbar, badges, tooltips y menús. |
| **Contenedores** | Cards, listas, carruseles, diálogos, bottom sheet y side sheet. |
| **Selectores** | Fecha, rango de fechas y hora. |

**[Ver el catálogo interactivo →](https://seaglass-web.vercel.app/catalogo)**

## Hazlo tuyo

`SeaGlassThemeConfig` reúne la configuración. Los valores no especificados conservan el preset Arena/Noche.

```kotlin
val brand = SeaGlassThemeConfig(
    colorScheme = seaGlassLightColorScheme().copy(
        primary = Color(0xFF8050AB),
        onPrimary = Color.White,
    ),
    colors = seaGlassLightColors().copy(
        aqua = Color(0xFFAD80D0),
    ),
    surfaces = SeaGlassSurfaceStyle(
        borderWidth = 2.dp,
        shadowMultiplier = 0.5f,
    ),
    strings = SeaGlassStrings.Spanish,
)

SeaGlassTheme(darkTheme = false, config = brand) {
    // Tu interfaz.
}
```

Importa `androidx.compose.ui.graphics.Color` y los tipos Compose utilizados en cada ejemplo.

| Configuración | Controla |
| :--- | :--- |
| `colorScheme` / `colors` | Roles Material y tokens propios de Sea Glass. |
| `fonts` | Familias tipográficas de la app. |
| `typography` / `extraTypography` | Estilos Material y adicionales de Sea Glass. |
| `shapes` | Formas Material consumidas por tarjetas, campos y diálogos. |
| `motion` | Animaciones y movimiento reducido. |
| `surfaces` | Política de blur, opacidad, bordes, luces y sombras. |
| `strings` | Etiquetas internas y accesibles. Inglés por defecto; español disponible. |

<details>
<summary><strong>Fuentes, colores y temas anidados</strong></summary>

Los roles de `MaterialTheme.colorScheme` y los tokens de `SeaGlassTheme.colors` son independientes: cambiar uno no deriva automáticamente el otro. Ajusta los pares de color de fondo y contenido para conservar el contraste.

Para evitar cargar las fuentes incluidas, proporciona tus familias:

```kotlin
fonts = SeaGlassFonts(
    FontFamily.SansSerif,
    FontFamily.SansSerif,
    FontFamily.Monospace,
)
```

`FontFamily` pertenece a `androidx.compose.ui.text.font`. Si proporcionas tanto `typography` como `extraTypography`, tampoco se cargan las fuentes predeterminadas. Si proporcionas solo una, la otra conserva su preset y puede cargarlas.

Los botones mantienen su geometría expresiva según `square` y `size`; las formas decorativas de `SeaGlassShapes` son independientes de Material Shapes.

Los temas anidados inician una configuración nueva, incluidos backdrop, etiquetas, movimiento y superficies. Reutiliza explícitamente `config` y `backdrop` cuando quieras heredarlos.

</details>

## Vidrio con o sin blur

El núcleo funciona sin Haze. Los modificadores `glass()`, `glassPanel()`, `frost()` y `pebble()` permiten aplicar el acabado Sea Glass a tus propios componentes.

| Política de superficie | Comportamiento |
| :--- | :--- |
| `Auto` | Usa blur si el proveedor declara soporte; en caso contrario, un relleno de reserva. |
| `Blur` | Solicita blur y aplica el mismo fallback cuando no está disponible. |
| `Opaque` | Usa opacidad completa y no invoca el backdrop. |

<details>
<summary><strong>Integración con Haze</strong></summary>

El módulo opcional `seaglass-haze` utiliza Haze **1.5.3**.

```kotlin
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import io.github.mariorsh.seaglass.compose.*
import io.github.mariorsh.seaglass.haze.*

@Composable
fun BlurExample() {
    SeaGlassTheme {
        val haze = remember { HazeState() }
        val backdrop = rememberHazeBackdrop(haze)

        SeaGlassTheme(backdrop = backdrop) {
            Box {
                LazyColumn(Modifier.seaGlassHazeSource(haze)) {
                    items(100) {
                        Text("Contenido detrás: $it", Modifier.padding(24.dp))
                    }
                }
                Box(Modifier.glassPanel().padding(24.dp)) {
                    Text("Contenido nítido")
                }
            }
        }
    }
}
```

La fuente y la superficie son hermanas: no captures el contenedor que incluye el propio efecto. Fuente y efecto deben estar en la misma ventana.

`rememberHazeBackdrop` utiliza `HazeDefaults.blurEnabled()` como comprobación conservadora de disponibilidad. Android API 31 y anteriores usan fallback. Los wrappers de diálogos y menús desactivan el backdrop entre ventanas; el bottom sheet conserva un relleno legible.

</details>

<details>
<summary><strong>Opacidades, proveedores y fallback</strong></summary>

`glass()` y `glassPanel()` comparten `SeaGlassSurfaceStyle`. Usa `forceFallback = true` para forzar el relleno de reserva.

`GlassBackdrop.isBlurAvailable` declara soporte y vale `false` por defecto. Instalar un adaptador no demuestra disponibilidad; `GlassBackdrop.available(adapter)` permite declararla explícitamente cuando ya la hayas comprobado.

Precedencia de opacidad:

1. `Opaque` fuerza alpha 1.
2. Un color explícito conserva su alpha en los demás modos.
3. Sin color explícito, se utilizan los tokens y la política del tema.

Un color explícito translúcido conserva su transparencia en fallback; comprueba su contraste. Sin color explícito, el mínimo predeterminado es 0.9 en vidrio y 0.85 en paneles.

En blur, `opacityMultiplier` modula la opacidad. `shadowMultiplier`, `borderWidth` y `highlightMultiplier` controlan el acabado. Los paneles conservan su relleno y elevación base de 10 dp; los niveles de vidrio mantienen sus valores de elevación, opacidad y blur.

</details>

## Accesibilidad y movimiento

- Los botones pequeños reservan un mínimo de **48 dp** en layout y permiten crecer al texto. Evita imponer restricciones inferiores.
- Los botones muestran foco y hover; los controles utilizan primitivas Compose para roles, selección y activación.
- `SeaGlassMotionConfig(reduced = true)` elimina morphing y muelles decorativos, conservando un indicador de carga identificable.
- `SeaGlassStrings.Spanish` permite seleccionar español; puedes sobrescribir cada etiqueta.
- Drawer y side sheet utilizan ventanas modales y extremos lógicos para RTL.

<details>
<summary><strong>Comportamiento por plataforma</strong></summary>

Compose aplica la escala de duración del host cuando está disponible. Usa el control explícito de movimiento reducido para una preferencia uniforme entre plataformas.

Los grupos de botones hacen morphing sin crecimiento horizontal. Usa `Modifier.weight(1f)` para repartir ancho; los grupos conectados y segmentados distribuyen el espacio y admiten etiquetas multilínea.

Los modales delegan foco y cierre con Atrás/Escape al host. `SgAlertDialog` y `SgFullScreenDialog` exponen `DialogProperties`. La restauración del foco al control lanzador y el comportamiento con tecnologías de asistencia requieren validación en cada host.

Consulta la [matriz de validación](docs/VALIDATION.md) para distinguir las comprobaciones automatizadas de las revisiones visuales pendientes.

</details>

## Compatibilidad

| Plataforma / dependencia | Compatibilidad |
| :--- | :--- |
| Android | minSdk 24. El adaptador utiliza blur desde API 32 y fallback en versiones anteriores. |
| iOS | `iosArm64`, `iosSimulatorArm64`, `iosX64`. |
| Desktop | JVM. |
| Web | Wasm. El blur depende del renderer y del navegador. |
| Kotlin | 2.1.21. |
| Compose Multiplatform | 1.8.2. |
| Haze, opcional | 1.5.3. |

## Documentación y desarrollo

| Recurso | Para qué sirve |
| :--- | :--- |
| [Web de Sea Glass](https://seaglass-web.vercel.app/) | Conocer el sistema de diseño. |
| [Catálogo interactivo](https://seaglass-web.vercel.app/catalogo) | Explorar las representaciones web de los componentes. |
| [Aplicación de ejemplo](sample) | Probar la implementación Compose. |
| [Desarrollo](docs/DEVELOPMENT.md) | Compilar y trabajar con el repositorio. |
| [Migración](docs/MIGRATION.md) | Actualizar desde 0.1.1. |
| [Validación](docs/VALIDATION.md) | Consultar pruebas, límites y escenario de rendimiento. |
| [Publicación](docs/PUBLISHING.md) | Preparar los artefactos de distribución. |
| [Changelog](CHANGELOG.md) | Revisar los cambios de cada versión. |

## Licencia

Sea Glass se distribuye bajo **[Apache License 2.0](LICENSE)**. Las fuentes Bricolage Grotesque, Figtree y JetBrains Mono se distribuyen bajo **SIL Open Font License**; sus textos están en [`licenses/`](licenses/).

---

<div align="center">

Creado por **[Mario Ríos Holgado](https://github.com/mariorsh)**

**[Explora Sea Glass ↗](https://seaglass-web.vercel.app/)**

</div>
