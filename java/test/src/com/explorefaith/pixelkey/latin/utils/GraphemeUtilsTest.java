/*
 * Copyright (c) 2026 Eyasin Arafat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.explorefaith.pixelkey.latin.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for grapheme-cluster handling (STEP 4): backspace must delete one
 * user-perceived character — Bangla conjuncts, emoji sequences, surrogate pairs.
 */
public class GraphemeUtilsTest {
    @Test
    public void banglaConjunctIsOneCluster() {
        // ক্ষ = ক + ্ + ষ = 3 Java chars, 1 grapheme cluster.
        final String conjunct = "\u0995\u09CD\u09B7";
        assertEquals(3, conjunct.length());
        assertEquals(1, GraphemeUtils.countGraphemeClusters(conjunct));
        assertEquals(3, GraphemeUtils.lastGraphemeClusterLength(conjunct));
    }

    @Test
    public void banglaWordClusters() {
        // আমি = আ + ম + ি = 3 clusters; last cluster is the vowel sign ি (1 char).
        final String ami = "\u0986\u09AE\u09BF";
        assertEquals(3, GraphemeUtils.countGraphemeClusters(ami));
        assertEquals(1, GraphemeUtils.lastGraphemeClusterLength(ami));
    }

    @Test
    public void emojiFamilyIsOneCluster() {
        // 👨‍👩‍👧‍👦 = 7 code points + 4 ZWJ = 11 Java chars, one cluster.
        final String family = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        assertEquals(11, family.length());
        assertEquals(1, GraphemeUtils.countGraphemeClusters(family));
        assertEquals(11, GraphemeUtils.lastGraphemeClusterLength(family));
    }

    @Test
    public void surrogatePairIsOneCluster() {
        final String smiley = "\uD83D\uDE42"; // 🙂
        assertEquals(2, smiley.length());
        assertEquals(2, GraphemeUtils.lastGraphemeClusterLength(smiley));
    }

    @Test
    public void asciiAndEmpty() {
        assertEquals(3, GraphemeUtils.countGraphemeClusters("abc"));
        assertEquals(1, GraphemeUtils.lastGraphemeClusterLength("abc"));
        assertEquals(0, GraphemeUtils.countGraphemeClusters(""));
        assertEquals(0, GraphemeUtils.lastGraphemeClusterLength(""));
        assertEquals(0, GraphemeUtils.lastGraphemeClusterLength(null));
    }

    @Test
    public void raHasantYaitsWithZwjIsOneCluster() {
        // র‍্য = র + ZWJ + ্ + য = one deletion unit (Bengali ra-with-yaphala).
        final String raYaphala = "\u09B0\u200D\u09CD\u09AF";
        assertEquals(4, raYaphala.length());
        assertEquals(1, GraphemeUtils.countGraphemeClusters(raYaphala));
        assertEquals(4, GraphemeUtils.lastGraphemeClusterLength(raYaphala));
    }

    @Test
    public void vowelSignDeletedSeparatelyFromBase() {
        // আমি: backspace deletes ি first (1 char), then ম, then আ.
        final String ami = "\u0986\u09AE\u09BF";
        int pos = ami.length();
        final int[] clusterLengths = new int[3];
        int count = 0;
        while (pos > 0) {
            final int len = GraphemeUtils.lastGraphemeClusterLength(ami.substring(0, pos));
            clusterLengths[count++] = len;
            pos -= len;
        }
        assertEquals(3, count);
        assertEquals(1, clusterLengths[0]); // ি
        assertEquals(1, clusterLengths[1]); // ম
        assertEquals(1, clusterLengths[2]); // আ
    }

    @Test
    public void pasteRevertRuleSingleToken() {
        // Mirrors the onTextInput arming heuristic: single token with 1 cluster or
        // <=5 Java chars is revertible; pastes are not.
        final String paste = "hello brave world of keyboards";
        assertTrue(GraphemeUtils.countGraphemeClusters(paste) > 1);
        assertTrue(paste.length() > 5);
        final String comKey = ".com";
        assertEquals(4, comKey.length());
        final String emoji = "\uD83D\uDE42";
        assertEquals(1, GraphemeUtils.countGraphemeClusters(emoji));
    }

    private static void assertTrue(boolean condition) {
        org.junit.Assert.assertTrue(condition);
    }
}
