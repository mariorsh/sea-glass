package io.github.mariorsh.seaglass.compose

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape

/** Null fields select the Arena/Noche preset. Nested themes start a fresh preset. */
@Immutable
data class SeaGlassThemeConfig(
    val colorScheme: ColorScheme? = null,
    val colors: SeaGlassColors? = null,
    val fonts: SeaGlassFonts? = null,
    val typography: Typography? = null,
    val extraTypography: SeaGlassTypography? = null,
    val shapes: Shapes? = null,
    val motion: SeaGlassMotionConfig = SeaGlassMotionConfig(),
    val surfaces: SeaGlassSurfaceStyle = SeaGlassSurfaceStyle(),
    val strings: SeaGlassStrings = SeaGlassStrings(),
)

/** Explicit override supplements Compose's platform duration scale. */
@Immutable
data class SeaGlassMotionConfig(val reduced: Boolean = false) {
    fun <T> spatialFast(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(0.6f, 800f)
    fun <T> spatialDefault(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(0.8f, 380f)
    fun <T> spatialSlow(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(0.8f, 200f)
    fun <T> effectsFast(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(1f, 3800f)
    fun <T> effectsDefault(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(1f, 1600f)
    fun <T> effectsSlow(): FiniteAnimationSpec<T> = if (reduced) snap() else spring(1f, 800f)
}

enum class GlassSurfacePolicy { Auto, Blur, Opaque }

@Immutable
data class SeaGlassSurfaceStyle(
    val policy: GlassSurfacePolicy = GlassSurfacePolicy.Auto,
    val forceFallback: Boolean = false,
    val opacityMultiplier: Float = 1f,
    val fallbackAlpha: Float = 0.9f,
    val panelFallbackAlpha: Float = 0.85f,
    val borderWidth: Dp = 1.dp,
    val highlightMultiplier: Float = 1f,
    val shadowMultiplier: Float = 1f,
    val panelShape: Shape? = null,
) {
    init {
        require(opacityMultiplier.isFinite() && opacityMultiplier >= 0)
        require(fallbackAlpha in 0f..1f && panelFallbackAlpha in 0f..1f)
        require(borderWidth >= 0.dp)
        require(highlightMultiplier.isFinite() && highlightMultiplier >= 0)
        require(shadowMultiplier.isFinite() && shadowMultiplier >= 0)
    }
    fun usesBlur(available: Boolean): Boolean = available && !forceFallback && policy != GlassSurfacePolicy.Opaque
}

/** Override any label, or supply Spanish. Labels never depend on the device locale implicitly. */
@Immutable
data class SeaGlassStrings(
    val close: String = "Close",
    val back: String = "Back",
    val cancel: String = "Cancel",
    val moreOptions: String = "More options",
    val loading: String = "Loading",
    val clear: String = "Clear",
    val search: String = "Search",
    val closeWithoutSaving: String = "Close without saving",
    val create: String = "Create",
    val remove: String = "Remove",
    val clearSearch: String = "Clear search",
    val confirm: String = "OK",
    val selectTime: String = "Select time",
) {
    companion object {
        val English = SeaGlassStrings()
        val Spanish = SeaGlassStrings(
            close = "Cerrar", back = "Volver", cancel = "Cancelar", moreOptions = "Más opciones",
            loading = "Cargando", clear = "Borrar", search = "Buscar", closeWithoutSaving = "Cerrar sin guardar",
            create = "Crear", remove = "Quitar", clearSearch = "Borrar búsqueda", confirm = "Aceptar", selectTime = "Selecciona la hora",
        )
    }
}

internal val LocalSeaGlassConfig = staticCompositionLocalOf { SeaGlassThemeConfig() }
