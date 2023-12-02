package app.revanced.music.patches.utils;

import android.app.Instrumentation;
import android.icu.text.DateFormat;
import android.view.KeyEvent;

import java.util.Date;

import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public class InterstitialBannerPatch {
    /**
     * Last time method is called
     */
    private static long lastTimeCalled = 0;

    public static void onBackPressed() {
        final long currentTime = System.currentTimeMillis();
        if (lastTimeCalled != 0 && currentTime - lastTimeCalled < 1000)
            return;

        lastTimeCalled = currentTime;
        LogHelper.printDebug(() -> "onBackPressed: " + DateFormat.getDateTimeInstance().format(new Date(lastTimeCalled)));
        ReVancedUtils.runOnMainThreadDelayed(() -> new Thread(() -> new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)).start(), 1000);
    }
}
