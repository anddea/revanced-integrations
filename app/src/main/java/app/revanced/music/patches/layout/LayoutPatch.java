package app.revanced.music.patches.layout;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.music.settings.MusicSettingsEnum;

public class LayoutPatch {

    public static boolean disableAutoCaptions(boolean original) {
        return MusicSettingsEnum.DISABLE_AUTO_CAPTIONS.getBoolean() || original;
    }

    public static boolean enableBlackNavbar() {
        return MusicSettingsEnum.ENABLE_BLACK_NAVBAR.getBoolean();
    }

    public static boolean enableColorMatchPlayer() {
        return MusicSettingsEnum.ENABLE_COLOR_MATCH_PLAYER.getBoolean();
    }

    public static int enableCompactDialog(int original) {
        return MusicSettingsEnum.ENABLE_COMPACT_DIALOG.getBoolean() && original < 600 ? 600 : original;
    }

    public static boolean enableForceMinimizedPlayer(boolean original) {
        return MusicSettingsEnum.ENABLE_FORCE_MINIMIZED_PLAYER.getBoolean() || original;
    }

    public static boolean enableLandScapeMode(boolean original) {
        try {
            return MusicSettingsEnum.ENABLE_LANDSCAPE_MODE.getBoolean() || original;
        } catch (Exception ignored) {
            return original;
        }
    }

    public static boolean enableZenMode() {
        return MusicSettingsEnum.ENABLE_ZEN_MODE.getBoolean();
    }

    public static int hideCastButton(int original) {
        return MusicSettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static void hideCategoryBar(View view) {
        if (MusicSettingsEnum.HIDE_CATEGORY_BAR.getBoolean()) AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static boolean hideNewPlaylistButton() {
        return MusicSettingsEnum.HIDE_NEW_PLAYLIST_BUTTON.getBoolean();
    }

}
