package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class SpoofAppVersionPatch {

    public static String getVersionOverride(String appVersion) {
        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return appVersion;

        return SettingsEnum.SPOOF_APP_VERSION_TARGET.getString();
    }
}
