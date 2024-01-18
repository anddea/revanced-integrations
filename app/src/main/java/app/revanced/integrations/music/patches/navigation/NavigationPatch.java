package app.revanced.integrations.music.patches.navigation;

import static app.revanced.integrations.music.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class NavigationPatch {
    public static Enum<?> lastPivotTab;

    public static int enableBlackNavigationBar() {
        return SettingsEnum.ENABLE_BLACK_NAVIGATION_BAR.getBoolean() ? -16777216 : -14869219;
    }

    public static void hideNavigationLabel(TextView textview) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), textview);
    }

    public static void hideNavigationButton(@NonNull View view) {
        if (SettingsEnum.HIDE_NAVIGATION_BAR.getBoolean() && view.getParent() != null) {
            hideViewUnderCondition(true, (View) view.getParent());
            return;
        }

        for (NavigationButton button : NavigationButton.values())
            if (lastPivotTab.name().equals(button.name))
                hideViewUnderCondition(button.enabled, view);
    }

    private enum NavigationButton {
        HOME("TAB_HOME", SettingsEnum.HIDE_HOME_BUTTON.getBoolean()),
        SAMPLES("TAB_SAMPLES", SettingsEnum.HIDE_SAMPLES_BUTTON.getBoolean()),
        EXPLORE("TAB_EXPLORE", SettingsEnum.HIDE_EXPLORE_BUTTON.getBoolean()),
        LIBRARY("LIBRARY_MUSIC", SettingsEnum.HIDE_LIBRARY_BUTTON.getBoolean()),
        UPGRADE("TAB_MUSIC_PREMIUM", SettingsEnum.HIDE_UPGRADE_BUTTON.getBoolean());

        private final boolean enabled;
        private final String name;

        NavigationButton(String name, boolean enabled) {
            this.enabled = enabled;
            this.name = name;
        }
    }
}
