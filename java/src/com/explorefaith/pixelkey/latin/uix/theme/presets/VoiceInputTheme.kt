package com.explorefaith.pixelkey.latin.uix.theme.presets

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.theme.DarkColorScheme
import com.explorefaith.pixelkey.latin.uix.theme.ThemeOption
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePreview

val VoiceInputTheme = ThemeOption(
    dynamic = false,
    key = "VoiceInputTheme",
    name = R.string.voice_input_theme_name,
    available = { true }
) {
    DarkColorScheme
}

@Composable
@Preview
private fun PreviewTheme() {
    ThemePreview(VoiceInputTheme)
}