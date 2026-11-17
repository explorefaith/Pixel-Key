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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Unit tests for per-app suggestion dismissal persistence (BUG-2). */
class SuggestionDismissalsTest {
    @Test
    fun disableAndReEnableRoundTrip() {
        var json = "{}"
        json = SuggestionDismissals.setDisabledJson(json, "com.whatsapp", true)
        assertTrue(SuggestionDismissals.isDisabledJson(json, "com.whatsapp"))
        assertFalse(SuggestionDismissals.isDisabledJson(json, "com.telegram"))

        json = SuggestionDismissals.setDisabledJson(json, "com.whatsapp", false)
        assertFalse(SuggestionDismissals.isDisabledJson(json, "com.whatsapp"))
        assertEquals("{}", json)
    }

    @Test
    fun independentApps() {
        var json = "{}"
        json = SuggestionDismissals.setDisabledJson(json, "com.a", true)
        json = SuggestionDismissals.setDisabledJson(json, "com.b", true)
        assertTrue(SuggestionDismissals.isDisabledJson(json, "com.a"))
        assertTrue(SuggestionDismissals.isDisabledJson(json, "com.b"))
        json = SuggestionDismissals.setDisabledJson(json, "com.a", false)
        assertFalse(SuggestionDismissals.isDisabledJson(json, "com.a"))
        assertTrue(SuggestionDismissals.isDisabledJson(json, "com.b"))
    }

    @Test
    fun nullAndCorruptedInputsAreSafe() {
        assertFalse(SuggestionDismissals.isDisabledJson(null, "com.a"))
        assertFalse(SuggestionDismissals.isDisabledJson("garbage{", "com.a"))
        assertFalse(SuggestionDismissals.isDisabledJson("{}", null))
        // Dismissing with a null package must not corrupt the store.
        val json = SuggestionDismissals.setDisabledJson(null, null, true)
        assertEquals("{}", json)
    }
}
