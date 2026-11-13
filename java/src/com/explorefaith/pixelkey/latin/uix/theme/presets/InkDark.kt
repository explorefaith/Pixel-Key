package com.explorefaith.pixelkey.latin.uix.theme.presets

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.theme.ThemeOption
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePreview

private val md_theme_dark_primary = Color(0xFFA5B4FC)
private val md_theme_dark_onPrimary = Color(0xFF1E1B4B)
private val md_theme_dark_primaryContainer = Color(0xFF3730A3)
private val md_theme_dark_onPrimaryContainer = Color(0xFFE0E7FF)
private val md_theme_dark_secondary = Color(0xFFC7C9D4)
private val md_theme_dark_onSecondary = Color(0xFF1A1B22)
private val md_theme_dark_secondaryContainer = Color(0xFF2A2C36)
private val md_theme_dark_onSecondaryContainer = Color(0xFFE4E5EC)
private val md_theme_dark_tertiary = Color(0xFFFBBF24)
private val md_theme_dark_onTertiary = Color(0xFF422006)
private val md_theme_dark_tertiaryContainer = Color(0xFF5C4208)
private val md_theme_dark_onTertiaryContainer = Color(0xFFFEF3C7)
private val md_theme_dark_error = Color(0xFFF2B8B5)
private val md_theme_dark_onError = Color(0xFF601410)
private val md_theme_dark_errorContainer = Color(0xFF8C1D18)
private val md_theme_dark_onErrorContainer = Color(0xFFF9DEDC)
private val md_theme_dark_outline = Color(0xFF8E90A0)
private val md_theme_dark_background = Color(0xFF101116)
private val md_theme_dark_onBackground = Color(0xFFE9E9EF)
private val md_theme_dark_surface = Color(0xFF16171D)
private val md_theme_dark_onSurface = Color(0xFFE9E9EF)
private val md_theme_dark_surfaceVariant = Color(0xFF232530)
private val md_theme_dark_onSurfaceVariant = Color(0xFFC3C4CF)
private val md_theme_dark_inverseSurface = Color(0xFFE9E9EF)
private val md_theme_dark_inverseOnSurface = Color(0xFF2A2C36)
private val md_theme_dark_inversePrimary = Color(0xFF4F46E5)
private val md_theme_dark_shadow = Color(0xFF000000)
private val md_theme_dark_surfaceTint = Color(0xFFA5B4FC)
private val md_theme_dark_outlineVariant = Color(0xFF2E303C)
private val md_theme_dark_scrim = Color(0xFF000000)

//private val pixel_accent = Color(0xFFA5B4FC)
//private val pixel_text = Color(0xFFE9E9EF)
//private val nothing_black = Color(0xFF000000)
//private val pixel_variant = Color(0xFF16171D);
//private val pixel_on_variant = Color(0xFF16171D);
//private val pixel_container = Color(0xFF232530);

private val pixel_accent = Color(0xFFA5B4FC)
private val pixel_text = Color(0xFFE9E9EF)
private val nothing_black = Color(0xFF000000)
private val pixel_variant = Color(0xFF16171D);
private val pixel_on_variant = Color(0xFF16171D);
private val pixel_container = Color(0xFF232530);

private val colorScheme = darkColorScheme(
    primary = pixel_accent,
    onPrimary = pixel_text,
    primaryContainer = pixel_text,
    onPrimaryContainer = nothing_black,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = pixel_variant,
    onTertiary = pixel_text,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    outline = md_theme_dark_outline,
    background = md_theme_dark_surface,
    onBackground = md_theme_dark_onBackground,
    surface = pixel_on_variant,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surface,
    onSurfaceVariant = pixel_text,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

fun getInkDarkColorScheme(): ColorScheme {
    return colorScheme
}

val InkDark = ThemeOption(
    dynamic = false,
    key = "InkDark",
    name = R.string.theme_ink_dark,
    available = { true }
) {
    colorScheme
}

@Composable
@Preview
private fun PreviewTheme() {
    ThemePreview(InkDark)
}