package io.github.mariorsh.seaglass.compose

import kotlin.test.*

class SurfacePolicyTest {
    @Test fun policyMatrix() {
        for (policy in GlassSurfacePolicy.entries) {
            assertFalse(SeaGlassSurfaceStyle(policy = policy).usesBlur(false), "$policy cannot invent provider support")
            assertFalse(SeaGlassSurfaceStyle(policy = policy, forceFallback = true).usesBlur(true))
            assertEquals(policy != GlassSurfacePolicy.Opaque, SeaGlassSurfaceStyle(policy = policy).usesBlur(true))
        }
    }
    @Test fun invalidTokensFailEarly() {
        assertFailsWith<IllegalArgumentException> { SeaGlassSurfaceStyle(fallbackAlpha = 1.1f) }
        assertFailsWith<IllegalArgumentException> { SeaGlassSurfaceStyle(shadowMultiplier = Float.NaN) }
    }
    @Test fun adapterPresenceIsNotSupport() {
        val noOp = GlassBackdrop { _, _ -> androidx.compose.ui.Modifier }
        assertFalse(noOp.isBlurAvailable)
        assertFalse(GlassBackdrop.None.isBlurAvailable)
        assertTrue(GlassBackdrop.available(noOp).isBlurAvailable)
    }
}
