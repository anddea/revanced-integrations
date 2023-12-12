package app.revanced.integrations.patches.swipe;

import android.annotation.SuppressLint;
import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class SwipeControlsPatch {
    @SuppressLint("StaticFieldLeak")
    public static View engagementOverlay;

    public static boolean disableHDRAutoBrightness() {
        return SettingsEnum.DISABLE_HDR_AUTO_BRIGHTNESS.getBoolean();
    }

    public static boolean isEngagementOverlayVisible() {
        return engagementOverlay != null && engagementOverlay.getVisibility() == View.VISIBLE;
    }

}
