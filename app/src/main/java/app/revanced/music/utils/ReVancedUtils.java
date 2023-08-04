package app.revanced.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ReVancedUtils {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    private ReVancedUtils() {
    } // utility class

    public static Context getContext() {
        return context;
    }

    public static void hideViewBy0dpUnderCondition(boolean condition, View view) {
        if (!condition) return;
        hideViewByLayoutParams(view);
    }

    public static void hideViewUnderCondition(boolean condition, View view) {
        if (!condition) return;
        view.setVisibility(View.GONE);
    }

    public static void hideViewByLayoutParams(View view) {
        if (view instanceof LinearLayout) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams);
        } else if (view instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams2);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams3);
        } else if (view instanceof Toolbar) {
            Toolbar.LayoutParams layoutParams4 = new Toolbar.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams4);
        } else if (view instanceof ViewGroup) {
            ViewGroup.LayoutParams layoutParams5 = new ViewGroup.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams5);
        }
    }

    public static boolean containsAny(@NonNull String value, @NonNull String... targets) {
        for (String string : targets)
            if (!string.isEmpty() && value.contains(string)) return true;
        return false;
    }

    public static void showToastShort(Context context, String messageToToast) {
        showToast(context, messageToToast);
    }

    /**
     * Safe to call from any thread
     */
    public static void showToastShort(@NonNull String messageToToast) {
        showToast(context, messageToToast);
    }

    private static void showToast(Context context, @NonNull String messageToToast) {
        Objects.requireNonNull(messageToToast);
        runOnMainThreadNowOrLater(() -> {
                    // cannot use getContext(), otherwise if context is null it will cause infinite recursion of error logging
                    assert context != null;
                    Toast.makeText(context, messageToToast, Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * Automatically logs any exceptions the runnable throws.
     *
     * @see #runOnMainThreadNowOrLater(Runnable)
     */
    public static void runOnMainThread(@NonNull Runnable runnable) {
        runOnMainThreadDelayed(runnable, 0);
    }

    /**
     * Automatically logs any exceptions the runnable throws
     */
    public static void runOnMainThreadDelayed(@NonNull Runnable runnable, long delayMillis) {
        Runnable loggingRunnable = () -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                LogHelper.printException(ReVancedUtils.class, runnable.getClass() + ": " + ex.getMessage(), ex);
            }
        };
        new Handler(Looper.getMainLooper()).postDelayed(loggingRunnable, delayMillis);
    }

    /**
     * If called from the main thread, the code is run immediately.<p>
     * If called off the main thread, this is the same as {@link #runOnMainThread(Runnable)}.
     */
    public static void runOnMainThreadNowOrLater(@NonNull Runnable runnable) {
        if (isCurrentlyOnMainThread()) {
            runnable.run();
        } else {
            runOnMainThread(runnable);
        }
    }

    /**
     * @return if the calling thread is on the main thread
     */
    public static boolean isCurrentlyOnMainThread() {
        return Looper.getMainLooper().isCurrentThread();
    }

    /**
     * @throws IllegalStateException if the calling thread is _off_ the main thread
     */
    public static void verifyOnMainThread() throws IllegalStateException {
        if (!isCurrentlyOnMainThread()) {
            throw new IllegalStateException("Must call _on_ the main thread");
        }
    }

    @SuppressLint("MissingPermission") // permission already included in YouTube
    public static NetworkType getNetworkType() {
        assert context != null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        var networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) return NetworkType.NONE;
        return switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_BLUETOOTH ->
                    NetworkType.MOBILE;
            default -> NetworkType.WIFI;
        };
    }

    public enum NetworkType {
        MOBILE("mobile"),
        WIFI("wifi"),
        NONE("none");

        private final String name;

        NetworkType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}