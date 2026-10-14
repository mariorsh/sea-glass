package io.github.mariorsh.seaglass.compose

import androidx.compose.ui.unit.*
import kotlin.test.*

class MenuPositionTest {
    private val provider = MenuPositionProvider(4)
    @Test fun logicalStartAndVerticalFlip() {
        val anchor = IntRect(100, 100, 180, 140)
        assertEquals(IntOffset(100, 144), provider.calculatePosition(anchor, IntSize(300, 400), LayoutDirection.Ltr, IntSize(120, 80)))
        assertEquals(IntOffset(60, 144), provider.calculatePosition(anchor, IntSize(300, 400), LayoutDirection.Rtl, IntSize(120, 80)))
        assertEquals(IntOffset(100, 16), provider.calculatePosition(anchor, IntSize(300, 180), LayoutDirection.Ltr, IntSize(120, 80)))
    }
    @Test fun oversizedAndOffscreenAnchorsNeverProduceNegativeOffsets() {
        for (direction in LayoutDirection.entries) {
            assertEquals(IntOffset.Zero, provider.calculatePosition(IntRect(-40, -30, 20, 10), IntSize(100, 80), direction, IntSize(200, 200)))
            val p = provider.calculatePosition(IntRect(290, 390, 330, 410), IntSize(300, 400), direction, IntSize(120, 80))
            assertEquals(IntOffset(180, 306), p)
        }
    }
}
