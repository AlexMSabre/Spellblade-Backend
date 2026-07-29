package com.spellblade.operations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GameOperations{


    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHANUMERIC.length(); // Base 62
    private static final int CODE_LENGTH = 4;

    public static String generate4DigitCode(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        try {
            // 1. Hash the input string using SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // 2. Convert the first 4 bytes of the hash into a positive integer
            int number = ((hashBytes[0] & 0xFF) << 24) |
                         ((hashBytes[1] & 0xFF) << 16) |
                         ((hashBytes[2] & 0xFF) << 8)  |
                         ((hashBytes[3] & 0xFF));
            
            // Ensure the number is positive
            number = Math.abs(number);

            // 3. Convert the integer to a Base-62 alphanumeric string of length 4
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                int remainder = number % BASE;
                code.append(ALPHANUMERIC.charAt(remainder));
                number /= BASE;
            }

            return code.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }    
}