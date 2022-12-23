package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class MinimizedPlaybackPatch {

    public static boolean enableMinimizedPlayback() {
        return SettingsEnum.ENABLE_MINIMIZED_PLAYBACK.getBoolean();
    }

}
