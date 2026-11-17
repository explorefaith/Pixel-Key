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

import android.content.SharedPreferences
import org.json.JSONObject

/**
 * Persists the user's "hide suggestions" choice PER APP (not per session). When the user
 * closes the suggestion bar in an app, no word candidates are shown there again until they
 * explicitly re-enable them — the bar must stay dismissed.
 *
 * Stored in default SharedPreferences as a JSON object {"app.package": true, ...} so the
 * mapping logic stays pure and unit-testable.
 */
object SuggestionDismissals {
    private const val PREF_KEY = "suggestions_disabled_apps"

    /** Pure core: is suggestions dismissed for this app? */
    @JvmStatic
    fun isDisabledJson(json: String?, appPackage: String?): Boolean {
        if (json.isNullOrEmpty() || appPackage.isNullOrEmpty()) return false
        return runCatching { JSONObject(json).optBoolean(appPackage, false) }.getOrDefault(false)
    }

    /** Pure core: returns the updated JSON string. */
    @JvmStatic
    fun setDisabledJson(json: String?, appPackage: String?, disabled: Boolean): String {
        if (appPackage.isNullOrEmpty()) return json ?: "{}"
        val obj = runCatching { JSONObject(json ?: "{}") }.getOrDefault(JSONObject())
        if (disabled) {
            obj.put(appPackage, true)
        } else {
            obj.remove(appPackage)
        }
        return obj.toString()
    }

    @JvmStatic
    fun isDisabled(prefs: SharedPreferences?, appPackage: String?): Boolean {
        if (prefs == null) return false
        return isDisabledJson(prefs.getString(PREF_KEY, null), appPackage)
    }

    @JvmStatic
    fun setDisabled(prefs: SharedPreferences?, appPackage: String?, disabled: Boolean) {
        if (prefs == null || appPackage.isNullOrEmpty()) return
        prefs.edit().putString(PREF_KEY, setDisabledJson(prefs.getString(PREF_KEY, null), appPackage, disabled)).apply()
    }
}
