package app.revanced.music.utils;

import static app.revanced.music.utils.ReVancedUtils.getContext;
import static app.revanced.music.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SharedPrefHelper {
    public static void saveString(String key, String value) {
        Objects.requireNonNull(getPreferences()).edit().putString(key, value).apply();
    }

    public static void saveBoolean(String key, boolean value) {
        Objects.requireNonNull(getPreferences()).edit().putBoolean(key, value).apply();
    }

    public static void saveInteger(String key, Integer value) {
        Objects.requireNonNull(getPreferences()).edit().putInt(key, value).apply();
    }

    public static void saveFloat(String key, float value) {
        Objects.requireNonNull(getPreferences()).edit().putFloat(key, value).apply();
    }

    public static String getString(String key, String _default) {
        return Objects.requireNonNull(getPreferences()).getString(key, _default);
    }

    public static boolean getBoolean(String key, boolean _default) {
        return Objects.requireNonNull(getPreferences()).getBoolean(key, _default);
    }

    public static Integer getInt(String key, Integer _default) {
        return Objects.requireNonNull(getPreferences()).getInt(key, _default);
    }

    public static SharedPreferences getPreferences() {
        var context = getContext();
        if (context == null) return null;
        return context.getSharedPreferences(YOUTUBE.getName(), Context.MODE_PRIVATE);
    }

    public enum SharedPrefNames {

        YOUTUBE("youtube");

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
