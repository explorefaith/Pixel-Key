package com.explorefaith.pixelkey.latin.uix.settings.pages

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.settings.Settings
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItem
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItemStyle
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.SettingToggleSharedPrefs

@Preview
@Composable
fun PredictiveTextScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    ScrollableList {
        ScreenTitle("Predictive Text", showBack = true, navController)

        NavigationItem(
            title = stringResource(R.string.edit_personal_dictionary),
            style = NavigationItemStyle.HomePrimary,
            icon = painterResource(id = R.drawable.book),
            navigate = {
                val intent = Intent("android.settings.USER_DICTIONARY_SETTINGS")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        )

        NavigationItem(
            title = "Blacklisted Suggestions",
            style = NavigationItemStyle.HomeSecondary,
            icon = painterResource(id = R.drawable.file_text),
            navigate = {
                navController.navigate("blacklist")
            }
        )

        NavigationItem(
            title = "Text Shortcuts",
            subtitle = "Expand words like /mail into full text",
            style = NavigationItemStyle.HomeSecondary,
            icon = painterResource(id = R.drawable.file_text),
            navigate = {
                navController.navigate("snippets")
            }
        )

        SettingToggleSharedPrefs(
            title = "Incognito mode",
            subtitle = "No word learning and no clipboard history while typing.",
            key = Settings.PREF_INCOGNITO_MODE,
            default = false
        )

        SettingToggleSharedPrefs(
            title = stringResource(R.string.auto_correction),
            subtitle = stringResource(R.string.auto_correction_summary),
            key = Settings.PREF_AUTO_CORRECTION,
            default = true
        )

        SettingToggleSharedPrefs(
            title = stringResource(R.string.prefs_show_suggestions),
            subtitle = stringResource(R.string.prefs_show_suggestions_summary),
            key = Settings.PREF_SHOW_SUGGESTIONS,
            default = true
        )

        SettingToggleSharedPrefs(
            title = stringResource(R.string.use_personalized_dicts),
            subtitle = stringResource(R.string.use_personalized_dicts_summary),
            key = Settings.PREF_KEY_USE_PERSONALIZED_DICTS,
            default = true
        )

        SettingToggleSharedPrefs(
            title = stringResource(R.string.bigram_prediction),
            subtitle = stringResource(R.string.bigram_prediction_summary),
            key = Settings.PREF_BIGRAM_PREDICTIONS,
            default = booleanResource(R.bool.config_default_next_word_prediction)
        )
    }
}
