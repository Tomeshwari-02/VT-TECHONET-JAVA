package com.meditrack.util;

import java.util.Scanner;

public class InputUtil {
    private final Scanner scanner = new Scanner(System.in);

    public String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
