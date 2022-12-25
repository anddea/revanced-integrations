package app.revanced.integrations.patches.utils;

import com.google.android.apps.youtube.app.watchwhile.WatchWhileActivity;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;

public class DoubleBackToExitPatch {
    private static int pressedCount = 0;
    private static long pressedTime = 0;
    private static boolean isActivated = false;

    public static void doubleBackToExit(WatchWhileActivity activity) {
        long pressedTimeOut = SettingsEnum.DOUBLE_BACK_TO_EXIT_TIMEOUT.getLong();

        if (!isActivated) return;

        if (pressedCount > 2) reset();

        if (System.currentTimeMillis() - pressedTime >= pressedTimeOut) {
            pressedTime = System.currentTimeMillis();
            pressedCount += 1;
        } else if (System.currentTimeMillis() - pressedTime < pressedTimeOut) {
            activity.finish();
        }
    }

    public static void onCreate() {
        isActivated = false;
    }

    public static void onDestroy() {
        isActivated = true;
    }

    public static void playerTypeChanged(PlayerType playerType) {
        if (!(playerType == PlayerType.NONE ||
                playerType == PlayerType.HIDDEN ||
                playerType == PlayerType.WATCH_WHILE_MINIMIZED ||
                playerType == PlayerType.INLINE_MINIMAL
        )) reset();
    }

    public static void reset() {
        pressedCount = 0;
        pressedTime = 0;
        isActivated = false;
    }

}
