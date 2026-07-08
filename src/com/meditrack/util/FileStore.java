package com.meditrack.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileStore {
    public static List<String> readLines(String fileName) {
        Path path = Path.of("data", fileName);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            return Files.readAllLines(path);
        } catch (IOException error) {
            System.out.println("Could not read " + fileName + ": " + error.getMessage());
            return new ArrayList<>();
        }
    }

    public static void appendLine(String fileName, String line) {
        Path path = Path.of("data", fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException error) {
            System.out.println("Could not save " + fileName + ": " + error.getMessage());
        }
    }

    public static void writeLines(String fileName, List<String> lines) {
        Path path = Path.of("data", fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException error) {
            System.out.println("Could not update " + fileName + ": " + error.getMessage());
        }
    }
}
