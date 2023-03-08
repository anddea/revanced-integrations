package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;

import app.revanced.integrations.utils.LogHelper;

public class VersionOverridePatch {

    /*
     * Context is overridden when trying to play a YouTube video from the Google Play Store,
     * Which is speculated to affect VersionOverridePatch
     */
    public static String getVersionOverride(String version) {

        try {
            boolean isOldLayoutEnabled = getBoolean(REVANCED, "revanced_enable_old_layout", false);

            return isOldLayoutEnabled ? "17.28.35" : version;
        } catch (Exception ex){
            LogHelper.printException(VersionOverridePatch.class, "Failed to getBoolean", ex);
            return version;
        }
    }
}
