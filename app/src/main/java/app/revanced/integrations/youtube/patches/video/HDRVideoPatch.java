package app.revanced.integrations.youtube.patches.video;

import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class HDRVideoPatch {

    public static boolean disableHDRVideo() {
        return !Settings.DISABLE_HDR_VIDEO.get();
    }
}
