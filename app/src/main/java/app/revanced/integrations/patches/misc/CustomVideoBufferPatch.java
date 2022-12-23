package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class CustomVideoBufferPatch {

    public static int setMaxBuffer() {
        int confVal = SettingsEnum.CUSTOM_VIDEO_BUFFER_MAXIMUM.getInt();
        if (confVal < 1) confVal = 1;
        return confVal;
    }

    public static int setPlaybackBuffer() {
        int confVal = SettingsEnum.CUSTOM_VIDEO_BUFFER_PLAYBACK_START.getInt();
        if (confVal < 1) confVal = 1;
        return confVal;
    }

    public static int setReBuffer() {
        int confVal = SettingsEnum.CUSTOM_VIDEO_BUFFER_REBUFFER.getInt();
        if (confVal < 1) confVal = 1;
        return confVal;
    }


}
