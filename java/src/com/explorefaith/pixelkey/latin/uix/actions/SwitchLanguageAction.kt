package com.explorefaith.pixelkey.latin.uix.actions

import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.Subtypes
import com.explorefaith.pixelkey.latin.uix.Action


val SwitchLanguageAction = Action(
    icon = R.drawable.globe,
    name = R.string.show_language_switch_key,
    simplePressImpl = { manager, _ ->
        Subtypes.switchToNextLanguage(manager.getContext(), 1)
    },
    altPressImpl = { manager, _ ->
        manager.openInputMethodPicker()
    },
    windowImpl = null,
)