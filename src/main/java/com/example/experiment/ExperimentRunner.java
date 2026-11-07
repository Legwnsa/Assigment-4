package com.example.experiment;

import com.example.edit.EditDistance;
import com.example.edit.RecursiveEditDistance;
import com.example.edit.TableEditDistance;
import com.example.edit.TwoRowEditDistance;
import com.example.search.KmpSearch;
import com.example.search.NaiveSearch;
import com.example.search.StringSearch;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class ExperimentRunner {
    static final long seed = 42;
    static final int warmup = 2;
    static final int runs = 5;
    static final int recursive_warmup = 1;
    static final int recursive_runs = 3;

    static final int[] small_sizes = {3, 6, 9, 12};
    static final int[] large_sizes = {500, 1000, 2000, 4000};
    static final int[] search_sizes = {100_000, 200_000, 400_000, 800_000, 1_600_000};
    static final int pattern_length = 100;

    static final String[] edit_inputs = {"random", "identical", "disjoint"};
    static final String[] search_inputs = {"random", "worst_case", "all_match"};

    static final String header = "family,algorithm,input,n,m,time_ms,ops,memory_cells,result";

    static volatile long sink;

    public static void main(String[] args) throws IOException {
        Path out = Path.of(args.length > 0 ? args[0] : "results/results.csv");

        System.out.println("JVM warm-up (discarded)...");
        warmup();

        List<String> rows = new ArrayList<>();
        System.out.println("Edit Distance, all three versions, small n...");
        for (int n : small_sizes) {
            for (String input : edit_inputs) {
                editCase(rows, new RecursiveEditDistance(), input, n, recursive_warmup, recursive_runs);
                editCase(rows, new TableEditDistance(), input, n, warmup, runs);
                editCase(rows, new TwoRowEditDistance(), input, n, warmup, runs);
            }
        }
        System.out.println("Edit Distance, table vs two rows, large n...");
        for (int n : large_sizes) {
            for (String input : edit_inputs) {
                editCase(rows, new TableEditDistance(), input, n, warmup, runs);
                editCase(rows, new TwoRowEditDistance(), input, n, warmup, runs);
            }
        }
        System.out.println("String search...");
        for (int n : search_sizes) {
            for (String input : search_inputs) {
                searchCase(rows, new NaiveSearch(), input, n);
                searchCase(rows, new KmpSearch(), input, n);
            }
        }

        if (out.getParent() != null) {
            Files.createDirectories(out.getParent());
        }
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(out))) {
            w.println(header);
            rows.forEach(w::println);
        }
        System.out.println("Saved " + rows.size() + " rows to " + out);
    }

    private static void warmup() {
        editCase(null, new RecursiveEditDistance(), "disjoint", 9, 1, 1);
        editCase(null, new TableEditDistance(), "random", 1000, 3, 3);
        editCase(null, new TwoRowEditDistance(), "random", 1000, 3, 3);
        searchCase(null, new NaiveSearch(), "worst_case", 100_000);
        searchCase(null, new KmpSearch(), "worst_case", 100_000);
        searchCase(null, new NaiveSearch(), "random", 100_000);
        searchCase(null, new KmpSearch(), "random", 100_000);
    }

    static String[] editInput(String input, int n) {
        InputGenerator gen = new InputGenerator(seed + n);
        switch (input) {
            case "random":
                return new String[]{gen.randomString(n, 4), gen.randomString(n, 4)};
            case "identical":
                String s = gen.randomString(n, 4);
                return new String[]{s, s};
            case "disjoint":
                return new String[]{InputGenerator.repeated('A', n), InputGenerator.repeated('B', n)};
            default:
                throw new IllegalArgumentException(input);
        }
    }

    static String[] searchInput(String input, int n) {
        InputGenerator gen = new InputGenerator(seed + n);
        switch (input) {
            case "random":
                return new String[]{gen.randomString(n, 4), gen.randomString(pattern_length, 4)};
            case "worst_case":
                return new String[]{InputGenerator.repeated('A', n),
                        InputGenerator.repeated('A', pattern_length - 1) + "B"};
            case "all_match":
                return new String[]{InputGenerator.repeated('A', n),
                        InputGenerator.repeated('A', pattern_length)};
            default:
                throw new IllegalArgumentException(input);
        }
    }

    private static void editCase(List<String> rows, EditDistance alg, String input, int n,
                                 int warmup, int runs) {
        String[] ab = editInput(input, n);
        for (int w = 0; w < warmup; w++) {
            sink += alg.distance(ab[0], ab[1]);
        }
        double[] times = new double[runs];
        int result = 0;
        for (int r = 0; r < runs; r++) {
            long t0 = System.nanoTime();
            result = alg.distance(ab[0], ab[1]);
            times[r] = (System.nanoTime() - t0) / 1e6;
        }
        sink += result;
        Arrays.sort(times);
        if (rows != null) {
            rows.add(String.format(Locale.ROOT, "edit_distance,%s,%s,%d,%d,%.4f,%d,%d,%d",
                    alg.name(), input, n, n, times[runs / 2], alg.getOps(), alg.getMemoryCells(), result));
            System.out.printf(Locale.ROOT, "  %-10s %-9s n=%-5d %12.4f ms%n", alg.name(), input, n, times[runs / 2]);
        }
    }

    private static void searchCase(List<String> rows, StringSearch alg, String input, int n) {
        String[] tp = searchInput(input, n);
        for (int w = 0; w < warmup; w++) {
            sink += alg.search(tp[0], tp[1]).size();
        }
        double[] times = new double[runs];
        int matches = 0;
        for (int r = 0; r < runs; r++) {
            long t0 = System.nanoTime();
            matches = alg.search(tp[0], tp[1]).size();
            times[r] = (System.nanoTime() - t0) / 1e6;
        }
        sink += matches;
        Arrays.sort(times);
        if (rows != null) {
            rows.add(String.format(Locale.ROOT, "string_search,%s,%s,%d,%d,%.4f,%d,%d,%d",
                    alg.name(), input, n, tp[1].length(), times[runs / 2],
                    alg.getComparisons(), alg.getMemoryCells(), matches));
            System.out.printf(Locale.ROOT, "  %-6s %-10s n=%-8d %12.4f ms%n", alg.name(), input, n, times[runs / 2]);
        }
    }
}
