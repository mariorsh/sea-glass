package io.github.mariorsh.seaglass.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Internal copies of the six Material glyphs the components use (check, close, search, add,
 * back arrow, dropdown chevron), so the library does not depend on material-icons-core.
 */
internal object SgIcons {
    val Check: ImageVector
        get() = cachedCheck ?: icon("SgIcons.Check") {
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineToRelative(-1.42f, 1.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineToRelative(-1.41f, -1.41f)
            close()
        }.also { cachedCheck = it }

    val Close: ImageVector
        get() = cachedClose ?: icon("SgIcons.Close") {
            moveTo(19f, 6.41f)
            lineTo(17.59f, 5f)
            lineTo(12f, 10.59f)
            lineTo(6.41f, 5f)
            lineTo(5f, 6.41f)
            lineTo(10.59f, 12f)
            lineTo(5f, 17.59f)
            lineTo(6.41f, 19f)
            lineTo(12f, 13.41f)
            lineTo(17.59f, 19f)
            lineTo(19f, 17.59f)
            lineTo(13.41f, 12f)
            close()
        }.also { cachedClose = it }

    val Search: ImageVector
        get() = cachedSearch ?: icon("SgIcons.Search") {
            moveTo(15.5f, 14f)
            horizontalLineToRelative(-0.79f)
            lineToRelative(-0.28f, -0.27f)
            curveTo(15.41f, 12.59f, 16f, 11.11f, 16f, 9.5f)
            curveTo(16f, 5.91f, 13.09f, 3f, 9.5f, 3f)
            reflectiveCurveTo(3f, 5.91f, 3f, 9.5f)
            reflectiveCurveTo(5.91f, 16f, 9.5f, 16f)
            curveToRelative(1.61f, 0f, 3.09f, -0.59f, 4.23f, -1.57f)
            lineToRelative(0.27f, 0.28f)
            verticalLineToRelative(0.79f)
            lineToRelative(5f, 4.99f)
            lineTo(20.49f, 19f)
            close()
            moveToRelative(-6f, 0f)
            curveTo(7.01f, 14f, 5f, 11.99f, 5f, 9.5f)
            reflectiveCurveTo(7.01f, 5f, 9.5f, 5f)
            reflectiveCurveTo(14f, 7.01f, 14f, 9.5f)
            reflectiveCurveTo(11.99f, 14f, 9.5f, 14f)
            close()
        }.also { cachedSearch = it }

    val Add: ImageVector
        get() = cachedAdd ?: icon("SgIcons.Add") {
            moveTo(19f, 13f)
            horizontalLineToRelative(-6f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-6f)
            horizontalLineTo(5f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(6f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(6f)
            close()
        }.also { cachedAdd = it }

    val ArrowBack: ImageVector
        get() = cachedArrowBack ?: icon("SgIcons.ArrowBack", autoMirror = true) {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineToRelative(5.59f, -5.59f)
            lineTo(12f, 4f)
            lineToRelative(-8f, 8f)
            lineToRelative(8f, 8f)
            lineToRelative(1.41f, -1.41f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            close()
        }.also { cachedArrowBack = it }

    val KeyboardArrowDown: ImageVector
        get() = cachedKeyboardArrowDown ?: icon("SgIcons.KeyboardArrowDown") {
            moveTo(7.41f, 8.59f)
            lineTo(12f, 13.17f)
            lineToRelative(4.59f, -4.58f)
            lineTo(18f, 10f)
            lineToRelative(-6f, 6f)
            lineToRelative(-6f, -6f)
            close()
        }.also { cachedKeyboardArrowDown = it }

    private var cachedCheck: ImageVector? = null
    private var cachedClose: ImageVector? = null
    private var cachedSearch: ImageVector? = null
    private var cachedAdd: ImageVector? = null
    private var cachedArrowBack: ImageVector? = null
    private var cachedKeyboardArrowDown: ImageVector? = null

    private inline fun icon(
        name: String,
        autoMirror: Boolean = false,
        block: PathBuilder.() -> Unit,
    ): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = autoMirror,
    ).path(fill = SolidColor(Color.Black), pathBuilder = block).build()
}
