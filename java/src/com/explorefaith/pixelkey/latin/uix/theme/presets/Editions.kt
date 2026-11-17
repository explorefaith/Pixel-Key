package com.explorefaith.pixelkey.latin.uix.theme.presets

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.theme.ThemeOption
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePreview

// Themed keyboard editions. Each one restyles the whole keyboard and settings
// through its Material color scheme; functional keys keep their accents.

/* ---------------- Naruto Edition — Konoha orange, scroll paper, Akatsuki red ---------------- */
private val narutoOrange = Color(0xFFF97316)
private val narutoScroll = Color(0xFFE7E0D2)
private val narutoRed = Color(0xFFDC2626)
private val narutoBg = Color(0xFF14100A)
private val narutoSurface = Color(0xFF1C1712)
private val narutoVariant = Color(0xFF2A211A)

private val narutoScheme = darkColorScheme(
    primary = narutoOrange,
    onPrimary = Color(0xFF1A0E05),
    primaryContainer = Color(0xFF7C2D12),
    onPrimaryContainer = narutoScroll,
    secondary = narutoScroll,
    onSecondary = Color(0xFF241D14),
    secondaryContainer = narutoVariant,
    onSecondaryContainer = narutoScroll,
    tertiary = narutoRed,
    onTertiary = Color(0xFFFFEDE8),
    tertiaryContainer = Color(0xFF7F1D1D),
    onTertiaryContainer = Color(0xFFFFDAD4),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = narutoBg,
    onBackground = Color(0xFFEDE6DA),
    surface = narutoSurface,
    onSurface = Color(0xFFEDE6DA),
    surfaceVariant = narutoVariant,
    onSurfaceVariant = Color(0xFFC9BFAE),
    outline = Color(0xFF57534E),
    outlineVariant = Color(0xFF3A2F25),
    inversePrimary = Color(0xFF93410B),
    inverseSurface = Color(0xFFEDE6DA),
    inverseOnSurface = Color(0xFF241D14),
    surfaceTint = narutoOrange,
    scrim = Color(0xFF000000)
)

val NarutoEdition = ThemeOption(
    dynamic = false,
    key = "NarutoEdition",
    name = R.string.theme_naruto,
    available = { true }
) { narutoScheme }

/* ---------------- Doraemon Edition — robot blue, bell yellow, collar red ---------------- */
private val doraemonBlue = Color(0xFF29ABE2)
private val doraemonBell = Color(0xFFFDD835)
private val doraemonCollar = Color(0xFFEF5350)
private val doraemonBg = Color(0xFF0B2237)
private val doraemonSurface = Color(0xFF10293E)
private val doraemonVariant = Color(0xFF1B3A55)

private val doraemonScheme = darkColorScheme(
    primary = doraemonBlue,
    onPrimary = Color(0xFF062033),
    primaryContainer = Color(0xFF0E4C74),
    onPrimaryContainer = Color(0xFFD6ECFB),
    secondary = Color(0xFFEAF2F8),
    onSecondary = Color(0xFF0B2237),
    secondaryContainer = doraemonVariant,
    onSecondaryContainer = Color(0xFFD6ECFB),
    tertiary = doraemonBell,
    onTertiary = Color(0xFF3E3200),
    tertiaryContainer = Color(0xFF8A6D00),
    onTertiaryContainer = Color(0xFFFFF0B8),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = doraemonBg,
    onBackground = Color(0xFFE3EEF7),
    surface = doraemonSurface,
    onSurface = Color(0xFFE3EEF7),
    surfaceVariant = doraemonVariant,
    onSurfaceVariant = Color(0xFFB7CADD),
    outline = Color(0xFF2C4A63),
    outlineVariant = Color(0xFF1F3648),
    inversePrimary = Color(0xFF1273A8),
    inverseSurface = Color(0xFFE3EEF7),
    inverseOnSurface = Color(0xFF10293E),
    surfaceTint = doraemonBlue,
    scrim = Color(0xFF000000)
)

val DoraemonEdition = ThemeOption(
    dynamic = false,
    key = "DoraemonEdition",
    name = R.string.theme_doraemon,
    available = { true }
) { doraemonScheme }

/* ---------------- Anime Neon — city-pop pink, violet, cyan glow ---------------- */
private val animePink = Color(0xFFE879F9)
private val animeViolet = Color(0xFF818CF8)
private val animeCyan = Color(0xFF22D3EE)
private val animeBg = Color(0xFF12081F)
private val animeSurface = Color(0xFF1A0E2E)
private val animeVariant = Color(0xFF271540)

private val animeScheme = darkColorScheme(
    primary = animePink,
    onPrimary = Color(0xFF2A0A38),
    primaryContainer = Color(0xFF86198F),
    onPrimaryContainer = Color(0xFFFBD7FC),
    secondary = animeViolet,
    onSecondary = Color(0xFF10103B),
    secondaryContainer = animeVariant,
    onSecondaryContainer = Color(0xFFDBDBFF),
    tertiary = animeCyan,
    onTertiary = Color(0xFF003444),
    tertiaryContainer = Color(0xFF0E7490),
    onTertiaryContainer = Color(0xFFC6F4FB),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = animeBg,
    onBackground = Color(0xFFEADDF6),
    surface = animeSurface,
    onSurface = Color(0xFFEADDF6),
    surfaceVariant = animeVariant,
    onSurfaceVariant = Color(0xFFC6B2DD),
    outline = Color(0xFF4C1D95),
    outlineVariant = Color(0xFF311A4D),
    inversePrimary = Color(0xFFA621B1),
    inverseSurface = Color(0xFFEADDF6),
    inverseOnSurface = Color(0xFF1A0E2E),
    surfaceTint = animePink,
    scrim = Color(0xFF000000)
)

val AnimeNeonEdition = ThemeOption(
    dynamic = false,
    key = "AnimeNeonEdition",
    name = R.string.theme_anime,
    available = { true }
) { animeScheme }

/* ---------------- Nature Edition — forest canopy, leaf green, pollen yellow ---------------- */
private val natureLeaf = Color(0xFF4ADE80)
private val naturePollen = Color(0xFFFACC15)
private val natureMist = Color(0xFFD9E8DC)
private val natureBg = Color(0xFF0D1B12)
private val natureSurface = Color(0xFF122418)
private val natureVariant = Color(0xFF1C3324)

private val natureScheme = darkColorScheme(
    primary = natureLeaf,
    onPrimary = Color(0xFF05140A),
    primaryContainer = Color(0xFF14532D),
    onPrimaryContainer = Color(0xFFC9F2D4),
    secondary = natureMist,
    onSecondary = Color(0xFF14231A),
    secondaryContainer = natureVariant,
    onSecondaryContainer = Color(0xFFC9F2D4),
    tertiary = naturePollen,
    onTertiary = Color(0xFF2E2500),
    tertiaryContainer = Color(0xFF6D5A00),
    onTertiaryContainer = Color(0xFFFDEFA6),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = natureBg,
    onBackground = Color(0xFFE0EBE2),
    surface = natureSurface,
    onSurface = Color(0xFFE0EBE2),
    surfaceVariant = natureVariant,
    onSurfaceVariant = Color(0xFFAEC7B4),
    outline = Color(0xFF365241),
    outlineVariant = Color(0xFF243829),
    inversePrimary = Color(0xFF1F9D53),
    inverseSurface = Color(0xFFE0EBE2),
    inverseOnSurface = Color(0xFF122418),
    surfaceTint = natureLeaf,
    scrim = Color(0xFF000000)
)

val NatureEdition = ThemeOption(
    dynamic = false,
    key = "NatureEdition",
    name = R.string.theme_nature,
    available = { true }
) { natureScheme }

/* ---------------- BMW M Edition — motor blue, silver, M red stripe ---------------- */
private val bmwBlue = Color(0xFF169BD8)
private val bmwSilver = Color(0xFFA7B0BD)
private val bmwRed = Color(0xFFE7222E)
private val bmwBg = Color(0xFF10141C)
private val bmwSurface = Color(0xFF151B26)
private val bmwVariant = Color(0xFF202938)

private val bmwScheme = darkColorScheme(
    primary = bmwBlue,
    onPrimary = Color(0xFF06131F),
    primaryContainer = Color(0xFF0A3D66),
    onPrimaryContainer = Color(0xFFCCE4F5),
    secondary = bmwSilver,
    onSecondary = Color(0xFF141A23),
    secondaryContainer = bmwVariant,
    onSecondaryContainer = Color(0xFFD5DBE3),
    tertiary = bmwRed,
    onTertiary = Color(0xFFFFE7E8),
    tertiaryContainer = Color(0xFF8C1219),
    onTertiaryContainer = Color(0xFFFFD2D4),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = bmwBg,
    onBackground = Color(0xFFE2E6EC),
    surface = bmwSurface,
    onSurface = Color(0xFFE2E6EC),
    surfaceVariant = bmwVariant,
    onSurfaceVariant = Color(0xFFB4BDCA),
    outline = Color(0xFF33415C),
    outlineVariant = Color(0xFF253044),
    inversePrimary = Color(0xFF0C6BA0),
    inverseSurface = Color(0xFFE2E6EC),
    inverseOnSurface = Color(0xFF151B26),
    surfaceTint = bmwBlue,
    scrim = Color(0xFF000000)
)

val BmwEdition = ThemeOption(
    dynamic = false,
    key = "BmwEdition",
    name = R.string.theme_bmw,
    available = { true }
) { bmwScheme }

@Composable
@Preview
private fun PreviewNaruto() { ThemePreview(NarutoEdition) }

@Composable
@Preview
private fun PreviewDoraemon() { ThemePreview(DoraemonEdition) }

@Composable
@Preview
private fun PreviewAnime() { ThemePreview(AnimeNeonEdition) }

@Composable
@Preview
private fun PreviewNature() { ThemePreview(NatureEdition) }

@Composable
@Preview
private fun PreviewBmw() { ThemePreview(BmwEdition) }
