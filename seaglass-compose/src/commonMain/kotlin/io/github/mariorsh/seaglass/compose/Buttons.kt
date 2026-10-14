package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Énfasis de un botón. */
enum class SgButtonStyle { Filled, Tonal, Elevated, Outlined, Text, Danger }

/** Tamaños de botón de M3 Expressive (XS–XL). [squareRadius] es la esquina de la forma cuadrada y del estado pulsado. */
enum class SgButtonSize(
    val height: Dp,
    val horizontalPadding: Dp,
    val fontSize: TextUnit,
    val iconSize: Dp,
    val gap: Dp,
    val squareRadius: Dp,
) {
    ExtraSmall(32.dp, 12.dp, 13.sp, 18.dp, 6.dp, 12.dp),
    Small(40.dp, 16.dp, 14.sp, 20.dp, 8.dp, 12.dp),
    Medium(56.dp, 24.dp, 16.sp, 24.dp, 8.dp, 16.dp),
    Large(96.dp, 48.dp, 24.sp, 32.dp, 12.dp, 28.dp),
    ExtraLarge(136.dp, 64.dp, 32.sp, 40.dp, 16.dp, 28.dp),
}

internal enum class Fill { Pebble, Frost, Glass, Outline, None }

internal val LocalInButtonGroup = compositionLocalOf { false }

/**
 * Superficie común a botones: aplica el relleno (guijarro, escarcha, vidrio, borde),
 * el shape morph al pulsar y los colores de contenido.
 */
@Composable
internal fun ButtonSurface(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    height: Dp,
    horizontalPadding: Dp,
    gap: Dp,
    restRadius: Dp,
    pressedRadius: Dp,
    fill: Fill,
    containerColor: Color,
    contentColor: Color,
    textStyle: TextStyle,
    interactionSource: MutableInteractionSource?,
    fixedWidth: Dp? = null,
    role: Role = Role.Button,
    toggleValue: Boolean? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val radius by animateDpAsState(
        targetValue = if (pressed && enabled && !SeaGlassTheme.motion.reduced) pressedRadius else restRadius,
        animationSpec = SeaGlassTheme.motion.spatialFast(),
        label = "sg-button-radius",
    )
    val shape = RoundedCornerShape(radius)
    val c = SeaGlassTheme.colors
    val surface = when {
        !enabled && fill == Fill.Outline -> Modifier.clip(shape).border(1.5.dp, c.disabledContainer, shape)
        !enabled && fill == Fill.None -> Modifier.clip(shape)
        !enabled -> Modifier.clip(shape).background(c.disabledContainer)
        fill == Fill.Pebble && pressed -> Modifier.pressedPebble(containerColor, shape)
        fill == Fill.Pebble -> Modifier.pebble(containerColor, shape, elevation = 3.dp)
        fill == Fill.Frost -> Modifier.frost(containerColor, shape)
        fill == Fill.Glass -> Modifier.glass(shape, GlassLevel.Level2)
        fill == Fill.Outline -> Modifier.clip(shape).border(1.5.dp, MaterialTheme.colorScheme.outline, shape)
        else -> Modifier.clip(shape)
    }
    val interaction = if (toggleValue != null) {
        Modifier.toggleable(
            value = toggleValue,
            interactionSource = source,
            indication = LocalIndication.current,
            enabled = enabled,
            role = role,
            onValueChange = { onClick() },
        )
    } else {
        Modifier.clickable(
            interactionSource = source,
            indication = LocalIndication.current,
            enabled = enabled,
            role = role,
            onClick = onClick,
        )
    }
    val fg = if (enabled) contentColor else c.disabledContent
    Box(modifier.then(interaction).defaultMinSize(minWidth = 48.dp, minHeight = 48.dp), contentAlignment = Alignment.Center) {
    Row(
        modifier = Modifier
            .then(if (fixedWidth != null) Modifier.width(fixedWidth) else Modifier.defaultMinSize(minWidth = height))
            .heightIn(min = height)
            .then(surface)
            .then(if (enabled && (focused || hovered)) Modifier.border(if (focused) 2.dp else 1.dp, if (focused) c.focusRing else c.primaryHover, shape) else Modifier)
            .padding(horizontal = horizontalPadding, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides fg, LocalTextStyle provides textStyle) {
            content()
        }
    }
    }
}

@Composable
private fun styleColors(style: SgButtonStyle): Triple<Fill, Color, Color> {
    val s = MaterialTheme.colorScheme
    return when (style) {
        SgButtonStyle.Filled -> Triple(Fill.Pebble, s.primary, s.onPrimary)
        SgButtonStyle.Tonal -> Triple(Fill.Frost, s.primaryContainer, s.onPrimaryContainer)
        SgButtonStyle.Elevated -> Triple(Fill.Glass, Color.Transparent, s.primary)
        SgButtonStyle.Outlined -> Triple(Fill.Outline, Color.Transparent, s.primary)
        SgButtonStyle.Text -> Triple(Fill.None, Color.Transparent, s.primary)
        SgButtonStyle.Danger -> Triple(Fill.Pebble, s.error, s.onError)
    }
}

@Composable
private fun buttonTextStyle(size: SgButtonSize): TextStyle =
    MaterialTheme.typography.labelLarge.copy(fontSize = size.fontSize, lineHeight = size.fontSize * 1.25f)

/**
 * Botón de Sea Glass. Una sola acción [SgButtonStyle.Filled] por pantalla.
 * Al pulsarlo, la forma redonda se cuadra con el muelle spatial fast (shape morph de Expressive).
 */
@Composable
fun SgButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SgButtonStyle = SgButtonStyle.Filled,
    size: SgButtonSize = SgButtonSize.Small,
    square: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val (fill, container, contentColor) = styleColors(style)
    ButtonSurface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        height = size.height,
        horizontalPadding = if (style == SgButtonStyle.Text) size.horizontalPadding * 0.75f else size.horizontalPadding,
        gap = size.gap,
        restRadius = if (square) size.squareRadius else size.height / 2,
        pressedRadius = size.squareRadius,
        fill = fill,
        containerColor = container,
        contentColor = contentColor,
        textStyle = buttonTextStyle(size),
        interactionSource = interactionSource,
    ) {
        if (icon != null) Icon(icon, contentDescription = null, modifier = Modifier.size(size.iconSize))
        content()
    }
}

/** Atajo con texto. */
@Composable
fun SgButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SgButtonStyle = SgButtonStyle.Filled,
    size: SgButtonSize = SgButtonSize.Small,
    square: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) = SgButton(onClick, modifier, style, size, square, enabled, icon) { Text(text) }

/** Toggle button de Expressive: redondo en reposo, cuadrado y `primary` al seleccionarlo. */
@Composable
fun SgToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    checkedIcon: ImageVector? = icon,
    size: SgButtonSize = SgButtonSize.Small,
    enabled: Boolean = true,
) {
    val s = MaterialTheme.colorScheme
    ButtonSurface(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        height = size.height,
        horizontalPadding = size.horizontalPadding,
        gap = size.gap,
        restRadius = if (checked) size.squareRadius else size.height / 2,
        pressedRadius = size.squareRadius,
        fill = if (checked) Fill.Pebble else Fill.Frost,
        containerColor = if (checked) s.primary else s.surfaceContainerHigh,
        contentColor = if (checked) s.onPrimary else s.onSurface,
        textStyle = buttonTextStyle(size),
        interactionSource = null,
        role = Role.Switch,
        toggleValue = checked,
    ) {
        val shown = if (checked) checkedIcon else icon
        if (shown != null) Icon(shown, contentDescription = null, modifier = Modifier.size(size.iconSize))
        Text(text)
    }
}

enum class SgIconButtonStyle { Standard, Filled, Tonal, Outlined }

enum class SgIconButtonSize(val box: Dp, val iconSize: Dp, val squareRadius: Dp) {
    ExtraSmall(32.dp, 20.dp, 10.dp),
    Small(40.dp, 22.dp, 12.dp),
    Medium(56.dp, 24.dp, 16.dp),
    Large(96.dp, 32.dp, 28.dp),
}

enum class SgIconButtonWidth(internal val factor: Float) { Narrow(0.8f), Default(1f), Wide(1.3f) }

/** Botón de solo icono. [contentDescription] es obligatorio salvo que el icono sea decorativo. */
@Composable
fun SgIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: SgIconButtonStyle = SgIconButtonStyle.Standard,
    size: SgIconButtonSize = SgIconButtonSize.Small,
    width: SgIconButtonWidth = SgIconButtonWidth.Default,
    enabled: Boolean = true,
    contentColor: Color? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val s = MaterialTheme.colorScheme
    val (fill, container, fg) = when (style) {
        SgIconButtonStyle.Standard -> Triple(Fill.None, Color.Transparent, s.onSurfaceVariant)
        SgIconButtonStyle.Filled -> Triple(Fill.Pebble, s.primary, s.onPrimary)
        SgIconButtonStyle.Tonal -> Triple(Fill.Frost, s.secondaryContainer, s.onSecondaryContainer)
        SgIconButtonStyle.Outlined -> Triple(Fill.Outline, Color.Transparent, s.onSurfaceVariant)
    }
    ButtonSurface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        height = size.box,
        horizontalPadding = 0.dp,
        gap = 0.dp,
        restRadius = size.box / 2,
        pressedRadius = size.squareRadius,
        fill = fill,
        containerColor = container,
        contentColor = contentColor ?: fg,
        textStyle = LocalTextStyle.current,
        interactionSource = interactionSource,
        fixedWidth = size.box * width.factor,
    ) {
        Icon(icon, contentDescription, Modifier.size(size.iconSize))
    }
}

/** Icon button con estado: seleccionado pasa a `tertiary` y se cuadra. */
@Composable
fun SgIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    checkedIcon: ImageVector = icon,
    size: SgIconButtonSize = SgIconButtonSize.Small,
    enabled: Boolean = true,
) {
    val s = MaterialTheme.colorScheme
    ButtonSurface(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        height = size.box,
        horizontalPadding = 0.dp,
        gap = 0.dp,
        restRadius = if (checked) size.squareRadius else size.box / 2,
        pressedRadius = size.squareRadius,
        fill = if (checked) Fill.Pebble else Fill.Frost,
        containerColor = if (checked) s.tertiary else s.surfaceContainerHigh,
        contentColor = if (checked) s.onTertiary else s.onSurfaceVariant,
        textStyle = LocalTextStyle.current,
        interactionSource = null,
        fixedWidth = size.box,
        role = Role.Switch,
        toggleValue = checked,
    ) {
        Icon(if (checked) checkedIcon else icon, contentDescription, Modifier.size(size.iconSize))
    }
}

/** Grupo estándar de Expressive: 8 dp entre botones; morphing sin crecimiento horizontal para respetar las restricciones. */
@Composable
fun SgButtonGroup(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        CompositionLocalProvider(LocalInButtonGroup provides true) { content() }
    }
}

@Composable
private fun animatedSegmentShape(outerStart: Boolean, outerEnd: Boolean, selected: Boolean, full: Dp, inner: Dp): Shape {
    val ts by animateDpAsState(if (selected || outerStart) full else inner, SeaGlassTheme.motion.spatialFast(), label = "ts")
    val te by animateDpAsState(if (selected || outerEnd) full else inner, SeaGlassTheme.motion.spatialFast(), label = "te")
    return RoundedCornerShape(topStart = ts, bottomStart = ts, topEnd = te, bottomEnd = te)
}

/**
 * Connected button group de Expressive (sustituye a segmented buttons): selección única.
 * Los exteriores son píldora, los interiores `corner-s`; el seleccionado pasa a `secondary`.
 */
@Composable
fun SgConnectedButtonGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
) {
    ConnectedRow(options, { it == selectedIndex }, modifier, height, single = true) { onSelect(it) }
}

/** Connected button group con selección múltiple. */
@Composable
fun SgConnectedButtonGroupMulti(
    options: List<String>,
    selected: Set<Int>,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
) {
    ConnectedRow(options, { it in selected }, modifier, height, single = false, onClick = onToggle)
}

@Composable
private fun ConnectedRow(
    options: List<String>,
    isSelected: (Int) -> Boolean,
    modifier: Modifier,
    height: Dp,
    single: Boolean,
    onClick: (Int) -> Unit,
) {
    val s = MaterialTheme.colorScheme
    Row(
        modifier.then(if (single) Modifier.selectableGroup() else Modifier),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEachIndexed { i, label ->
            val sel = isSelected(i)
            val shape = animatedSegmentShape(i == 0, i == options.lastIndex, sel, height / 2, 8.dp)
            val select = if (single) {
                Modifier.selectable(selected = sel, role = Role.RadioButton, onClick = { onClick(i) })
            } else {
                Modifier.toggleable(value = sel, role = Role.Checkbox, onValueChange = { onClick(i) })
            }
            Row(
                Modifier
                    .weight(1f).heightIn(min = maxOf(height, 48.dp))
                    .then(if (sel) Modifier.pebble(s.secondary, shape, 2.dp) else Modifier.frost(s.surfaceContainerHigh, shape))
                    .then(select)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (sel) Icon(Icons.Filled.Check, null, Modifier.size(18.dp), tint = s.onSecondary)
                Text(label, style = MaterialTheme.typography.labelLarge, color = if (sel) s.onSecondary else s.onSurface)
            }
        }
    }
}

/** Segmented buttons de M3 base. En diseños nuevos, usa [SgConnectedButtonGroup]. */
@Composable
fun SgSegmentedButtons(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val s = MaterialTheme.colorScheme
    val shape = SeaGlassCorners.full
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .clip(shape)
            .border(1.5.dp, s.outline, shape)
            .selectableGroup(),
    ) {
        options.forEachIndexed { i, label ->
            val sel = i == selectedIndex
            Row(
                Modifier
                    .weight(1f).heightIn(min = 48.dp)
                    .background(if (sel) s.secondaryContainer else Color.Transparent)
                    .selectable(selected = sel, role = Role.RadioButton, onClick = { onSelect(i) })
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (sel) Icon(Icons.Filled.Check, null, Modifier.size(18.dp), tint = s.onSecondaryContainer)
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (sel) s.onSecondaryContainer else s.onSurface,
                )
            }
            if (i < options.lastIndex) Box(Modifier.width(1.5.dp).fillMaxHeight().background(s.outline))
        }
    }
}

/** Split button de Expressive: acción por defecto + disparador de menú. */
@Composable
fun SgSplitButton(
    text: String,
    onClick: () -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: SgButtonStyle = SgButtonStyle.Filled,
    icon: ImageVector? = null,
    menuContentDescription: String = SeaGlassTheme.strings.moreOptions,
    enabled: Boolean = true,
) {
    val (fill, container, fg) = styleColors(style)
    val s = MaterialTheme.colorScheme
    val rotation by animateFloatAsState(if (expanded) 180f else 0f, SeaGlassTheme.motion.spatialFast(), label = "chevron")
    val trailingRadius by animateDpAsState(if (expanded) 24.dp else 4.dp, SeaGlassTheme.motion.spatialFast(), label = "split")
    val trailingWidth by animateDpAsState(if (expanded) 48.dp else 44.dp, SeaGlassTheme.motion.spatialFast(), label = "splitW")
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        ButtonSurface(
            onClick = onClick,
            modifier = Modifier,
            enabled = enabled,
            height = 48.dp,
            horizontalPadding = 18.dp,
            gap = 8.dp,
            restRadius = 24.dp,
            pressedRadius = 12.dp,
            fill = fill,
            containerColor = container,
            contentColor = fg,
            textStyle = MaterialTheme.typography.labelLarge,
            interactionSource = null,
        ) {
            if (icon != null) Icon(icon, null, Modifier.size(20.dp))
            Text(text)
        }
        val shape = RoundedCornerShape(topStart = trailingRadius, bottomStart = trailingRadius, topEnd = 24.dp, bottomEnd = 24.dp)
        val trailingFill = if (expanded) Fill.Pebble else fill
        Box(
            Modifier
                .width(trailingWidth)
                .height(48.dp)
                .then(
                    when {
                        !enabled -> Modifier.clip(shape).background(SeaGlassTheme.colors.disabledContainer)
                        trailingFill == Fill.Pebble -> Modifier.pebble(if (expanded) s.primary else container, shape, 3.dp)
                        trailingFill == Fill.Frost -> Modifier.frost(container, shape)
                        trailingFill == Fill.Glass -> Modifier.glass(shape, GlassLevel.Level2)
                        trailingFill == Fill.Outline -> Modifier.clip(shape).border(1.5.dp, s.outline, shape)
                        else -> Modifier.clip(shape)
                    },
                )
                .toggleable(value = expanded, enabled = enabled, role = Role.Button, onValueChange = onExpandedChange),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = menuContentDescription,
                tint = if (expanded) s.onPrimary else fg,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}
