package com.explorefaith.pixelkey.latin.uix.theme

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import com.explorefaith.pixelkey.latin.uix.theme.presets.AMOLEDDarkPurple
import com.explorefaith.pixelkey.latin.uix.theme.presets.AMOLEDNothing
import com.explorefaith.pixelkey.latin.uix.theme.presets.PixelTheme
import com.explorefaith.pixelkey.latin.uix.theme.presets.NothingLight
import com.explorefaith.pixelkey.latin.uix.theme.presets.NothingDark
import com.explorefaith.pixelkey.latin.uix.theme.presets.ClassicMaterialDark
import com.explorefaith.pixelkey.latin.uix.theme.presets.ClassicMaterialLight
import com.explorefaith.pixelkey.latin.uix.theme.presets.CottonCandy
import com.explorefaith.pixelkey.latin.uix.theme.presets.DeepSeaDark
import com.explorefaith.pixelkey.latin.uix.theme.presets.DeepSeaLight
import com.explorefaith.pixelkey.latin.uix.theme.presets.DynamicDarkTheme
import com.explorefaith.pixelkey.latin.uix.theme.presets.DynamicLightTheme
import com.explorefaith.pixelkey.latin.uix.theme.presets.DynamicNothing
import com.explorefaith.pixelkey.latin.uix.theme.presets.DynamicSystemTheme
import com.explorefaith.pixelkey.latin.uix.theme.presets.Emerald
import com.explorefaith.pixelkey.latin.uix.theme.presets.Snowfall
import com.explorefaith.pixelkey.latin.uix.theme.presets.SteelGray
import com.explorefaith.pixelkey.latin.uix.theme.presets.Sunflower
import com.explorefaith.pixelkey.latin.uix.theme.presets.VoiceInputTheme

data class ThemeOption(
    val dynamic: Boolean,
    val key: String,
    @StringRes val name: Int,
    val available: (Context) -> Boolean,
    val obtainColors: (Context) -> ColorScheme,
)

val ThemeOptions = mapOf(
    PixelTheme.key to PixelTheme,
    VoiceInputTheme.key to VoiceInputTheme,

    DynamicNothing.key to DynamicNothing,
    DynamicSystemTheme.key to DynamicSystemTheme,
    DynamicDarkTheme.key to DynamicDarkTheme,
    DynamicLightTheme.key to DynamicLightTheme,

    ClassicMaterialDark.key to ClassicMaterialDark,
    ClassicMaterialLight.key to ClassicMaterialLight,
    AMOLEDDarkPurple.key to AMOLEDDarkPurple,
    AMOLEDNothing.key to AMOLEDNothing,
    NothingLight.key to NothingLight,
    NothingDark.key to NothingDark,

    Sunflower.key to Sunflower,
    Snowfall.key to Snowfall,
    SteelGray.key to SteelGray,
    Emerald.key to Emerald,
    CottonCandy.key to CottonCandy,

    DeepSeaLight.key to DeepSeaLight,
    DeepSeaDark.key to DeepSeaDark,

)

val ThemeOptionKeys = ThemeOptions.keys
