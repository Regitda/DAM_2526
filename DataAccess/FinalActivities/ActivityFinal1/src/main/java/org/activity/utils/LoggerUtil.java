package org.activity.utils;

import java.util.logging.Logger;

public class LoggerUtil {
    private static final String BOLD = "\033[1m";
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String YELLOW = "\033[33m";


    public static void logError(final String message) {
        System.out.println(BOLD+RED+message+RESET);
    }
    public static void logWarning(final String message) {
        System.out.println(BOLD+YELLOW+message+RESET);
    }
    public static void logSuccess(final String message) {
        System.out.println(BOLD+GREEN+message+RESET);
    }
    public static void logInfo(final String message) {
        System.out.println(message);
    }
}
