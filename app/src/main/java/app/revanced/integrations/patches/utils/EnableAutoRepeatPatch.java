package app.revanced.integrations.patches.utils;

import app.revanced.integrations.settings.SettingsEnum;

public class EnableAutoRepeatPatch {

    public static boolean enableAutoRepeat(boolean original) {
        return !SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean() && original;
    }

    public static boolean shouldAutoRepeat() {
        return SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean();
    }
}
