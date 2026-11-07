package com.example.experiment;

import java.util.Random;

public final class InputGenerator {
    private final Random random;

    public InputGenerator(long seed) {
        this.random = new Random(seed);
    }

    public String randomString(int length, int alphabetSize) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + random.nextInt(alphabetSize)));
        }
        return sb.toString();
    }

    public static String repeated(char c, int length) {
        return String.valueOf(c).repeat(length);
    }
}
