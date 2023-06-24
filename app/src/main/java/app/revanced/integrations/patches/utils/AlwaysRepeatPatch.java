package app.revanced.integrations.patches.utils;

import app.revanced.integrations.settings.SettingsEnum;

public class AlwaysRepeatPatch {

    public static boolean enableAlwaysRepeat(boolean original) {
        return !SettingsEnum.ALWAYS_REPEAT.getBoolean() && original;
    }

    public static boolean shouldAlwaysRepeat() {
        return SettingsEnum.ALWAYS_REPEAT.getBoolean();
    }
}
