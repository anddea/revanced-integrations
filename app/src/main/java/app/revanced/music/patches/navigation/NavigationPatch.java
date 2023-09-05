package app.revanced.music.patches.navigation;

import static app.revanced.music.utils.ReVancedUtils.hideViewUnderCondition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;

public class NavigationPatch {

    public static int enableBlackNavigationBar() {
        return SettingsEnum.ENABLE_BLACK_NAVIGATION_BAR.getBoolean() ? -16777216 : -14869219;
    }

    public static void hideNavigationLabel(TextView textview) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), textview);
    }

    public static boolean hideUpgradeButton(@NonNull Enum button) {
        final String buttonName = button.name();

        return buttonName.equals("TAB_MUSIC_PREMIUM");
    }
}
