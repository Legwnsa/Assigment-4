package com.example.edit;

public class RecursiveEditDistance extends CountedEditDistance {
    private String a;
    private String b;
    private int depth;

    @Override
    public int distance(String a, String b) {
        resetCounters();
        this.a = a;
        this.b = b;
        depth = 0;
        return d(a.length(), b.length());
    }

    private int d(int i, int j) {
        ops++;
        depth++;
        if (depth > memoryCells) {
            memoryCells = depth;
        }
        int result;
        if (i == 0) {
            result = j;
        } else if (j == 0) {
            result = i;
        } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
            result = d(i - 1, j - 1);
        } else {
            result = 1 + Math.min(d(i - 1, j), Math.min(d(i, j - 1), d(i - 1, j - 1)));
        }
        depth--;
        return result;
    }

    @Override
    public String name() {
        return "recursive";
    }
}
