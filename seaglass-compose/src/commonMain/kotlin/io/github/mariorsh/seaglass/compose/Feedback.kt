package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

// ---------------------------------------------------------------- Progress

private val Wavelength = 20.dp
private val WaveAmplitude = 3.dp

/** Aplana la ola cerca del 0 % y del 100 % para que el inicio y el final se lean en calma. */
private fun waveFlatten(p: Float): Float = when {
    p < 0.1f -> p / 0.1f
    p > 0.9f -> (1f - p) / 0.1f
    else -> 1f
}

/**
 * Progreso lineal determinado. [wavy] = true dibuja la ola de Expressive en la parte activa.
 * [progress] es una lambda para no recomponer en cada frame.
 */
@Composable
fun SgLinearProgress(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    thick: Boolean = false,
    wavy: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val reduced = SeaGlassTheme.motion.reduced
    val phase: State<Float> = if (wavy && !reduced) {
        rememberInfiniteTransition(label = "wave").animateFloat(0f, 1f, infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "phase")
    } else remember { mutableStateOf(0f) }
    val wavePath = remember { Path() }
    Canvas(
        modifier
            .fillMaxWidth()
            .height(if (wavy) 14.dp else if (thick) 8.dp else 4.dp)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(progress().let { if (it.isNaN()) 0f else it.coerceIn(0f, 1f) }, 0f..1f) },
    ) {
        val p = progress().let { if (it.isNaN()) 0f else it.coerceIn(0f, 1f) }
        val h = (if (thick) 8.dp else 4.dp).toPx()
        val gap = 4.dp.toPx()
        val cy = size.height / 2f
        val end = p * size.width
        val activeColor = color
        if (end > h / 2) {
            if (wavy && !reduced) {
                drawWave(activeColor, start = h / 2, end = end - h / 2, cy = cy, amp = WaveAmplitude.toPx() * waveFlatten(p), phase = phase.value, stroke = h, path = wavePath)
            } else {
                drawLine(activeColor, Offset(h / 2, cy), Offset(max(h / 2, end - h / 2), cy), h, StrokeCap.Round)
            }
        }
        val trackStart = end + gap + h / 2
        if (trackStart < size.width - h / 2) {
            drawLine(activeColor.copy(alpha = 0.22f), Offset(trackStart, cy), Offset(size.width - h / 2, cy), h, StrokeCap.Round)
            drawCircle(activeColor, radius = min(h / 2, 2.dp.toPx()), center = Offset(size.width - h / 2, cy))
        }
    }
}

private fun DrawScope.drawWave(color: Color, start: Float, end: Float, cy: Float, amp: Float, phase: Float, stroke: Float, path: Path) {
    val wl = Wavelength.toPx()
    path.reset()
    var x = start
    val step = 2f
    var first = true
    while (x <= end) {
        val y = cy + amp * sin(((x / wl) - phase) * 2f * PI.toFloat())
        if (first) {
            path.moveTo(x, y)
            first = false
        } else {
            path.lineTo(x, y)
        }
        x += step
    }
    drawPath(path, color, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

/** Progreso lineal indeterminado: un tramo recorre la pista. */
@Composable
fun SgLinearProgressIndeterminate(
    modifier: Modifier = Modifier,
    thick: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val loadingLabel = SeaGlassTheme.strings.loading
    val head: State<Float> = if (SeaGlassTheme.motion.reduced) remember { mutableStateOf(0.65f) }
        else rememberInfiniteTransition(label = "indeterminate").animateFloat(0f, 1.3f, infiniteRepeatable(tween(1800, easing = LinearEasing)), label = "head")
    Canvas(
        modifier
            .fillMaxWidth()
            .height(if (thick) 8.dp else 4.dp)
            .semantics { contentDescription = loadingLabel; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
    ) {
        val h = size.height
        val cy = h / 2
        drawLine(color.copy(alpha = 0.22f), Offset(h / 2, cy), Offset(size.width - h / 2, cy), h, StrokeCap.Round)
        val endX = (head.value * size.width).coerceIn(0f, size.width)
        val startX = ((head.value - 0.35f) * size.width).coerceIn(0f, size.width)
        if (endX - startX > h) {
            drawLine(color, Offset(startX + h / 2, cy), Offset(endX - h / 2, cy), h, StrokeCap.Round)
        }
    }
}

/** Progreso circular determinado. [wavy] dibuja el borde ondulado de Expressive. */
@Composable
fun SgCircularProgress(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    thick: Boolean = false,
    wavy: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val path = remember { Path() }
    val reduced = SeaGlassTheme.motion.reduced
    Canvas(
        modifier
            .size(size)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(progress().let { if (it.isNaN()) 0f else it.coerceIn(0f, 1f) }, 0f..1f) },
    ) {
        val p = progress().let { if (it.isNaN()) 0f else it.coerceIn(0f, 1f) }
        val strokeW = (if (thick) 8.dp else 4.dp).toPx()
        val amp = if (wavy && !reduced) 1.6.dp.toPx() * waveFlatten(p) else 0f
        val r = this.size.minDimension / 2f - strokeW / 2f - amp
        val c = center
        drawCircle(color.copy(alpha = 0.22f), radius = r, center = c, style = Stroke(strokeW))
        if (p <= 0f) return@Canvas
        if (!wavy || reduced) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * p,
                useCenter = false,
                topLeft = Offset(c.x - r, c.y - r),
                size = Size(r * 2, r * 2),
                style = Stroke(strokeW, cap = StrokeCap.Round),
            )
        } else {
            path.reset()
            val steps = 120
            val start = -PI.toFloat() / 2f
            val sweep = 2f * PI.toFloat() * p
            for (i in 0..steps) {
                val a = start + sweep * i / steps
                val rr = r + amp * sin(12f * a)
                val x = c.x + rr * cos(a)
                val y = c.y + rr * sin(a)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

/**
 * Loading indicator de Expressive: un guijarro que rueda y cambia de forma recorriendo la shape
 * library. Para esperas de menos de 5 s sin progreso medible.
 */
@Composable
fun SgLoadingIndicator(
    modifier: Modifier = Modifier,
    contained: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
    contentDescription: String = SeaGlassTheme.strings.loading,
) {
    val reduced = SeaGlassTheme.motion.reduced
    val rotation: State<Float>
    val phaseState: State<Float>
    if (reduced) {
        rotation = remember { mutableStateOf(0f) }
        phaseState = remember { mutableStateOf(0f) }
    } else {
        val t = rememberInfiniteTransition(label = "loading")
        rotation = t.animateFloat(0f, 360f, infiniteRepeatable(tween(2400, easing = LinearEasing)), label = "rot")
        phaseState = t.animateFloat(0f, 4f, infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart), label = "morph")
    }
    val phase = phaseState.value
    val stage = phase.toInt().coerceIn(0, 3)
    val local = phase - stage
    val lobes = intArrayOf(9, 4, 8, 12)[stage]
    val sharp = floatArrayOf(1f, 0.6f, 0.7f, 0.5f)[stage]
    val amp = floatArrayOf(0.12f, 0.24f, 0.2f, 0.14f)[stage] * sin(PI.toFloat() * local)
    val shape = SgPolarShape(lobes = lobes, amplitude = amp, sharpness = sharp)
    val s = MaterialTheme.colorScheme
    Box(
        modifier
            .size(48.dp)
            .then(if (contained) Modifier.frost(s.primaryContainer, SeaGlassCorners.full) else Modifier)
            .semantics { this.contentDescription = contentDescription; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(if (contained) 32.dp else 38.dp)
                .graphicsLayer { rotationZ = rotation.value }
                .clip(shape)
                .background(color),
        )
    }
}

// ---------------------------------------------------------------- Snackbar

/** Snackbar en `inverse-surface`. Una sola acción como mucho. */
@Composable
fun SgSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    onDismiss: (() -> Unit)? = null,
) {
    val s = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(SeaGlassCorners.Large)
    Row(
        modifier
            .widthIn(max = 480.dp)
            .heightIn(min = 48.dp)
            .glass(shape, GlassLevel.Level3, color = s.inverseSurface)
            .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(message, style = MaterialTheme.typography.bodyMedium, color = s.inverseOnSurface, modifier = Modifier.weight(1f, fill = false))
        if (actionLabel != null) {
            SgButton(onClick = onAction, style = SgButtonStyle.Text) {
                Text(actionLabel, color = s.inversePrimary, fontWeight = FontWeight.Bold)
            }
        }
        if (onDismiss != null) {
            SgIconButton(onClick = onDismiss, icon = Icons.Filled.Close, contentDescription = SeaGlassTheme.strings.close, contentColor = s.inverseOnSurface)
        }
    }
}

/** SnackbarHost de Material 3 que pinta [SgSnackbar]. Úsalo en el `snackbarHost` del Scaffold. */
@Composable
fun SgSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState, modifier) { data ->
        SgSnackbar(
            message = data.visuals.message,
            modifier = Modifier.padding(12.dp),
            actionLabel = data.visuals.actionLabel,
            onAction = { data.performAction() },
            onDismiss = if (data.visuals.withDismissAction) {
                { data.dismiss() }
            } else {
                null
            },
        )
    }
}

// ---------------------------------------------------------------- Tooltips

@Composable
fun SgPlainTooltip(text: String, modifier: Modifier = Modifier) {
    val s = MaterialTheme.colorScheme
    Box(
        modifier
            .clip(RoundedCornerShape(SeaGlassCorners.Small))
            .background(s.inverseSurface)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall, color = s.inverseOnSurface)
    }
}

@Composable
fun SgRichTooltip(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val s = MaterialTheme.colorScheme
    Column(
        modifier
            .widthIn(max = 320.dp)
            .glass(RoundedCornerShape(SeaGlassCorners.Large), GlassLevel.Level4)
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = if (action != null) 4.dp else 14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (title != null) Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = s.onSurface)
        Text(text, style = MaterialTheme.typography.bodyMedium, color = s.onSurfaceVariant)
        if (action != null) action()
    }
}

/** Tooltip plano al mantener pulsado o al pasar el cursor por [content]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgTooltipBox(
    tooltip: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { SgPlainTooltip(tooltip) },
        state = rememberTooltipState(),
        modifier = modifier,
        content = content,
    )
}

/** Tooltip rico (persistente hasta que se toca fuera). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgRichTooltipBox(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = { SgRichTooltip(text = text, title = title, action = action) },
        state = rememberTooltipState(isPersistent = true),
        modifier = modifier,
        content = content,
    )
}

// ---------------------------------------------------------------- Menus

enum class SgMenuGroupPosition { Single, First, Middle, Last }

/** Grupo del menú vertical de Expressive. Los grupos van separados 4 dp. */
@Composable
fun SgMenuGroup(
    position: SgMenuGroupPosition = SgMenuGroupPosition.Single,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val big = SeaGlassCorners.LargeIncreased
    val small = SeaGlassCorners.ExtraSmall
    val top = if (position == SgMenuGroupPosition.Single || position == SgMenuGroupPosition.First) big else small
    val bottom = if (position == SgMenuGroupPosition.Single || position == SgMenuGroupPosition.Last) big else small
    Column(
        modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(topStart = top, topEnd = top, bottomEnd = bottom, bottomStart = bottom), GlassLevel.Level3)
            .padding(4.dp),
        content = content,
    )
}

/** Opción de menú. [selected] la pinta como píldora `secondary-container`; [destructive] en `error`. */
@Composable
fun SgMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingText: String? = null,
    selected: Boolean = false,
    destructive: Boolean = false,
    enabled: Boolean = true,
) {
    val s = MaterialTheme.colorScheme
    val fg = when {
        !enabled -> SeaGlassTheme.colors.disabledContent
        destructive -> s.error
        selected -> s.onSecondaryContainer
        else -> s.onSurface
    }
    ButtonSurface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().semantics { this.selected = selected },
        enabled = enabled,
        height = 44.dp,
        horizontalPadding = 12.dp,
        gap = 12.dp,
        restRadius = SeaGlassCorners.Medium,
        pressedRadius = SeaGlassCorners.Medium,
        fill = if (selected) Fill.Frost else Fill.None,
        containerColor = s.secondaryContainer,
        contentColor = fg,
        textStyle = if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.bodyMedium,
        interactionSource = null,
    ) {
        if (leadingIcon != null) Icon(leadingIcon, null, Modifier.size(20.dp))
        Text(text, modifier = Modifier.weight(1f))
        if (trailingText != null) {
            Text(trailingText.uppercase(), style = SeaGlassTheme.typography.captionMono, color = SeaGlassTheme.colors.inkMuted)
        }
    }
}

/**
 * Menú desplegable anclado a su contenedor padre. Pon dentro uno o más [SgMenuGroup].
 * Colócalo en un Box junto al control que lo abre.
 */
@Composable
fun SgDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return
    val gap = with(androidx.compose.ui.platform.LocalDensity.current) { 4.dp.roundToPx() }
    val provider = androidx.compose.runtime.remember(gap) { MenuPositionProvider(gap) }
    Popup(
        popupPositionProvider = provider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface, LocalGlassBackdrop provides GlassBackdrop.None) {
            Column(
                modifier.widthIn(max = 300.dp).heightIn(max = androidx.compose.ui.platform.LocalWindowInfo.current.containerSize.height.let { with(androidx.compose.ui.platform.LocalDensity.current) { it.toDp() } }).verticalScroll(rememberScrollState()).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                content = content,
            )
        }
    }
}

internal class MenuPositionProvider(private val gap: Int) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val start = if (layoutDirection == LayoutDirection.Ltr) anchorBounds.left else anchorBounds.right - popupContentSize.width
        val x = start.coerceIn(0, max(0, windowSize.width - popupContentSize.width))
        var y = anchorBounds.bottom + gap
        if (y + popupContentSize.height > windowSize.height) y = anchorBounds.top - popupContentSize.height - gap
        return IntOffset(x, y.coerceIn(0, max(0, windowSize.height - popupContentSize.height)))
    }
}
