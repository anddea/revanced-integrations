package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.hideViewBy0dpUnderCondition;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.HorizontalScrollView;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class ShortsPatch {
    public static Object pivotBar;

    public static boolean disableStartupShortsPlayer() {
        return SettingsEnum.DISABLE_STARTUP_SHORTS_PLAYER.getBoolean();
    }

    public static View hideShortsPlayerNavigationBar(View view) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_NAVIGATION_BAR.getBoolean() ? null : view;
    }

    public static void hideShortsPlayerNavigationBar() {
        if (!SettingsEnum.HIDE_SHORTS_PLAYER_NAVIGATION_BAR.getBoolean())
            return;

        if (pivotBar instanceof HorizontalScrollView) {
            Objects.requireNonNull((HorizontalScrollView) pivotBar).setVisibility(View.GONE);
        }
    }

    public static void hideShortsPlayerCommentsButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_COMMENTS_BUTTON.getBoolean(), view);
    }

    public static ViewGroup hideShortsPlayerInfoPanel(ViewGroup viewGroup) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_INFO_PANEL.getBoolean() ? null : viewGroup;
    }

    public static ViewStub hideShortsPlayerPaidContent(ViewStub viewStub) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_PAID_CONTENT.getBoolean() ? null : viewStub;
    }

    public static void hideShortsPlayerRemixButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_REMIX_BUTTON.getBoolean(), view);
    }

    public static void hideShortsPlayerSubscriptionsButton(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean(), view);
    }

    public static int hideShortsPlayerSubscriptionsButton(int original) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean() ? 0 : original;
    }

}
