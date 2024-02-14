package app.revanced.integrations.youtube.patches.utils;

import static app.revanced.integrations.youtube.utils.StringRef.str;

import android.app.Instrumentation;
import android.view.KeyEvent;

import app.revanced.integrations.youtube.utils.ReVancedUtils;

public class InterstitialBannerPatch {
    /**
     * Last time method is called
     */
    private static volatile long lastTimeCalled = 0;
    private static final Instrumentation instrumentation = new Instrumentation();

    public static void onBackPressed() {
        final long currentTime = System.currentTimeMillis();
        if (lastTimeCalled != 0 && currentTime - lastTimeCalled < 10000)
            return;

        lastTimeCalled = currentTime;
        ReVancedUtils.runOnMainThreadDelayed(() -> {
            // Must run off main thread (Odd, but whatever).
            ReVancedUtils.runOnBackgroundThread(() -> instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK));
        }, 1000);
        ReVancedUtils.runOnMainThreadDelayed(() -> ReVancedUtils.showToastShort(str("revanced_close_interstitial_ads_toast")), 1000);
    }
}
