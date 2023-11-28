package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.patches.utils.NavBarIndexPatch.isShortsTab;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewBy0dpUnderCondition;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;

import android.annotation.SuppressLint;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.HorizontalScrollView;

import app.revanced.integrations.settings.SettingsEnum;

public class ShortsPatch {
    @SuppressLint("StaticFieldLeak")
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

        if (!(pivotBar instanceof HorizontalScrollView horizontalScrollView))
            return;

        horizontalScrollView.setVisibility(View.GONE);
    }

    public static void hideShortsPlayerCommentsButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_COMMENTS_BUTTON.getBoolean(), view);
    }

    public static boolean hideShortsPlayerDislikeButton() {
        return SettingsEnum.HIDE_SHORTS_PLAYER_DISLIKE_BUTTON.getBoolean();
    }

    public static ViewGroup hideShortsPlayerInfoPanel(ViewGroup viewGroup) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_INFO_PANEL.getBoolean() ? null : viewGroup;
    }

    public static boolean hideShortsPlayerLikeButton() {
        return SettingsEnum.HIDE_SHORTS_PLAYER_LIKE_BUTTON.getBoolean();
    }

    public static ViewStub hideShortsPlayerPaidPromotionBanner(ViewStub viewStub) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_PAID_PROMOTION.getBoolean() ? null : viewStub;
    }

    public static boolean hideShortsPlayerPivotButton() {
        return SettingsEnum.HIDE_SHORTS_PLAYER_PIVOT_BUTTON.getBoolean();
    }

    public static Object hideShortsPlayerPivotButton(Object object) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_PIVOT_BUTTON.getBoolean() ? null : object;
    }

    public static void hideShortsPlayerRemixButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_REMIX_BUTTON.getBoolean(), view);
    }

    public static void hideShortsPlayerShareButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_SHARE_BUTTON.getBoolean(), view);
    }

    public static void hideShortsPlayerSubscriptionsButton(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean(), view);
    }

    public static int hideShortsPlayerSubscriptionsButton(int original) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean() ? 0 : original;
    }

    public static boolean hideShortsToolBarBanner() {
        return SettingsEnum.HIDE_SHORTS_TOOLBAR_BANNER.getBoolean();
    }

    public static void hideShortsToolBar(Toolbar toolbar) {
        if (!SettingsEnum.HIDE_SHORTS_TOOLBAR.getBoolean())
            return;
        final int visibility = isShortsTab() ? View.GONE : View.VISIBLE;
        toolbar.setVisibility(visibility);
    }

    public static void hideShortsToolBarButton(String enumString, View view) {
        for (ToolBarButton button : ToolBarButton.values()) {
            if (enumString.equals(button.name)) {
                hideViewUnderCondition(button.enabled, view);
                break;
            }
        }
    }

    private enum ToolBarButton {
        SEARCH("SEARCH_BOLD", SettingsEnum.HIDE_SHORTS_TOOLBAR_SEARCH_BUTTON.getBoolean()),
        SEARCH_OLD_LAYOUT("SEARCH_FILLED", SettingsEnum.HIDE_SHORTS_TOOLBAR_SEARCH_BUTTON.getBoolean()),
        CAMERA("SHORTS_HEADER_CAMERA_BOLD", SettingsEnum.HIDE_SHORTS_TOOLBAR_CAMERA_BUTTON.getBoolean()),
        CAMERA_OLD_LAYOUT("SHORTS_HEADER_CAMERA", SettingsEnum.HIDE_SHORTS_TOOLBAR_CAMERA_BUTTON.getBoolean()),
        MENU("MORE_VERT_BOLD", SettingsEnum.HIDE_SHORTS_TOOLBAR_MENU_BUTTON.getBoolean()),
        MENU_TABLET("MORE_VERT", SettingsEnum.HIDE_SHORTS_TOOLBAR_MENU_BUTTON.getBoolean());

        private final boolean enabled;
        private final String name;

        ToolBarButton(String name, boolean enabled) {
            this.enabled = enabled;
            this.name = name;
        }
    }
}
