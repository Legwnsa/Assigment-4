package com.example.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LpsTest {
    private final KmpSearch kmp = new KmpSearch();

    @Test
    void referenceArrays() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    void shortPatterns() {
        assertArrayEquals(new int[]{0}, kmp.lps("A"));
        assertArrayEquals(new int[]{0, 0, 0}, kmp.lps("ABC"));
        assertArrayEquals(new int[]{0, 1, 2, 3}, kmp.lps("AAAA"));
    }
}
