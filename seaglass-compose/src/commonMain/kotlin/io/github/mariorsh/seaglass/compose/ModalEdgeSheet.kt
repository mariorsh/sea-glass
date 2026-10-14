package io.github.mariorsh.seaglass.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/** Platform modal window owns focus, Back/Escape, input isolation and focus restoration. */
@Composable
internal fun ModalEdgeSheet(
    onDismiss: () -> Unit,
    alignment: Alignment,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        CompositionLocalProvider(LocalGlassBackdrop provides GlassBackdrop.None) {
            val focus = remember { FocusRequester() }
            Box(Modifier.fillMaxSize().safeDrawingPadding().imePadding()) {
                Box(Modifier.fillMaxSize().clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss))
                Column(
                    Modifier.align(alignment).fillMaxHeight().widthIn(max = 300.dp).fillMaxWidth()
                        .glass(MaterialTheme.shapes.extraLarge, GlassLevel.Level5)
                        .pointerInput(Unit) { detectTapGestures(onTap = {}) }
                        .focusRequester(focus).focusable().semantics { paneTitle = title }.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content,
                )
            }
            LaunchedEffect(Unit) { focus.requestFocus() }
        }
    }
}
