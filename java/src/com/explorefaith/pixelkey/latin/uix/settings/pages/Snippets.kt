/*
 * Copyright (c) 2026 Eyasin Arafat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.explorefaith.pixelkey.latin.uix.settings.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.settings.Settings
import com.explorefaith.pixelkey.latin.uix.PreferenceUtils
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.SettingItem
import com.explorefaith.pixelkey.latin.uix.settings.SettingToggleSharedPrefs
import com.explorefaith.pixelkey.latin.uix.settings.Tip
import com.explorefaith.pixelkey.latin.utils.SnippetUtils

@Composable
fun SnippetItem(shortcut: String, expansion: String, remove: () -> Unit) {
    SettingItem(shortcut) {
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(expansion, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = remove) {
            Icon(Icons.Default.Clear, contentDescription = "Remove snippet")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SnippetsScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val prefs = PreferenceUtils.getDefaultSharedPreferences(context)
    var snippetsJson by remember {
        mutableStateOf(prefs.getString(Settings.PREF_TEXT_SNIPPETS, "{}") ?: "{}")
    }

    fun persist(newSnippets: Map<String, String>) {
        snippetsJson = SnippetUtils.snippetsToJson(newSnippets)
        prefs.edit().putString(Settings.PREF_TEXT_SNIPPETS, snippetsJson).apply()
    }

    var newShortcut by remember { mutableStateOf("") }
    var newExpansion by remember { mutableStateOf("") }
    val snippets = SnippetUtils.parseSnippets(snippetsJson)

    ScrollableList {
        ScreenTitle("Text Shortcuts", showBack = true, navController)

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp, 0.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newShortcut,
                onValueChange = { newShortcut = it },
                modifier = Modifier.weight(0.35f),
                label = { Text("Shortcut") },
                singleLine = true
            )
            TextField(
                value = newExpansion,
                onValueChange = { newExpansion = it },
                modifier = Modifier.weight(0.65f).padding(start = 8.dp),
                label = { Text("Expands to") },
                singleLine = true
            )
            IconButton(
                onClick = {
                    val key = newShortcut.trim()
                    if (key.isNotEmpty() && newExpansion.isNotEmpty() && !key.contains(" ")) {
                        val updated = snippets.toMutableMap()
                        updated[key] = newExpansion
                        persist(updated)
                        newShortcut = ""
                        newExpansion = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add shortcut")
            }
        }

        Tip("Type the shortcut and press space to expand it. Use a single word, e.g. mail or /mail.")

        if (snippets.isEmpty()) {
            Tip("No shortcuts yet.")
        }
        snippets.forEach { (shortcut, expansion) ->
            SnippetItem(shortcut = shortcut, expansion = expansion) {
                val updated = snippets.toMutableMap()
                updated.remove(shortcut)
                persist(updated)
            }
        }

        SettingToggleSharedPrefs(
            title = "Incognito mode",
            subtitle = "No word learning and no clipboard history while typing.",
            key = Settings.PREF_INCOGNITO_MODE,
            default = false
        )
    }
}
