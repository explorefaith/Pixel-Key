package com.explorefaith.pixelkey.latin.uix.theme.presets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.theme.ThemeOption

// Matte Bauhaus Nothing Theme Palette
val PixelMatteYellow = Color(0xFFF0C24B)
val PixelMatteBlue = Color(0xFF3B6EA5)
val PixelMatteRed = Color(0xFFD64550)
val PixelMatteWhite = Color(0xFFE9E7E3)
val PixelMatteDark = Color(0xFF212329)
val PixelBlack = Color(0xFF000000)

private val pixelColorScheme = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = PixelMatteDark,
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFF3B6EA5),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = PixelMatteDark,
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = PixelMatteYellow,
    onTertiary = Color(0xFF121212),
    background = PixelBlack,
    onBackground = Color(0xFFFFFFFF),
    surface = PixelBlack,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = PixelMatteDark,
    onSurfaceVariant = Color(0xFFFFFFFF),
    outline = Color(0xFF8E90A0),
    outlineVariant = Color(0xFF2B2D31)
)

val PixelTheme = ThemeOption(
    dynamic = false,
    key = "PixelTheme",
    name = R.string.theme_pixel,
    available = { true }
) {
    pixelColorScheme
}

@Composable
fun PixelPreviewKeyboard() {
    val yellow = PixelMatteYellow
    val blue = PixelMatteBlue
    val dark = PixelMatteDark
    val white = PixelMatteWhite
    val red = PixelMatteRed

    val row1 = listOf(yellow, blue, dark, white, red, blue, white, red, yellow, dark)
    val row2 = listOf(white, red, blue, dark, yellow, white, red, blue, dark)
    val row3 = listOf(yellow, dark, yellow, white, red, blue, dark, yellow, white)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.5.dp, Alignment.CenterHorizontally)
        ) {
            row1.forEach { color ->
                Surface(
                    modifier = Modifier.weight(1f).height(10.dp),
                    color = color,
                    shape = RoundedCornerShape(2.dp)
                ) {}
            }
        }
        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(0.92f),
            horizontalArrangement = Arrangement.spacedBy(1.5.dp, Alignment.CenterHorizontally)
        ) {
            row2.forEach { color ->
                Surface(
                    modifier = Modifier.weight(1f).height(10.dp),
                    color = color,
                    shape = RoundedCornerShape(2.dp)
                ) {}
            }
        }
        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.5.dp, Alignment.CenterHorizontally)
        ) {
            row3.forEach { color ->
                Surface(
                    modifier = Modifier.weight(1f).height(10.dp),
                    color = color,
                    shape = RoundedCornerShape(2.dp)
                ) {}
            }
        }
        // Row 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.5.dp, Alignment.CenterHorizontally)
        ) {
            Surface(modifier = Modifier.weight(1.2f).height(10.dp), color = red, shape = RoundedCornerShape(2.dp)) {}
            Surface(modifier = Modifier.weight(0.9f).height(10.dp), color = blue, shape = RoundedCornerShape(2.dp)) {}
            Surface(modifier = Modifier.weight(0.9f).height(10.dp), color = dark, shape = RoundedCornerShape(2.dp)) {}
            Surface(modifier = Modifier.weight(3.5f).height(10.dp), color = dark, shape = RoundedCornerShape(2.dp)) {}
            Surface(modifier = Modifier.weight(0.9f).height(10.dp), color = white, shape = RoundedCornerShape(2.dp)) {}
            Surface(modifier = Modifier.weight(1.2f).height(10.dp), color = red, shape = RoundedCornerShape(2.dp)) {}
        }
    }
}
