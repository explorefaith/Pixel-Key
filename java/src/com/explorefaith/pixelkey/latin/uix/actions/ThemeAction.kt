package com.explorefaith.pixelkey.latin.uix.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.Action
import com.explorefaith.pixelkey.latin.uix.ActionWindow
import com.explorefaith.pixelkey.latin.uix.theme.selector.ThemePicker

val ThemeAction = Action(
    icon = R.drawable.themes,
    name = R.string.theme_switcher_action_title,
    simplePressImpl = null,
    canShowKeyboard = true,
    windowImpl = { manager, _ ->
        object : ActionWindow {
            @Composable
            override fun windowName(): String {
                return stringResource(R.string.theme_switcher_action_title)
            }

            @Composable
            override fun WindowContents(keyboardShown: Boolean) {
                ThemePicker { manager.updateTheme(it) }
            }

            override fun close() {

            }
        }
    }
)