package app.revanced.integrations.patches.swipe;

import app.revanced.integrations.settings.SettingsEnum;

public class DisableHDRAutoBrightnessPatch {

    public static boolean disableHDRAutoBrightness() {
        return SettingsEnum.DISABLE_HDR_AUTO_BRIGHTNESS.getBoolean();
    }
}
