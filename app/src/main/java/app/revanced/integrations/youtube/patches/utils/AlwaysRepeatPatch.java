package app.revanced.integrations.youtube.patches.utils;

import app.revanced.integrations.youtube.settings.SettingsEnum;

public class AlwaysRepeatPatch {

    public static boolean enableAlwaysRepeat(boolean original) {
        return !SettingsEnum.ALWAYS_REPEAT.getBoolean() && original;
    }
}
