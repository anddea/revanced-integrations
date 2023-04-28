package app.revanced.music.patches.misc;

import app.revanced.music.settings.MusicSettingsEnum;

public class ForceShufflePatch {

    public static boolean enableForceShuffle() {
        return MusicSettingsEnum.ENABLE_FORCE_SHUFFLE.getBoolean();
    }
}
