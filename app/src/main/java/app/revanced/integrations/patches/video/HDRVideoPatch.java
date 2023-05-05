package app.revanced.integrations.patches.video;

import app.revanced.integrations.settings.SettingsEnum;

public class HDRVideoPatch {

    public static boolean disableHDRVideo() {
        return !SettingsEnum.DISABLE_HDR_VIDEO.getBoolean();
    }
}
