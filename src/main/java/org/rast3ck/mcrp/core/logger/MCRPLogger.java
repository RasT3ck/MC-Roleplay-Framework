package org.rast3ck.mcrp.core.logger;

import org.rast3ck.mcrp.core.MCRPConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class MCRPLogger {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private MCRPLogger() {
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    public static void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    public static void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    private static void log(
            LogLevel level,
            String message,
            Throwable throwable
    ) {

        String time = FORMAT.format(LocalDateTime.now());

        System.out.printf(
                "[%s] [%s] [%s] %s%n",
                time,
                MCRPConstants.MOD_NAME,
                level,
                message
        );

        if (throwable != null) {
            throwable.printStackTrace();
        }

    }

}