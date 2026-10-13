package dev.seaglass.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Tokens de color de Sea Glass que no tienen hueco en el [ColorScheme] de Material 3:
 * el suelo, las tres capas de vidrio, los estados del primario y los tintes de guijarro.
 *
 * Los roles de M3 (primary, onPrimaryContainer, surfaceContainerHigh…) se leen con
 * `MaterialTheme.colorScheme`; estos, con `SeaGlassTheme.colors`.
 */
@Immutable
data class SeaGlassColors(
    /** Fondo de pantalla: arena seca (claro) o agua nocturna (oscuro). */
    val ground: Color,
    /** Paneles esmerilados grandes. */
    val glass: Color,
    /** Vidrio denso: app bars, toolbars, navegación, menús, dialogs, sheets. */
    val glassBar: Color,
    /** Borde de 1 dp de todo el vidrio. */
    val glassEdge: Color,
    /** Luz de canto superior. */
    val glassHighlight: Color,
    /** Espuma translúcida: campo con foco, barra de búsqueda, pista inactiva. */
    val primaryContainerGlass: Color,
    val primaryHover: Color,
    val primaryPressed: Color,
    /** Texto de captions, metadatos y etiquetas vacías. */
    val inkMuted: Color,
    val focusRing: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    /** Color de las sombras exteriores. */
    val shadow: Color,
    val seafoam: Color,
    val cobalt: Color,
    val amber: Color,
    val lavender: Color,
    val coral: Color,
    val aqua: Color,
    val isDark: Boolean,
)

/** Valores literales de la paleta. Úsalos solo si necesitas un color fuera del tema. */
object SeaGlassPalette {
    // Roles · Arena (claro)
    val Primary = Color(0xFF2E6B63)
    val OnPrimary = Color(0xFFF4FBF8)
    val PrimaryContainer = Color(0xFFBFE3D6)
    val OnPrimaryContainer = Color(0xFF0F3A33)
    val Secondary = Color(0xFF33598A)
    val OnSecondary = Color(0xFFF3F7FC)
    val SecondaryContainer = Color(0xFFCCDBEE)
    val OnSecondaryContainer = Color(0xFF12304F)
    val Tertiary = Color(0xFF8C5A22)
    val OnTertiary = Color(0xFFFFF7EC)
    val TertiaryContainer = Color(0xFFF0D9B5)
    val OnTertiaryContainer = Color(0xFF3D2508)
    val Error = Color(0xFFA3402F)
    val OnError = Color(0xFFFFF5F2)
    val ErrorContainer = Color(0xFFF4D3CB)
    val OnErrorContainer = Color(0xFF4A130A)
    val Ground = Color(0xFFE8E4D8)
    val SurfaceLowest = Color(0xFFFAF9F4)
    val Surface = Color(0xFFF3F1EA)
    val SurfaceLow = Color(0xFFEEECE4)
    val SurfaceContainer = Color(0xFFE8E5DC)
    val SurfaceHigh = Color(0xFFE1DED4)
    val SurfaceHighest = Color(0xFFD9D6CB)
    val Ink = Color(0xFF15303A)
    val InkVariant = Color(0xFF3E5A60)
    val InkMuted = Color(0xFF4A6468)
    val Outline = Color(0xFF6F8C8A)
    val InverseSurface = Color(0xEB22383D)
    val InverseOnSurface = Color(0xFFE6F0EC)
    val InversePrimary = Color(0xFF9FD3C2)

    // Roles · Noche (oscuro)
    val PrimaryDark = Color(0xFF8FD3C1)
    val OnPrimaryDark = Color(0xFF00382F)
    val PrimaryContainerDark = Color(0xFF1F5049)
    val OnPrimaryContainerDark = Color(0xFFBFE3D6)
    val SecondaryDark = Color(0xFFA9C7F0)
    val OnSecondaryDark = Color(0xFF0E2E52)
    val SecondaryContainerDark = Color(0xFF2A4A72)
    val OnSecondaryContainerDark = Color(0xFFD3E3F8)
    val TertiaryDark = Color(0xFFEBBE84)
    val OnTertiaryDark = Color(0xFF462A05)
    val TertiaryContainerDark = Color(0xFF5E4016)
    val OnTertiaryContainerDark = Color(0xFFF6DDB8)
    val ErrorDark = Color(0xFFFFB4A6)
    val OnErrorDark = Color(0xFF5C1408)
    val ErrorContainerDark = Color(0xFF7A2A1D)
    val OnErrorContainerDark = Color(0xFFFFDAD3)
    val GroundDark = Color(0xFF0C171B)
    val SurfaceLowestDark = Color(0xFF0A1417)
    val SurfaceDark = Color(0xFF111D21)
    val SurfaceLowDark = Color(0xFF16242A)
    val SurfaceContainerDark = Color(0xFF1A2A30)
    val SurfaceHighDark = Color(0xFF203238)
    val SurfaceHighestDark = Color(0xFF273A40)
    val InkDark = Color(0xFFE3EEEA)
    val InkVariantDark = Color(0xFFB4C8C6)
    val InkMutedDark = Color(0xFF9DB3B1)
    val OutlineDark = Color(0xFF7F9A98)
    val InverseSurfaceDark = Color(0xF0DCE8E4)
    val InverseOnSurfaceDark = Color(0xFF1C2F34)
    val InversePrimaryDark = Color(0xFF276E61)
}

private val InkRgb = Color(0xFF15303A)
private val MintRgb = Color(0xFFBEE1D7)

fun seaGlassLightColorScheme(): ColorScheme = with(SeaGlassPalette) {
    lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        onPrimaryContainer = OnPrimaryContainer,
        inversePrimary = InversePrimary,
        secondary = Secondary,
        onSecondary = OnSecondary,
        secondaryContainer = SecondaryContainer,
        onSecondaryContainer = OnSecondaryContainer,
        tertiary = Tertiary,
        onTertiary = OnTertiary,
        tertiaryContainer = TertiaryContainer,
        onTertiaryContainer = OnTertiaryContainer,
        background = Ground,
        onBackground = Ink,
        surface = Surface,
        onSurface = Ink,
        surfaceVariant = SurfaceHigh,
        onSurfaceVariant = InkVariant,
        surfaceTint = Primary,
        inverseSurface = InverseSurface,
        inverseOnSurface = InverseOnSurface,
        error = Error,
        onError = OnError,
        errorContainer = ErrorContainer,
        onErrorContainer = OnErrorContainer,
        outline = Outline,
        outlineVariant = InkRgb.copy(alpha = 0.12f),
        scrim = InkRgb.copy(alpha = 0.32f),
        surfaceBright = SurfaceLowest,
        surfaceContainer = SurfaceContainer,
        surfaceContainerHigh = SurfaceHigh,
        surfaceContainerHighest = SurfaceHighest,
        surfaceContainerLow = SurfaceLow,
        surfaceContainerLowest = SurfaceLowest,
        surfaceDim = SurfaceHighest,
    )
}

fun seaGlassDarkColorScheme(): ColorScheme = with(SeaGlassPalette) {
    darkColorScheme(
        primary = PrimaryDark,
        onPrimary = OnPrimaryDark,
        primaryContainer = PrimaryContainerDark,
        onPrimaryContainer = OnPrimaryContainerDark,
        inversePrimary = InversePrimaryDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = OnSecondaryContainerDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = OnTertiaryContainerDark,
        background = GroundDark,
        onBackground = InkDark,
        surface = SurfaceDark,
        onSurface = InkDark,
        surfaceVariant = SurfaceHighDark,
        onSurfaceVariant = InkVariantDark,
        surfaceTint = PrimaryDark,
        inverseSurface = InverseSurfaceDark,
        inverseOnSurface = InverseOnSurfaceDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = OnErrorContainerDark,
        outline = OutlineDark,
        outlineVariant = MintRgb.copy(alpha = 0.12f),
        scrim = Color.Black.copy(alpha = 0.55f),
        surfaceBright = SurfaceHighestDark,
        surfaceContainer = SurfaceContainerDark,
        surfaceContainerHigh = SurfaceHighDark,
        surfaceContainerHighest = SurfaceHighestDark,
        surfaceContainerLow = SurfaceLowDark,
        surfaceContainerLowest = SurfaceLowestDark,
        surfaceDim = SurfaceLowestDark,
    )
}

fun seaGlassLightColors(): SeaGlassColors = SeaGlassColors(
    ground = SeaGlassPalette.Ground,
    glass = Color(0xFFF7FAF7).copy(alpha = 0.58f),
    glassBar = Color(0xFFFAFCFA).copy(alpha = 0.78f),
    glassEdge = Color.White.copy(alpha = 0.85f),
    glassHighlight = Color.White,
    primaryContainerGlass = SeaGlassPalette.PrimaryContainer.copy(alpha = 0.55f),
    primaryHover = Color(0xFF3A7A71),
    primaryPressed = Color(0xFF255A53),
    inkMuted = SeaGlassPalette.InkMuted,
    focusRing = SeaGlassPalette.Primary,
    disabledContainer = InkRgb.copy(alpha = 0.10f),
    disabledContent = InkRgb.copy(alpha = 0.38f),
    shadow = InkRgb,
    seafoam = Color(0xFF7FBFAE),
    cobalt = Color(0xFF7E9FCC),
    amber = Color(0xFFD9A963),
    lavender = Color(0xFFB6ABD6),
    coral = Color(0xFFE5A08F),
    aqua = Color(0xFF9FD3C2),
    isDark = false,
)

fun seaGlassDarkColors(): SeaGlassColors = SeaGlassColors(
    ground = SeaGlassPalette.GroundDark,
    glass = Color(0xFF18282E).copy(alpha = 0.58f),
    glassBar = Color(0xFF1C2E34).copy(alpha = 0.78f),
    glassEdge = MintRgb.copy(alpha = 0.20f),
    glassHighlight = MintRgb.copy(alpha = 0.25f),
    primaryContainerGlass = SeaGlassPalette.PrimaryContainerDark.copy(alpha = 0.55f),
    primaryHover = Color(0xFFA6E0D0),
    primaryPressed = Color(0xFF6FB8A6),
    inkMuted = SeaGlassPalette.InkMutedDark,
    focusRing = SeaGlassPalette.PrimaryDark,
    disabledContainer = MintRgb.copy(alpha = 0.10f),
    disabledContent = MintRgb.copy(alpha = 0.38f),
    shadow = Color.Black,
    seafoam = Color(0xFF2F6E62),
    cobalt = Color(0xFF36557F),
    amber = Color(0xFF7A5422),
    lavender = Color(0xFF5A4E86),
    coral = Color(0xFF8C4A3C),
    aqua = Color(0xFF276E61),
    isDark = true,
)

val LocalSeaGlassColors = staticCompositionLocalOf { seaGlassLightColors() }
