package app.revanced.integrations.patches.extended;

import app.revanced.integrations.settings.SettingsEnum;

public class VersionOverridePatch {

    public static String getVersionOverride(String version) {
        if (SettingsEnum.ENABLE_OLD_LAYOUT.getBoolean()){
            version = "17.28.35";
        }
        return version;
    }
}
