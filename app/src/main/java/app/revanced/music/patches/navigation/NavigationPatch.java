package app.revanced.music.patches.navigation;

import static app.revanced.music.utils.ReVancedUtils.hideViewUnderCondition;

import app.revanced.music.settings.SettingsEnum;

public class NavigationPatch {

    public static int enableBlackNavigationBar() {
        return SettingsEnum.ENABLE_BLACK_NAVIGATION_BAR.getBoolean() ? -16777216 : -14869219;
    }

    public static void hideNavigationLabel(TextView textview) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), textview);
    }

}
