package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ResourceType;

public class GeneralLayoutPatch {
    private static final String PREMIUM_HEADER_NAME = "ytPremiumWordmarkHeader";
    public static boolean captionsButtonStatus;
    public static Enum lastPivotTab;

    public static void hideStoriesShelf(View view) {
        if (SettingsEnum.HIDE_STORIES_SHELF.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static void hideCreateButton(View view) {
        boolean enabled = SettingsEnum.HIDE_CREATE_BUTTON.getBoolean();
        view.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    public static void hideShortsButton(View view) {
        if (lastPivotTab != null && lastPivotTab.name().equals("TAB_SHORTS")) {
            boolean show = SettingsEnum.HIDE_SHORTS_BUTTON.getBoolean();
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static boolean hideStartupShortsPlayer() {
        return SettingsEnum.HIDE_STARTUP_SHORTS_PLAYER.getBoolean();
    }

    public static void hideShortsPlayerCommentsButton(View view) {
        if (SettingsEnum.HIDE_SHORTS_PLAYER_COMMENTS_BUTTON.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static void hideShortsPlayerRemixButton(View view) {
        if (SettingsEnum.HIDE_SHORTS_PLAYER_REMIX_BUTTON.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static void hideShortsPlayerSubscriptionsButton(View view) {
        if (SettingsEnum.HIDE_SHORTS_PLAYER_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static boolean enableWideSearchbar() {
        return SettingsEnum.ENABLE_WIDE_SEARCHBAR.getBoolean();
    }

    public static boolean enableTabletMiniPlayer(boolean original) {
        return SettingsEnum.ENABLE_TABLET_MINIPLAYER.getBoolean() || original;
    }

    public static boolean hideAutoCaptions() {
        return SettingsEnum.HIDE_AUTO_CAPTIONS.getBoolean();
    }

    public static boolean hideAutoPlayerPopupPanels() {
        return SettingsEnum.HIDE_AUTO_PLAYER_POPUP_PANELS.getBoolean();
    }

    public static void hideMixPlaylists(View view) {
        if (SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static void hideCrowdfundingBox(View view) {
        if (SettingsEnum.HIDE_CROWDFUNDING_BOX.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static int hideEmailAddress(int originalValue) {
        return SettingsEnum.HIDE_EMAIL_ADDRESS.getBoolean() ? 8 : originalValue;
    }

    public static boolean hideSnackbar() {
        return SettingsEnum.HIDE_SNACKBAR.getBoolean();
    }

    public static int enablePremiumHeader(int originalValue) {
        if (SettingsEnum.ENABLE_PREMIUM_HEADER.getBoolean()) {
            originalValue = identifier(PREMIUM_HEADER_NAME, ResourceType.ATTR);
        }
        return originalValue;
    }

}
