package com.example.edit;

public interface EditDistance {
    int distance(String a, String b);
    String name();
    long getOps();
    long getMemoryCells();
}
