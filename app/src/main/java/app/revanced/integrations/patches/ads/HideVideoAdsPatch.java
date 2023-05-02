package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

public class HideVideoAdsPatch {

    public static boolean hideVideoAds() {
        return !SettingsEnum.HIDE_VIDEO_ADS.getBoolean();
    }
}
