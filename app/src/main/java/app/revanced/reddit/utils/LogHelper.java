package app.revanced.reddit.utils;

import android.util.Log;

import app.revanced.reddit.settings.SettingsEnum;

public class LogHelper {

    private static final String LOG_PREFIX = "Extended: ";

    public static void printDebug(Class<?> clazz, String message) {
        if (!SettingsEnum.DEBUG_LOGGING.getBoolean()) return;
        Log.d(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void printException(Class<?> clazz, String message, Throwable ex) {
        Log.e(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message, ex);
    }

    public static void printException(Class<?> clazz, String message) {
        Log.e(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void info(Class<?> clazz, String message) {
        Log.i(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message);
    }
}
