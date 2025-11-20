package com.awdassignment.primaryschooladministrationwebapplication.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    private static final int WORKLOAD = 12;

    private PasswordUtils() {
    }

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(WORKLOAD));
    }

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return hashedPassword != null && BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}

