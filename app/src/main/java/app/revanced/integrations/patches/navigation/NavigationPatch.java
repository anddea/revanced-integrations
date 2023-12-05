package app.revanced.integrations.patches.navigation;

import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class NavigationPatch {
    public static Enum<?> lastPivotTab;

    public static boolean changeHomePage() {
        return SettingsEnum.CHANGE_HOMEPAGE_TO_SUBSCRIPTION.getBoolean();
    }

    public static void changeHomePage(Activity activity) {
        if (!SettingsEnum.CHANGE_HOMEPAGE_TO_SUBSCRIPTION.getBoolean())
            return;

        final Intent intent = activity.getIntent();
        if (Objects.equals(intent.getAction(), "android.intent.action.MAIN")) {
            intent.setAction("com.google.android.youtube.action.open.subscriptions");
            intent.setPackage(activity.getPackageName());
            activity.startActivity(intent);
        }
    }

    public static boolean switchCreateNotification(boolean original) {
        return SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean() || original;
    }

    public static void hideCreateButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_CREATE_BUTTON.getBoolean(), view);
    }

    public static void hideNavigationButton(View view) {
        if (lastPivotTab == null)
            return;

        final String pivotTabString = lastPivotTab.name();
        openLibraryTab(view, pivotTabString);

        for (NavigationButton button : NavigationButton.values())
            if (button.name.equals(pivotTabString))
                hideViewUnderCondition(button.enabled, view);
    }

    public static void hideNavigationLabel(TextView view) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), view);
    }

    public static void hideYouButton(View view) {
        openYouTab(view);
        // hideViewUnderCondition(SettingsEnum.HIDE_YOU_BUTTON.getBoolean(), view);
    }

    public static boolean enableTabletNavBar(boolean original) {
        return SettingsEnum.ENABLE_TABLET_NAVIGATION_BAR.getBoolean() || original;
    }

    private static void openLibraryTab(View view, String pivotTabString) {
        if (!SettingsEnum.OPEN_LIBRARY_YOU_STARTUP.getBoolean())
            return;

        if (!NavigationButton.LIBRARY.name.equals(pivotTabString))
            return;

        view.setSoundEffectsEnabled(false);
        view.performClick();
        view.setSoundEffectsEnabled(true);
    }

    private static void openYouTab(View view) {
        if (!SettingsEnum.OPEN_LIBRARY_YOU_STARTUP.getBoolean())
            return;

        view.setSoundEffectsEnabled(false);
        view.performClick();
        view.setSoundEffectsEnabled(true);
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
