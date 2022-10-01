package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.settingsmenu.ReVancedSettingsFragment;

public class VersionOverridePatch {

    public static String getVersionOverride(String version) {
        // if the version is newer than v17.28.35 (1530518976) -> true
        boolean after29 = ReVancedSettingsFragment.getVersionCode() > 1530518976;
        // if the version is newer than v17.32.39 (1531051456) & older than v17.38.32 (1531823552) -> true
        boolean hasrotationissue = (ReVancedSettingsFragment.getVersionCode() > 1531051456) && (ReVancedSettingsFragment.getVersionCode() < 1531823552);
        boolean rotation = SettingsEnum.FULLSCREEN_ROTATION.getBoolean();
        boolean oldlayout = SettingsEnum.DISABLE_NEWLAYOUT.getBoolean();
        if (after29 && oldlayout){
            version = "17.28.35";
		} else if (hasrotationissue && rotation && !oldlayout){
            version = "17.32.39";
        }
        return version;
    }
}
