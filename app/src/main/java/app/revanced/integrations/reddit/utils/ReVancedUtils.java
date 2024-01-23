package app.revanced.integrations.reddit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public class ReVancedUtils {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context getContext() {
        return context;
    }

    /**
     * Automatically logs any exceptions the runnable throws
     */
    public static void runOnMainThreadDelayed(@NonNull Runnable runnable, long delayMillis) {
        Runnable loggingRunnable = () -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                LogHelper.printException(() -> runnable.getClass() + ": " + ex.getMessage(), ex);
            }
        };
        new Handler(Looper.getMainLooper()).postDelayed(loggingRunnable, delayMillis);
    }
}