package app.revanced.integrations.patches.layout;

import app.revanced.integrations.settings.SettingsEnum;

public class SeekbarLayoutPatch {

    public static boolean enableSeekbarTapping() {
        return SettingsEnum.ENABLE_SEEKBAR_TAPPING.getBoolean();
    }

    public static boolean hideTimeAndSeekbar() {
        return SettingsEnum.HIDE_TIME_AND_SEEKBAR.getBoolean();
    }
}
