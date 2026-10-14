package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private val BarShape = RoundedCornerShape(SeaGlassCorners.ExtraLarge)

// ---------------------------------------------------------------- Top app bar

enum class SgTopAppBarVariant { Small, CenterAligned, Medium, Large }

/**
 * Barra superior de vidrio flotante. Medium y Large son las flexibles de Expressive (con subtítulo).
 * Aplica tú los insets del sistema (por ejemplo `Modifier.statusBarsPadding()`) y el margen exterior.
 */
@Composable
fun SgTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    variant: SgTopAppBarVariant = SgTopAppBarVariant.Small,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val s = MaterialTheme.colorScheme
    when (variant) {
        SgTopAppBarVariant.Small, SgTopAppBarVariant.CenterAligned -> Row(
            modifier
                .fillMaxWidth()
                .height(64.dp)
                .glass(BarShape, GlassLevel.Level3)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides s.onSurfaceVariant) {
                if (navigationIcon != null) navigationIcon() else Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = MaterialTheme.typography.headlineSmall.fontFamily),
                        color = s.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (variant == SgTopAppBarVariant.CenterAligned) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (subtitle != null) {
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = s.onSurfaceVariant,
                            maxLines = 1,
                            textAlign = if (variant == SgTopAppBarVariant.CenterAligned) TextAlign.Center else TextAlign.Start,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, content = actions)
            }
        }

        SgTopAppBarVariant.Medium, SgTopAppBarVariant.Large -> Column(
            modifier
                .fillMaxWidth()
                .glass(BarShape, GlassLevel.Level3)
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides s.onSurfaceVariant) {
                Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (navigationIcon != null) navigationIcon()
                    Spacer(Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically, content = actions)
                }
            }
            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    title,
                    style = if (variant == SgTopAppBarVariant.Large) {
                        MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold)
                    } else {
                        MaterialTheme.typography.headlineMedium
                    },
                    color = s.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = s.onSurfaceVariant)
            }
        }
    }
}

// ---------------------------------------------------------------- Search

/** Píldora de búsqueda de espuma. Úsala sola o dentro de [SgSearchAppBar]. */
@Composable
fun SgSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    leadingIcon: ImageVector = Icons.Filled.Search,
    trailing: (@Composable () -> Unit)? = null,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    Row(
        modifier.height(56.dp).frost(c.primaryContainerGlass, SeaGlassCorners.full).padding(start = 18.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(leadingIcon, null, tint = s.primary, modifier = Modifier.size(22.dp))
        SearchInput(query, onQueryChange, placeholder, onSearch, Modifier.weight(1f))
        if (trailing != null) trailing()
    }
}

@Composable
private fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onSearch: (String) -> Unit,
    modifier: Modifier,
) {
    val s = MaterialTheme.colorScheme
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = s.onSurface),
        cursorBrush = SolidColor(s.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (query.isEmpty()) {
                    Text(placeholder, style = MaterialTheme.typography.bodyLarge, color = SeaGlassTheme.colors.inkMuted, maxLines = 1)
                }
                inner()
            }
        },
    )
}

/** Search app bar de Expressive: menú, barra de búsqueda y avatar o acción. */
@Composable
fun SgSearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    navigationIcon: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier.fillMaxWidth().height(72.dp).glass(BarShape, GlassLevel.Level3).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            if (navigationIcon != null) navigationIcon()
            SgSearchBar(query, onQueryChange, placeholder, Modifier.weight(1f), onSearch)
            if (trailing != null) trailing()
        }
    }
}

/**
 * Vista de búsqueda. A pantalla completa (móvil) es `surface` opaca con la barra en vidrio;
 * [docked] (tablet, escritorio) es una lámina de vidrio bajo el campo.
 */
@Composable
fun SgSearchView(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    docked: Boolean = false,
    onSearch: (String) -> Unit = {},
    filters: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val s = MaterialTheme.colorScheme
    val container = if (docked) {
        modifier.widthIn(max = 480.dp).glass(BarShape, GlassLevel.Level4)
    } else {
        modifier.fillMaxSize().background(s.surface)
    }
    Column(container) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(if (docked) 56.dp else 64.dp)
                .then(if (docked) Modifier else Modifier.background(SeaGlassTheme.colors.glassBar))
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (docked) {
                Icon(Icons.Filled.Search, null, tint = s.primary, modifier = Modifier.padding(horizontal = 12.dp))
            } else {
                SgIconButton(onClick = onBack, icon = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = SeaGlassTheme.strings.back)
            }
            SearchInput(query, onQueryChange, placeholder, onSearch, Modifier.weight(1f))
            if (query.isNotEmpty()) {
                SgIconButton(onClick = { onQueryChange("") }, icon = Icons.Filled.Close, contentDescription = SeaGlassTheme.strings.clearSearch)
            }
        }
        HorizontalDivider(color = s.outlineVariant)
        if (filters != null) {
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = filters,
            )
        }
        Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), content = content)
    }
}

// ---------------------------------------------------------------- Toolbars

enum class SgToolbarVariant { Docked, Floating, FloatingVibrant }

private val LocalToolbarVibrant = compositionLocalOf { false }

/** Toolbars de Expressive: acciones de la tarea actual. Sustituyen a la bottom app bar. */
@Composable
fun SgToolbar(
    modifier: Modifier = Modifier,
    variant: SgToolbarVariant = SgToolbarVariant.Floating,
    vertical: Boolean = false,
    content: @Composable () -> Unit,
) {
    val s = MaterialTheme.colorScheme
    val shape = if (variant == SgToolbarVariant.Docked) RoundedCornerShape(SeaGlassCorners.LargeIncreased) else SeaGlassCorners.full
    val surface = if (variant == SgToolbarVariant.FloatingVibrant) {
        Modifier.pebble(s.primary, shape, 6.dp)
    } else {
        Modifier.glass(shape, GlassLevel.Level3)
    }
    CompositionLocalProvider(LocalToolbarVibrant provides (variant == SgToolbarVariant.FloatingVibrant)) {
        if (vertical) {
            Column(
                modifier.width(64.dp).then(surface).padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) { content() }
        } else {
            Row(
                modifier
                    .then(if (variant == SgToolbarVariant.Docked) Modifier.fillMaxWidth() else Modifier)
                    .height(64.dp)
                    .then(surface)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (variant == SgToolbarVariant.Docked) Arrangement.SpaceAround else Arrangement.spacedBy(2.dp),
            ) { content() }
        }
    }
}

/** Acción de toolbar; [selected] la convierte en una píldora de espuma de 56×48. */
@Composable
fun SgToolbarItem(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    selectedIcon: ImageVector = icon,
) {
    val s = MaterialTheme.colorScheme
    val vibrant = LocalToolbarVibrant.current
    if (selected) {
        ButtonSurface(
            onClick = onClick,
            modifier = modifier,
            enabled = true,
            height = 48.dp,
            horizontalPadding = 0.dp,
            gap = 0.dp,
            restRadius = 16.dp,
            pressedRadius = 12.dp,
            fill = Fill.Frost,
            containerColor = s.primaryContainer,
            contentColor = s.onPrimaryContainer,
            textStyle = MaterialTheme.typography.labelLarge,
            interactionSource = null,
            fixedWidth = 56.dp,
            role = Role.Switch,
            toggleValue = true,
        ) { Icon(selectedIcon, contentDescription, Modifier.size(22.dp)) }
    } else {
        SgIconButton(
            onClick = onClick,
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            size = SgIconButtonSize.Small,
            width = SgIconButtonWidth.Wide,
            contentColor = if (vibrant) s.onPrimary else s.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------- Badges

/** Badge: punto (texto nulo) o píldora con número. Pon `999+` a partir de 1.000. */
@Composable
fun SgBadge(text: String? = null, modifier: Modifier = Modifier) {
    val s = MaterialTheme.colorScheme
    val ring = SeaGlassTheme.colors.ground
    if (text == null) {
        Box(modifier.size(9.dp).clip(SeaGlassCorners.full).background(ring).padding(1.dp).clip(SeaGlassCorners.full).background(s.error))
    } else {
        Box(
            modifier
                .height(18.dp)
                .widthIn(min = 18.dp)
                .clip(SeaGlassCorners.full)
                .background(ring)
                .padding(1.dp)
                .clip(SeaGlassCorners.full)
                .background(s.error)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = s.onError, maxLines = 1)
        }
    }
}

/** Coloca un badge en la esquina superior derecha de un icono. */
@Composable
fun SgBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        content()
        Box(Modifier.align(Alignment.TopEnd).offset(x = 8.dp, y = (-6).dp)) { badge() }
    }
}

// ---------------------------------------------------------------- Navigation bar

private val LocalNavBarHorizontal = compositionLocalOf { false }

/**
 * Navigation bar flotante para ventanas compactas (3–5 destinos).
 * [horizontal] = true para ventanas medianas: icono y texto en línea.
 */
@Composable
fun SgNavigationBar(
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier
            .then(if (horizontal) Modifier else Modifier.fillMaxWidth())
            .height(if (horizontal) 64.dp else 72.dp)
            .glass(if (horizontal) SeaGlassCorners.full else BarShape, GlassLevel.Level4)
            .padding(horizontal = 8.dp)
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (horizontal) Arrangement.spacedBy(8.dp) else Arrangement.Start,
    ) {
        CompositionLocalProvider(LocalNavBarHorizontal provides horizontal) { content() }
    }
}

/** Destino de [SgNavigationBar]. [badge]: null sin badge, "" punto, "3" número. */
@Composable
fun RowScope.SgNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector = icon,
    badge: String? = null,
) {
    val s = MaterialTheme.colorScheme
    val horizontal = LocalNavBarHorizontal.current
    val fg = if (selected) s.onSecondaryContainer else s.onSurfaceVariant
    val labelStyle = if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
    val iconContent: @Composable () -> Unit = {
        if (badge != null) {
            SgBadgedBox(badge = { SgBadge(badge.ifEmpty { null }) }) {
                Icon(if (selected) selectedIcon else icon, null, Modifier.size(22.dp), tint = fg)
            }
        } else {
            Icon(if (selected) selectedIcon else icon, null, Modifier.size(22.dp), tint = fg)
        }
    }
    if (horizontal) {
        val shape = SeaGlassCorners.full
        Row(
            modifier
                .height(40.dp)
                .then(if (selected) Modifier.frost(s.secondaryContainer, shape) else Modifier.clip(shape))
                .selectable(selected = selected, onClick = onClick, role = Role.Tab)
                .padding(start = 12.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            iconContent()
            Text(label, style = labelStyle.copy(fontSize = MaterialTheme.typography.labelLarge.fontSize), color = fg)
        }
    } else {
        Column(
            modifier
                .weight(1f)
                .fillMaxHeight()
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    role = Role.Tab,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val shape = SeaGlassCorners.full
            Box(
                Modifier
                    .width(56.dp)
                    .height(32.dp)
                    .then(if (selected) Modifier.frost(s.secondaryContainer, shape) else Modifier.clip(shape)),
                contentAlignment = Alignment.Center,
            ) { iconContent() }
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = (if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.labelMedium)
                    .copy(fontSize = MaterialTheme.typography.labelMedium.fontSize),
                color = fg,
                maxLines = 1,
            )
        }
    }
}

// ---------------------------------------------------------------- Rail & drawer

private val LocalRailExpanded = compositionLocalOf { false }

/**
 * Navigation rail para ventanas medianas y grandes. [expanded] = true lo abre a 260 dp con los
 * destinos en píldora (en Expressive sustituye al navigation drawer).
 */
@Composable
fun SgNavigationRail(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val width by animateDpAsState(if (expanded) 260.dp else 96.dp, SeaGlassTheme.motion.spatialDefault(), label = "rail")
    Column(
        modifier
            .width(width)
            .glass(BarShape, GlassLevel.Level3)
            .padding(vertical = 16.dp, horizontal = if (expanded) 16.dp else 0.dp)
            .selectableGroup(),
        horizontalAlignment = if (expanded) Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        CompositionLocalProvider(LocalRailExpanded provides expanded) {
            if (header != null) {
                header()
                Spacer(Modifier.height(14.dp))
            }
            content()
        }
    }
}

/** Destino de rail o de drawer. [badge] en el rail expandido/drawer se muestra como contador a la derecha. */
@Composable
fun SgNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector = icon,
    badge: String? = null,
) {
    val s = MaterialTheme.colorScheme
    val expanded = LocalRailExpanded.current
    val fg = if (selected) s.onSecondaryContainer else s.onSurfaceVariant
    val shape = SeaGlassCorners.full
    if (expanded) {
        Row(
            modifier
                .fillMaxWidth()
                .height(56.dp)
                .then(if (selected) Modifier.frost(s.secondaryContainer, shape) else Modifier.clip(shape))
                .selectable(selected = selected, onClick = onClick, role = Role.Tab)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(if (selected) selectedIcon else icon, null, Modifier.size(22.dp), tint = fg)
            Text(
                label,
                style = if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                color = fg,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            if (!badge.isNullOrEmpty()) Text(badge, style = MaterialTheme.typography.labelMedium, color = fg)
        }
    } else {
        Column(
            modifier
                .height(64.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    role = Role.Tab,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                Modifier.width(56.dp).height(32.dp).then(if (selected) Modifier.frost(s.secondaryContainer, shape) else Modifier.clip(shape)),
                contentAlignment = Alignment.Center,
            ) {
                if (badge != null) {
                    SgBadgedBox(badge = { SgBadge(badge.ifEmpty { null }) }) {
                        Icon(if (selected) selectedIcon else icon, null, Modifier.size(22.dp), tint = fg)
                    }
                } else {
                    Icon(if (selected) selectedIcon else icon, null, Modifier.size(22.dp), tint = fg)
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = (if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.labelMedium)
                    .copy(fontSize = MaterialTheme.typography.labelMedium.fontSize),
                color = fg,
                maxLines = 1,
            )
        }
    }
}

/**
 * Cajón lateral modal sobre `scrim`. Expressive lo considera legado: en diseños nuevos usa
 * [SgNavigationRail] con `expanded = true`. El diálogo de plataforma gestiona Atrás/Escape y aislamiento modal.
 */
@Composable
fun SgModalNavigationDrawer(
    open: Boolean,
    onDismiss: () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier.fillMaxSize()) { content() }
    if (open) ModalEdgeSheet(onDismiss, Alignment.CenterStart, SeaGlassTheme.strings.moreOptions) {
        Column(Modifier.selectableGroup().verticalScroll(androidx.compose.foundation.rememberScrollState())) {
            CompositionLocalProvider(LocalRailExpanded provides true) { drawerContent() }
        }
    }
}

@Composable
fun SgDrawerHeadline(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge.copy(fontFamily = MaterialTheme.typography.headlineSmall.fontFamily),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 14.dp),
    )
}

@Composable
fun SgDrawerSectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = SeaGlassTheme.typography.captionMono,
        color = SeaGlassTheme.colors.inkMuted,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

// ---------------------------------------------------------------- Tabs

data class SgTab(val label: String, val icon: ImageVector? = null)

enum class SgTabVariant { Primary, Secondary }

/** Pestañas. Primary: píldora de 48×4 dp bajo el contenido. Secondary: subrayado de 2 dp a todo el ancho. */
@Composable
fun SgTabRow(
    tabs: List<SgTab>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    variant: SgTabVariant = SgTabVariant.Primary,
    scrollable: Boolean = false,
) {
    val s = MaterialTheme.colorScheme
    val hasIcons = tabs.any { it.icon != null }
    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
                .selectableGroup(),
        ) {
            tabs.forEachIndexed { i, tab ->
                val selected = i == selectedIndex
                val fg = when {
                    selected && variant == SgTabVariant.Primary -> s.primary
                    selected -> s.onSurface
                    else -> s.onSurfaceVariant
                }
                Box(
                    Modifier
                        .then(if (scrollable) Modifier else Modifier.weight(1f))
                        .height(if (hasIcons) 64.dp else 48.dp)
                        .selectable(selected = selected, onClick = { onSelect(i) }, role = Role.Tab),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        Modifier.padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (tab.icon != null) Icon(tab.icon, null, Modifier.size(22.dp), tint = fg)
                        Text(
                            tab.label,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium),
                            color = fg,
                            maxLines = 1,
                        )
                    }
                    if (selected) {
                        if (variant == SgTabVariant.Primary) {
                            Box(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .width(48.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(s.primary),
                            )
                        } else {
                            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(2.dp).background(s.primary))
                        }
                    }
                }
            }
        }
        HorizontalDivider(color = s.outlineVariant)
    }
}
