package dev.seaglass.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

/**
 * Tema Sea Glass: aplica el ColorScheme, la tipografía y las formas a MaterialTheme y expone
 * los tokens propios del sistema en [SeaGlassTheme.colors] y [SeaGlassTheme.typography].
 *
 * @param darkTheme Noche (oscuro) o Arena (claro).
 * @param backdrop desenfoque real del fondo para el vidrio; ver [GlassBackdrop].
 */
@Composable
fun SeaGlassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    backdrop: GlassBackdrop = GlassBackdrop.None,
    content: @Composable () -> Unit,
) {
    val fonts = rememberSeaGlassFonts()
    val typography = remember(fonts) { seaGlassTypography(fonts) }
    val extra = remember(fonts) { seaGlassExtraTypography(fonts) }
    val colors = remember(darkTheme) { if (darkTheme) seaGlassDarkColors() else seaGlassLightColors() }
    val scheme = remember(darkTheme) { if (darkTheme) seaGlassDarkColorScheme() else seaGlassLightColorScheme() }

    CompositionLocalProvider(
        LocalSeaGlassColors provides colors,
        LocalSeaGlassTypography provides extra,
        LocalGlassBackdrop provides backdrop,
    ) {
        MaterialTheme(
            colorScheme = scheme,
            typography = typography,
            shapes = seaGlassShapes(),
            content = content,
        )
    }
}

object SeaGlassTheme {
    val colors: SeaGlassColors
        @Composable @ReadOnlyComposable get() = LocalSeaGlassColors.current

    val typography: SeaGlassTypography
        @Composable @ReadOnlyComposable get() = LocalSeaGlassTypography.current

    val corners get() = SeaGlassCorners
    val spacing get() = SeaGlassSpacing
    val motion get() = SeaGlassMotion
}

/**
 * Suelo de pantalla con dos o tres guijarros de color difuminados detrás.
 * El vidrio necesita algo detrás para leerse: pon tu Scaffold dentro.
 */
@Composable
fun SeaGlassBackground(
    modifier: Modifier = Modifier,
    pebbles: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val c = SeaGlassTheme.colors
    Box(modifier.fillMaxSize().background(c.ground)) {
        if (pebbles) {
            Pebble(Alignment.TopStart, -80, -60, 320, 240, c.aqua, RoundedCornerShape(58, 42, 61, 39))
            Pebble(Alignment.CenterEnd, 120, 40, 300, 260, c.cobalt, RoundedCornerShape(41, 59, 38, 62))
            Pebble(Alignment.BottomStart, 20, 80, 280, 220, c.amber, RoundedCornerShape(63, 37, 52, 48))
        }
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            content()
        }
    }
}

@Composable
private fun BoxScope.Pebble(
    align: Alignment,
    x: Int,
    y: Int,
    w: Int,
    h: Int,
    color: androidx.compose.ui.graphics.Color,
    shape: RoundedCornerShape,
) {
    Box(
        Modifier
            .align(align)
            .offset(x.dp, y.dp)
            .size(w.dp, h.dp)
            .blur(48.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .alpha(0.55f)
            .background(color, shape),
    )
}
