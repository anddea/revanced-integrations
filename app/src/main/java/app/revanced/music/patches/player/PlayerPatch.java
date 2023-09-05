package app.revanced.music.patches.player;

import app.revanced.music.settings.SettingsEnum;

public class PlayerPatch {

    public static boolean enableColorMatchPlayer() {
        return SettingsEnum.ENABLE_COLOR_MATCH_PLAYER.getBoolean();
    }

    public static boolean enableForceMinimizedPlayer(boolean original) {
        return SettingsEnum.ENABLE_FORCE_MINIMIZED_PLAYER.getBoolean() || original;
    }

    public static boolean enableNewLayout() {
        return SettingsEnum.ENABLE_NEW_LAYOUT.getBoolean();
    }

    public static boolean enableOldStyleMiniPlayer(boolean original) {
        return !SettingsEnum.ENABLE_OLD_STYLE_MINI_PLAYER.getBoolean() && original;
    }

    public static boolean enableZenMode() {
        return SettingsEnum.ENABLE_ZEN_MODE.getBoolean();
    }
}
