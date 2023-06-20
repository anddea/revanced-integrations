package app.revanced.music.patches.misc;

import app.revanced.music.settings.SettingsEnum;

public class ForceShufflePatch {

    public static boolean enableForceShuffle() {
        return SettingsEnum.ENABLE_FORCE_SHUFFLE.getBoolean();
    }
}
