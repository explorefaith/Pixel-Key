package com.explorefaith.pixelkey.latin.uix.settings.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.uix.THEME_KEY
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.useDataStore
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePicker

@Preview
@Composable
fun ThemeScreen(navController: NavHostController = rememberNavController()) {
    val (theme, setTheme) = useDataStore(THEME_KEY.key, THEME_KEY.default)

    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        ScreenTitle("Theme", showBack = true, navController)
        ThemePicker {
            setTheme(it.key)
        }
    }
}