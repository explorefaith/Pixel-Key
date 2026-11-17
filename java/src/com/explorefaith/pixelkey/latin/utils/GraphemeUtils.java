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

/**
 * Grapheme-cluster helpers. User-perceived characters (emoji sequences, Bangla
 * conjuncts/juktakkhor like ক্ষ, Devanagari clusters) must be deleted as ONE unit,
 * not one Java char.
 *
 * Implemented with deterministic Unicode-category rules instead of BreakIterator so
 * behavior is identical on old Android ICU versions and on the JVM test environment:
 * - combining marks (vowel signs, viramas) attach to their base;
 * - ZWJ/ZWNJ glue adjacent pictographs/letters (👨‍👩‍👧‍👦, র‍্য);
 * - consonant+virama chains keep absorbing consonants (whole conjunct at once).
 */
public final class GraphemeUtils {
    private static final int ZWJ = 0x200D;
    private static final int ZWNJ = 0x200C;
    private static final int DEVANAGARI_VIRAMA = 0x094D;
    private static final int BENGALI_VIRAMA = 0x09CD;

    private GraphemeUtils() {
        // Not instantiable.
    }

    private static boolean isCombiningMark(final int codePoint) {
        final int type = Character.getType(codePoint);
        return type == Character.NON_SPACING_MARK || type == Character.ENCLOSING_MARK
                || type == Character.COMBINING_SPACING_MARK;
    }

    private static boolean isVirama(final int codePoint) {
        return codePoint == DEVANAGARI_VIRAMA || codePoint == BENGALI_VIRAMA;
    }

    /**
     * @return the length in Java chars of the last user-perceived character (grapheme
     * cluster) of {@code text}. Returns 0 for empty text, 1 if nothing can be resolved.
     */
    public static int lastGraphemeClusterLength(final CharSequence text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        return lastClusterLengthBefore(text, text.length());
    }

    /**
     * @return the number of user-perceived characters (grapheme clusters) in {@code text}.
     */
    public static int countGraphemeClusters(final CharSequence text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        int count = 0;
        int pos = text.length();
        while (pos > 0) {
            final int clusterLength = lastClusterLengthBefore(text, pos);
            if (clusterLength <= 0) {
                break;
            }
            pos -= clusterLength;
            count++;
        }
        return count;
    }

    /** Steps back over one base code point, keeping surrogate pairs together. */
    private static int absorbBaseCodePoint(final CharSequence text, int i) {
        if (i >= 2 && Character.isSurrogatePair(text.charAt(i - 2), text.charAt(i - 1))) {
            return i - 2;
        }
        return i - 1;
    }

    private static int lastClusterLengthBefore(final CharSequence text, final int end) {
        if (end <= 0) {
            return 0;
        }
        int i = absorbBaseCodePoint(text, end);
        boolean absorbedVirama = false;
        while (i > 0) {
            final int prevCodePoint = Character.codePointBefore(text, i);
            final int prevLength = Character.charCount(prevCodePoint);
            if (isCombiningMark(prevCodePoint)) {
                // Vowel signs and viramas attach to the base character.
                if (isVirama(prevCodePoint)) {
                    absorbedVirama = true;
                }
                i -= prevLength;
            } else if ((prevCodePoint == ZWJ || prevCodePoint == ZWNJ) && i - prevLength > 0) {
                // ZWJ/ZWNJ glue the cluster to the preceding pictograph or letter.
                final char charBefore = text.charAt(i - prevLength - 1);
                if (Character.isSurrogate(charBefore) || Character.isLetterOrDigit(charBefore)) {
                    i -= prevLength;
                    i = absorbBaseCodePoint(text, i);
                } else {
                    break;
                }
            } else if (absorbedVirama && Character.isLetterOrDigit(prevCodePoint)) {
                // Conjunct continues: consonant + virama + consonant (juktakkhor).
                absorbedVirama = false;
                i -= prevLength;
            } else {
                break;
            }
        }
        final int clusterLength = end - i;
        return clusterLength > 0 ? clusterLength : 1;
    }
}
