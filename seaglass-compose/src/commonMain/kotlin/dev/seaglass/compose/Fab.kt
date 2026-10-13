package dev.seaglass.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SgFabSize(
    val box: Dp,
    val radius: Dp,
    val iconSize: Dp,
    val fontSize: TextUnit,
    val paddingStart: Dp,
    val paddingEnd: Dp,
) {
    Small(56.dp, 16.dp, 24.dp, 16.sp, 16.dp, 20.dp),
    Medium(80.dp, 20.dp, 28.dp, 22.sp, 24.dp, 28.dp),
    Large(96.dp, 28.dp, 36.dp, 26.sp, 30.dp, 36.dp),
}

enum class SgFabColor { PrimaryContainer, SecondaryContainer, TertiaryContainer, Primary }

@Composable
private fun fabColors(color: SgFabColor): Triple<Fill, Color, Color> {
    val s = MaterialTheme.colorScheme
    return when (color) {
        SgFabColor.PrimaryContainer -> Triple(Fill.Frost, s.primaryContainer, s.onPrimaryContainer)
        SgFabColor.SecondaryContainer -> Triple(Fill.Frost, s.secondaryContainer, s.onSecondaryContainer)
        SgFabColor.TertiaryContainer -> Triple(Fill.Frost, s.tertiaryContainer, s.onTertiaryContainer)
        SgFabColor.Primary -> Triple(Fill.Pebble, s.primary, s.onPrimary)
    }
}

/** Floating action button: la acción más importante de la pantalla. 56 / 80 / 96 dp. */
@Composable
fun SgFab(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: SgFabSize = SgFabSize.Small,
    color: SgFabColor = SgFabColor.PrimaryContainer,
) {
    val (fill, container, fg) = fabColors(color)
    ButtonSurface(
        onClick = onClick,
        modifier = modifier.then(
            if (fill == Fill.Frost) {
                Modifier.shadow(6.dp, RoundedCornerShape(size.radius), clip = false, ambientColor = SeaGlassTheme.colors.shadow, spotColor = SeaGlassTheme.colors.shadow)
            } else {
                Modifier
            },
        ),
        enabled = true,
        height = size.box,
        horizontalPadding = 0.dp,
        gap = 0.dp,
        restRadius = size.radius,
        pressedRadius = size.radius * 0.6f,
        fill = fill,
        containerColor = container,
        contentColor = fg,
        textStyle = MaterialTheme.typography.labelLarge,
        interactionSource = null,
        fixedWidth = size.box,
    ) {
        Icon(icon, contentDescription, Modifier.size(size.iconSize))
    }
}

/** FAB extendido con texto. Con [expanded] = false se pliega a FAB (útil al hacer scroll). */
@Composable
fun SgExtendedFab(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: SgFabSize = SgFabSize.Small,
    color: SgFabColor = SgFabColor.PrimaryContainer,
    expanded: Boolean = true,
) {
    val (fill, container, fg) = fabColors(color)
    ButtonSurface(
        onClick = onClick,
        modifier = modifier.then(
            if (fill == Fill.Frost) {
                Modifier.shadow(6.dp, RoundedCornerShape(size.radius), clip = false, ambientColor = SeaGlassTheme.colors.shadow, spotColor = SeaGlassTheme.colors.shadow)
            } else {
                Modifier
            },
        ),
        enabled = true,
        height = size.box,
        horizontalPadding = if (expanded) size.paddingStart else (size.box - size.iconSize) / 2,
        gap = if (expanded) size.paddingStart / 2 else 0.dp,
        restRadius = size.radius,
        pressedRadius = size.radius * 0.6f,
        fill = fill,
        containerColor = container,
        contentColor = fg,
        textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = size.fontSize),
        interactionSource = null,
    ) {
        Icon(icon, null, Modifier.size(size.iconSize))
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Text(text)
        }
    }
}

data class SgFabMenuItem(val label: String, val icon: ImageVector, val onClick: () -> Unit)

/**
 * FAB menu de Expressive: el FAB se convierte en un botón de cierre y despliega encima
 * de 2 a 6 píldoras `primary` de 56 dp.
 */
@Composable
fun SgFabMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<SgFabMenuItem>,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add,
    contentDescription: String = "Crear",
) {
    val s = MaterialTheme.colorScheme
    Column(modifier, horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
        ) {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items.forEach { item ->
                    ButtonSurface(
                        onClick = {
                            item.onClick()
                            onExpandedChange(false)
                        },
                        modifier = Modifier,
                        enabled = true,
                        height = 56.dp,
                        horizontalPadding = 20.dp,
                        gap = 8.dp,
                        restRadius = 28.dp,
                        pressedRadius = 16.dp,
                        fill = Fill.Pebble,
                        containerColor = s.primary,
                        contentColor = s.onPrimary,
                        textStyle = MaterialTheme.typography.titleMedium,
                        interactionSource = null,
                    ) {
                        Icon(item.icon, null, Modifier.size(22.dp))
                        Text(item.label)
                    }
                }
            }
        }
        if (expanded) {
            ButtonSurface(
                onClick = { onExpandedChange(false) },
                modifier = Modifier,
                enabled = true,
                height = 56.dp,
                horizontalPadding = 0.dp,
                gap = 0.dp,
                restRadius = 28.dp,
                pressedRadius = 16.dp,
                fill = Fill.Pebble,
                containerColor = s.onPrimaryContainer,
                contentColor = s.primaryContainer,
                textStyle = MaterialTheme.typography.labelLarge,
                interactionSource = null,
                fixedWidth = 56.dp,
            ) {
                Icon(Icons.Filled.Close, "Cerrar menú", Modifier.size(22.dp))
            }
        } else {
            SgFab(onClick = { onExpandedChange(true) }, icon = icon, contentDescription = contentDescription)
        }
    }
}
