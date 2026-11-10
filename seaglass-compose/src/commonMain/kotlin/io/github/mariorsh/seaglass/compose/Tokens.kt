package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Escala de esquinas de M3 Expressive. */
public object SeaGlassCorners {
    public val None: Dp = 0.dp
    public val ExtraSmall: Dp = 4.dp
    public val Small: Dp = 8.dp
    public val Medium: Dp = 12.dp
    public val Large: Dp = 16.dp
    public val LargeIncreased: Dp = 20.dp
    public val ExtraLarge: Dp = 28.dp
    public val ExtraLargeIncreased: Dp = 32.dp
    public val ExtraExtraLarge: Dp = 48.dp

    public val full: RoundedCornerShape = RoundedCornerShape(percent = 50)
}

public fun seaGlassShapes(): Shapes = Shapes(
    extraSmall = RoundedCornerShape(SeaGlassCorners.ExtraSmall),
    small = RoundedCornerShape(SeaGlassCorners.Small),
    medium = RoundedCornerShape(SeaGlassCorners.Medium),
    large = RoundedCornerShape(SeaGlassCorners.Large),
    extraLarge = RoundedCornerShape(SeaGlassCorners.ExtraLarge),
)

/** Rejilla de 4 dp. */
public object SeaGlassSpacing {
    public val S1: Dp = 4.dp
    public val S2: Dp = 8.dp
    public val S3: Dp = 12.dp
    public val S4: Dp = 16.dp
    public val S5: Dp = 20.dp
    public val S6: Dp = 24.dp
    public val S7: Dp = 28.dp
    public val S8: Dp = 32.dp
    public val S12: Dp = 48.dp
    public val S16: Dp = 64.dp
}

/**
 * Muelles de M3 Expressive. *Spatial* (posición, tamaño, forma) rebota;
 * *effects* (color, opacidad) nunca rebota.
 */
public object SeaGlassMotion {
    public fun <T> spatialFast(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.6f, stiffness = 800f)
    public fun <T> spatialDefault(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.8f, stiffness = 380f)
    public fun <T> spatialSlow(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.8f, stiffness = 200f)
    public fun <T> effectsFast(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 3800f)
    public fun <T> effectsDefault(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 1600f)
    public fun <T> effectsSlow(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 800f)
}
