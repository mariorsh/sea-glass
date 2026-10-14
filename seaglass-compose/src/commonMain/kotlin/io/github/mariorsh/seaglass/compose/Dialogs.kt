package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private val DialogShape: androidx.compose.ui.graphics.Shape
    @Composable get() = MaterialTheme.shapes.extraLarge

/**
 * Dialog básico de vidrio (nivel 5). [title] como pregunta directa, [text] con la consecuencia.
 * [destructive] pinta la confirmación en `error`.
 */
@Composable
fun SgAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissLabel: String? = SeaGlassTheme.strings.cancel,
    onDismiss: () -> Unit = onDismissRequest,
    icon: ImageVector? = null,
    destructive: Boolean = false,
    properties: DialogProperties = DialogProperties(),
) {
    val s = MaterialTheme.colorScheme
    Dialog(onDismissRequest = onDismissRequest, properties = properties) {
        CompositionLocalProvider(LocalGlassBackdrop provides GlassBackdrop.None) {
        Column(
            modifier
                .widthIn(max = 360.dp)
                .glass(DialogShape, GlassLevel.Level5)
                .verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (icon != null) {
                val container = if (destructive) s.errorContainer else s.secondaryContainer
                val fg = if (destructive) s.error else s.onSecondaryContainer
                Box(Modifier.size(48.dp).frost(container, MaterialTheme.shapes.large), contentAlignment = Alignment.Center) {
                    Icon(icon, null, Modifier.size(24.dp), tint = fg)
                }
            }
            Text(title, style = MaterialTheme.typography.headlineSmall, color = s.onSurface, textAlign = TextAlign.Center)
            Text(text, style = MaterialTheme.typography.bodyMedium, color = s.onSurfaceVariant, textAlign = TextAlign.Center)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)) {
                if (dismissLabel != null) SgButton(dismissLabel, onClick = onDismiss, style = SgButtonStyle.Text)
                SgButton(confirmLabel, onClick = onConfirm, style = if (destructive) SgButtonStyle.Danger else SgButtonStyle.Filled)
            }
        }
        }
    }
}

/**
 * Dialog a pantalla completa: la única superficie opaca del sistema (`surface`), para formularios
 * largos en ventanas compactas. En ventanas grandes usa [SgAlertDialog] o un dialog básico propio.
 */
@Composable
fun SgFullScreenDialog(
    onDismissRequest: () -> Unit,
    title: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    closeContentDescription: String = SeaGlassTheme.strings.closeWithoutSaving,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable ColumnScope.() -> Unit,
) {
    val s = MaterialTheme.colorScheme
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(modifier.fillMaxSize().background(s.surface).safeDrawingPadding().imePadding()) {
            Row(
                Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SgIconButton(onClick = onDismissRequest, icon = Icons.Filled.Close, contentDescription = closeContentDescription)
                Text(title, style = MaterialTheme.typography.titleLarge, color = s.onSurface, modifier = Modifier.weight(1f).padding(start = 4.dp))
                SgButton(confirmLabel, onClick = onConfirm, modifier = Modifier.padding(end = 8.dp))
            }
            Column(
                Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                CompositionLocalProvider(LocalContentColor provides s.onSurface) { content() }
            }
        }
    }
}

// ---------------------------------------------------------------- Sheets

/**
 * Bottom sheet modal de vidrio con asa. Envuelve `ModalBottomSheet` de Material 3.
 * [skipPartiallyExpanded] = true lo abre directamente a altura completa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState? = null,
    skipPartiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val resolvedSheetState = sheetState ?: rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = resolvedSheetState,
        shape = RoundedCornerShape(topStart = SeaGlassCorners.ExtraLarge, topEnd = SeaGlassCorners.ExtraLarge),
        containerColor = c.glassBar.copy(alpha = if (SeaGlassTheme.surfaces.policy == GlassSurfacePolicy.Opaque) 1f else 0.96f),
        contentColor = s.onSurface,
        tonalElevation = 0.dp,
        scrimColor = s.scrim,
        dragHandle = {
            Box(
                Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .background(s.outline, SeaGlassCorners.full),
            )
        },
    ) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            content()
        }
    }
}

/** Acción rápida de bottom sheet (rejilla de hasta 4). */
@Composable
fun RowScope.SgSheetAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    destructive: Boolean = false,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    ButtonSurface(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        enabled = true,
        height = 76.dp,
        horizontalPadding = 4.dp,
        gap = 0.dp,
        restRadius = 18.dp,
        pressedRadius = 12.dp,
        fill = Fill.Frost,
        containerColor = if (destructive) s.errorContainer else c.primaryContainerGlass,
        contentColor = if (destructive) s.onErrorContainer else s.onPrimaryContainer,
        textStyle = MaterialTheme.typography.labelMedium,
        interactionSource = null,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, null, Modifier.size(22.dp))
            Text(label, maxLines = 1)
        }
    }
}

/**
 * Side sheet modal desde el extremo lógico final (300 dp) para filtros o detalles.
 * La ventana de diálogo gestiona Atrás/Escape y aislamiento del contenido de fondo.
 */
@Composable
fun SgModalSideSheet(
    open: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val s = MaterialTheme.colorScheme
    Box(modifier.fillMaxSize()) { content() }
    if (open) ModalEdgeSheet(onDismissRequest, Alignment.CenterEnd, title) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            SgIconButton(onClick = onDismissRequest, icon = Icons.Filled.Close, contentDescription = SeaGlassTheme.strings.close)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp), content = sheetContent)
        if (actions != null) Row(horizontalArrangement = Arrangement.spacedBy(8.dp), content = actions)
    }
}
