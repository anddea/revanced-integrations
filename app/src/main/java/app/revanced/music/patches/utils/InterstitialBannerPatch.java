package app.revanced.music.patches.utils;

import static app.revanced.music.utils.StringRef.str;

import android.app.Instrumentation;
import android.view.KeyEvent;

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
        ReVancedUtils.runOnMainThreadDelayed(() -> new Thread(() -> new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)).start(), 1000);
        ReVancedUtils.runOnMainThreadDelayed(() -> ReVancedUtils.showToastShort(str("revanced_close_interstitial_ads_toast")), 1000
        );
    }
}
