package app.revanced.integrations.patches.swipe;

import app.revanced.integrations.settings.SettingsEnum;

public class EnableSwipeGestureBrightnessInHDRPatch {

    public static boolean enableSwipeGestureBrightnessInHDR() {
        return SettingsEnum.ENABLE_SWIPE_BRIGHTNESS_HDR.getBoolean();
    }
}
