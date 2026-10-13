package dev.seaglass.compose

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val ChipShape = RoundedCornerShape(SeaGlassCorners.Small)

@Composable
private fun ChipFrame(
    modifier: Modifier,
    shape: Shape,
    selected: Boolean,
    elevated: Boolean,
    startPadding: Dp,
    endPadding: Dp,
    interaction: Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val surface = when {
        selected -> Modifier.frost(s.primaryContainer, shape)
        elevated -> Modifier
            .shadow(1.dp, shape, clip = false, ambientColor = c.shadow, spotColor = c.shadow)
            .clip(shape)
            .background(c.glassBar)
            .border(1.dp, edgeBrush(c), shape)
        else -> Modifier.clip(shape).border(1.dp, s.outline, shape)
    }
    Row(
        modifier
            .height(32.dp)
            .then(surface)
            .then(interaction)
            .padding(start = startPadding, end = endPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides if (selected) s.onPrimaryContainer else s.onSurface) {
            content()
        }
    }
}

/** Assist chip: acción contextual con icono en `primary`. [elevated] para usarlo sobre imágenes. */
@Composable
fun SgAssistChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    elevated: Boolean = false,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    ChipFrame(
        modifier, ChipShape, selected = false, elevated = elevated,
        startPadding = if (icon != null) 8.dp else 14.dp, endPadding = 14.dp,
        interaction = Modifier.clickable(source, LocalIndication.current, enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        if (icon != null) Icon(icon, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium))
    }
}

/** Filter chip: al seleccionarlo pasa a espuma con un check delante. */
@Composable
fun SgFilterChip(
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    ChipFrame(
        modifier, ChipShape, selected = selected, elevated = false,
        startPadding = if (selected) 8.dp else 14.dp, endPadding = if (trailingIcon != null) 8.dp else 14.dp,
        interaction = Modifier.toggleable(selected, source, LocalIndication.current, enabled = enabled, role = Role.Checkbox, onValueChange = onSelectedChange),
    ) {
        if (selected) Icon(Icons.Filled.Check, null, Modifier.size(18.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium),
        )
        if (trailingIcon != null) Icon(trailingIcon, null, Modifier.size(18.dp))
    }
}

/** Input chip: un valor introducido por el usuario, con avatar opcional y botón para quitarlo. */
@Composable
fun SgInputChip(
    label: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    avatar: (@Composable () -> Unit)? = null,
    removeContentDescription: String = "Quitar $label",
) {
    ChipFrame(
        modifier, SeaGlassCorners.full, selected = false, elevated = false,
        startPadding = if (avatar != null) 4.dp else 12.dp, endPadding = 4.dp,
        interaction = Modifier,
    ) {
        if (avatar != null) avatar()
        Text(label, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium))
        Box(
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClickLabel = removeContentDescription, onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Close, removeContentDescription, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Suggestion chip: una respuesta o búsqueda propuesta, solo texto. */
@Composable
fun SgSuggestionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    ChipFrame(
        modifier, ChipShape, selected = false, elevated = false,
        startPadding = 14.dp, endPadding = 14.dp,
        interaction = Modifier.clickable(source, LocalIndication.current, enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium))
    }
}

/** Avatar circular con iniciales. */
@Composable
fun SgAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
) {
    Box(
        modifier.size(size).clip(CircleShape).background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            initials,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.labelSmall.fontSize * (size.value / 24f),
            ),
        )
    }
}
