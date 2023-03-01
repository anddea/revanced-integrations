package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.ReVancedUtils.context;

import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.SharedPrefHelper;

public class LayoutOverridePatch {
    public static SharedPrefHelper.SharedPrefNames prefName = SharedPrefHelper.SharedPrefNames.REVANCED;

    /*
     * Context is overridden when trying to play a YouTube video from the Google Play Store,
     * Which is speculated to affect LayoutOverridePatch
     */
    public static int getLayoutOverride(int original) {
        try {
            boolean isTabletLayoutEnabled = SharedPrefHelper.getBoolean(context, prefName, "revanced_enable_tablet_layout", false);
            boolean isPhoneLayoutEnabled = SharedPrefHelper.getBoolean(context, prefName, "revanced_enable_phone_layout", false);

            return isTabletLayoutEnabled ? 720 : (isPhoneLayoutEnabled ? 480 : original);
        } catch (Exception ex){
            LogHelper.printException(() -> "Failed to getBoolean", ex);
            return original;
        }

    }
}
