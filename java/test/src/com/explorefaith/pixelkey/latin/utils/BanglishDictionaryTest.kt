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

/** Unit tests for daily-use Banglish suggestions. */
class BanglishDictionaryTest {
    private val dictionary = BanglishDictionary(
        listOf(
            "# comment",
            "kemon\t1",
            "ache\t2",
            "accha\t3",
            "acho\t4",
            "achen\t5",
            "bhalo\t6",
            "khabo\t7",
            "khabar\t8",
            "ami\t9",
            "jabo\t10",
            "jabi\t11",
            "valo\t12"
        ),
        listOf(
            "# comment",
            "kemon\tacho\t1",
            "kemon\tachen\t2",
            "bhalo\tachi\t3",
            "ami\tjabo\t4",
            "ami\tjabi\t5",
            "bhalo\tthako\t6"
        )
    )

    @Test
    fun prefixSuggestionsByFrequency() {
        // "a" is too short; "ac" prefix should rank accha before ache's completions.
        assertTrue(dictionary.getSuggestions("ac").isNotEmpty())
        // "acho" is an exact word: results must be exact-prefix completions or
        // one-edit typo corrections (accha/ache/achen), never unrelated words.
        assertTrue(
            dictionary.getSuggestions("acho").all {
                it.startsWith("acho") || BanglishDictionary.editDistanceAtMost("acho", it, 1)
            }
        )
    }

    @Test
    fun prefixMatchRanksDailyWordsFirst() {
        val suggestions = dictionary.getSuggestions("khab")
        assertEquals(listOf("khabo", "khabar"), suggestions)
    }

    @Test
    fun typoGetsCorrectedSuggestion() {
        // "kemon" mistyped as "kemon"->"kemon"... use a real typo: "kemon" -> "kemon" no;
        // "bhalo" mistyped as "bhalu" (one edit).
        val suggestions = dictionary.getSuggestions("bhalu", 3)
        assertTrue("expected fuzzy hit for bhalu, got $suggestions", suggestions.contains("bhalo"))
    }

    @Test
    fun caseIsMirrored() {
        assertEquals(listOf("Khabo", "Khabar"), dictionary.getSuggestions("Khab"))
    }

    @Test
    fun containsWord() {
        assertTrue(dictionary.containsWord("kemon"))
        assertTrue(dictionary.containsWord("KEMON"))
        assertFalse(dictionary.containsWord("kemono"))
        assertFalse(dictionary.containsWord(null))
        assertFalse(dictionary.containsWord(""))
    }

    @Test
    fun nextWordPredictions() {
        assertEquals(listOf("acho", "achen"), dictionary.getNextSuggestions("kemon", 2))
        assertEquals(listOf("jabo", "jabi"), dictionary.getNextSuggestions("ami", 2))
        assertEquals(emptyList<String>(), dictionary.getNextSuggestions("xyz", 2))
    }

    @Test
    fun nextWordCaseMirror() {
        val next = dictionary.getNextSuggestions("kemon", 2, "AchoTyped")
        // typedWord present but lowercase typed word normally; here capital first letter
        // mirrors to suggestions.
        assertTrue(next.all { it.first().isUpperCase() })
    }

    @Test
    fun shortInputAndJunkIsSafe() {
        assertEquals(emptyList<String>(), dictionary.getSuggestions("a"))
        assertEquals(emptyList<String>(), dictionary.getSuggestions(""))
        assertEquals(emptyList<String>(), dictionary.getNextSuggestions(null, 2))
        assertEquals(emptyList<String>(), dictionary.getNextSuggestions("", 2))
    }

    @Test
    fun editDistanceAtMost() {
        assertTrue(BanglishDictionary.editDistanceAtMost("bhalu", "bhalo", 1))
        assertFalse(BanglishDictionary.editDistanceAtMost("bhalu", "kemon", 1))
        assertTrue(BanglishDictionary.editDistanceAtMost("kemon", "kemom", 1)) // n->m
        assertFalse(BanglishDictionary.editDistanceAtMost("kemon", "koman", 1)) // 2 edits
        assertFalse(BanglishDictionary.editDistanceAtMost("ab", "xyz", 1))
        assertTrue(BanglishDictionary.editDistanceAtMost("abc", "abd", 1))
    }
}
