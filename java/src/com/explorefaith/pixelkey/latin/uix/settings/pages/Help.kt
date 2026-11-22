package com.explorefaith.pixelkey.latin.uix.settings.pages

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import com.explorefaith.pixelkey.latin.BuildConfig
import com.explorefaith.pixelkey.latin.uix.getSetting
import com.explorefaith.pixelkey.latin.uix.setSetting
import com.explorefaith.pixelkey.latin.uix.settings.DeveloperInfo
import com.explorefaith.pixelkey.latin.utils.CrashLogSaver
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItem
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItemStyle
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.Tip
import com.explorefaith.pixelkey.updates.openURI

@Preview(showBackground = true)
@Composable
fun HelpScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    val numPresses = remember { mutableIntStateOf(0) }
    var lastToast: MutableState<Toast?> = remember { mutableStateOf(null) }

    ScrollableList {
        ScreenTitle("Help & Feedback", showBack = true, navController)

        NavigationItem(
            title = "Version Name: ${BuildConfig.VERSION_NAME}",
            style = NavigationItemStyle.MiscNoArrow,
            navigate = {

            }
        )
        NavigationItem(
            title = "Version Code: ${BuildConfig.VERSION_CODE}",
            style = NavigationItemStyle.MiscNoArrow,
            navigate = {
                val makeToast: (String) -> Unit = { text ->
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).let {
                        lastToast.value?.cancel()
                        lastToast.value = it
                        it.show()
                    }
                }
                if(runBlocking { context.getSetting(IS_DEVELOPER) }) {
                    makeToast("You're already a developer")
                } else {
                    numPresses.intValue += 1
                    if (numPresses.intValue > 3) {
                        val pressesUntilDeveloper = 8 - numPresses.intValue
                        if (pressesUntilDeveloper <= 0) {
                            makeToast("You're now a developer")
                            runBlocking { context.setSetting(IS_DEVELOPER, true) }
                        } else {
                            makeToast("You are $pressesUntilDeveloper steps until becoming a developer")
                        }
                    }
                }
            }
        )

        NavigationItem(
            title = "Report a Bug / Feedback",
            subtitle = "WhatsApp ${DeveloperInfo.WHATSAPP_NUMBER}",
            style = NavigationItemStyle.Misc,
            navigate = {
                DeveloperInfo.openUrl(
                    context,
                    "${DeveloperInfo.WHATSAPP_URL}?text=" + android.net.Uri.encode(
                        "PixelKey bug report / feedback\n\nApp Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\nDevice: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} (Android ${android.os.Build.VERSION.RELEASE})\n\n"
                    )
                )
            }
        )

        NavigationItem(
            title = "Send crash report to developer",
            subtitle = "If the keyboard crashed, send the saved log via WhatsApp",
            style = NavigationItemStyle.Misc,
            navigate = {
                val crash = CrashLogSaver.lastCrashText(context)
                if (crash == null) {
                    Toast.makeText(context, "No crash has been recorded", Toast.LENGTH_SHORT).show()
                } else {
                    DeveloperInfo.openUrl(
                        context,
                        "${DeveloperInfo.WHATSAPP_URL}?text=" + android.net.Uri.encode(
                            "PixelKey CRASH REPORT\nApp Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\nDevice: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} (Android ${android.os.Build.VERSION.RELEASE})\n\n$crash"
                        )
                    )
                }
            }
        )
    }
}