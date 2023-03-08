package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;

import app.revanced.integrations.utils.LogHelper;

public class LayoutOverridePatch {
    /*
     * Context is overridden when trying to play a YouTube video from the Google Play Store,
     * Which is speculated to affect LayoutOverridePatch
     */
    public static int getLayoutOverride(int original) {
        try {
            boolean isTabletLayoutEnabled = getBoolean(REVANCED, "revanced_enable_tablet_layout", false);
            boolean isPhoneLayoutEnabled = getBoolean(REVANCED, "revanced_enable_phone_layout", false);

            return isTabletLayoutEnabled ? 720 : (isPhoneLayoutEnabled ? 480 : original);
        } catch (Exception ex){
            LogHelper.printException(LayoutOverridePatch.class, "Failed to getBoolean", ex);
            return original;
        }

    }
}
