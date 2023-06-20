package app.revanced.integrations.patches.utils;

import android.app.Activity;

import app.revanced.integrations.settings.SettingsEnum;

public class DoubleBackToClosePatch {
    /**
     * Time between two back button presses
     */
    private static final long PRESSED_TIMEOUT_MILLISECONDS = SettingsEnum.DOUBLE_BACK_TIMEOUT.getInt() * 1000L;

    /**
     * Last time back button was pressed
     */
    private static long lastTimeBackPressed = 0;

    /**
     * State whether scroll position reaches the top
     */
    private static boolean isScrollTop = false;

    /**
     * Detect event when back button is pressed
     *
     * @param activity is used when closing the app
     */
    public static void closeActivityOnBackPressed(Activity activity) {
        // Check scroll position reaches the top in home feed
        if (!isScrollTop) return;

        // If the time between two back button presses does not reach PRESSED_TIMEOUT_MILLISECONDS,
        // set lastTimeBackPressed to the current time.
        if (System.currentTimeMillis() - lastTimeBackPressed < PRESSED_TIMEOUT_MILLISECONDS ||
                PRESSED_TIMEOUT_MILLISECONDS == 0L)
            activity.finish();
        else
            lastTimeBackPressed = System.currentTimeMillis();
    }

    /**
     * Detect event when ScrollView is created by RecyclerView
     * <p>
     * start of ScrollView
     */
    public static void onStartScrollView() {
        isScrollTop = false;
    }

    /**
     * Detect event when the scroll position reaches the top by the back button
     * <p>
     * stop of ScrollView
     */
    public static void onStopScrollView() {
        isScrollTop = true;
    }
}
