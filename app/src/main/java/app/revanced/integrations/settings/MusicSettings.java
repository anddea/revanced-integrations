package app.revanced.integrations.settings;

import static app.revanced.integrations.utils.ReVancedUtils.getContext;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getPreferences;

import android.view.View;

import java.util.Objects;

import app.revanced.integrations.adremover.AdRemoverAPI;

public class MusicSettings {
    // ADS
    public static boolean hideMusicAds() {
        return !getPrefBoolean("revanced_hide_music_ads", true);
    }

    // Design
    public static boolean disableAutoCaptions(boolean original) {
        return getPrefBoolean("revanced_disable_auto_captions", false) || original;
    }

    public static boolean enableBlackNavbar() {
        return getPrefBoolean("revanced_enable_black_navbar", true);
    }

    public static boolean enableColorMatchPlayer() {
        return getPrefBoolean("revanced_enable_color_match_player", true);
    }

    public static boolean enableZenMode() {
        return getPrefBoolean("revanced_enable_zen_mode", false);
    }

    // Listening
    public static boolean enableOpusCodec() {
        return getPrefBoolean("revanced_enable_opus_codec", true);
    }

    public static boolean enableForceMinimizedPlayer(boolean original) {
        return getPrefBoolean("revanced_enable_force_minimized_player", true) || original;
    }

    public static boolean enableForceShuffle() {
        return getPrefBoolean("revanced_enable_force_shuffle", true);
    }

    // Navigation
    public static int enableCompactDialog(int original) {
        return getPrefBoolean("revanced_enable_compact_dialog", true) && original < 600 ? 600 : original;
    }

    public static boolean enableTabletMode(boolean original) {
        return getPrefBoolean("revanced_enable_tablet_mode", true) || original;
    }

    public static int hideCastButton(int original) {
        return getPrefBoolean("revanced_hide_cast_button", true) ? View.GONE : original;
    }

    public static void hideCompactHeader(View view) {
        if (getPrefBoolean("revanced_hide_compact_header", true)) AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static boolean hideNewPlaylistButton() {
        return getPrefBoolean("revanced_hide_new_playlist_button", false);
    }

    public static String spoofVersion(String original) {
        return getPrefBoolean("revanced_enable_spoof_version", false) ? "4.27.53" : original;
    }

    // Utils
    public static boolean getPrefBoolean(String key, boolean defaultValue) {
        return Objects.requireNonNull(getPreferences(getContext(), YOUTUBE)).getBoolean(key, defaultValue);
    }
}
