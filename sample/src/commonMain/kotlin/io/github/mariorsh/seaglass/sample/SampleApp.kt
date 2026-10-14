package io.github.mariorsh.seaglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import dev.chrisbanes.haze.HazeState
import io.github.mariorsh.seaglass.compose.*
import io.github.mariorsh.seaglass.haze.*

/** Local-only catalogue and a repeatable scrolling workload. */
@Composable
fun SampleApp() {
    var dark by remember { mutableStateOf(false) }
    var brand by remember { mutableStateOf(false) }
    var rtl by remember { mutableStateOf(false) }
    var large by remember { mutableStateOf(false) }
    var reduced by remember { mutableStateOf(false) }
    var mode by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val fonts = remember { SeaGlassFonts(FontFamily.SansSerif, FontFamily.SansSerif, FontFamily.Monospace) }
    val config = SeaGlassThemeConfig(
        colorScheme = if (brand) (if (dark) seaGlassDarkColorScheme() else seaGlassLightColorScheme()).copy(primary = Color(0xff8050ab)) else null,
        colors = if (brand) (if (dark) seaGlassDarkColors() else seaGlassLightColors()).copy(aqua = Color(0xffad80d0)) else null,
        fonts = if (brand) fonts else null,
        shapes = if (brand) Shapes(small = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)) else null,
        motion = SeaGlassMotionConfig(reduced),
        surfaces = SeaGlassSurfaceStyle(policy = if (mode == 2) GlassSurfacePolicy.Opaque else GlassSurfacePolicy.Auto, forceFallback = mode == 1),
    )
    CompositionLocalProvider(
        LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr,
        LocalDensity provides Density(density.density, if (large) 2f else density.fontScale),
    ) {
        SeaGlassTheme(darkTheme = dark, config = config) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            Column(Modifier.fillMaxSize().background(SeaGlassTheme.colors.ground).safeDrawingPadding()) {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SgToggleButton(dark, { dark = it }, "Noche")
                    SgToggleButton(brand, { brand = it }, "Brand")
                    SgToggleButton(rtl, { rtl = it }, "RTL")
                    SgToggleButton(large, { large = it }, "200%")
                    SgToggleButton(reduced, { reduced = it }, "Reduced motion")
                }
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Blur", "Fallback", "Opaque").forEachIndexed { i, title -> SgToggleButton(mode == i, { mode = i }, title) }
                }
                SgTabRow(listOf(SgTab("Checks"), SgTab("Catalogue"), SgTab("Scroll / perf")), page, { page = it })
                val haze = remember { HazeState() }
                val backdrop = rememberHazeBackdrop(haze, mode == 0)
                SeaGlassTheme(darkTheme = dark, config = config, backdrop = backdrop) {
                    when (page) {
                        0 -> Checks()
                        1 -> SeaGlassGallery(startDark = dark, config = config)
                        else -> ScrollScenario(haze)
                    }
                }
            }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Checks() {
    var text by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf(false) }
    var drawer by remember { mutableStateOf(false) }
    var bottom by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf(false) }
    SgModalNavigationDrawer(drawer, { drawer = false }, drawerContent = {
        SgDrawerHeadline("Navigation")
        SgButton("Close drawer", { drawer = false })
    }) {
        SgModalSideSheet(sheet, { sheet = false }, "Filters", sheetContent = { Text("Local filters"); SgButton("Apply", { sheet = false }) }) {
            LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("Sea Glass 0.2.0", style = MaterialTheme.typography.headlineMedium) }
                item { SgTextField(value = text, onValueChange = { text = it }, label = "Name", isError = submitted && text.isBlank()) }
                item { SgButton("Validate", { submitted = true }); if (submitted) Text(if (text.isBlank()) "Name is required" else "Saved locally") }
                item { SgButton("A long button label that wraps at large font sizes", {}, size = SgButtonSize.ExtraSmall) }
                item { SgButtonGroup { SgButton("Small", {}, size = SgButtonSize.ExtraSmall); SgButton("Disabled", {}, enabled = false) } }
                item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SgButton("Dialog", { dialog = true }); SgButton("Sheet", { sheet = true }) } }
                item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SgButton("Drawer", { drawer = true }); SgButton("Bottom sheet", { bottom = true }) } }
                item { Box { SgButton("Menu", { menu = true }); SgDropdownMenu(menu, { menu = false }) { SgMenuGroup { SgMenuItem("Close menu", { menu = false }) } } } }
                item { SgLoadingIndicator(); SgLinearProgress({ 0.65f }, wavy = true) }
            }
        }
    }
    if (dialog) SgAlertDialog({ dialog = false }, "Discard changes?", "This is a local example.", "Confirm", { dialog = false })
    if (bottom) SgModalBottomSheet({ bottom = false }) { Text("Bottom sheet"); SgButton("Close sheet", { bottom = false }) }
}

/** Source and effect are siblings: the overlay never captures itself or blurs its own text. */
@Composable
private fun ScrollScenario(haze: HazeState) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize().seaGlassHazeSource(haze), contentPadding = PaddingValues(top = 100.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items((1..200).toList(), key = { it }) { index ->
                // Captured content never applies an effect sampling its own source.
                CompositionLocalProvider(LocalGlassBackdrop provides GlassBackdrop.None) {
                SgCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Column(Modifier.padding(20.dp)) { Text("Sea pebble $index", style = MaterialTheme.typography.titleLarge); Text("A local scrolling workload with cards and glass surfaces.") }
                }
                }
            }
        }
        Box(Modifier.fillMaxWidth().padding(12.dp).glassPanel().padding(20.dp)) {
            Text("Scroll behind this surface", style = MaterialTheme.typography.titleLarge)
        }
    }
}
