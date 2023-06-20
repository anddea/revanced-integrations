package app.revanced.music.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;

/**
 * @noinspection ALL
 */
public class LogHelper {
    private static final String LOG_PREFIX = "Extended: ";

    public static void printException(Class clazz, @NonNull String message, Throwable ex) {
        Log.e(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message, ex);
    }

    public static void printException(Class clazz, @NonNull String message) {
        Log.e(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void printDebug(Class clazz, @NonNull String message) {
        if (SettingsEnum.ENABLE_DEBUG.getBoolean()) {
            Log.d(LOG_PREFIX + (clazz != null ? clazz.getSimpleName() : ""), message);
        }
    }
}