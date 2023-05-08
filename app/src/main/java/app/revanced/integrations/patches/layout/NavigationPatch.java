package app.revanced.integrations.patches.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class NavigationPatch {
    @SuppressLint("StaticFieldLeak")
    public static Context shortsContext;
    public static Object pivotBar;
    public static Enum lastPivotTab;

    public static boolean changeHomePage() {
        return SettingsEnum.CHANGE_HOMEPAGE_TO_SUBSCRIPTION.getBoolean();
    }

    public static void changeHomePage(Activity activity) {
        var intent = activity.getIntent();
        if (SettingsEnum.CHANGE_HOMEPAGE_TO_SUBSCRIPTION.getBoolean() &&
                Objects.equals(intent.getAction(), "android.intent.action.MAIN")) {
            intent.setAction("com.google.android.youtube.action.open.subscriptions");
            intent.setPackage(activity.getPackageName());
            activity.startActivity(intent);
        }
    }

    public static boolean switchCreateNotification(boolean original) {
        return SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean() || original;
    }

    public static void hideCreateButton(View view) {
        boolean enabled = SettingsEnum.HIDE_CREATE_BUTTON.getBoolean();
        if (enabled) view.setVisibility(View.GONE);
    }

    public static void hideNavigationButton(View view) {
        if (lastPivotTab == null) return;
        var lastPivotTabName = lastPivotTab.name();

        if (lastPivotTabName.equals(PivotEnum.HOME.getName()))
            setNavigationButtonVisibility(PivotEnum.HOME, view);
        else if (lastPivotTabName.equals(PivotEnum.SHORTS.getName()))
            setNavigationButtonVisibility(PivotEnum.SHORTS, view);
        else if (lastPivotTabName.equals(PivotEnum.SUBSCRIPTIONS.getName()))
            setNavigationButtonVisibility(PivotEnum.SUBSCRIPTIONS, view);
        else if (lastPivotTabName.equals(PivotEnum.LIBRARY.getName()))
            setNavigationButtonVisibility(PivotEnum.LIBRARY, view);
    }

    public static void setNavigationButtonVisibility(PivotEnum pivotEnum, View view) {
        if (pivotEnum.getBoolean()) view.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    public static void hideShortsPlayerNavBar() {
        if (SettingsEnum.HIDE_SHORTS_NAVIGATION_BAR.getBoolean() && shortsContext != null) {
            if (pivotBar instanceof HorizontalScrollView) {
                Objects.requireNonNull((HorizontalScrollView) pivotBar).setVisibility(8);
            }
        }
    }

    public static void hideNavigationLabel(TextView view) {
        boolean enabled = SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean();
        if (enabled) view.setVisibility(View.GONE);
    }

    public static View hideShortsPlayerNavBar(View view) {
        return SettingsEnum.HIDE_SHORTS_NAVIGATION_BAR.getBoolean() ? null : view;
    }

    public static boolean enableTabletNavBar(boolean original) {
        return SettingsEnum.ENABLE_TABLET_NAVIGATION_BAR.getBoolean() || original;
    }

    private enum PivotEnum {
        HOME("PIVOT_HOME", SettingsEnum.HIDE_HOME_BUTTON.getBoolean()),
        SHORTS("TAB_SHORTS", SettingsEnum.HIDE_SHORTS_BUTTON.getBoolean()),
        SUBSCRIPTIONS("PIVOT_SUBSCRIPTIONS", SettingsEnum.HIDE_SUBSCRIPTIONS_BUTTON.getBoolean()),
        LIBRARY("VIDEO_LIBRARY_WHITE", SettingsEnum.HIDE_LIBRARY_BUTTON.getBoolean());

        private final String name;
        private final boolean enabled;
        PivotEnum(String name, boolean enabled) {
            this.name = name;
            this.enabled = enabled;
        }
        private String getName() {
            return name;
        }
        private boolean getBoolean() {
            return enabled;
        }
    }
}
