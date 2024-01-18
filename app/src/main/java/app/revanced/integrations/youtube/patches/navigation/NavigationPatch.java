package app.revanced.integrations.youtube.patches.navigation;

import static app.revanced.integrations.youtube.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;
import android.widget.TextView;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class NavigationPatch {
    public static Enum<?> lastPivotTab;

    public static boolean switchCreateNotification(boolean original) {
        return SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean() || original;
    }

    public static void hideCreateButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_CREATE_BUTTON.getBoolean(), view);
    }

    public static void hideNavigationButton(View view) {
        if (lastPivotTab == null)
            return;

        for (NavigationButton button : NavigationButton.values())
            if (button.name.equals(lastPivotTab.name()))
                hideViewUnderCondition(button.enabled, view);
    }

    public static void hideNavigationLabel(TextView view) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), view);
    }

    public static boolean enableTabletNavBar(boolean original) {
        return SettingsEnum.ENABLE_TABLET_NAVIGATION_BAR.getBoolean() || original;
    }

    private enum NavigationButton {
        HOME("PIVOT_HOME", SettingsEnum.HIDE_HOME_BUTTON.getBoolean()),
        SHORTS("TAB_SHORTS", SettingsEnum.HIDE_SHORTS_BUTTON.getBoolean()),
        SUBSCRIPTIONS("PIVOT_SUBSCRIPTIONS", SettingsEnum.HIDE_SUBSCRIPTIONS_BUTTON.getBoolean()),
        NOTIFICATIONS("TAB_ACTIVITY", SettingsEnum.HIDE_NOTIFICATIONS_BUTTON.getBoolean()),
        LIBRARY("VIDEO_LIBRARY_WHITE", SettingsEnum.HIDE_LIBRARY_BUTTON.getBoolean());

        private final boolean enabled;
        private final String name;

        NavigationButton(String name, boolean enabled) {
            this.enabled = enabled;
            this.name = name;
        }
    }
}
