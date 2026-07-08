package com.meditrack.util;

public class IdGenerator {
    private IdGenerator() {
    }

    public static String nextId(String prefix, int existingCount, int startNumber) {
        return prefix + (startNumber + existingCount + 1);
    }
}
