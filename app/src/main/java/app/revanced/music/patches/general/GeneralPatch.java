package app.revanced.music.patches.general;

import static app.revanced.music.utils.ReVancedUtils.hideViewBy0dpUnderCondition;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class GeneralPatch {
    @NonNull
    private static String videoId = "";
    private static boolean subtitlePrefetched = true;

    public static String changeStartPage(final String browseId) {
        if (!browseId.equals("FEmusic_home"))
            return browseId;

        return SettingsEnum.CHANGE_START_PAGE.getString();
    }

    public static boolean disableAutoCaptions(boolean original) {
        if (!SettingsEnum.DISABLE_AUTO_CAPTIONS.getBoolean())
            return original;

        return subtitlePrefetched;
    }

    public static boolean enableLandScapeMode(boolean original) {
        try {
            return SettingsEnum.ENABLE_LANDSCAPE_MODE.getBoolean() || original;
        } catch (Exception ignored) {
            return original;
        }
    }

    public static String enableOldStyleLibraryShelf(final String browseId) {
        if (SettingsEnum.ENABLE_OLD_STYLE_LIBRARY_SHELF.getBoolean() || SettingsEnum.SPOOF_APP_VERSION.getBoolean()) {
            if (browseId.equals("FEmusic_library_landing"))
                return "FEmusic_liked";
        }

        return browseId;
    }

    public static int hideCastButton(int original) {
        return SettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static void hideCastButton(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_CAST_BUTTON.getBoolean(), view);
    }

    public static void hideCategoryBar(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_CATEGORY_BAR.getBoolean(), view);
    }

    public static boolean hideHistoryButton(boolean original) {
        return !SettingsEnum.HIDE_HISTORY_BUTTON.getBoolean() && original;
    }

    public static boolean hideNewPlaylistButton() {
        return SettingsEnum.HIDE_NEW_PLAYLIST_BUTTON.getBoolean();
    }

    public static void newVideoStarted(@NonNull String newlyLoadedVideoId) {
        if (Objects.equals(newlyLoadedVideoId, videoId)) {
            return;
        }
        videoId = newlyLoadedVideoId;
        subtitlePrefetched = false;
    }

    public static void prefetchSubtitleTrack() {
        subtitlePrefetched = true;
    }
}
