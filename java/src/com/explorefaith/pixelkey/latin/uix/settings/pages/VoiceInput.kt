package com.explorefaith.pixelkey.latin.uix.settings.pages

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.AUDIO_FOCUS
import com.explorefaith.pixelkey.latin.uix.DISALLOW_SYMBOLS
import com.explorefaith.pixelkey.latin.uix.ENABLE_SOUND
import com.explorefaith.pixelkey.latin.uix.PREFER_BLUETOOTH
import com.explorefaith.pixelkey.latin.uix.USE_SYSTEM_VOICE_INPUT
import com.explorefaith.pixelkey.latin.uix.VERBOSE_PROGRESS
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItem
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItemStyle
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.SettingToggleDataStore
import com.explorefaith.pixelkey.latin.uix.settings.useDataStore

@Preview
@Composable
fun VoiceInputScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val systemVoiceInput = useDataStore(key = USE_SYSTEM_VOICE_INPUT.key, default = USE_SYSTEM_VOICE_INPUT.default)
    ScrollableList {
        ScreenTitle("Voice Input", showBack = true, navController)

        SettingToggleDataStore(
            title = "Disable built-in voice input",
            subtitle = "Use voice input provided by external app",
            setting = USE_SYSTEM_VOICE_INPUT
        )

        if(!systemVoiceInput.value) {
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

            SettingToggleDataStore(
                title = "Indication sounds",
                subtitle = "Play sounds on start and cancel",
                setting = ENABLE_SOUND
            )

            SettingToggleDataStore(
                title = "Verbose progress",
                subtitle = "Display verbose information such as mic being used",
                setting = VERBOSE_PROGRESS
            )

            SettingToggleDataStore(
                title = "Prefer Bluetooth Mic",
                subtitle = "There may be extra delay to recording starting as Bluetooth SCO connection must be negotiated",
                setting = PREFER_BLUETOOTH
            )


            SettingToggleDataStore(
                title = "Audio Focus",
                subtitle = "Pause videos/music when voice input is activated",
                setting = AUDIO_FOCUS
            )

            SettingToggleDataStore(
                title = "Suppress symbols",
                setting = DISALLOW_SYMBOLS
            )

            NavigationItem(
                title = "Models",
                subtitle = "To change the models, visit Languages & Models menu",
                style = NavigationItemStyle.Misc,
                navigate = { navController.navigate("languages") }
            )
        }
    }
}