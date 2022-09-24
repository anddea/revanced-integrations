package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class PremiumHeaderPatch {

    public static boolean getPremiumHeaderOverride() {
        return SettingsEnum.PREMIUM_HEADER.getBoolean();
    }
}