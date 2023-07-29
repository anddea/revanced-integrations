package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;

import app.revanced.integrations.utils.LogHelper;

public class SplashAnimationPatch {

    /**
     * Context overrides when you open YouTube in an external browser.
     * This is expected to affect SplashAnimationPatch.
     */
    public static boolean enableNewSplashAnimationBoolean() {
        try {
            return getBoolean(REVANCED, "revanced_enable_new_splash_animation", false);
        } catch (Exception ex) {
            LogHelper.printException(SplashAnimationPatch.class, "Failed to load enableNewSplashAnimation", ex);
        }
        return false;
    }

    /**
     * Context overrides when you open YouTube in an external browser.
     * This is expected to affect SplashAnimationPatch.
     */
    public static int enableNewSplashAnimationInt() {
        try {
            return getBoolean(REVANCED, "revanced_enable_new_splash_animation", false)
                    ? 3
                    : 0;
        } catch (Exception ex) {
            LogHelper.printException(SplashAnimationPatch.class, "Failed to load enableNewSplashAnimation", ex);
        }

        return 0;
    }
}
