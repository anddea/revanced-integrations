package app.revanced.integrations.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPrefHelper {
    public static void saveString(Context context, SharedPrefNames prefName, String key, String value) {
        getPreferences(context, prefName).edit().putString(key, value).apply();
    }

    public static void saveBoolean(Context context, SharedPrefNames prefName, String key, boolean value) {
        getPreferences(context, prefName).edit().putBoolean(key, value).apply();
    }

    public static void saveFloat(Context context, SharedPrefNames prefName, String key, float value) {
        getPreferences(context, prefName).edit().putFloat(key, value).apply();
    }

    public static void saveInt(Context context, SharedPrefNames prefName, String key, int value) {
        getPreferences(context, prefName).edit().putInt(key, value).apply();
    }

    public static void saveLong(Context context, SharedPrefNames prefName, String key, long value) {
        getPreferences(context, prefName).edit().putLong(key, value).apply();
    }

    public static String getString(Context context, SharedPrefNames prefName, String key, String _default) {
        return getPreferences(context, prefName).getString(key, _default);
    }

    public static boolean getBoolean(Context context, SharedPrefNames prefName, String key, boolean _default) {
        return getPreferences(context, prefName).getBoolean(key, _default);
    }

    public static Long getLong(Context context, SharedPrefNames prefName, String key, Long _default) {
        SharedPreferences sharedPreferences = getPreferences(context, prefName);
        try {
            return Long.valueOf(sharedPreferences.getString(key, _default + ""));
        } catch (ClassCastException ex) {
            return sharedPreferences.getLong(key, _default);
        }
    }

    public static Float getFloat(Context context, SharedPrefNames prefName, String key, Float _default) {
        SharedPreferences sharedPreferences = getPreferences(context, prefName);
        try {
            return Float.valueOf(sharedPreferences.getString(key, _default + ""));
        } catch (ClassCastException ex) {
            return sharedPreferences.getFloat(key, _default);
        }
    }

    public static Integer getInt(Context context, SharedPrefNames prefName, String key, Integer _default) {
        SharedPreferences sharedPreferences = getPreferences(context, prefName);
        try {
            return Integer.valueOf(sharedPreferences.getString(key, _default + ""));
        } catch (ClassCastException ex) {
            return sharedPreferences.getInt(key, _default);
        }
    }

    public static SharedPreferences getPreferences(Context context, SharedPrefNames name) {
        if (context == null) return null;
        return context.getSharedPreferences(name.getName(), Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreferences(Context context, String name) {
        if (context == null) return null;
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public enum SharedPrefNames {

        YOUTUBE("youtube"),
        RYD("ryd"),
        SPONSOR_BLOCK("sponsor-block"),
        REVANCED("revanced");

        private final String name;

        SharedPrefNames(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
