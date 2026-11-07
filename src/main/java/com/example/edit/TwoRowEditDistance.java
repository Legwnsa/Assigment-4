package com.example.edit;

public class TwoRowEditDistance extends CountedEditDistance {

    @Override
    public int distance(String a, String b) {
        resetCounters();
        int n = a.length();
        int m = b.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        memoryCells += 2L * (m + 1);

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
            ops++;
        }
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            ops++;
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    cur[j] = prev[j - 1];
                } else {
                    cur[j] = 1 + Math.min(prev[j], Math.min(cur[j - 1], prev[j - 1]));
                }
                ops++;
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return prev[m];
    }

    @Override
    public String name() {
        return "two_rows";
    }
}
