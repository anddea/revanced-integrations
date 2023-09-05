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

    public static String enableOldStyleLibraryShelf(final String original) {
        if (original.startsWith("LV") || !SettingsEnum.ENABLE_OLD_STYLE_LIBRARY_SHELF.getBoolean())
            return original;

        LogHelper.printDebug(GeneralPatch.class, "Current Browser ID: " + original);

        if (Stream.of("FEmusic_library_landing").anyMatch(original::contains))
            return "FEmusic_liked";

        return original;
    }

    public static int hideCastButton(int original) {
        return SettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static void hideCategoryBar(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_CATEGORY_BAR.getBoolean(), view);
    }

    public static boolean hideNewPlaylistButton() {
        return SettingsEnum.HIDE_NEW_PLAYLIST_BUTTON.getBoolean();
    }
}
