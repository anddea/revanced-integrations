package app.revanced.integrations.patches.utils;

import app.revanced.integrations.settings.SettingsEnum;

public class EnableAutoRepeatPatch {

    public static boolean enableAutoRepeat() {
        return SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean();
    }
}
