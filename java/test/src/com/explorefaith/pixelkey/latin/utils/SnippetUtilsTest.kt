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
import org.junit.Assert.assertNull
import org.junit.Test

/** Unit tests for the text-snippets feature (STEP 3 #5). */
class SnippetUtilsTest {
    private val store = SnippetUtils.snippetsToJson(
        mapOf("/mail" to "arafat@example.com", "brb" to "be right back")
    )

    @Test
    fun exactMatchExpands() {
        assertEquals("arafat@example.com", SnippetUtils.findExpansion(store, "/mail"))
        assertEquals("be right back", SnippetUtils.findExpansion(store, "brb"))
    }

    @Test
    fun noMatchReturnsNull() {
        assertNull(SnippetUtils.findExpansion(store, "mail"))
        assertNull(SnippetUtils.findExpansion(store, "MAIL"))
        assertNull(SnippetUtils.findExpansion(store, "/mailx"))
        assertNull(SnippetUtils.findExpansion(store, ""))
        assertNull(SnippetUtils.findExpansion(null, "/mail"))
        assertNull(SnippetUtils.findExpansion("{}", "/mail"))
    }

    @Test
    fun corruptedJsonIsSafe() {
        assertNull(SnippetUtils.findExpansion("not json at all{", "/mail"))
        assertEquals(emptyMap<String, String>(), SnippetUtils.parseSnippets("broken"))
    }

    @Test
    fun parseAndSerializeRoundTrip() {
        val parsed = SnippetUtils.parseSnippets(store)
        assertEquals("arafat@example.com", parsed["/mail"])
        assertEquals(store, SnippetUtils.snippetsToJson(parsed))
    }
}
