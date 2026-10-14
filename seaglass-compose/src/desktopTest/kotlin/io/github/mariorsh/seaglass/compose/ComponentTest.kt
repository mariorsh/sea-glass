package io.github.mariorsh.seaglass.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontFamily
import org.junit.Rule
import org.junit.Test
import kotlin.test.*

@OptIn(ExperimentalTestApi::class)
class ComponentTest {
    @get:Rule val rule = createComposeRule()
    private val fonts = SeaGlassFonts(FontFamily.SansSerif, FontFamily.SansSerif, FontFamily.Monospace)

    @Test fun transparentPanelContentDoesNotPaintAnotherRectangle() {
        rule.setContent {
            SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts)) {
                Box(Modifier.size(200.dp).testTag("frame").glassPanel().padding(24.dp)) {
                    Box(Modifier.size(120.dp))
                }
            }
        }
        val image = rule.onNodeWithTag("frame").captureToImage()
        val pixels = image.toPixelMap()
        val outer = pixels[image.width / 2, 15]
        val inner = pixels[image.width / 2, image.height / 2]
        assertEquals(outer.red, inner.red, 0.01f, "Transparent content must not repaint the glass fill")
        assertEquals(outer.alpha, inner.alpha, 0.01f, "The panel must have one uniform translucent fill")
    }

    @Test fun glassShadowRemainsOutsideTheSurface() {
        rule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f)) {
                SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts)) {
                    Box(Modifier.size(200.dp).background(Color.White).testTag("shadow-frame")) {
                        Box(Modifier.padding(40.dp).size(120.dp).glassPanel())
                    }
                }
            }
        }
        val pixels = rule.onNodeWithTag("shadow-frame").captureToImage().toPixelMap()
        val far = pixels[100, 195]
        val shadow = pixels[100, 164]
        assertTrue(shadow.red < far.red - 0.005f, "The fix must keep the exterior shadow")
        val expected = seaGlassLightColors().glass.copy(alpha = 0.85f)
        val actual = pixels[100, 100]
        assertEquals(expected.red * 0.85f + 0.15f, actual.red, 0.01f, "No shadow may tint the interior")
    }

    @Test fun smallButtonsReserveTargetsAndDisabledButtonsDoNotActivate() {
        var clicks = 0
        rule.setContent {
            SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts)) {
                Column {
                    SgButton("Validate", { clicks++ }, size = SgButtonSize.ExtraSmall)
                    SgButton("Disabled", { clicks++ }, enabled = false)
                }
            }
        }
        rule.onNodeWithText("Validate").assertHeightIsAtLeast(48.dp).performClick()
        rule.onNodeWithText("Disabled").assertIsNotEnabled().performClick()
        rule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun themeTokensAreIndependentAndNestedThemesReset() {
        val scheme = seaGlassLightColorScheme().copy(primary = Color.Magenta)
        val colors = seaGlassLightColors().copy(ground = Color.Yellow)
        val shapes = Shapes(small = androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
        rule.setContent {
            SeaGlassTheme(config = SeaGlassThemeConfig(colorScheme = scheme, colors = colors, fonts = fonts, shapes = shapes, strings = SeaGlassStrings.Spanish)) {
                assertEquals(Color.Magenta, MaterialTheme.colorScheme.primary)
                assertEquals(Color.Yellow, SeaGlassTheme.colors.ground)
                assertEquals(shapes, MaterialTheme.shapes)
                assertEquals("Cerrar", SeaGlassTheme.strings.close)
                assertEquals(FontFamily.SansSerif, MaterialTheme.typography.bodyLarge.fontFamily)
                assertEquals(FontFamily.SansSerif, SeaGlassTheme.typography.labelLargeEmphasized.fontFamily)
                SeaGlassTheme(darkTheme = false, config = SeaGlassThemeConfig(fonts = fonts)) {
                    assertEquals(seaGlassLightColors(), SeaGlassTheme.colors)
                    assertEquals(seaGlassLightColorScheme().primary, MaterialTheme.colorScheme.primary)
                    assertEquals("Close", SeaGlassTheme.strings.close)
                }
            }
        }
        rule.waitForIdle()
    }

    @Test fun keyboardActivationAndToggleSemantics() {
        var clicks = 0
        var checked by mutableStateOf(false)
        rule.setContent {
            SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts)) {
                Column {
                    SgButton("Validate", { clicks++ })
                    SgToggleButton(checked, { checked = it }, "Noche")
                }
            }
        }
        val button = rule.onNodeWithText("Validate")
        button.performSemanticsAction(SemanticsActions.RequestFocus) { it() }
        button.assertIsFocused().performKeyInput { keyDown(Key.Enter); keyUp(Key.Enter) }
        rule.runOnIdle { assertEquals(1, clicks) }
        rule.onNodeWithText("Noche").assertIsOff().performClick().assertIsOn()
    }

    @Test fun opaqueOverridesExplicitTranslucentColor() {
        rule.setContent {
            SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts, surfaces = SeaGlassSurfaceStyle(policy = GlassSurfacePolicy.Opaque))) {
                Box(Modifier.size(100.dp).testTag("surface").glass(MaterialTheme.shapes.small, color = Color.Red.copy(alpha = 0.2f)))
            }
        }
        val bitmap = rule.onNodeWithTag("surface").captureToImage()
        val center = bitmap.toPixelMap()[bitmap.width / 2, bitmap.height / 2]
        assertEquals(1f, center.red, 0.01f)
        assertEquals(0f, center.green, 0.01f)
        assertEquals(1f, center.alpha, 0.01f)
    }

    @Test fun opaqueAndUnavailableProvidersNeverReceiveEffectCalls() {
        var calls = 0
        var policy by mutableStateOf(GlassSurfacePolicy.Opaque)
        val provider = object : GlassBackdrop {
            override val isBlurAvailable = true
            override fun modifier(shape: Shape, level: GlassLevel): Modifier { calls++; return Modifier }
        }
        rule.setContent {
            SeaGlassTheme(backdrop = provider, config = SeaGlassThemeConfig(fonts = fonts, surfaces = SeaGlassSurfaceStyle(policy = policy))) {
                Box(Modifier.size(100.dp).glass(MaterialTheme.shapes.small))
                Box(Modifier.size(100.dp).glassPanel())
            }
        }
        rule.runOnIdle { assertEquals(0, calls); policy = GlassSurfacePolicy.Blur }
        rule.waitForIdle()
        rule.runOnIdle { assertTrue(calls >= 2) }
    }

    @Test fun largeTextGrowsAndProgressSemanticsUpdate() {
        var progress by mutableFloatStateOf(0.1f)
        rule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f, 2f)) {
                SeaGlassTheme(config = SeaGlassThemeConfig(fonts = fonts, motion = SeaGlassMotionConfig(reduced = true))) {
                    Column(Modifier.width(160.dp)) {
                        SgButton("A long button label that wraps at large font sizes", {}, size = SgButtonSize.ExtraSmall)
                        SgLinearProgress({ progress }, Modifier.testTag("progress"))
                        SgLoadingIndicator()
                    }
                }
            }
        }
        rule.onNodeWithText("A long button label that wraps at large font sizes").assertHeightIsAtLeast(64.dp)
        rule.onNodeWithTag("progress").assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(0.1f, 0f..1f))
        rule.runOnIdle { progress = 0.8f }
        rule.onNodeWithTag("progress").assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo(0.8f, 0f..1f))
        rule.onNodeWithContentDescription("Loading").assertRangeInfoEquals(androidx.compose.ui.semantics.ProgressBarRangeInfo.Indeterminate)
    }
}
