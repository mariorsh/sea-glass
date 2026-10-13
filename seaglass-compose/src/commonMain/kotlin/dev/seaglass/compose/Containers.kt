package dev.seaglass.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------- Cards

enum class SgCardVariant { Elevated, Filled, Outlined }

private val CardShape = RoundedCornerShape(SeaGlassCorners.ExtraLarge)

/**
 * Card de un solo tema. Elevated (vidrio, por defecto), Filled (`secondary-container`) u Outlined.
 * Con [onClick] toda la card es clicable: no pongas botones dentro en ese caso.
 */
@Composable
fun SgCard(
    modifier: Modifier = Modifier,
    variant: SgCardVariant = SgCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val s = MaterialTheme.colorScheme
    val surface = when (variant) {
        SgCardVariant.Elevated -> Modifier.glass(CardShape, GlassLevel.Level3)
        SgCardVariant.Filled -> Modifier.frost(s.secondaryContainer, CardShape)
        SgCardVariant.Outlined -> Modifier.clip(CardShape).border(1.5.dp, s.outline, CardShape)
    }
    val fg = if (variant == SgCardVariant.Filled) s.onSecondaryContainer else s.onSurface
    Column(
        modifier
            .then(surface)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides fg) { content() }
    }
}

/**
 * Imagen de card o carrusel. Sin foto, deja [content] vacío y elige un tinte de guijarro
 * (`SeaGlassTheme.colors.seafoam`, `cobalt`, `amber`…). Con foto, pon un `Image` con `ContentScale.Crop`.
 */
@Composable
fun SgMedia(
    modifier: Modifier = Modifier,
    color: Color = SeaGlassTheme.colors.seafoam,
    shape: Shape = RoundedCornerShape(SeaGlassCorners.LargeIncreased),
    height: Dp? = 160.dp,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val c = SeaGlassTheme.colors
    Box(
        modifier
            .fillMaxWidth()
            .then(if (height != null) Modifier.height(height) else Modifier)
            .clip(shape)
            .background(color),
    ) {
        content()
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(0.55f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.18f)))
                .border(1.dp, Brush.verticalGradient(0f to c.glassHighlight.copy(alpha = 0.6f), 0.3f to Color.Transparent), shape),
        )
    }
}

/** Título, supertítulo y texto de una card, con el padding interior correcto. */
@Composable
fun SgCardBody(
    title: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    text: String? = null,
) {
    Column(modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (overline != null) {
            Text(overline.uppercase(), style = SeaGlassTheme.typography.captionMono, color = LocalContentColor.current.copy(alpha = 0.8f))
        }
        Text(title, style = MaterialTheme.typography.titleLarge.copy(fontFamily = MaterialTheme.typography.headlineSmall.fontFamily))
        if (text != null) Text(text, style = MaterialTheme.typography.bodyMedium, color = LocalContentColor.current.copy(alpha = 0.85f))
    }
}

/** Fila de acciones al pie de una card, alineada a la derecha. */
@Composable
fun SgCardActions(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.End),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

// ---------------------------------------------------------------- Lists

/** Elemento de lista de una, dos o tres líneas. */
@Composable
fun SgListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    overline: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val s = MaterialTheme.colorScheme
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = if (supporting != null && supporting.length > 60) Alignment.Top else Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (leading != null) leading()
        Column(Modifier.weight(1f)) {
            if (overline != null) Text(overline.uppercase(), style = SeaGlassTheme.typography.captionMono, color = SeaGlassTheme.colors.inkMuted)
            Text(headline, style = MaterialTheme.typography.titleMedium, color = s.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (supporting != null) Text(supporting, style = MaterialTheme.typography.bodyMedium, color = s.onSurfaceVariant, maxLines = 3)
        }
        if (trailing != null) {
            CompositionLocalProvider(LocalContentColor provides s.onSurfaceVariant) { trailing() }
        }
    }
}

/** Icono inicial de lista: guijarro de espuma de 40 dp. */
@Composable
fun SgListIcon(icon: ImageVector, modifier: Modifier = Modifier) {
    val s = MaterialTheme.colorScheme
    Box(modifier.size(40.dp).frost(s.primaryContainer, RoundedCornerShape(SeaGlassCorners.Medium)), contentAlignment = Alignment.Center) {
        Icon(icon, null, Modifier.size(20.dp), tint = s.onPrimaryContainer)
    }
}

/** Metadato final de lista (hora, tamaño) en mono. */
@Composable
fun SgListMeta(text: String) {
    Text(text.uppercase(), style = SeaGlassTheme.typography.captionMono, color = SeaGlassTheme.colors.inkMuted)
}

@Composable
fun SgDivider(modifier: Modifier = Modifier, inset: Boolean = false) {
    HorizontalDivider(modifier.padding(start = if (inset) 64.dp else 0.dp), color = MaterialTheme.colorScheme.outlineVariant)
}

/**
 * Elemento de lista segmentada de Expressive: láminas separadas 2 dp, esquinas grandes fuera y
 * `corner-xs` dentro. Ponlas en un `Column(verticalArrangement = Arrangement.spacedBy(2.dp))`.
 */
@Composable
fun SgSegmentedListItem(
    headline: String,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val s = MaterialTheme.colorScheme
    val c = SeaGlassTheme.colors
    val big = SeaGlassCorners.LargeIncreased
    val small = SeaGlassCorners.ExtraSmall
    val top = if (index == 0) big else small
    val bottom = if (index == count - 1) big else small
    val shape = RoundedCornerShape(topStart = top, topEnd = top, bottomEnd = bottom, bottomStart = bottom)
    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .frost(if (selected) s.primaryContainer else c.glassBar, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            headline,
            style = if (selected) SeaGlassTheme.typography.labelLargeEmphasized else MaterialTheme.typography.bodyLarge,
            color = if (selected) s.onPrimaryContainer else s.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (trailing != null) {
            CompositionLocalProvider(LocalContentColor provides if (selected) s.onPrimaryContainer else s.onSurfaceVariant) { trailing() }
        }
    }
}

// ---------------------------------------------------------------- Carousel

/** Carrusel multi-browse: un elemento grande y el resto encogiéndose hacia el borde. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgMultiBrowseCarousel(
    itemCount: Int,
    modifier: Modifier = Modifier,
    preferredItemWidth: Dp = 240.dp,
    height: Dp = 200.dp,
    item: @Composable BoxScope.(index: Int) -> Unit,
) {
    val state = rememberCarouselState { itemCount }
    HorizontalMultiBrowseCarousel(
        state = state,
        preferredItemWidth = preferredItemWidth,
        modifier = modifier.height(height),
        itemSpacing = 8.dp,
    ) { index ->
        Box(Modifier.fillMaxHeight().maskClip(RoundedCornerShape(SeaGlassCorners.ExtraLarge))) { item(index) }
    }
}

/** Carrusel sin contener: elementos de ancho fijo que se cortan en el borde. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SgUncontainedCarousel(
    itemCount: Int,
    modifier: Modifier = Modifier,
    itemWidth: Dp = 170.dp,
    height: Dp = 110.dp,
    item: @Composable BoxScope.(index: Int) -> Unit,
) {
    val state = rememberCarouselState { itemCount }
    HorizontalUncontainedCarousel(
        state = state,
        itemWidth = itemWidth,
        modifier = modifier.height(height),
        itemSpacing = 8.dp,
    ) { index ->
        Box(Modifier.fillMaxHeight().maskClip(RoundedCornerShape(SeaGlassCorners.ExtraLarge))) { item(index) }
    }
}

/** Celda de carrusel de reserva: guijarro de color con etiqueta opcional abajo. */
@Composable
fun BoxScope.SgCarouselTile(color: Color, label: String? = null) {
    SgMedia(Modifier.fillMaxSize(), color = color, shape = RoundedCornerShape(0.dp), height = null) {
        if (label != null) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
                maxLines = 1,
            )
        }
    }
}
