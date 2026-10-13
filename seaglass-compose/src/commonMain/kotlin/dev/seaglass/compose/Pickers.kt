package dev.seaglass.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sgDatePickerColors(): DatePickerColors {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    return DatePickerDefaults.colors(
        containerColor = Color.Transparent,
        titleContentColor = s.onSurfaceVariant,
        headlineContentColor = s.onSurface,
        weekdayContentColor = s.onSurfaceVariant,
        subheadContentColor = s.onSurfaceVariant,
        navigationContentColor = s.onSurfaceVariant,
        yearContentColor = s.onSurface,
        currentYearContentColor = s.primary,
        selectedYearContentColor = s.onPrimary,
        selectedYearContainerColor = s.primary,
        dayContentColor = s.onSurface,
        selectedDayContentColor = s.onPrimary,
        selectedDayContainerColor = s.primary,
        todayContentColor = s.primary,
        todayDateBorderColor = s.primary,
        dayInSelectionRangeContentColor = s.onPrimaryContainer,
        dayInSelectionRangeContainerColor = c.primaryContainerGlass,
        dividerColor = s.outlineVariant,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sgTimePickerColors(): TimePickerColors {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    return TimePickerDefaults.colors(
        clockDialColor = s.onSurface.copy(alpha = 0.07f),
        clockDialSelectedContentColor = s.onPrimary,
        clockDialUnselectedContentColor = s.onSurface,
        selectorColor = s.primary,
        containerColor = Color.Transparent,
        periodSelectorBorderColor = s.outline,
        periodSelectorSelectedContainerColor = s.tertiaryContainer,
        periodSelectorUnselectedContainerColor = Color.Transparent,
        periodSelectorSelectedContentColor = s.onTertiaryContainer,
        periodSelectorUnselectedContentColor = s.onSurface,
        timeSelectorSelectedContainerColor = s.primaryContainer,
        timeSelectorUnselectedContainerColor = c.disabledContainer,
        timeSelectorSelectedContentColor = s.onPrimaryContainer,
        timeSelectorUnselectedContentColor = s.onSurface,
    )
}

private val PickerShape = RoundedCornerShape(SeaGlassCorners.ExtraLarge)

@Composable
private fun PickerActions(confirmLabel: String, dismissLabel: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
        SgButton(dismissLabel, onClick = onDismiss, style = SgButtonStyle.Text)
        SgButton(confirmLabel, onClick = onConfirm, style = SgButtonStyle.Text)
    }
}

/** Selector de fecha en línea (docked) sobre vidrio. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    showModeToggle: Boolean = true,
) {
    Column(modifier.widthIn(max = 380.dp).glass(PickerShape, GlassLevel.Level5)) {
        DatePicker(state = state, colors = sgDatePickerColors(), showModeToggle = showModeToggle)
    }
}

/** Selector de fecha modal. Crea el estado con `rememberDatePickerState()`. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgDatePickerDialog(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    confirmLabel: String = "Aceptar",
    dismissLabel: String = "Cancelar",
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(Modifier.padding(16.dp).widthIn(max = 380.dp).glass(PickerShape, GlassLevel.Level5)) {
            DatePicker(state = state, colors = sgDatePickerColors())
            PickerActions(confirmLabel, dismissLabel, onConfirm, onDismissRequest)
        }
    }
}

/** Selector de rango de fechas modal. Crea el estado con `rememberDateRangePickerState()`. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgDateRangePickerDialog(
    state: DateRangePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    confirmLabel: String = "Aceptar",
    dismissLabel: String = "Cancelar",
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(Modifier.padding(16.dp).widthIn(max = 380.dp).glass(PickerShape, GlassLevel.Level5)) {
            DateRangePicker(state = state, modifier = Modifier.weight(1f, fill = false), colors = sgDatePickerColors())
            PickerActions(confirmLabel, dismissLabel, onConfirm, onDismissRequest)
        }
    }
}

/**
 * Selector de hora modal con esfera. Crea el estado con
 * `rememberTimePickerState(initialHour, initialMinute, is24Hour = true)` (en es-ES lo habitual es 24 h).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgTimePickerDialog(
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Selecciona la hora",
    confirmLabel: String = "Aceptar",
    dismissLabel: String = "Cancelar",
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            Modifier.glass(PickerShape, GlassLevel.Level5).padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            )
            TimePicker(state = state, colors = sgTimePickerColors(), modifier = Modifier.padding(horizontal = 24.dp))
            PickerActions(confirmLabel, dismissLabel, onConfirm, onDismissRequest)
        }
    }
}
