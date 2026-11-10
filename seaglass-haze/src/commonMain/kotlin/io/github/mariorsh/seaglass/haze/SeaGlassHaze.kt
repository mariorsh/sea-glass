package io.github.mariorsh.seaglass.haze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.blur.HazeBlurDefaults
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.hazeBlur
import io.github.mariorsh.seaglass.compose.GlassBackdrop
import io.github.mariorsh.seaglass.compose.GlassLevel
import io.github.mariorsh.seaglass.compose.SeaGlassTheme

/** Capture only background siblings; never wrap both source and glass in this modifier. */
public fun Modifier.seaGlassHazeSource(state: HazeState): Modifier = hazeSource(state)

/** Call inside a SeaGlassTheme. Provider support is conservative; false forces fallback. */
@Composable
public fun rememberHazeBackdrop(state: HazeState, enabled: Boolean = true): GlassBackdrop {
    val background = SeaGlassTheme.colors.ground
    val available = enabled && HazeBlurDefaults.isBlurEnabledByDefault()
    return remember(state, background, available) {
        object : GlassBackdrop {
            override val isBlurAvailable: Boolean = available
            override fun modifier(shape: Shape, level: GlassLevel): Modifier =
                if (!available) Modifier else Modifier.hazeBlur(
                    input = HazeInput.Sources(state),
                    style = HazeBlurStyle {
                        blurEnabled(true)
                        backgroundColor(background)
                        blurRadius(level.blur)
                        noiseFactor(0.15f)
                    },
                )
        }
    }
}
