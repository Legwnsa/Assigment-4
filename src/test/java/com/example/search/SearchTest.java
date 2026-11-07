package com.example.search;

import com.example.experiment.InputGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchTest {

    private final StringSearch[] methods = {new NaiveSearch(), new KmpSearch()};

    @Test
    void referenceMatches() {
        for (StringSearch s : methods) {
            assertEquals(List.of(10), s.search("ABABDABACDABABCABAB", "ABABCABAB"), s.name());
            assertEquals(List.of(0, 1, 2, 3), s.search("AAAAA", "AA"), s.name());
            assertEquals(List.of(0, 2, 4), s.search("ABABABA", "ABA"), s.name());
        }
    }

    @Test
    void noMatchReturnsEmptyList() {
        for (StringSearch s : methods) {
            assertEquals(List.of(), s.search("ABCDEF", "XYZ"), s.name());
            assertEquals(List.of(), s.search("", "A"), s.name());
        }
    }

    @Test
    void emptyPatternThrows() {
        for (StringSearch s : methods) {
            assertThrows(IllegalArgumentException.class, () -> s.search("ABC", ""), s.name());
            assertThrows(IllegalArgumentException.class, () -> s.search("", ""), s.name());
        }
    }

    @Test
    void patternLongerThanTextReturnsEmptyList() {
        for (StringSearch s : methods) {
            assertEquals(List.of(), s.search("AB", "ABC"), s.name());
        }
    }

    @Test
    void patternEqualToText() {
        for (StringSearch s : methods) {
            assertEquals(List.of(0), s.search("ABC", "ABC"), s.name());
        }
    }

    @Test
    void naiveAndKmpAgreeOnRandomTexts() {
        InputGenerator gen = new InputGenerator(42);
        Random rnd = new Random(7);
        NaiveSearch naive = new NaiveSearch();
        KmpSearch kmp = new KmpSearch();
        for (int k = 0; k < 100; k++) {
            String text = gen.randomString(rnd.nextInt(60), 2);
            String pattern = gen.randomString(1 + rnd.nextInt(5), 2);
            assertEquals(naive.search(text, pattern), kmp.search(text, pattern), text + " / " + pattern);
        }
    }

    @Test
    void kmpComparisonsStayLinearOnTheWorstCaseForNaive() {
        int n = 10_000;
        int m = 100;
        String text = InputGenerator.repeated('A', n);
        String pattern = InputGenerator.repeated('A', m - 1) + "B";
        NaiveSearch naive = new NaiveSearch();
        KmpSearch kmp = new KmpSearch();
        naive.search(text, pattern);
        kmp.search(text, pattern);
        assertTrue(naive.getComparisons() >= (long) (n - m + 1) * m);
        assertTrue(kmp.getComparisons() <= 2L * n + 2L * m);
    }
}
