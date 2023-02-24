package app.revanced.integrations.settings;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;

public class MusicSettings {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    // ADS
    public static boolean hideMusicAds() {
        return !getPrefBoolean("revanced_hide_music_ads", true);
    }

    // Design
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
    public static int hideCastButton(int original) {
        return getPrefBoolean("revanced_hide_cast_button", true) ? View.GONE : original;
    }

    public static void hideCompactHeader(View view) {
        if (getPrefBoolean("revanced_hide_compact_header", true)) AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static boolean enableTabletMode(boolean original) {
        return getPrefBoolean("revanced_enable_tablet_mode", true) || original;
    }


    // Utils
    public static boolean getPrefBoolean(String key, boolean defaultValue) {
        return getBoolean(context, YOUTUBE, key, defaultValue);
    }
}
