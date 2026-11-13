package com.explorefaith.pixelkey.latin.uix.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.explorefaith.pixelkey.latin.uix.THEME_KEY
import com.explorefaith.pixelkey.latin.uix.settings.useDataStoreValue
import com.explorefaith.pixelkey.latin.uix.theme.presets.VoiceInputTheme
import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt

val PixelAccent = Color(0xFF6366F1)
val PixelInk = Color(0xFF000000)
val PixelCardSurface = Color(0xFF191B22)
val PixelSurfaceVariant2 = Color(0xFF232530)
val PixelCardBorder = Color(0xFF2E303C)
val PixelTextPrimary = Color(0xFFFFFFFF)
val PixelTextMuted = Color(0xFFA0A1AC)

val DarkColorScheme = darkColorScheme(
    primary = PixelAccent,
    onPrimary = PixelTextPrimary,

    primaryContainer = PixelSurfaceVariant2,
    onPrimaryContainer = PixelTextPrimary,

    secondary = PixelTextPrimary,
    onSecondary = PixelInk,

    secondaryContainer = PixelSurfaceVariant2,
    onSecondaryContainer = PixelTextPrimary,

    tertiary = Color(0xFFFBBF24),
    onTertiary = PixelTextPrimary,

    tertiaryContainer = PixelSurfaceVariant2,
    onTertiaryContainer = PixelTextPrimary,

    background = PixelInk,
    onBackground = PixelTextPrimary,

    surface = PixelCardSurface,
    onSurface = PixelTextPrimary,

    outline = PixelCardBorder,

    surfaceVariant = PixelSurfaceVariant2,
    onSurfaceVariant = PixelTextMuted
)

fun applyWindowColors(window: Window, @ColorInt color: Int, statusBar: Boolean) {
    if(statusBar) {
        window.statusBarColor = color
    }
    window.navigationBarColor = color

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val view = window.decorView
        val uiFlags = view.systemUiVisibility

        val luminance = sqrt(
            0.299 * android.graphics.Color.red(color) / 255.0
                    + 0.587 * android.graphics.Color.green(color) / 255.0
                    + 0.114 * android.graphics.Color.blue(color) / 255.0
        )

        if (luminance > 0.5 && color != android.graphics.Color.TRANSPARENT) {
            view.systemUiVisibility = uiFlags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            view.systemUiVisibility = uiFlags and (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
        }
    }
}

@Composable
fun StatusBarColorSetter() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val context = LocalContext.current
    LaunchedEffect(backgroundColor) {
        applyWindowColors((context as Activity).window, backgroundColor.toArgb(), statusBar = false)
    }
}

@Composable
fun UixThemeWrapper(colorScheme: ColorScheme, content: @Composable () -> Unit) {
    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
    )
}

fun ThemeOption?.ensureAvailable(context: Context): ThemeOption? {
    return if(this == null) {
        null
    } else {
        if(!this.available(context)) {
            null
        } else {
            this
        }
    }
}

@Composable
fun UixThemeAuto(content: @Composable () -> Unit) {
    val context = LocalContext.current

    val themeIdx = useDataStoreValue(THEME_KEY)

    val theme: ThemeOption = themeIdx?.let { ThemeOptions[it].ensureAvailable(context) }
        ?: VoiceInputTheme

    val colors = remember(theme.key) { theme.obtainColors(context) }

    UixThemeWrapper(colorScheme = colors, content)
}