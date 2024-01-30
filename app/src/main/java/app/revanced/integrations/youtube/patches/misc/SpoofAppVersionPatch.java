package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class SpoofAppVersionPatch {

    private static final String SPOOF_APP_VERSION_TARGET = SettingsEnum.SPOOF_APP_VERSION_TARGET.getString();

    public static String getVersionOverride(String appVersion) {
        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return appVersion;

        return SPOOF_APP_VERSION_TARGET;
    }

    public static boolean isSpoofingToEqualOrLessThan(String version) {
        return SettingsEnum.SPOOF_APP_VERSION.getBoolean() && SPOOF_APP_VERSION_TARGET.compareTo(version) <= 0;
    }

}
