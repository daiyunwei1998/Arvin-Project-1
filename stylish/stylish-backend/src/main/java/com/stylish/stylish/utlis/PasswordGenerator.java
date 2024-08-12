package com.stylish.stylish.utlis;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
/**
 * Utility class for generating random passwords.
 */
public class PasswordGenerator {

    private final Random random = new SecureRandom();
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+";

    public String generatePassword(int length, boolean useLowercase, boolean useUppercase, boolean useDigits, boolean useSpecialChars) {
        StringBuilder password = new StringBuilder(length);
        String chars = "";

        if (useLowercase) chars += LOWERCASE;
        if (useUppercase) chars += UPPERCASE;
        if (useDigits) chars += DIGITS;
        if (useSpecialChars) chars += SPECIAL_CHARS;

        if (chars.isEmpty()) {
            throw new IllegalArgumentException("At least one character type must be selected");
        }

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
