package app.revanced.integrations.utils;

import android.util.Log;

public class LogHelper {

    public static void printException(Class<?> clazz, String message, Throwable ex) {
        Log.e("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message, ex);
    }

    public static void printException(Class<?> clazz, String message) {
        Log.e("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void info(Class<?> clazz, String message) {
        Log.i("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message);
    }
}
