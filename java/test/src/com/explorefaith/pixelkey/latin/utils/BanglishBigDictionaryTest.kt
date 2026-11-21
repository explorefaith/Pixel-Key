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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Unit tests for the million-scale Banglish completion index. */
class BanglishBigDictionaryTest {
    // Fixture mirrors the generated file order: buckets contiguous by first-two-letters,
    // each bucket sorted by (length, alphabetical) — shortest-first completions.
    private val dictionary = BanglishBigDictionary.fromLines(listOf(
        "# header comment",
        "amar",
        "amara",
        "amare",
        "amaro",
        "ambor",
        "amarbone",
        "banana",
        "bandor",
        "bandhobi",
        "kemon",
        "komol",
        "komolot",
        "komolota"
    ))!!

    @Test
    fun prefixCompletions() {
        // Shortest completions first (generator sorts buckets by length, alphabetical).
        assertEquals(
            listOf("amara", "amare", "amaro"),
            dictionary.getCompletions("amar", 3)
        )
    }

    @Test
    fun completionExcludesExactTypedWord() {
        val results = dictionary.getCompletions("amar", 3)
        assertFalse(results.contains("amar"))
        val kemonResults = dictionary.getCompletions("kemon", 3)
        assertFalse(kemonResults.contains("kemon"))
        assertTrue(kemonResults.isEmpty()) // nothing longer starts with kemon in fixture
    }

    @Test
    fun shortestCompletionsFirst() {
        assertEquals(
            listOf("komolot", "komolota"),
            dictionary.getCompletions("komol", 2)
        )
    }

    @Test
    fun caseMirrored() {
        assertEquals(listOf("Amara"), dictionary.getCompletions("Amar", 1))
    }

    @Test
    fun unknownBucketAndShortPrefix() {
        assertNull(BanglishBigDictionary.fromLines(emptyList()))
        assertTrue(dictionary.getCompletions("xy", 3).isEmpty())
        assertTrue(dictionary.getCompletions("xyz", 3).isEmpty())
        assertTrue(dictionary.getCompletions("", 3).isEmpty())
    }

    @Test
    fun maxRespected() {
        assertEquals(2, dictionary.getCompletions("amar", 2).size)
        assertEquals(3, dictionary.getCompletions("amar", 3).size)
    }

    @Test
    fun bucketsDoNotLeakAcrossTwoLetterKeys() {
        // "ba" bucket must not serve "komol" queries and vice versa.
        assertTrue(dictionary.getCompletions("komol", 3).all { it.startsWith("komol") })
        assertTrue(dictionary.getCompletions("ban", 3).all { it.startsWith("ban") })
    }
}
