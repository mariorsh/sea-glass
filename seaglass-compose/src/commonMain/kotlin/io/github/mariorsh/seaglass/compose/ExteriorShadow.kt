package io.github.mariorsh.seaglass.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.layer.setOutline
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

/**
 * An independent shadow caster prevents Android's elevation renderer from using
 * descendant bounds to occlude a translucent surface's shadow. Its interior is
 * excluded so no shadow pixels show through the glass; content is drawn normally.
 */
internal fun Modifier.exteriorShadow(elevation: Dp, shape: Shape, color: Color): Modifier = composed {
    val caster = rememberGraphicsLayer()
    this.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        val interior = Path().apply {
            when (outline) {
                is Outline.Rectangle -> addRect(outline.rect)
                is Outline.Rounded -> addRoundRect(outline.roundRect)
                is Outline.Generic -> addPath(outline.path)
            }
        }
        caster.record(this, layoutDirection, IntSize(size.width.roundToInt(), size.height.roundToInt())) {
            drawOutline(outline, Color.Black)
        }
        caster.setOutline(outline)
        caster.shadowElevation = elevation.toPx()
        caster.ambientShadowColor = color
        caster.spotShadowColor = color
        onDrawBehind {
            clipPath(interior, ClipOp.Difference) { drawLayer(caster) }
        }
    }
}
