package io.github.mariorsh.seaglass.compose

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.github.mariorsh.seaglass.compose.resources.Res
import io.github.mariorsh.seaglass.compose.resources.bricolage_bold
import io.github.mariorsh.seaglass.compose.resources.bricolage_regular
import io.github.mariorsh.seaglass.compose.resources.bricolage_semibold
import io.github.mariorsh.seaglass.compose.resources.figtree_bold
import io.github.mariorsh.seaglass.compose.resources.figtree_medium
import io.github.mariorsh.seaglass.compose.resources.figtree_regular
import io.github.mariorsh.seaglass.compose.resources.figtree_semibold
import io.github.mariorsh.seaglass.compose.resources.jetbrainsmono_medium
import io.github.mariorsh.seaglass.compose.resources.jetbrainsmono_regular
import org.jetbrains.compose.resources.Font

/** Las tres familias de Sea Glass (incluidas en la librería, licencia OFL). */
@Immutable
data class SeaGlassFonts(
    val display: FontFamily,
    val body: FontFamily,
    val mono: FontFamily,
)

@Composable
fun rememberSeaGlassFonts(): SeaGlassFonts = SeaGlassFonts(
    display = FontFamily(
        Font(Res.font.bricolage_regular, FontWeight.Normal),
        Font(Res.font.bricolage_semibold, FontWeight.SemiBold),
        Font(Res.font.bricolage_bold, FontWeight.Bold),
    ),
    body = FontFamily(
        Font(Res.font.figtree_regular, FontWeight.Normal),
        Font(Res.font.figtree_medium, FontWeight.Medium),
        Font(Res.font.figtree_semibold, FontWeight.SemiBold),
        Font(Res.font.figtree_bold, FontWeight.Bold),
    ),
    mono = FontFamily(
        Font(Res.font.jetbrainsmono_regular, FontWeight.Normal),
        Font(Res.font.jetbrainsmono_medium, FontWeight.Medium),
    ),
)

/** Estilos de Sea Glass que M3 no define: los Emphasized de Expressive y el caption mono. */
@Immutable
data class SeaGlassTypography(
    val displayLargeEmphasized: TextStyle,
    val headlineMediumEmphasized: TextStyle,
    val titleLargeEmphasized: TextStyle,
    val labelLargeEmphasized: TextStyle,
    /** Captions en mayúsculas, valores de token, horas y tamaños. Aplica `uppercase()` al texto. */
    val captionMono: TextStyle,
)

fun seaGlassTypography(fonts: SeaGlassFonts): Typography {
    fun display(size: Int, line: Int, weight: FontWeight = FontWeight.Normal, tracking: Double = -0.02) = TextStyle(
        fontFamily = fonts.display, fontWeight = weight, fontSize = size.sp, lineHeight = line.sp, letterSpacing = tracking.em,
    )
    fun body(size: Int, line: Int, weight: FontWeight = FontWeight.Normal, tracking: Double = 0.0) = TextStyle(
        fontFamily = fonts.body, fontWeight = weight, fontSize = size.sp, lineHeight = line.sp, letterSpacing = tracking.em,
    )
    return Typography(
        displayLarge = display(57, 64),
        displayMedium = display(45, 52),
        displaySmall = display(36, 44),
        headlineLarge = display(32, 40, FontWeight.SemiBold, -0.01),
        headlineMedium = display(28, 36, FontWeight.SemiBold, -0.01),
        headlineSmall = display(24, 32, FontWeight.SemiBold, 0.0),
        titleLarge = body(22, 28, FontWeight.SemiBold),
        titleMedium = body(16, 24, FontWeight.SemiBold),
        titleSmall = body(14, 20, FontWeight.SemiBold),
        bodyLarge = body(16, 24),
        bodyMedium = body(14, 20),
        bodySmall = body(12, 16),
        labelLarge = body(14, 20, FontWeight.SemiBold, 0.01),
        labelMedium = body(12, 16, FontWeight.SemiBold),
        labelSmall = body(11, 16, FontWeight.SemiBold),
    )
}

fun seaGlassExtraTypography(fonts: SeaGlassFonts): SeaGlassTypography = SeaGlassTypography(
    displayLargeEmphasized = TextStyle(fontFamily = fonts.display, fontWeight = FontWeight.Bold, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.02).em),
    headlineMediumEmphasized = TextStyle(fontFamily = fonts.display, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
    titleLargeEmphasized = TextStyle(fontFamily = fonts.body, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    labelLargeEmphasized = TextStyle(fontFamily = fonts.body, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
    captionMono = TextStyle(fontFamily = fonts.mono, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.04.em),
)

val LocalSeaGlassTypography = staticCompositionLocalOf<SeaGlassTypography> {
    error("SeaGlassTypography no está disponible: envuelve tu UI en SeaGlassTheme { }")
}
