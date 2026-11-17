/*
 * Copyright (c) 2026 Eyasin Arafat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.explorefaith.pixelkey.latin.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Text snippets / shortcuts: the user types a token like "/mail", presses space, and it
 * expands to the configured text. Storage is a JSON object {"shortcut": "expansion"}.
 * Pure functions so the logic is unit-testable on the JVM.
 */
object SnippetUtils {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * @return the expansion for an exact (case-sensitive) match of [typedWord] against a
     * shortcut, or null when there is no match or the store is corrupted.
     */
    @JvmStatic
    fun findExpansion(snippetsJson: String?, typedWord: String?): String? {
        if (snippetsJson.isNullOrEmpty() || typedWord.isNullOrEmpty()) return null
        return runCatching {
            val expansion = Json.parseToJsonElement(snippetsJson)
                .jsonObject[typedWord]?.jsonPrimitive?.contentOrNull
            expansion?.takeIf { it.isNotEmpty() }
        }.getOrNull()
    }

    @JvmStatic
    fun parseSnippets(snippetsJson: String?): Map<String, String> {
        if (snippetsJson.isNullOrEmpty()) return emptyMap()
        return runCatching {
            Json.parseToJsonElement(snippetsJson).jsonObject.entries.associate {
                it.key to (it.value.jsonPrimitive.contentOrNull ?: "")
            }
        }.getOrDefault(emptyMap())
    }

    @JvmStatic
    fun snippetsToJson(snippets: Map<String, String>): String =
        json.encodeToString(serializer(), snippets)

    private fun serializer(): KSerializer<Map<String, String>> =
        MapSerializer(String.serializer(), String.serializer())
}
