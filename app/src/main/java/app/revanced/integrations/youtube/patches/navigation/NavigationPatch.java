package app.revanced.integrations.youtube.patches.navigation;

import static app.revanced.integrations.youtube.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;
import android.widget.TextView;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.NavigationBar;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("unused")
public class NavigationPatch {
    public static Enum<?> lastPivotTab;

    public static void hideNavigationLabel(TextView view) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), view);
    }

    public static boolean enableTabletNavBar(boolean original) {
        return SettingsEnum.ENABLE_TABLET_NAVIGATION_BAR.getBoolean() || original;
    }

    private static final Map<NavigationBar.NavigationButton, Boolean> shouldHideMap = new EnumMap<>(NavigationBar.NavigationButton.class) {
        {
            put(NavigationBar.NavigationButton.HOME, SettingsEnum.HIDE_HOME_BUTTON.getBoolean());
            put(NavigationBar.NavigationButton.CREATE, SettingsEnum.HIDE_CREATE_BUTTON.getBoolean());
            put(NavigationBar.NavigationButton.SHORTS, SettingsEnum.HIDE_SHORTS_BUTTON.getBoolean());
            put(NavigationBar.NavigationButton.SUBSCRIPTIONS, SettingsEnum.HIDE_SUBSCRIPTIONS_BUTTON.getBoolean());
        }
    };

    private static final Boolean SWITCH_CREATE_WITH_NOTIFICATIONS_BUTTON
            = SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean();

    /**
     * Injection point.
     */
    public static boolean switchCreateWithNotificationButton() {
        return SWITCH_CREATE_WITH_NOTIFICATIONS_BUTTON;
    }

    /**
     * Injection point.
     */
    public static void navigationTabCreated(NavigationBar.NavigationButton button, View tabView) {
        if (Boolean.TRUE.equals(shouldHideMap.get(button))) {
            tabView.setVisibility(View.GONE);
        }
    }
}
