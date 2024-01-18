package app.revanced.integrations.youtube.patches.video;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class HDRVideoPatch {

    public static boolean disableHDRVideo() {
        return !SettingsEnum.DISABLE_HDR_VIDEO.getBoolean();
    }
}
