package com.example.search;

import java.util.ArrayList;
import java.util.List;

public class KmpSearch implements StringSearch {
    private long comparisons;
    private long memoryCells;

    public int[] lps(String p) {
        int m = p.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            comparisons++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        comparisons = 0;
        memoryCells = 0;
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m > n) {
            return result;
        }
        int[] lps = lps(pattern);
        memoryCells = m;

        int i = 0;
        int j = 0;
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    result.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "kmp";
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getMemoryCells() {
        return memoryCells;
    }
}
