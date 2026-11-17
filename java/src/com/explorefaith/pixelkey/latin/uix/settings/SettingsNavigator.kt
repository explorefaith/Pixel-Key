package com.explorefaith.pixelkey.latin.uix.settings

import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.ErrorDialog
import com.explorefaith.pixelkey.latin.uix.InfoDialog
import com.explorefaith.pixelkey.latin.uix.settings.pages.AdvancedParametersScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.BlacklistScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.DevEditTextVariationsScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.DeveloperScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.HelpScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.HomeScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.LanguagesScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.PredictiveTextScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.SnippetsScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.ThemeScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.addTypingNavigation
import com.explorefaith.pixelkey.latin.uix.urlDecode
import com.explorefaith.pixelkey.latin.uix.urlEncode

// Utility function for quick error messages
fun NavHostController.navigateToError(title: String, body: String) {
    this.navigate("error/${title.urlEncode()}/${body.urlEncode()}")
}

fun NavHostController.navigateToInfo(title: String, body: String) {
    this.navigate("info/${title.urlEncode()}/${body.urlEncode()}")
}

@Composable
fun SettingsNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable("home") { HomeScreen(navController) }
        composable("languages") { LanguagesScreen(navController) }
        composable("addLanguage") { com.explorefaith.pixelkey.latin.uix.settings.pages.AddLanguageScreen(navController) }
        composable("predictiveText") { PredictiveTextScreen(navController) }
        composable("advancedparams") { AdvancedParametersScreen(navController) }
        addTypingNavigation(navController)

        composable("themes") { ThemeScreen(navController) }
        composable("help") { HelpScreen(navController) }
        composable("developer") { DeveloperScreen(navController) }
        composable("devtextedit") { DevEditTextVariationsScreen(navController) }
        composable("blacklist") { BlacklistScreen(navController) }
        composable("snippets") { SnippetsScreen(navController) }

        dialog("error/{title}/{body}") {
            ErrorDialog(
                it.arguments?.getString("title")?.urlDecode() ?: stringResource(R.string.unknown_error),
                it.arguments?.getString("body")?.urlDecode() ?: stringResource(R.string.an_unknown_error_has_occurred),
                navController
            )
        }
        dialog("info/{title}/{body}") {
            InfoDialog(
                it.arguments?.getString("title")?.urlDecode() ?: "",
                it.arguments?.getString("body")?.urlDecode() ?: "",
                navController
            )
        }
    }
}
