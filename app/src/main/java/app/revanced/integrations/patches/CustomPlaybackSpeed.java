package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class CustomPlaybackSpeed {

    public static boolean isCustomPlaybackSpeedEnabled() {
        return SettingsEnum.CUSTOM_PLAYBACK_SPEED_ENABLED.getBoolean();
    }

}
