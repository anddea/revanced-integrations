package app.revanced.integrations.settings;

import android.view.View;

import app.revanced.integrations.utils.ReVancedMusicUtils;
import app.revanced.integrations.utils.SharedPrefHelper;

public class MusicSettings {

    public static boolean getBlackNavbar() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_black_navbar", true).booleanValue();
    }
    public static int getCastButtonOverrideV2(int original) {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_cast_button", true).booleanValue() ? View.GONE : original;
    }

    public static boolean getCodecsUnlock() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_codecs_unlock", true).booleanValue();
    }

    public static int getCompactHeader() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_compact_header", true).booleanValue() ? 8 : 0;
    }

    public static boolean getEnforceMinimizedPlayer() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_enforce_minimized_player", true).booleanValue();
    }

    public static boolean getMiniPlayerColor() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_mini_player_color", true).booleanValue();
    }

    public static boolean getShowAds() {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_video_ads", false).booleanValue();
    }

    public static boolean getTabletMode(boolean original) {
        return SharedPrefHelper.getBoolean(ReVancedMusicUtils.getAppContext(), SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_tablet_mode", true).booleanValue() || original;
    }
}
