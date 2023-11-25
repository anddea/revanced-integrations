package app.revanced.music.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.music.settings.SettingsEnum;

public class LogHelper {

    /**
     * Log messages using lambdas.
     */
    public interface LogMessage {
        @NonNull
        String buildMessageString();
    }

    private static final String LOG_PREFIX = "Extended: ";

    /**
     * Logs debug messages under the outer class name of the code calling this method.
     * Whenever possible, the log string should be constructed entirely inside {@link LogMessage#buildMessageString()}
     * so the performance cost of building strings is paid only if {@link SettingsEnum#ENABLE_DEBUG_LOGGING} is enabled.
     */
    public static void printDebug(@NonNull LogMessage message) {
        if (SettingsEnum.ENABLE_DEBUG_LOGGING.getBoolean()) {
            String logTag = LOG_PREFIX + message.getClass().getSimpleName().replaceAll("\\$.+", "");
            String messageString = message.buildMessageString();
            Log.d(logTag, messageString);
        }
    }

    /**
     * Logs information messages using the outer class name of the code calling this method.
     */
    public static void printInfo(@NonNull LogMessage message) {
        printInfo(message, null);
    }

    /**
     * Logs information messages using the outer class name of the code calling this method.
     */
    public static void printInfo(@NonNull LogMessage message, @Nullable Exception ex) {
        String logTag = LOG_PREFIX + message.getClass().getSimpleName().replaceAll("\\$.+", "");
        String logMessage = message.buildMessageString();
        if (ex == null) {
            Log.i(logTag, logMessage);
        } else {
            Log.i(logTag, logMessage, ex);
        }
    }

    /**
     * Logs exceptions under the outer class name of the code calling this method.
     */
    public static void printException(@NonNull LogMessage message) {
        printException(message, null);
    }

    /**
     * Logs exceptions under the outer class name of the code calling this method.
     * <p>
     * If the calling code is showing it's own error toast,
     * instead use {@link #printInfo(LogMessage, Exception)}
     *
     * @param message log message
     * @param ex      exception (optional)
     */
    public static void printException(@NonNull LogMessage message, @Nullable Throwable ex) {
        String messageString = message.buildMessageString();
        String outerClassSimpleName = message.getClass().getSimpleName().replaceAll("\\$.+", "");
        String logMessage = LOG_PREFIX + outerClassSimpleName;
        if (ex == null) {
            Log.e(logMessage, messageString);
        } else {
            Log.e(logMessage, messageString, ex);
        }
    }

}