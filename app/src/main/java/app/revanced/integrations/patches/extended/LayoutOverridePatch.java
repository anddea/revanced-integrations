package app.revanced.integrations.patches.extended;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class LayoutOverridePatch {

    /*
    * Context is overridden when trying to play a YouTube video from the Google Play Store,
    * Which is speculated to affect LayoutOverridePatch
    */
    public static int getLayoutOverride(int original) {
        try {
            boolean tabletLayoutEnabled = SettingsEnum.ENABLE_TABLET_LAYOUT.getBoolean();
            boolean phoneLayoutEnabled = SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();

            return tabletLayoutEnabled ? 720 : (phoneLayoutEnabled ? 480 : original);
        } catch (Exception ex){
            LogHelper.printException(LayoutOverridePatch.class, "Failed to getBoolean", ex);
        }
        return original;
    }
}
