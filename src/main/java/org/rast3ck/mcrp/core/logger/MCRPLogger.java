package org.rast3ck.mcrp.core.logger;

import com.mojang.logging.LogUtils;
import org.rast3ck.mcrp.core.MCRPConstants;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class MCRPLogger {

    private static final Logger LOGGER = LogUtils.getLogger();
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

        String formatted = String.format("[%s] [%s] [%s] %s", time, MCRPConstants.MOD_NAME, level, message);

        switch (level) {
            case DEBUG -> {
                if (throwable != null) LOGGER.debug(formatted, throwable);
                else LOGGER.debug(formatted);
            }
            case INFO -> {
                if (throwable != null) LOGGER.info(formatted, throwable);
                else LOGGER.info(formatted);
            }
            case WARN -> {
                if (throwable != null) LOGGER.warn(formatted, throwable);
                else LOGGER.warn(formatted);
            }
            case ERROR -> {
                if (throwable != null) LOGGER.error(formatted, throwable);
                else LOGGER.error(formatted);
            }
        }

    }

}