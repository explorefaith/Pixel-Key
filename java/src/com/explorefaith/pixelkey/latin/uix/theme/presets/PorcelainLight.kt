package com.explorefaith.pixelkey.latin.uix.theme.presets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.theme.ThemeOption
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePreview

private val pixel_accent = Color(0xFF4F46E5)
private val pixel_accent_dark = Color(0xFF1E1B4B)
private val pixel_accent_container = Color(0xFFE0E7FF)

private val pixel_slate = Color(0xFF475569)
private val pixel_slate_container = Color(0xFFE2E8F0)
private val pixel_slate_on_container = Color(0xFF0F172A)

private val pixel_amber = Color(0xFFF59E0B)
private val pixel_amber_container = Color(0xFFFEF3C7)
private val pixel_amber_on_container = Color(0xFF451A03)

private val pixel_variant = Color(0xFFE6E6EE)
private val pixel_on_variant = Color(0xFF1A1C24)

private val pixel_background = Color(0xFFF7F7FA)
private val pixel_text = Color(0xFF17181D)
private val pixel_surface = Color(0xFFFFFFFF)
private val pixel_outline = Color(0xFFC7C8D4)
private val pixel_outline_variant = Color(0xFFE1E2EA)

private val colorScheme = lightColorScheme(
    primary = pixel_accent,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = pixel_accent_container,
    onPrimaryContainer = pixel_accent_dark,
    secondary = pixel_slate,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = pixel_slate_container,
    onSecondaryContainer = pixel_slate_on_container,
    tertiary = pixel_amber,
    onTertiary = Color(0xFF422006),
    tertiaryContainer = pixel_amber_container,
    onTertiaryContainer = pixel_amber_on_container,
    error = Color(0xFFBA1A1B),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = pixel_background,
    onBackground = pixel_text,
    surface = pixel_surface,
    onSurface = pixel_text,
    surfaceVariant = pixel_variant,
    onSurfaceVariant = pixel_on_variant,
    outline = pixel_outline,
    inverseOnSurface = Color(0xFFF1F1F5),
    inverseSurface = Color(0xFF2A2C36),
    inversePrimary = Color(0xFFA5B4FC),
    surfaceTint = pixel_accent,
    outlineVariant = pixel_outline_variant,
    scrim = Color(0xFF000000),
)

fun getPorcelainLightColorScheme(): ColorScheme {
    return colorScheme
}

val PorcelainLight = ThemeOption(
    dynamic = false,
    key = "PorcelainLight",
    name = R.string.theme_porcelain_light,
    available = { true }
) {
    colorScheme
}

@Composable
@Preview
private fun PreviewTheme() {
    ThemePreview(PorcelainLight)
}