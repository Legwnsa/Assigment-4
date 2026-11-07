package com.example.search;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearch implements StringSearch {
    private long comparisons;

    @Override
    public List<Integer> search(String text, String pattern) {
        comparisons = 0;
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        for (int start = 0; start + m <= n; start++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(start + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.add(start);
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "naive";
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getMemoryCells() {
        return 0;
    }
}
