package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class SplashAnimationPatch {

    public static boolean enableNewSplashAnimation() {
        return SettingsEnum.ENABLE_NEW_SPLASH_ANIMATION.getBoolean();
    }
}
