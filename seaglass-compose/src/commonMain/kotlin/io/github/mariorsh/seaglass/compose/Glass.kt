package io.github.mariorsh.seaglass.compose

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
 * The optional seaglass-haze module provides a capability-aware adapter. Legacy
 * SAM adapters need an explicit availability declaration; see the migration guide.
 */
fun interface GlassBackdrop {
    /** Conservative default: installing an adapter does not prove blur is supported. */
    val isBlurAvailable: Boolean get() = false

    fun modifier(shape: Shape, level: GlassLevel): Modifier

    companion object {
        /** Explicit support declaration for legacy SAM adapters. */
        fun available(adapter: GlassBackdrop): GlassBackdrop = object : GlassBackdrop {
            override val isBlurAvailable = true
            override fun modifier(shape: Shape, level: GlassLevel) = adapter.modifier(shape, level)
        }
        val None: GlassBackdrop = object : GlassBackdrop {
            override val isBlurAvailable = false
            override fun modifier(shape: Shape, level: GlassLevel) = Modifier
        }
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
): Modifier = composed { glassSurface(shape, level, color, panel = false) }

/** Panel retains its own fill and 10dp elevation, sharing surface policy with glass. */
fun Modifier.glassPanel(shape: Shape? = null): Modifier = composed {
    val resolvedShape = shape ?: SeaGlassTheme.surfaces.panelShape
        ?: androidx.compose.foundation.shape.RoundedCornerShape(SeaGlassCorners.ExtraLargeIncreased)
    glassSurface(resolvedShape, GlassLevel.Level4, null, panel = true)
}

@androidx.compose.runtime.Composable
private fun Modifier.glassSurface(shape: Shape, level: GlassLevel, color: Color?, panel: Boolean): Modifier {
    val c = SeaGlassTheme.colors
    val style = SeaGlassTheme.surfaces
    val backdrop = LocalGlassBackdrop.current
    val blur = style.usesBlur(backdrop.isBlurAvailable) && level.blur > 0.dp
    val base = color ?: if (panel) c.glass else c.glassBar.copy(alpha = level.alpha)
    val alpha = when {
        style.policy == GlassSurfacePolicy.Opaque -> 1f
        color != null -> color.alpha
        blur -> (base.alpha * style.opacityMultiplier).coerceIn(0f, 1f)
        else -> maxOf(base.alpha, if (panel) style.panelFallbackAlpha else style.fallbackAlpha)
    }
    val elevation = (if (panel) 10.dp else level.shadow) * style.shadowMultiplier
    val brush = androidx.compose.runtime.remember(c, style.highlightMultiplier) {
        edgeBrush(c.copy(glassHighlight = c.glassHighlight.copy(alpha = (c.glassHighlight.alpha * style.highlightMultiplier).coerceIn(0f, 1f))))
    }
    return this
        .then(if (elevation > 0.dp) Modifier.exteriorShadow(elevation, shape, c.shadow) else Modifier)
        .clip(shape)
        .then(if (blur) backdrop.modifier(shape, level) else Modifier)
        .background(base.copy(alpha = alpha))
        .border(style.borderWidth, brush, shape)
}

/**
 * Relleno sólido de guijarro (botón filled, FAB primario, switch encendido, día seleccionado):
 * luz arriba, sombra interior abajo y sombra exterior.
 */
fun Modifier.pebble(color: Color, shape: Shape, elevation: Dp = 4.dp): Modifier = composed {
    val c = SeaGlassTheme.colors
    val style = SeaGlassTheme.surfaces
    this
        .then(if (elevation > 0.dp) Modifier.shadow(elevation * style.shadowMultiplier, shape, clip = false, ambientColor = c.shadow, spotColor = c.shadow) else Modifier)
        .clip(shape)
        .background(color)
        .background(
            androidx.compose.runtime.remember(c.isDark, style.highlightMultiplier) { Brush.verticalGradient(
                0f to Color.White.copy(alpha = (0.10f * style.highlightMultiplier).coerceIn(0f, 1f)),
                0.45f to Color.Transparent,
                1f to Color.Black.copy(alpha = if (c.isDark) 0.22f else 0.16f),
            ) },
        )
        .border(
            style.borderWidth,
            androidx.compose.runtime.remember(style.highlightMultiplier) { Brush.verticalGradient(0f to Color.White.copy(alpha = (0.38f * style.highlightMultiplier).coerceIn(0f, 1f)), 0.35f to Color.Transparent) },
            shape,
        )
}

/** Relleno tonal (containers): luz de canto sin sombra exterior. */
fun Modifier.frost(color: Color, shape: Shape): Modifier = composed {
    val c = SeaGlassTheme.colors
    val style = SeaGlassTheme.surfaces
    this
        .clip(shape)
        .background(color)
        .background(
            androidx.compose.runtime.remember(c.isDark, style.highlightMultiplier) { Brush.verticalGradient(
                0.6f to Color.Transparent,
                1f to Color.Black.copy(alpha = if (c.isDark) 0.09f else 0.05f),
            ) },
        )
        .border(style.borderWidth, androidx.compose.runtime.remember(c, style.highlightMultiplier) { edgeBrush(c.copy(glassHighlight = c.glassHighlight.copy(alpha = (c.glassHighlight.alpha * style.highlightMultiplier).coerceIn(0f, 1f)))) }, shape)
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
