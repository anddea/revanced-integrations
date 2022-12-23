package app.revanced.integrations.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import app.revanced.integrations.utils.ReVancedMusicUtils;
import app.revanced.integrations.utils.SharedPrefHelper;

public class MusicSettings {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static SharedPreferences sharedPreferences;

    public static boolean getBlackNavbar() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_black_navbar", true);
    }
    public static int getCastButtonOverrideV2(int original) {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_cast_button", true) ? View.GONE : original;
    }

    public static boolean getCodecsUnlock() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_codecs_unlock", true);
    }

    public static int getCompactHeader() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_compact_header", true) ? 8 : 0;
    }

    public static boolean getEnforceMinimizedPlayer() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_enforce_minimized_player", true);
    }

    public static boolean getMiniPlayerColor() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_mini_player_color", true);
    }

    public static boolean getShowAds() {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_music_video_ads", false);
    }

    public static boolean getTabletMode(boolean original) {
        return SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "revanced_tablet_mode", true) || original;
    }
}
