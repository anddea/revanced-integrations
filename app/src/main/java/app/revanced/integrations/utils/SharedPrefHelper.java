package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.ReVancedUtils.getContext;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SharedPrefHelper {
    public static void saveString(SharedPrefNames prefName, String key, String value) {
        Objects.requireNonNull(getPreferences(prefName)).edit().putString(key, value).apply();
    }

    public static void saveBoolean(SharedPrefNames prefName, String key, boolean value) {
        Objects.requireNonNull(getPreferences(prefName)).edit().putBoolean(key, value).apply();
    }

    public static String getString(SharedPrefNames prefName, String key, String _default) {
        return Objects.requireNonNull(getPreferences(prefName)).getString(key, _default);
    }

    public static boolean getBoolean(SharedPrefNames prefName, String key, boolean _default) {
        return Objects.requireNonNull(getPreferences(prefName)).getBoolean(key, _default);
    }

    public static Long getLong(SharedPrefNames prefName, String key, Long _default) {
        SharedPreferences sharedPreferences = getPreferences(prefName);
        try {
            assert sharedPreferences != null;
            return Long.valueOf(Objects.requireNonNull(sharedPreferences.getString(key, String.valueOf(_default))));
        } catch (ClassCastException ex) {
            return sharedPreferences.getLong(key, _default);
        }
    }

    public static Float getFloat(SharedPrefNames prefName, String key, Float _default) {
        SharedPreferences sharedPreferences = getPreferences(prefName);
        try {
            assert sharedPreferences != null;
            return Float.valueOf(Objects.requireNonNull(sharedPreferences.getString(key, String.valueOf(_default))));
        } catch (ClassCastException ex) {
            return sharedPreferences.getFloat(key, _default);
        }
    }

    public static Integer getInt(SharedPrefNames prefName, String key, Integer _default) {
        SharedPreferences sharedPreferences = getPreferences(prefName);
        try {
            assert sharedPreferences != null;
            return Integer.valueOf(Objects.requireNonNull(sharedPreferences.getString(key, String.valueOf(_default))));
        } catch (ClassCastException ex) {
            return sharedPreferences.getInt(key, _default);
        }
    }

    public static SharedPreferences getPreferences(SharedPrefNames name) {
        var context = getContext();
        if (context == null) return null;
        return getPreferences(context, name);
    }

    public static SharedPreferences getPreferences(String name) {
        var context = getContext();
        if (context == null) return null;
        return getPreferences(context, name);
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
