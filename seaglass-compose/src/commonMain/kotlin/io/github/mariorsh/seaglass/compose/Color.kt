package io.github.mariorsh.seaglass.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
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
public data class SeaGlassColors(
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
public object SeaGlassPalette {
    // Roles · Arena (claro)
    public val Primary: Color = Color(0xFF2E6B63)
    public val OnPrimary: Color = Color(0xFFF4FBF8)
    public val PrimaryContainer: Color = Color(0xFFBFE3D6)
    public val OnPrimaryContainer: Color = Color(0xFF0F3A33)
    public val Secondary: Color = Color(0xFF33598A)
    public val OnSecondary: Color = Color(0xFFF3F7FC)
    public val SecondaryContainer: Color = Color(0xFFCCDBEE)
    public val OnSecondaryContainer: Color = Color(0xFF12304F)
    public val Tertiary: Color = Color(0xFF8C5A22)
    public val OnTertiary: Color = Color(0xFFFFF7EC)
    public val TertiaryContainer: Color = Color(0xFFF0D9B5)
    public val OnTertiaryContainer: Color = Color(0xFF3D2508)
    public val Error: Color = Color(0xFFA3402F)
    public val OnError: Color = Color(0xFFFFF5F2)
    public val ErrorContainer: Color = Color(0xFFF4D3CB)
    public val OnErrorContainer: Color = Color(0xFF4A130A)
    public val Ground: Color = Color(0xFFE8E4D8)
    public val SurfaceLowest: Color = Color(0xFFFAF9F4)
    public val Surface: Color = Color(0xFFF3F1EA)
    public val SurfaceLow: Color = Color(0xFFEEECE4)
    public val SurfaceContainer: Color = Color(0xFFE8E5DC)
    public val SurfaceHigh: Color = Color(0xFFE1DED4)
    public val SurfaceHighest: Color = Color(0xFFD9D6CB)
    public val Ink: Color = Color(0xFF15303A)
    public val InkVariant: Color = Color(0xFF3E5A60)
    public val InkMuted: Color = Color(0xFF4A6468)
    public val Outline: Color = Color(0xFF6F8C8A)
    public val InverseSurface: Color = Color(0xEB22383D)
    public val InverseOnSurface: Color = Color(0xFFE6F0EC)
    public val InversePrimary: Color = Color(0xFF9FD3C2)

    // Roles · Noche (oscuro)
    public val PrimaryDark: Color = Color(0xFF8FD3C1)
    public val OnPrimaryDark: Color = Color(0xFF00382F)
    public val PrimaryContainerDark: Color = Color(0xFF1F5049)
    public val OnPrimaryContainerDark: Color = Color(0xFFBFE3D6)
    public val SecondaryDark: Color = Color(0xFFA9C7F0)
    public val OnSecondaryDark: Color = Color(0xFF0E2E52)
    public val SecondaryContainerDark: Color = Color(0xFF2A4A72)
    public val OnSecondaryContainerDark: Color = Color(0xFFD3E3F8)
    public val TertiaryDark: Color = Color(0xFFEBBE84)
    public val OnTertiaryDark: Color = Color(0xFF462A05)
    public val TertiaryContainerDark: Color = Color(0xFF5E4016)
    public val OnTertiaryContainerDark: Color = Color(0xFFF6DDB8)
    public val ErrorDark: Color = Color(0xFFFFB4A6)
    public val OnErrorDark: Color = Color(0xFF5C1408)
    public val ErrorContainerDark: Color = Color(0xFF7A2A1D)
    public val OnErrorContainerDark: Color = Color(0xFFFFDAD3)
    public val GroundDark: Color = Color(0xFF0C171B)
    public val SurfaceLowestDark: Color = Color(0xFF0A1417)
    public val SurfaceDark: Color = Color(0xFF111D21)
    public val SurfaceLowDark: Color = Color(0xFF16242A)
    public val SurfaceContainerDark: Color = Color(0xFF1A2A30)
    public val SurfaceHighDark: Color = Color(0xFF203238)
    public val SurfaceHighestDark: Color = Color(0xFF273A40)
    public val InkDark: Color = Color(0xFFE3EEEA)
    public val InkVariantDark: Color = Color(0xFFB4C8C6)
    public val InkMutedDark: Color = Color(0xFF9DB3B1)
    public val OutlineDark: Color = Color(0xFF7F9A98)
    public val InverseSurfaceDark: Color = Color(0xF0DCE8E4)
    public val InverseOnSurfaceDark: Color = Color(0xFF1C2F34)
    public val InversePrimaryDark: Color = Color(0xFF276E61)
}

private val InkRgb = Color(0xFF15303A)
private val MintRgb = Color(0xFFBEE1D7)

public fun seaGlassLightColorScheme(): ColorScheme = with(SeaGlassPalette) {
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

public fun seaGlassDarkColorScheme(): ColorScheme = with(SeaGlassPalette) {
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

public fun seaGlassLightColors(): SeaGlassColors = SeaGlassColors(
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

public fun seaGlassDarkColors(): SeaGlassColors = SeaGlassColors(
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

public val LocalSeaGlassColors: ProvidableCompositionLocal<SeaGlassColors> =
    staticCompositionLocalOf { seaGlassLightColors() }
