package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.settingsmenu.ReVancedSettingsFragment;

public class VersionOverridePatch {

    public static String getVersionOverride(String version) {
        // if the version is newer than v17.28.35 (1530518976) -> true
        boolean after29 = ReVancedSettingsFragment.getVersionCode() > 1530518976 ? true : false;
        // if the version is newer than v17.32.39 (1531051456) -> true
        boolean after33 = ReVancedSettingsFragment.getVersionCode() > 1531051456 ? true : false;
        boolean rotation = SettingsEnum.FULLSCREEN_ROTATION.getBoolean();
        boolean oldlayout = SettingsEnum.DISABLE_NEWLAYOUT.getBoolean();
        if (after29 && oldlayout){
            version = "17.28.35";
		} else if (after33 && rotation && !oldlayout){
            version = "17.32.39";
        }
        return version;
    }
}
