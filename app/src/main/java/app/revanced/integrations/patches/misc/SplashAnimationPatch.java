package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class SplashAnimationPatch {

    public static boolean enableNewSplashAnimationBoolean(boolean original) {
        try {
            return SettingsEnum.ENABLE_NEW_SPLASH_ANIMATION.getBoolean();
        } catch (Exception ex) {
            LogHelper.printException(SplashAnimationPatch.class, "Failed to load enableNewSplashAnimation", ex);
        }
        return original;
    }

    public static int enableNewSplashAnimationInt(int original) {
        try {
            if (original == 0) {
                return SettingsEnum.ENABLE_NEW_SPLASH_ANIMATION.getBoolean() ? 3 : 0;
            }
        } catch (Exception ex) {
            LogHelper.printException(SplashAnimationPatch.class, "Failed to load enableNewSplashAnimation", ex);
        }

        return original;
    }
}
