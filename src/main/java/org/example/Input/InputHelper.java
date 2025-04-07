package org.example.Input;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                if(value >= 100) throw new NumberFormatException("Value must be less than 100.");
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = round(Double.parseDouble(scanner.nextLine().trim()),2);
                if (value < 0) throw new NumberFormatException("Value must be non-negative.");
                if(value >= 1e8) throw new NumberFormatException("Slow down millionaire you are too rich");
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
