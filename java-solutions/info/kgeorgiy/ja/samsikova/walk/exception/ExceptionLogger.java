package info.kgeorgiy.ja.samsikova.walk.exception;

import java.io.PrintStream;

public class ExceptionLogger {
    private static final PrintStream PRINT_STREAM = System.err;

    public static void log(String message, Exception cause, boolean printStacktrace) {
        PRINT_STREAM.println(message);
        PRINT_STREAM.println(cause.getMessage());
        if (printStacktrace) {
            cause.printStackTrace(PRINT_STREAM);
        }
    }

    public static void log(String message, Exception cause) {
        log(message, cause, false);
    }
}
