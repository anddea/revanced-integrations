package app.revanced.music.patches.misc;

import app.revanced.music.settings.SettingsEnum;

public class SpoofAppVersionPatch {

    public static String getVersionOverride(String version) {
        return SettingsEnum.SPOOF_APP_VERSION.getBoolean() ? "4.27.53" : version;
    }
}
