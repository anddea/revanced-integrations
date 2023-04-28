package app.revanced.music.patches.misc;

import app.revanced.music.settings.MusicSettingsEnum;

public class SpoofAppVersionPatch {

    public static String getVersionOverride(String version) {
        return MusicSettingsEnum.SPOOF_APP_VERSION.getBoolean() ? "4.27.53" : version;
    }
}
