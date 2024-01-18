package app.revanced.integrations.youtube.patches.ads;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class VideoAdsPatch {

    public static boolean hideVideoAds() {
        return !SettingsEnum.HIDE_VIDEO_ADS.getBoolean();
    }
}
