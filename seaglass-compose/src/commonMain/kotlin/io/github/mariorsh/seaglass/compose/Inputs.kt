package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// ---------------------------------------------------------------- Text field

enum class SgTextFieldVariant { Filled, Outlined }

/**
 * Campo de texto. Filled: vidrio con línea inferior que se tiñe de espuma al enfocar.
 * Outlined: borde de 1,5 dp. El error se marca con [isError] y se explica en [supportingText].
 */
@Composable
fun SgTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    variant: SgTextFieldVariant = SgTextFieldVariant.Filled,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    supportingText: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val labelSlot: @Composable () -> Unit = { Text(label) }
    val placeholderSlot: (@Composable () -> Unit)? = if (placeholder != null) {
        { Text(placeholder) }
    } else {
        null
    }
    val leading: (@Composable () -> Unit)? = if (leadingIcon != null) {
        { Icon(leadingIcon, null) }
    } else {
        null
    }
    val support: (@Composable () -> Unit)? = if (supportingText != null) {
        { Text(supportingText) }
    } else {
        null
    }
    when (variant) {
        SgTextFieldVariant.Filled -> TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            label = labelSlot,
            placeholder = placeholderSlot,
            leadingIcon = leading,
            trailingIcon = trailingIcon,
            supportingText = support,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            shape = RoundedCornerShape(
                topStart = SeaGlassCorners.Large,
                topEnd = SeaGlassCorners.Large,
                bottomEnd = SeaGlassCorners.ExtraSmall,
                bottomStart = SeaGlassCorners.ExtraSmall,
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = s.onSurface,
                unfocusedTextColor = s.onSurface,
                focusedContainerColor = c.primaryContainerGlass,
                unfocusedContainerColor = c.glassBar,
                disabledContainerColor = c.disabledContainer,
                errorContainerColor = s.errorContainer.copy(alpha = 0.55f),
                cursorColor = s.primary,
                errorCursorColor = s.error,
                focusedIndicatorColor = s.primary,
                unfocusedIndicatorColor = s.outline,
                errorIndicatorColor = s.error,
                focusedLabelColor = s.primary,
                unfocusedLabelColor = c.inkMuted,
                errorLabelColor = s.error,
                focusedLeadingIconColor = s.onSurfaceVariant,
                unfocusedLeadingIconColor = s.onSurfaceVariant,
                focusedSupportingTextColor = c.inkMuted,
                unfocusedSupportingTextColor = c.inkMuted,
                errorSupportingTextColor = s.error,
            ),
        )

        SgTextFieldVariant.Outlined -> OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            label = labelSlot,
            placeholder = placeholderSlot,
            leadingIcon = leading,
            trailingIcon = trailingIcon,
            supportingText = support,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = s.onSurface,
                unfocusedTextColor = s.onSurface,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = s.primary,
                errorCursorColor = s.error,
                focusedBorderColor = s.primary,
                unfocusedBorderColor = s.outline,
                errorBorderColor = s.error,
                focusedLabelColor = s.primary,
                unfocusedLabelColor = c.inkMuted,
                errorLabelColor = s.error,
                focusedSupportingTextColor = c.inkMuted,
                unfocusedSupportingTextColor = c.inkMuted,
                errorSupportingTextColor = s.error,
            ),
        )
    }
}

// ---------------------------------------------------------------- Checkbox & radio

/** Casilla con forma de guijarro. Pasa `onCheckedChange = null` si la fila entera ya es clicable. */
@Composable
fun SgCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    SgTriStateCheckbox(
        state = if (checked) ToggleableState.On else ToggleableState.Off,
        onClick = onCheckedChange?.let { cb -> { cb(!checked) } },
        modifier = modifier,
        enabled = enabled,
        isError = isError,
    )
}

/** Casilla con estado indeterminado (por ejemplo, «Seleccionar todos»). */
@Composable
fun SgTriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val on = state != ToggleableState.Off
    val progress by animateFloatAsState(if (on) 1f else 0f, SeaGlassTheme.motion.spatialFast(), label = "check")
    val shape = RoundedCornerShape(7.dp)
    val toggle = if (onClick != null) {
        Modifier.triStateToggleable(state = state, onClick = onClick, enabled = enabled, role = Role.Checkbox)
    } else {
        Modifier
    }
    Box(
        modifier.then(if (onClick != null) Modifier.minimumInteractiveComponentSize() else Modifier).then(toggle),
        contentAlignment = Alignment.Center,
    ) {
        val boxModifier = when {
            !enabled -> Modifier.clip(shape).background(c.disabledContainer)
            on -> Modifier.pebble(if (isError) s.error else s.primary, shape, 1.dp)
            else -> Modifier
                .clip(shape)
                .background(if (isError) s.errorContainer.copy(alpha = 0.5f) else c.glass)
                .border(2.dp, if (isError) s.error else s.onSurfaceVariant, shape)
        }
        Box(Modifier.size(22.dp).then(boxModifier), contentAlignment = Alignment.Center) {
            val mark = if (isError) s.onError else s.onPrimary
            Canvas(Modifier.size(14.dp).scale(progress)) {
                val stroke = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                if (state == ToggleableState.Indeterminate) {
                    drawLine(mark, Offset(size.width * 0.12f, size.height / 2), Offset(size.width * 0.88f, size.height / 2), stroke.width, StrokeCap.Round)
                } else {
                    val p = Path().apply {
                        moveTo(size.width * 0.10f, size.height * 0.52f)
                        lineTo(size.width * 0.40f, size.height * 0.80f)
                        lineTo(size.width * 0.90f, size.height * 0.22f)
                    }
                    drawPath(p, mark, style = stroke)
                }
            }
        }
    }
}

/** Radio de selección única. Agrupa varios en un contenedor con `Modifier.selectableGroup()`. */
@Composable
fun SgRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val dot by animateFloatAsState(if (selected) 1f else 0f, SeaGlassTheme.motion.spatialFast(), label = "radio")
    val select = if (onClick != null) {
        Modifier.minimumInteractiveComponentSize().selectable(selected = selected, onClick = onClick, enabled = enabled, role = Role.RadioButton)
    } else {
        Modifier
    }
    Box(modifier.then(select), contentAlignment = Alignment.Center) {
        val ring = when {
            !enabled -> c.disabledContent
            selected -> s.primary
            else -> s.onSurfaceVariant
        }
        Box(
            Modifier.size(22.dp).clip(CircleShape).background(c.glass).border(2.dp, ring, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(10.dp).scale(dot).clip(CircleShape).background(ring))
        }
    }
}

// ---------------------------------------------------------------- Switch

/** Interruptor de activación inmediata. Encendido es un guijarro `primary` con el pomo en `on-primary`. */
@Composable
fun SgSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showIcon: Boolean = true,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = if (checked && showIcon) {
            { Icon(Icons.Filled.Check, null, Modifier.size(SwitchDefaults.IconSize)) }
        } else {
            null
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = s.onPrimary,
            checkedTrackColor = s.primary,
            checkedBorderColor = s.primary,
            checkedIconColor = s.primary,
            uncheckedThumbColor = s.outline,
            uncheckedTrackColor = c.glass,
            uncheckedBorderColor = s.outline,
            uncheckedIconColor = c.ground,
        ),
    )
}

// ---------------------------------------------------------------- Sliders

enum class SgSliderSize(val track: Dp, val handle: Dp, val outerRadius: Dp) {
    ExtraSmall(16.dp, 44.dp, 8.dp),
    Small(24.dp, 44.dp, 8.dp),
    Medium(40.dp, 52.dp, 12.dp),
    Large(56.dp, 68.dp, 16.dp),
}

enum class SgSliderColor { Primary, Secondary }

private val TrackGap = 6.dp
private val InnerRadius = 2.dp

private fun DrawScope.drawSgSegment(color: Color, left: Float, right: Float, height: Float, startRadius: Float, endRadius: Float) {
    if (right - left <= 0f) return
    val top = (size.height - height) / 2f
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                left = left,
                top = top,
                right = right,
                bottom = top + height,
                topLeftCornerRadius = CornerRadius(startRadius),
                topRightCornerRadius = CornerRadius(endRadius),
                bottomRightCornerRadius = CornerRadius(endRadius),
                bottomLeftCornerRadius = CornerRadius(startRadius),
            ),
        )
    }
    drawPath(path, color)
}

@Composable
private fun sliderColors(color: SgSliderColor): Pair<Color, Color> {
    val s = MaterialTheme.colorScheme
    return when (color) {
        SgSliderColor.Primary -> s.primary to s.primaryContainer
        SgSliderColor.Secondary -> s.secondary to s.secondaryContainer
    }
}

@Composable
private fun SliderTrack(
    fractionStart: Float,
    fractionEnd: Float,
    size: SgSliderSize,
    active: Color,
    inactive: Color,
    insideIcon: ImageVector?,
    showStops: Boolean,
) {
    val stop = active
    Box(Modifier.fillMaxWidth().height(size.handle), contentAlignment = Alignment.CenterStart) {
        Canvas(Modifier.fillMaxWidth().height(size.handle)) {
            val gap = TrackGap.toPx()
            val h = size.track.toPx()
            val outer = size.outerRadius.toPx()
            val inner = InnerRadius.toPx()
            val w = this.size.width
            val xs = fractionStart * w
            val xe = fractionEnd * w
            if (fractionStart > 0f) drawSgSegment(inactive, 0f, xs - gap, h, outer, inner)
            drawSgSegment(active, if (fractionStart > 0f) xs + gap else 0f, xe - gap, h, if (fractionStart > 0f) inner else outer, inner)
            drawSgSegment(inactive, xe + gap, w, h, inner, outer)
            if (showStops) {
                val r = 2.dp.toPx()
                if (fractionEnd < 0.97f) drawCircle(stop, r, Offset(w - outer, this.size.height / 2))
                if (fractionStart > 0.03f) drawCircle(stop, r, Offset(outer, this.size.height / 2))
            }
        }
        if (insideIcon != null && fractionEnd > 0.18f) {
            Icon(
                insideIcon,
                null,
                tint = MaterialTheme.colorScheme.contentColorForActive(active),
                modifier = Modifier.padding(start = 10.dp).size(if (size.track >= 40.dp) 24.dp else 18.dp),
            )
        }
    }
}

@Composable
private fun androidx.compose.material3.ColorScheme.contentColorForActive(active: Color): Color =
    if (active == secondary) onSecondary else onPrimary

@Composable
private fun SliderHandle(
    size: SgSliderSize,
    color: Color,
    interactionSource: MutableInteractionSource,
    label: String?,
) {
    val dragged by interactionSource.collectIsDraggedAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val width by animateFloatAsState(if (dragged || pressed) 2f else 4f, SeaGlassTheme.motion.spatialFast(), label = "handle")
    Box(contentAlignment = Alignment.Center) {
        Box(Modifier.size(width.dp, size.handle).clip(RoundedCornerShape(2.dp)).background(color))
        if (label != null && (dragged || pressed)) {
            Box(
                Modifier
                    .offset(y = -(size.handle / 2 + 22.dp))
                    .requiredSize(44.dp, 30.dp)
                    .clip(SeaGlassCorners.full)
                    .background(MaterialTheme.colorScheme.inverseSurface),
                contentAlignment = Alignment.Center,
            ) {
                Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.inverseOnSurface)
            }
        }
    }
}

/**
 * Slider de Expressive: pista gruesa partida por un asa vertical de 4 dp con aire a cada lado.
 * [showValueLabel] muestra el valor encima del asa mientras se arrastra.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    size: SgSliderSize = SgSliderSize.ExtraSmall,
    color: SgSliderColor = SgSliderColor.Primary,
    insideIcon: ImageVector? = null,
    showValueLabel: Boolean = false,
    valueLabel: (Float) -> String = { it.roundToInt().toString() },
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val (active, inactive) = sliderColors(color)
    val source = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = source,
        steps = steps,
        valueRange = valueRange,
        thumb = { state ->
            SliderHandle(size, active, source, if (showValueLabel) valueLabel(state.value) else null)
        },
        track = { state ->
            val span = state.valueRange.endInclusive - state.valueRange.start
            val f = if (span == 0f) 0f else ((state.value - state.valueRange.start) / span).coerceIn(0f, 1f)
            SliderTrack(0f, f, size, active, inactive, insideIcon, showStops = true)
        },
    )
}

/** Slider de rango con dos asas. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    size: SgSliderSize = SgSliderSize.ExtraSmall,
    color: SgSliderColor = SgSliderColor.Secondary,
    enabled: Boolean = true,
) {
    val (active, inactive) = sliderColors(color)
    val startSource = remember { MutableInteractionSource() }
    val endSource = remember { MutableInteractionSource() }
    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        startInteractionSource = startSource,
        endInteractionSource = endSource,
        startThumb = { SliderHandle(size, active, startSource, null) },
        endThumb = { SliderHandle(size, active, endSource, null) },
        track = { state ->
            val span = state.valueRange.endInclusive - state.valueRange.start
            val fs = if (span == 0f) 0f else ((state.activeRangeStart - state.valueRange.start) / span).coerceIn(0f, 1f)
            val fe = if (span == 0f) 0f else ((state.activeRangeEnd - state.valueRange.start) / span).coerceIn(0f, 1f)
            SliderTrack(fs, fe, size, active, inactive, null, showStops = true)
        },
    )
}
