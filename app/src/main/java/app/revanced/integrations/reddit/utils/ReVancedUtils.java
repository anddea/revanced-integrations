package app.revanced.integrations.reddit.utils;

import android.annotation.SuppressLint;
import android.content.Context;

public class ReVancedUtils {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context getContext() {
        return context;
    }
}