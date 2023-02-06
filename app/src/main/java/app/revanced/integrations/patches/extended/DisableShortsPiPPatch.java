package app.revanced.integrations.patches.extended;

import app.revanced.integrations.settings.SettingsEnum;

public class DisableShortsPiPPatch {
    private static boolean isShortsPlaying = false;

    public static void generalPlayer() {
        isShortsPlaying = false;
    }

    public static void shortsPlayer() {
        isShortsPlaying = true;
    }

    public static boolean disableShortsPlayerPiP(boolean original) {
        return !(SettingsEnum.DISABLE_SHORTS_PLAYER_PIP.getBoolean() && isShortsPlaying) && original;
    }
}
