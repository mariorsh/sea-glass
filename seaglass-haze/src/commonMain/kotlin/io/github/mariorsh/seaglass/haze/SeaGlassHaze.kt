package io.github.mariorsh.seaglass.haze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import io.github.mariorsh.seaglass.compose.GlassBackdrop
import io.github.mariorsh.seaglass.compose.GlassLevel
import io.github.mariorsh.seaglass.compose.SeaGlassTheme

/** Capture only background siblings; never wrap both source and glass in this modifier. */
fun Modifier.seaGlassHazeSource(state: HazeState): Modifier = hazeSource(state)

/** Call inside a SeaGlassTheme. Provider support is conservative; false forces fallback. */
@Composable
fun rememberHazeBackdrop(state: HazeState, enabled: Boolean = true): GlassBackdrop {
    val background = SeaGlassTheme.colors.ground
    val available = enabled && HazeDefaults.blurEnabled()
    return remember(state, background, available) {
        object : GlassBackdrop {
            override val isBlurAvailable: Boolean = available
            override fun modifier(shape: Shape, level: GlassLevel): Modifier =
                if (!available) Modifier else Modifier.hazeEffect(state) {
                    blurEnabled = true
                    backgroundColor = background
                    blurRadius = level.blur
                    noiseFactor = 0.15f
                }
        }
    }
}
