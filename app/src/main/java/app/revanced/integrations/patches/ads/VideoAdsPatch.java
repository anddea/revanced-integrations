package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class VideoAdsPatch {

    public static boolean hideVideoAds() {
        return !SettingsEnum.HIDE_VIDEO_ADS.getBoolean();
    }
}
