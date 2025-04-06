package org.example.Input;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isBlank()) throw new IllegalArgumentException("Input cannot be blank.");
        return input;
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value <= 0) throw new NumberFormatException("Value must be positive.");
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input: " + e.getMessage());
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) throw new NumberFormatException("Value must be non-negative.");
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input: " + e.getMessage());
            }
        }
    }
}
