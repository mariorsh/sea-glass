package dev.seaglass.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Niveles de elevación. En Sea Glass la elevación es escarcha: cada nivel sube la opacidad
 * del vidrio, el desenfoque de fondo (si hay un [GlassBackdrop]) y la sombra.
 */
enum class GlassLevel(val blur: Dp, val shadow: Dp, val alpha: Float) {
    Level0(0.dp, 0.dp, 1f),
    Level1(6.dp, 1.dp, 0.55f),
    Level2(12.dp, 3.dp, 0.62f),
    Level3(18.dp, 6.dp, 0.78f),
    Level4(24.dp, 8.dp, 0.82f),
    Level5(32.dp, 12.dp, 0.86f),
}

/**
 * Desenfoque real del fondo. Compose no lo trae de serie en todas las plataformas, así que
 * Sea Glass lo deja enchufable: sin backdrop, el vidrio sube su opacidad para seguir legible.
 *
 * Ejemplo con Haze (dev.chrisbanes.haze), ver README:
 * ```
 * val haze = remember { HazeState() }
 * SeaGlassTheme(backdrop = GlassBackdrop { shape, level ->
 *     Modifier.hazeEffect(haze) { blurRadius = level.blur }
 * }) { … Modifier.hazeSource(haze) en el fondo … }
 * ```
 */
fun interface GlassBackdrop {
    fun modifier(shape: Shape, level: GlassLevel): Modifier

    companion object {
        val None: GlassBackdrop = GlassBackdrop { _, _ -> Modifier }
    }
}

val LocalGlassBackdrop = staticCompositionLocalOf { GlassBackdrop.None }

/**
 * Superficie de vidrio: sombra suave, relleno translúcido y borde de canto con la luz arriba.
 * Úsalo en todo lo que flota (barras, menús, dialogs, cards elevated).
 */
fun Modifier.glass(
    shape: Shape,
    level: GlassLevel = GlassLevel.Level3,
    color: Color? = null,
): Modifier = composed {
    val c = SeaGlassTheme.colors
    val backdrop = LocalGlassBackdrop.current
    val hasBackdrop = backdrop !== GlassBackdrop.None
    val fill = color ?: c.glassBar.copy(alpha = if (hasBackdrop) level.alpha else maxOf(level.alpha, 0.9f))
    this
        .then(
            if (level.shadow > 0.dp) {
                Modifier.shadow(level.shadow, shape, clip = false, ambientColor = c.shadow, spotColor = c.shadow)
            } else {
                Modifier
            },
        )
        .then(backdrop.modifier(shape, level))
        .clip(shape)
        .background(fill)
        .border(1.dp, edgeBrush(c), shape)
}

/** Panel esmerilado grande (`glass` + borde + sombra de panel). */
fun Modifier.glassPanel(shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(SeaGlassCorners.ExtraLargeIncreased)): Modifier =
    composed {
        val c = SeaGlassTheme.colors
        val backdrop = LocalGlassBackdrop.current
        val hasBackdrop = backdrop !== GlassBackdrop.None
        this
            .shadow(10.dp, shape, clip = false, ambientColor = c.shadow, spotColor = c.shadow)
            .then(backdrop.modifier(shape, GlassLevel.Level4))
            .clip(shape)
            .background(if (hasBackdrop) c.glass else c.glass.copy(alpha = 0.85f))
            .border(1.dp, edgeBrush(c), shape)
    }

/**
 * Relleno sólido de guijarro (botón filled, FAB primario, switch encendido, día seleccionado):
 * luz arriba, sombra interior abajo y sombra exterior.
 */
fun Modifier.pebble(color: Color, shape: Shape, elevation: Dp = 4.dp): Modifier = composed {
    val c = SeaGlassTheme.colors
    this
        .then(if (elevation > 0.dp) Modifier.shadow(elevation, shape, clip = false, ambientColor = c.shadow, spotColor = c.shadow) else Modifier)
        .clip(shape)
        .background(color)
        .background(
            Brush.verticalGradient(
                0f to Color.White.copy(alpha = 0.10f),
                0.45f to Color.Transparent,
                1f to Color.Black.copy(alpha = if (c.isDark) 0.22f else 0.16f),
            ),
        )
        .border(
            1.dp,
            Brush.verticalGradient(0f to Color.White.copy(alpha = 0.38f), 0.35f to Color.Transparent),
            shape,
        )
}

/** Relleno tonal (containers): luz de canto sin sombra exterior. */
fun Modifier.frost(color: Color, shape: Shape): Modifier = composed {
    val c = SeaGlassTheme.colors
    this
        .clip(shape)
        .background(color)
        .background(
            Brush.verticalGradient(
                0.6f to Color.Transparent,
                1f to Color.Black.copy(alpha = if (c.isDark) 0.09f else 0.05f),
            ),
        )
        .border(1.dp, edgeBrush(c), shape)
}

/** Guijarro hundido: estado pulsado. */
fun Modifier.pressedPebble(color: Color, shape: Shape): Modifier = this
    .clip(shape)
    .background(color)
    .background(Brush.verticalGradient(0f to Color.Black.copy(alpha = 0.26f), 0.35f to Color.Transparent))

internal fun edgeBrush(c: SeaGlassColors): Brush = Brush.verticalGradient(
    0f to c.glassHighlight,
    0.25f to c.glassEdge,
    1f to c.glassEdge.copy(alpha = c.glassEdge.alpha * 0.55f),
)
