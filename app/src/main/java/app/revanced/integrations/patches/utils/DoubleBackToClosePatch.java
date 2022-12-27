package app.revanced.integrations.patches.utils;

import com.google.android.apps.youtube.app.watchwhile.WatchWhileActivity;

public class DoubleBackToClosePatch {
    /**
     * Time between two back button presses
     */
    private static final long PRESSED_TIMEOUT_MILLISECONDS = 1000;

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
    public static void closeActivityOnBackPressed(WatchWhileActivity activity) {
        // Check scroll position reaches the top in home feed
        if (!isScrollTop) return;

        // If the time between two back button presses does not reach PRESSED_TIMEOUT_MILLISECONDS,
        // set lastTimeBackPressed to the current time.
        if (System.currentTimeMillis() - lastTimeBackPressed >= PRESSED_TIMEOUT_MILLISECONDS) {
            lastTimeBackPressed = System.currentTimeMillis();
        } else {
            activity.finish();
        }
    }

    /**
     * Detect event when ScrollView is created by RecyclerView
     *
     * @param start of ScrollView
     */
    public static void onStartScrollView() {
        isScrollTop = false;
    }

    /**
     * Detect event when the scroll position reaches the top by the back button
     *
     * @param stop of ScrollView
     */
    public static void onStopScrollView() {
        isScrollTop = true;
    }
}
