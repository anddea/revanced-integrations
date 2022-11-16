package app.revanced.integrations.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ReVancedMusicUtils {
    public static Context context;
    public static SharedPreferences sharedPreferences;

    public static Context getAppContext() {
        return context;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}