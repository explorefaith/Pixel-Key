package com.explorefaith.pixelkey.latin.uix.settings.pages

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.uix.settings.DeveloperInfo
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.updates.openURI

@Preview(showBackground = true)
@Composable
fun CreditsScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    ScrollableList {
        ScreenTitle("Pixel Key", showBack = true, navController)

        ParagraphText("Pixel Key is a modern, privacy-first Android keyboard with a clean Bauhaus-inspired design language, developed and maintained by Eyasin Arafat.")

        ScreenTitle("Open Source Foundations")

        ParagraphText("This keyboard is based on the LatinIME keyboard from the Android Open Source Project. Thank you to the original AOSP LatinIME Keyboard developers. The LatinIME keyboard is Apache-2.0 licensed and Copyright (C) 2011 The Android Open Source Project",
            modifier = Modifier.clickable {
                context.openURI("https://android.googlesource.com/platform/packages/inputmethods/LatinIME/")
            })

        ParagraphText("Thank you to the FUTO Keyboard project (keyboard.futo.org) for the modern Compose settings UIX, voice input pipeline and dictionary infrastructure. FUTO Keyboard is Apache-2.0 licensed. Copyright (c) 2023 FUTO Holdings, Inc.",
            modifier = Modifier.clickable {
                context.openURI("https://keyboard.futo.org")
            })

        ParagraphText("Prior community forks contributed layout work, styling and improvements that this project builds upon, including the nth-kbd Nothing-styled fork (Copyright (c) 2024 arunk140) and the Orbit Board fork (Copyright (c) 2024-2026 Fahim Takrim). Both are Apache-2.0 licensed.",
            modifier = Modifier.clickable {
                context.openURI("https://github.com/explorefaith/Pixel-Key")
            })

        ParagraphText("Thank you to llama.cpp, whisper.cpp, ggml devs for making a compact machine learning inference library. These projects are MIT-licensed. Copyright (c) 2023 Georgi Gerganov",
            modifier = Modifier.clickable {
                context.openURI("https://ggml.ai")
            })

        ParagraphText("Thank you to OpenAI Whisper for the speech recognition model which is used for local voice input. Whisper is MIT-licensed. Copyright (c) 2022 OpenAI",
            modifier = Modifier.clickable {
                context.openURI("https://github.com/openai/whisper")
            })

        ParagraphText("Thank you to Feather Icons for providing many of the icons seen in this app. Feather Icons is MIT-licensed. Copyright (c) 2013-2017 Cole Bemis",
            modifier = Modifier.clickable {
                context.openURI("https://feathericons.com")
            })

        ParagraphText("Thank you to the WebRTC project for the voice activity detection used to automatically stop voice input. WebRTC is BSD-3-Clause licensed. Copyright (c) 2011, The WebRTC project authors",
            modifier = Modifier.clickable {
                context.openURI("https://webrtc.org")
            })

        ParagraphText("Thank you to the android-vad project for providing Android bindings to the WebRTC voice activity detection. android-vad is MIT-licensed. Copyright (c) 2023 Georgiy Konovalov",
            modifier = Modifier.clickable {
                context.openURI("https://github.com/gkonovalov/android-vad")
            })

        ParagraphText("Emoji icons are taken from the Noto Emoji font, licensed under OFL.",
            modifier = Modifier.clickable {
                context.openURI("https://fonts.google.com/noto/specimen/Noto+Emoji")
            })

        ParagraphText("Actions rearrangement menu uses the Reorderable library made by Calvin Liang, licensed under Apache-2.0. Reorderable is Copyright (c) 2023 Calvin Liang",
            modifier = Modifier.clickable {
                context.openURI("https://github.com/Calvin-LL/Reorderable")
            })

        ScreenTitle("Developer")
        ParagraphText("Developer & Designer: Eyasin Arafat\nGitHub: @${DeveloperInfo.GITHUB} (tap to open)",
            modifier = Modifier.clickable {
                context.openURI(DeveloperInfo.GITHUB_URL)
            })
        ParagraphText("Instagram: @${DeveloperInfo.INSTAGRAM} (tap to open)",
            modifier = Modifier.clickable {
                context.openURI(DeveloperInfo.INSTAGRAM_URL)
            })
        ParagraphText("WhatsApp: +880 1610 709657 (tap to chat)",
            modifier = Modifier.clickable {
                context.openURI(DeveloperInfo.WHATSAPP_URL)
            })
    }
}
