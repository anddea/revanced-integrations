package app.revanced.integrations.music.patches.misc;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class SpoofAppVersionPatch {

    public static String getVersionOverride(String version) {
        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return version;

        return SettingsEnum.SPOOF_APP_VERSION_TARGET.getString();
    }
}
