package app.revanced.music.patches.general;

import static app.revanced.music.utils.ReVancedUtils.hideViewBy0dpUnderCondition;

import android.view.View;

import java.util.stream.Stream;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class GeneralPatch {

    public static boolean disableAutoCaptions(boolean original) {
        return SettingsEnum.DISABLE_AUTO_CAPTIONS.getBoolean() || original;
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

            if (Stream.of("FEmusic_library_landing").anyMatch(browseId::contains))
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

    public static boolean hideNewPlaylistButton() {
        return SettingsEnum.HIDE_NEW_PLAYLIST_BUTTON.getBoolean();
    }

    public static String setStartPage(final String browseId) {
        if (!browseId.equals("FEmusic_home"))
            return browseId;

        return SettingsEnum.START_PAGE.getString();
    }
}
