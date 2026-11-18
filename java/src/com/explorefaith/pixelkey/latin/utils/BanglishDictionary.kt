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

import android.content.res.Resources
import com.explorefaith.pixelkey.latin.R

/**
 * Daily-use Banglish (romanized Bengali) suggestions for the English QWERTY keyboard.
 *
 * Features:
 * - frequency-ranked wordlist of words people type every day;
 * - prefix matching first, then bounded Levenshtein fallback for typos;
 * - bigram next-word predictions ("kemon" -> "acho/achen");
 * - case mirroring (typed "Ami" -> suggestion "Ami").
 *
 * Pure string logic (lists in, lists out) so it is unit-testable on the JVM; the
 * raw-resource loader is the only Android touchpoint.
 */
class BanglishDictionary(
    wordLines: List<String>,
    bigramLines: List<String>
) {
    /** word -> rank (lower = more frequent). */
    private val words: Map<String, Int>
    /** "first" -> list of (second, rank). */
    private val bigrams: Map<String, List<Pair<String, Int>>>

    init {
        words = wordLines.mapNotNull { parseWordLine(it) }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, ranks) -> ranks.minOrNull() ?: Int.MAX_VALUE }
        bigrams = bigramLines.mapNotNull { parseBigramLine(it) }
            .groupBy({ it.first }, { it.second to it.third })
            .mapValues { (_, pairs) -> pairs.sortedBy { it.second } }
    }

    private fun parseWordLine(line: String): Pair<String, Int>? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return null
        val parts = trimmed.split("\t")
        val word = parts.getOrNull(0)?.trim()?.lowercase() ?: return null
        if (!word.matches(Regex("[a-z]+"))) return null
        val rank = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: Int.MAX_VALUE
        return word to rank
    }

    private fun parseBigramLine(line: String): Triple<String, String, Int>? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return null
        val parts = trimmed.split("\t")
        val first = parts.getOrNull(0)?.trim()?.lowercase() ?: return null
        val second = parts.getOrNull(1)?.trim()?.lowercase() ?: return null
        val rank = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: Int.MAX_VALUE
        return Triple(first, second, rank)
    }

    /** @return true when [word] (case-insensitive) is in the wordlist. */
    fun containsWord(word: String?): Boolean {
        if (word.isNullOrEmpty()) return false
        return words.containsKey(word.lowercase())
    }

    /**
     * Suggestions for the word being typed: exact prefix matches by frequency, then
     * one-or-two-edit typo corrections when prefixes are scarce. Case is mirrored from
     * [typedWord].
     */
    fun getSuggestions(typedWord: String, max: Int = 3): List<String> {
        if (typedWord.length < 2) return emptyList()
        val prefix = typedWord.lowercase()

        val byPrefix = words.entries.asSequence()
            .filter { it.key.startsWith(prefix) && it.key != prefix }
            .sortedBy { it.value }
            .map { it.key }
            .take(max)
            .toMutableList()

        if (byPrefix.size < max && prefix.length >= 3) {
            words.entries.asSequence()
                .filter { !it.key.startsWith(prefix) && it.key != prefix }
                .filter { editDistanceAtMost(prefix, it.key, if (prefix.length >= 5) 2 else 1) }
                .sortedBy { it.value }
                .take(max - byPrefix.size)
                .forEach { byPrefix.add(it.key) }
        }

        return byPrefix.map { mirrorCase(typedWord, it) }
    }

    /**
     * Next-word predictions after a committed Banglish word ("kemon" -> acho/achen).
     * Case is mirrored from [typedWord] when present.
     */
    fun getNextSuggestions(previousWord: String?, max: Int = 2, typedWord: String? = null): List<String> {
        if (previousWord.isNullOrEmpty()) return emptyList()
        val continuations = bigrams[previousWord.lowercase()] ?: return emptyList()
        val picked = continuations.asSequence().map { it.first }.distinct().take(max).toList()
        return picked.map { mirrorCase(typedWord ?: it, it) }
    }

    /** Mirrors the first character's case from [source] onto [word]. */
    private fun mirrorCase(source: String, word: String): String {
        if (source.isEmpty() || word.isEmpty()) return word
        val first = source.first()
        return if (first.isUpperCase()) {
            word.replaceFirstChar { it.uppercaseChar() }
        } else {
            word
        }
    }

    companion object {
        @JvmStatic
        fun fromResources(resources: Resources): BanglishDictionary {
            val wordLines = resources.openRawResource(R.raw.banglish_words)
                .bufferedReader(Charsets.UTF_8).readLines()
            val bigramLines = resources.openRawResource(R.raw.banglish_bigrams)
                .bufferedReader(Charsets.UTF_8).readLines()
            return BanglishDictionary(wordLines, bigramLines)
        }

        /** True when [a] can become [b] within [maxDistance] single-character edits. */
        fun editDistanceAtMost(a: String, b: String, maxDistance: Int): Boolean {
            if (kotlin.math.abs(a.length - b.length) > maxDistance) return false
            var previous = IntArray(a.length + 1) { it }
            val current = IntArray(a.length + 1)
            for (j in 1..b.length) {
                current[0] = j
                for (i in 1..a.length) {
                    val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                    current[i] = minOf(current[i - 1] + 1, previous[i] + 1, previous[i - 1] + cost)
                }
                System.arraycopy(current, 0, previous, 0, current.size)
                if (previous.minOrNull()!! > maxDistance) return false
            }
            return previous[a.length] <= maxDistance
        }
    }
}
