package app.revanced.integrations.patches.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.google.android.apps.youtube.app.ui.pivotbar.PivotBar;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class NavigationPatch {
    @SuppressLint("StaticFieldLeak")
    public static Context shortsContext;
    public static PivotBar pivotbar;
    public static Enum lastPivotTab;

    public static boolean switchCreateNotification(boolean original) {
        return SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean() || original;
    }

    public static void hideCreateButton(View view) {
        boolean enabled = SettingsEnum.HIDE_CREATE_BUTTON.getBoolean();
        if (enabled) view.setVisibility(View.GONE);
    }

    public static void hideShortsButton(View view) {
        if (lastPivotTab != null && lastPivotTab.name().equals("TAB_SHORTS")) {
            boolean enabled = SettingsEnum.HIDE_SHORTS_BUTTON.getBoolean();
            if (enabled) view.setVisibility(View.GONE);
        }
    }

    @SuppressLint("WrongConstant")
    public static void hideShortsPlayerNavBar() {
        if (SettingsEnum.HIDE_SHORTS_NAVIGATION_BAR.getBoolean() && shortsContext != null) {
            Objects.requireNonNull(pivotbar).setVisibility(8);
        }
    }
}
