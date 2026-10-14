package io.github.mariorsh.seaglass.compose

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

/**
 * Forma polar de la shape library de Expressive: un círculo con [lobes] ondulaciones de
 * amplitud [amplitude] (fracción del radio). [sharpness] < 1 afila los lóbulos.
 * Con amplitude = 0 es un círculo, lo que permite animar entre formas pasando por él.
 */
class SgPolarShape(
    private val lobes: Int,
    private val amplitude: Float,
    private val sharpness: Float = 1f,
    private val rotationDegrees: Float = 0f,
    private val points: Int = 120,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val base = min(cx, cy) / (1f + abs(amplitude))
        val rot = rotationDegrees / 180f * PI.toFloat()
        val path = Path()
        for (i in 0..points) {
            val t = (i.toFloat() / points) * 2f * PI.toFloat()
            val wave = cos(lobes * t)
            val shaped = sign(wave) * abs(wave).pow(sharpness)
            val r = base * (1f + amplitude * shaped)
            val x = cx + r * cos(t + rot)
            val y = cy + r * sin(t + rot)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return Outline.Generic(path)
    }

    override fun equals(other: Any?): Boolean =
        other is SgPolarShape && other.lobes == lobes && other.amplitude == amplitude &&
            other.sharpness == sharpness && other.rotationDegrees == rotationDegrees

    override fun hashCode(): Int = ((lobes * 31 + amplitude.hashCode()) * 31 + sharpness.hashCode()) * 31 + rotationDegrees.hashCode()
}

/** Formas de la shape library de M3 Expressive, listas para `clip`, `background` o `border`. */
object SeaGlassShapes {
    val Circle: Shape = RoundedCornerShape(percent = 50)
    val Pill: Shape = RoundedCornerShape(percent = 50)
    val Cookie9: Shape = SgPolarShape(lobes = 9, amplitude = 0.09f)
    val Cookie4: Shape = SgPolarShape(lobes = 4, amplitude = 0.12f)
    val Clover4: Shape = SgPolarShape(lobes = 4, amplitude = 0.26f, sharpness = 0.6f, rotationDegrees = 45f)
    val Sunny: Shape = SgPolarShape(lobes = 8, amplitude = 0.09f)
    val Flower8: Shape = SgPolarShape(lobes = 8, amplitude = 0.2f, sharpness = 0.7f)
    val SoftBurst: Shape = SgPolarShape(lobes = 12, amplitude = 0.12f, sharpness = 0.5f)

    /** Arco: semicírculo arriba, base recta. */
    val Arch: Shape = GenericShape { size, _ ->
        val r = size.width / 2f
        moveTo(0f, size.height)
        lineTo(0f, r)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.width),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false,
        )
        lineTo(size.width, size.height)
        close()
    }

    /** Gema hexagonal. */
    val Gem: Shape = GenericShape { size, _ ->
        moveTo(size.width / 2f, 0f)
        lineTo(size.width, size.height * 0.26f)
        lineTo(size.width, size.height * 0.74f)
        lineTo(size.width / 2f, size.height)
        lineTo(0f, size.height * 0.74f)
        lineTo(0f, size.height * 0.26f)
        close()
    }

    val all: List<Pair<String, Shape>> = listOf(
        "Cookie 9" to Cookie9,
        "Trébol" to Clover4,
        "Sunny" to Sunny,
        "Flor" to Flower8,
        "Burst" to SoftBurst,
        "Arco" to Arch,
        "Gema" to Gem,
        "Píldora" to Pill,
    )
}
