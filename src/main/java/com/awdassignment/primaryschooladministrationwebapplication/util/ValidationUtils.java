package com.awdassignment.primaryschooladministrationwebapplication.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NINE_DIGIT_PATTERN =
            Pattern.compile("^\\d{9}$");

    private ValidationUtils() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidNineDigit(String value) {
        return value != null && NINE_DIGIT_PATTERN.matcher(value).matches();
    }

    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}

