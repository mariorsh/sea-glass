package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Escala de esquinas de M3 Expressive. */
object SeaGlassCorners {
    val None = 0.dp
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val LargeIncreased = 20.dp
    val ExtraLarge = 28.dp
    val ExtraLargeIncreased = 32.dp
    val ExtraExtraLarge = 48.dp

    val full = RoundedCornerShape(percent = 50)
}

fun seaGlassShapes(): Shapes = Shapes(
    extraSmall = RoundedCornerShape(SeaGlassCorners.ExtraSmall),
    small = RoundedCornerShape(SeaGlassCorners.Small),
    medium = RoundedCornerShape(SeaGlassCorners.Medium),
    large = RoundedCornerShape(SeaGlassCorners.Large),
    extraLarge = RoundedCornerShape(SeaGlassCorners.ExtraLarge),
)

/** Rejilla de 4 dp. */
object SeaGlassSpacing {
    val S1 = 4.dp
    val S2 = 8.dp
    val S3 = 12.dp
    val S4 = 16.dp
    val S5 = 20.dp
    val S6 = 24.dp
    val S7 = 28.dp
    val S8 = 32.dp
    val S12 = 48.dp
    val S16 = 64.dp
}

/**
 * Muelles de M3 Expressive. *Spatial* (posición, tamaño, forma) rebota;
 * *effects* (color, opacidad) nunca rebota.
 */
object SeaGlassMotion {
    fun <T> spatialFast(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.6f, stiffness = 800f)
    fun <T> spatialDefault(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.8f, stiffness = 380f)
    fun <T> spatialSlow(): FiniteAnimationSpec<T> = spring(dampingRatio = 0.8f, stiffness = 200f)
    fun <T> effectsFast(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 3800f)
    fun <T> effectsDefault(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 1600f)
    fun <T> effectsSlow(): FiniteAnimationSpec<T> = spring(dampingRatio = 1f, stiffness = 800f)
}
