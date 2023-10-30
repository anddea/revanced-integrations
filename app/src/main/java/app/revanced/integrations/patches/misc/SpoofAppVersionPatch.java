package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class SpoofAppVersionPatch {
    /**
     * This is the most recent version of YouTube without RollingNumber applied.
     * When launching YouTube for the first time,
     * Request should be made to this YouTube version so a/b tests related to RollingNumber will not be fetched.
     */
    private static final String ROLLING_NUMBER_NOT_APPLIED_VERSION = "18.39.41";

    public static String getVersionOverride(String appVersion) {
        // if (!SettingsEnum.INITIALIZED.getBoolean())
        //     return ROLLING_NUMBER_NOT_APPLIED_VERSION;

        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return appVersion;

        return SettingsEnum.SPOOF_APP_VERSION_TARGET.getString();
    }
}
