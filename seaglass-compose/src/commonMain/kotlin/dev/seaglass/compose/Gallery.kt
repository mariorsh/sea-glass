package dev.seaglass.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Catálogo de todos los componentes. Llámalo desde cualquier app para revisar el sistema:
 * `SeaGlassGallery()` (incluye su propio SeaGlassTheme y el interruptor claro/oscuro).
 */
@Composable
fun SeaGlassGallery(modifier: Modifier = Modifier, startDark: Boolean = false) {
    var dark by remember { mutableStateOf(startDark) }
    SeaGlassTheme(darkTheme = dark) {
        SeaGlassBackground(modifier) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Sea Glass", style = SeaGlassTheme.typography.displayLargeEmphasized, color = MaterialTheme.colorScheme.onBackground)
                        Text("Material 3 + M3 Expressive", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(if (dark) "Noche" else "Arena", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(end = 8.dp))
                    SgSwitch(checked = dark, onCheckedChange = { dark = it })
                }
                ActionsSection()
                InputsSection()
                NavigationSection()
                FeedbackSection()
                ContainersSection()
                PickersSection()
                ShapesSection()
                Spacer(Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().glassPanel().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        content()
    }
}

@Composable
private fun Caption(text: String) {
    Text(text.uppercase(), style = SeaGlassTheme.typography.captionMono, color = SeaGlassTheme.colors.inkMuted)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActionsSection() = Section("Acciones") {
    Caption("Buttons")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SgButton("Añadir", onClick = {}, icon = Icons.Filled.Add)
        SgButton("Guardar", onClick = {}, style = SgButtonStyle.Tonal)
        SgButton("Compartir", onClick = {}, style = SgButtonStyle.Elevated)
        SgButton("Editar", onClick = {}, style = SgButtonStyle.Outlined)
        SgButton("Ver más", onClick = {}, style = SgButtonStyle.Text)
        SgButton("Desactivado", onClick = {}, enabled = false)
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), itemVerticalAlignment = Alignment.Bottom) {
        SgButton("XS", onClick = {}, size = SgButtonSize.ExtraSmall)
        SgButton("Small", onClick = {})
        SgButton("Medium", onClick = {}, size = SgButtonSize.Medium, square = true)
        SgButton("Large", onClick = {}, size = SgButtonSize.Large)
    }
    var fav by remember { mutableStateOf(false) }
    var follow by remember { mutableStateOf(true) }
    Caption("Toggle e icon buttons")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SgToggleButton(fav, { fav = it }, "Favorito", icon = Icons.Filled.FavoriteBorder, checkedIcon = Icons.Filled.Favorite)
        SgToggleButton(follow, { follow = it }, "Siguiendo")
        SgIconButton(onClick = {}, icon = Icons.Filled.Search, contentDescription = "Buscar")
        SgIconButton(onClick = {}, icon = Icons.Filled.Add, contentDescription = "Añadir", style = SgIconButtonStyle.Filled)
        SgIconButton(onClick = {}, icon = Icons.Filled.Edit, contentDescription = "Editar", style = SgIconButtonStyle.Tonal)
        SgIconButton(onClick = {}, icon = Icons.Filled.Share, contentDescription = "Compartir", style = SgIconButtonStyle.Outlined)
        SgIconToggleButton(fav, { fav = it }, Icons.Filled.FavoriteBorder, "Favorito", checkedIcon = Icons.Filled.Favorite)
    }
    Caption("Button groups")
    SgButtonGroup {
        SgButton("Marea", onClick = {}, style = SgButtonStyle.Tonal)
        SgButton("Corriente", onClick = {}, style = SgButtonStyle.Tonal)
        SgButton("Viento", onClick = {}, style = SgButtonStyle.Tonal)
    }
    var view by remember { mutableIntStateOf(1) }
    SgConnectedButtonGroup(listOf("Día", "Semana", "Mes"), view, { view = it })
    SgSegmentedButtons(listOf("Recientes", "Color", "Rareza"), view, { view = it })
    var split by remember { mutableStateOf(false) }
    Box {
        SgSplitButton("Guardar", onClick = {}, expanded = split, onExpandedChange = { split = it })
        SgDropdownMenu(expanded = split, onDismissRequest = { split = false }) {
            SgMenuGroup {
                SgMenuItem("Guardar como…", onClick = { split = false })
                SgMenuItem("Guardar copia", onClick = { split = false })
            }
        }
    }
    Caption("FAB")
    var menu by remember { mutableStateOf(false) }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Bottom) {
        SgFab(onClick = {}, icon = Icons.Filled.Edit, contentDescription = "Nueva nota")
        SgFab(onClick = {}, icon = Icons.Filled.Edit, contentDescription = "Nueva nota", size = SgFabSize.Medium, color = SgFabColor.SecondaryContainer)
        SgExtendedFab("Escribir", Icons.Filled.Edit, onClick = {}, color = SgFabColor.Primary)
        Spacer(Modifier.weight(1f))
        SgFabMenu(
            expanded = menu,
            onExpandedChange = { menu = it },
            items = listOf(
                SgFabMenuItem("Foto", Icons.Filled.Star) {},
                SgFabMenuItem("Nota", Icons.Filled.Edit) {},
                SgFabMenuItem("Lugar", Icons.Filled.LocationOn) {},
            ),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InputsSection() = Section("Entradas") {
    var beach by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("mar@orilla") }
    SgTextField(beach, { beach = it }, "Playa", Modifier.fillMaxWidth(), supportingText = "Escribe y la etiqueta sube")
    SgTextField(
        mail, { mail = it }, "Correo", Modifier.fillMaxWidth(),
        variant = SgTextFieldVariant.Outlined,
        isError = !mail.contains("."),
        supportingText = if (!mail.contains(".")) "Falta el dominio" else null,
    )
    var a by remember { mutableStateOf(true) }
    var b by remember { mutableStateOf(false) }
    var radio by remember { mutableIntStateOf(0) }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp), itemVerticalAlignment = Alignment.CenterVertically) {
        SgCheckbox(a, { a = it })
        SgCheckbox(b, { b = it }, isError = true)
        Row(Modifier.selectableGroup()) {
            repeat(3) { i -> SgRadioButton(radio == i, { radio = i }) }
        }
        SgSwitch(a, { a = it })
        SgSwitch(b, { b = it })
    }
    var v by remember { mutableFloatStateOf(62f) }
    var r by remember { mutableStateOf(20f..70f) }
    Caption("Sliders")
    SgSlider(v, { v = it }, valueRange = 0f..100f, showValueLabel = true)
    SgSlider(v, { v = it }, valueRange = 0f..100f, size = SgSliderSize.Medium, insideIcon = Icons.Filled.Notifications)
    SgRangeSlider(r, { r = it }, valueRange = 0f..100f)
    Caption("Chips")
    var green by remember { mutableStateOf(true) }
    var blue by remember { mutableStateOf(false) }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SgAssistChip("Añadir al calendario", onClick = {}, icon = Icons.Filled.DateRange)
        SgAssistChip("Cómo llegar", onClick = {}, icon = Icons.Filled.LocationOn, elevated = true)
        SgFilterChip("Verde", green, { green = it })
        SgFilterChip("Cobalto", blue, { blue = it })
        SgInputChip("Lucía M.", onRemove = {}, avatar = { SgAvatar("LM") })
        SgSuggestionChip("¿Marea baja hoy?", onClick = {})
    }
}

@Composable
private fun NavigationSection() = Section("Navegación") {
    var q by remember { mutableStateOf("") }
    SgSearchAppBar(
        q, { q = it }, "Buscar en la orilla",
        navigationIcon = { SgIconButton(onClick = {}, icon = Icons.Filled.Menu, contentDescription = "Menú") },
        trailing = { SgAvatar("M", size = 40.dp, containerColor = MaterialTheme.colorScheme.tertiary, contentColor = MaterialTheme.colorScheme.onTertiary) },
    )
    SgTopAppBar(
        "Colección",
        variant = SgTopAppBarVariant.CenterAligned,
        actions = { SgIconButton(onClick = {}, icon = Icons.Filled.MoreVert, contentDescription = "Más opciones") },
    )
    SgTopAppBar("Cala del Moral", variant = SgTopAppBarVariant.Medium, subtitle = "24 fragmentos · actualizado hoy")
    Caption("Toolbars")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        SgToolbar {
            SgToolbarItem(Icons.Filled.Edit, "Editar", onClick = {})
            SgToolbarItem(Icons.Filled.Share, "Compartir", onClick = {}, selected = true)
            SgToolbarItem(Icons.Filled.Delete, "Eliminar", onClick = {})
        }
        SgToolbar(variant = SgToolbarVariant.FloatingVibrant) {
            SgToolbarItem(Icons.Filled.FavoriteBorder, "Favorito", onClick = {}, selected = true, selectedIcon = Icons.Filled.Favorite)
            SgToolbarItem(Icons.Filled.Share, "Compartir", onClick = {})
        }
    }
    var tab by remember { mutableIntStateOf(0) }
    SgTabRow(listOf(SgTab("Mareas", Icons.Filled.Notifications), SgTab("Playas", Icons.Filled.LocationOn), SgTab("Guardado", Icons.Filled.Star)), tab, { tab = it })
    SgTabRow(listOf(SgTab("Todo"), SgTab("Verde"), SgTab("Azul"), SgTab("Ámbar")), tab, { tab = it }, variant = SgTabVariant.Secondary)
    var dest by remember { mutableIntStateOf(0) }
    val dests = listOf("Inicio" to Icons.Filled.Home, "Explorar" to Icons.Filled.Search, "Colección" to Icons.Filled.Star, "Perfil" to Icons.Filled.Person)
    SgNavigationBar {
        dests.forEachIndexed { i, (label, icon) ->
            SgNavigationBarItem(dest == i, { dest = i }, icon, label, badge = if (i == 2) "3" else null)
        }
    }
    var railOpen by remember { mutableStateOf(false) }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SgNavigationRail(
            expanded = railOpen,
            header = { SgIconButton(onClick = { railOpen = !railOpen }, icon = Icons.Filled.Menu, contentDescription = if (railOpen) "Contraer menú" else "Expandir menú") },
        ) {
            dests.forEachIndexed { i, (label, icon) ->
                SgNavigationRailItem(dest == i, { dest = i }, icon, label, badge = if (i == 2) "24" else null)
            }
        }
    }
}

@Composable
private fun FeedbackSection() = Section("Feedback") {
    Caption("Progreso")
    SgLinearProgress({ 0.6f })
    SgLinearProgress({ 0.6f }, wavy = true)
    SgLinearProgressIndeterminate(thick = true, color = MaterialTheme.colorScheme.secondary)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
        SgCircularProgress({ 0.6f })
        SgCircularProgress({ 0.68f }, wavy = true)
        SgCircularProgress({ 0.35f }, thick = true, color = MaterialTheme.colorScheme.tertiary)
        SgLoadingIndicator()
        SgLoadingIndicator(contained = true)
    }
    Caption("Snackbar, badges y tooltips")
    SgSnackbar("Fragmento eliminado", actionLabel = "Deshacer")
    Row(horizontalArrangement = Arrangement.spacedBy(28.dp), verticalAlignment = Alignment.CenterVertically) {
        SgBadgedBox(badge = { SgBadge() }) { Icon(Icons.Filled.Notifications, "Notificaciones") }
        SgBadgedBox(badge = { SgBadge("8") }) { Icon(Icons.Filled.Star, "Favoritos") }
        SgTooltipBox("Guardar en colección") {
            SgIconButton(onClick = {}, icon = Icons.Filled.Star, contentDescription = "Guardar")
        }
        SgPlainTooltip("Guardar en colección")
    }
    SgRichTooltip("El rojo y el naranja aparecen en uno de cada 10.000 fragmentos.", title = "Rareza del color")
    Caption("Menú")
    Column(Modifier.width(260.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SgMenuGroup(SgMenuGroupPosition.First) {
            SgMenuItem("Por fecha", onClick = {}, leadingIcon = Icons.Filled.DateRange, selected = true)
            SgMenuItem("Por rareza", onClick = {}, leadingIcon = Icons.Filled.Star)
        }
        SgMenuGroup(SgMenuGroupPosition.Last) {
            SgMenuItem("Compartir", onClick = {}, leadingIcon = Icons.Filled.Share, trailingText = "Ctrl S")
            SgMenuItem("Eliminar", onClick = {}, leadingIcon = Icons.Filled.Delete, destructive = true)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContainersSection() = Section("Contenedores") {
    val c = SeaGlassTheme.colors
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SgCard(Modifier.width(260.dp)) {
            SgMedia(color = c.seafoam)
            SgCardBody("Verde espuma", overline = "Elevated", text = "Encontrado tras el temporal.")
            SgCardActions {
                SgButton("Detalles", onClick = {}, style = SgButtonStyle.Text)
                SgButton("Guardar", onClick = {})
            }
        }
        SgCard(Modifier.width(260.dp), variant = SgCardVariant.Filled) {
            SgMedia(color = c.cobalt)
            SgCardBody("Azul cobalto", overline = "Filled", text = "Muy poco común en esta costa.")
        }
        SgCard(Modifier.width(260.dp), variant = SgCardVariant.Outlined) {
            SgMedia(color = c.amber)
            SgCardBody("Ámbar tostado", overline = "Outlined", text = "Burbujas atrapadas dentro.")
        }
    }
    Caption("Listas")
    Column {
        SgListItem("Lucía M.", supporting = "Te compartió 4 fragmentos", leading = { SgAvatar("LM", size = 40.dp) }, trailing = { SgListMeta("10:24") })
        SgDivider(inset = true)
        var alert by remember { mutableStateOf(true) }
        SgListItem(
            "Aviso de marea baja",
            supporting = "30 min antes del mínimo",
            leading = { SgListIcon(Icons.Filled.Notifications) },
            trailing = { SgSwitch(alert, { alert = it }) },
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        val items = listOf("Playas guardadas", "Fragmentos", "Rutas")
        items.forEachIndexed { i, label -> SgSegmentedListItem(label, i, items.size, selected = i == 1, onClick = {}) }
    }
    Caption("Carrusel")
    val tints = listOf(c.seafoam, c.cobalt, c.amber, c.lavender, c.coral, c.aqua)
    SgMultiBrowseCarousel(itemCount = tints.size) { i -> SgCarouselTile(tints[i], if (i == 0) "Cala del Moral" else null) }
    var dialog by remember { mutableStateOf(false) }
    var full by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf(false) }
    Caption("Dialogs y sheets")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SgButton("Dialog", onClick = { dialog = true }, style = SgButtonStyle.Tonal)
        SgButton("Pantalla completa", onClick = { full = true }, style = SgButtonStyle.Tonal)
        SgButton("Bottom sheet", onClick = { sheet = true }, style = SgButtonStyle.Tonal)
    }
    if (dialog) {
        SgAlertDialog(
            onDismissRequest = { dialog = false },
            title = "¿Devolver al mar?",
            text = "Se eliminarán los 3 fragmentos seleccionados. No se puede deshacer.",
            confirmLabel = "Eliminar",
            onConfirm = { dialog = false },
            icon = Icons.Filled.Delete,
            destructive = true,
        )
    }
    if (full) {
        var name by remember { mutableStateOf("Verde espuma") }
        SgFullScreenDialog(onDismissRequest = { full = false }, title = "Nuevo hallazgo", confirmLabel = "Guardar", onConfirm = { full = false }) {
            SgTextField(name, { name = it }, "Nombre", Modifier.fillMaxWidth())
        }
    }
    if (sheet) {
        SgModalBottomSheet(onDismissRequest = { sheet = false }) {
            SgListItem("Verde espuma, 18 mm", supporting = "Cala del Moral · 13 oct")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SgSheetAction(Icons.Filled.Share, "Compartir", onClick = {})
                SgSheetAction(Icons.Filled.Edit, "Editar", onClick = {})
                SgSheetAction(Icons.Filled.LocationOn, "Mapa", onClick = {})
                SgSheetAction(Icons.Filled.Delete, "Eliminar", onClick = {}, destructive = true)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PickersSection() = Section("Selectores") {
    var date by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(false) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SgButton("Fecha", onClick = { date = true }, style = SgButtonStyle.Tonal, icon = Icons.Filled.DateRange)
        SgButton("Hora", onClick = { time = true }, style = SgButtonStyle.Tonal)
    }
    if (date) {
        val state = rememberDatePickerState()
        SgDatePickerDialog(state, onDismissRequest = { date = false }, onConfirm = { date = false })
    }
    if (time) {
        val state = rememberTimePickerState(initialHour = 10, initialMinute = 30, is24Hour = true)
        SgTimePickerDialog(state, onDismissRequest = { time = false }, onConfirm = { time = false })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShapesSection() = Section("Formas Expressive") {
    val c = SeaGlassTheme.colors
    val tints = listOf(c.seafoam, c.cobalt, c.amber, c.lavender, c.coral, MaterialTheme.colorScheme.primary, c.aqua, c.cobalt)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SeaGlassShapes.all.forEachIndexed { i, (name, shape) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    Modifier
                        .size(if (name == "Píldora") 88.dp else 64.dp, 64.dp)
                        .background(tints[i].copy(alpha = 0.85f), shape)
                        .border(1.5.dp, c.glassHighlight, shape),
                )
                Text(name, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium))
            }
        }
    }
}
