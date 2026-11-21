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

import android.content.Context
import android.util.Log
import com.explorefaith.pixelkey.latin.R
import java.io.File
import java.util.zip.GZIPInputStream

/**
 * Million-scale Banglish completion index derived from the public-domain
 * tahmid02016/bangla-wordlist (454k Bangla words romanized Avro-style, incl. alternate
 * spellings like komol/komal, sh/s, f/ph, j/z — 1.5M unique forms).
 *
 * Storage: the plain-text index is cached in filesDir once (decompressed from the
 * bundled gz), then kept in memory as one ASCII byte array with a bucket index keyed on
 * the first two letters; each bucket is sorted by (length, alphabetical) so scans
 * early-exit after collecting enough completions.
 *
 * All lookups are byte-level prefix scans — no per-word allocations until a match is
 * returned, so keystroke latency stays in the microsecond range.
 */
class BanglishBigDictionary private constructor(
    private val data: ByteArray,
    private val bucketStarts: HashMap<String, Int>
) {
    /** Bucket key -> end offset (exclusive); start offsets live in bucketStarts. */
    private val bucketEnds: HashMap<String, Int> = HashMap()

    init {
        // Single pass: find bucket boundaries. Lines are grouped by first two letters.
        val sortedKeys = bucketStarts.keys.sortedBy { bucketStarts[it] }
        for (i in sortedKeys.indices) {
            val key = sortedKeys[i]
            val end = if (i + 1 < sortedKeys.size) bucketStarts[sortedKeys[i + 1]]!! else data.size
            bucketEnds[key] = end
        }
    }

    /**
     * @return up to [max] completions for [typedWord] (exact typed word excluded),
     * shortest first, matching the typed case.
     */
    fun getCompletions(typedWord: String, max: Int): List<String> {
        if (typedWord.length < 3 || max <= 0) return emptyList()
        val prefix = typedWord.lowercase()
        val bucket = prefix.substring(0, 2)
        val start = bucketStarts[bucket] ?: return emptyList()
        val end = bucketEnds[bucket] ?: return emptyList()

        val results = ArrayList<String>(max)
        var i = start
        var scanned = 0
        while (i < end && results.size < max && scanned < SCAN_LIMIT) {
            // Extract the line [i, lineEnd)
            var lineEnd = i
            while (lineEnd < end && data[lineEnd] != '\n'.code.toByte()) lineEnd++
            val word = String(data, i, lineEnd - i, Charsets.US_ASCII)
            i = lineEnd + 1
            scanned++
            if (word.length > prefix.length && word.startsWith(prefix)) {
                results.add(mirrorCase(typedWord, word))
            }
        }
        return results
    }

    private fun mirrorCase(source: String, word: String): String {
        if (source.isEmpty() || word.isEmpty()) return word
        return if (source.first().isUpperCase()) {
            word.replaceFirstChar { it.uppercaseChar() }
        } else {
            word
        }
    }

    companion object {
        private const val TAG = "BanglishBigDictionary"

        /** Test/dev factory: builds the index from plain lines (same bucketization). */
        @JvmStatic
        fun fromLines(lines: List<String>): BanglishBigDictionary? {
            val data = (lines.joinToString("\n") + "\n").toByteArray(Charsets.US_ASCII)
            val starts = HashMap<String, Int>()
            var i = 0
            while (i < data.size) {
                if (data[i] == '#'.code.toByte() || data[i] == '\n'.code.toByte()) {
                    while (i < data.size && data[i] != '\n'.code.toByte()) i++
                    i++
                    continue
                }
                var lineEnd = i
                while (lineEnd < data.size && data[lineEnd] != '\n'.code.toByte()) lineEnd++
                if (lineEnd - i >= 2) {
                    val key = String(data, i, 2, Charsets.US_ASCII)
                    starts.putIfAbsent(key, i)
                }
                i = lineEnd + 1
            }
            return if (starts.isEmpty()) null else BanglishBigDictionary(data, starts)
        }
        private const val CACHE_FILE = "banglish_big.txt"
        private const val CACHE_VERSION_FILE = "banglish_big.version"
        /** Bump when the bundled index changes so the cache regenerates. */
        private const val INDEX_VERSION = 1
        private const val SCAN_LIMIT = 8000

        /** Decompresses the bundled gz into filesDir if missing/changed. */
        @JvmStatic
        fun ensureCache(context: Context): File? {
            return try {
                val cache = File(context.filesDir, CACHE_FILE)
                val versionFile = File(context.filesDir, CACHE_VERSION_FILE)
                val assetBytes = context.resources.openRawResource(R.raw.banglish_big_gz)
                    .use { it.available() }
                val cachedVersion = if (versionFile.exists()) versionFile.readText().trim() else ""
                if (cache.exists() && cachedVersion == "$INDEX_VERSION:$assetBytes") {
                    return cache
                }
                // Rewrite atomically-ish: write temp then rename.
                val temp = File(context.filesDir, "$CACHE_FILE.tmp")
                GZIPInputStream(context.resources.openRawResource(R.raw.banglish_big_gz)).use { input ->
                    temp.outputStream().use { output -> input.copyTo(output, 1 shl 16) }
                }
                versionFile.writeText("$INDEX_VERSION:$assetBytes")
                if (cache.exists()) cache.delete()
                if (!temp.renameTo(cache)) {
                    temp.copyTo(cache, overwrite = true)
                    temp.delete()
                }
                cache
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to prepare Banglish big index cache", t)
                null
            }
        }

        /** Loads the index from the cache file; null when unavailable. */
        @JvmStatic
        fun fromCache(context: Context): BanglishBigDictionary? {
            return try {
                val cache = ensureCache(context) ?: return null
                val data = cache.readBytes()
                val starts = HashMap<String, Int>()
                var i = 0
                while (i < data.size) {
                    if (data[i] == '#'.code.toByte() || data[i] == '\n'.code.toByte()) {
                        // Skip header/comment/empty lines
                        while (i < data.size && data[i] != '\n'.code.toByte()) i++
                        i++
                        continue
                    }
                    var lineEnd = i
                    while (lineEnd < data.size && data[lineEnd] != '\n'.code.toByte()) lineEnd++
                    if (lineEnd - i >= 2) {
                        val key = String(data, i, 2, Charsets.US_ASCII)
                        if (!starts.containsKey(key)) {
                            starts[key] = i
                        }
                    }
                    i = lineEnd + 1
                }
                if (starts.isEmpty()) null else BanglishBigDictionary(data, starts)
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to load Banglish big index", t)
                null
            }
        }
    }
}
