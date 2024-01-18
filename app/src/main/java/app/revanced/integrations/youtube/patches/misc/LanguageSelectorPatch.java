package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class LanguageSelectorPatch {

    public static boolean enableLanguageSwitch() {
        return SettingsEnum.ENABLE_LANGUAGE_SWITCH.getBoolean();
    }
}
