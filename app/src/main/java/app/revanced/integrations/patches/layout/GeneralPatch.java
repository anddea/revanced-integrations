package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.annotation.SuppressLint;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.ResourceType;

public class GeneralPatch {
    private static final String PREMIUM_HEADER_NAME = "ytPremiumWordmarkHeader";
    public static boolean captionsButtonStatus;

    @SuppressLint("StaticFieldLeak")
    public static View compactLink;

    public static void hideAccountMenu(@NonNull Spanned span) {
        if (compactLink == null || !SettingsEnum.HIDE_ACCOUNT_MENU.getBoolean()) return;

        String[] blockList = SettingsEnum.ACCOUNT_MENU_CUSTOM_FILTER.getString().split(",");

        for (String filter : blockList) {
            if (span.toString().contains(filter) && !filter.isEmpty())
                AdRemoverAPI.HideViewWithLayout1dp(compactLink);
        }
    }

    public static void hideStoriesShelf(View view) {
        if (SettingsEnum.HIDE_STORIES_SHELF.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
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

    public static ViewGroup hideShortsPlayerInfoPanel(ViewGroup viewGroup) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_INFO_PANEL.getBoolean() ? null : viewGroup;
    }

    public static ViewStub hideShortsPlayerPaidContent(ViewStub viewStub) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_PAID_CONTENT.getBoolean() ? null : viewStub;
    }

    public static void hideShortsPlayerRemixButton(View view) {
        if (SettingsEnum.HIDE_SHORTS_PLAYER_REMIX_BUTTON.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static void hideShortsPlayerSubscriptionsButton(View view) {
        if (SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static int hideShortsPlayerSubscriptionsButton(int original) {
        return SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean() ? 0 : original;
    }

    public static boolean enableWideSearchbar() {
        return SettingsEnum.ENABLE_WIDE_SEARCHBAR.getBoolean();
    }

    public static boolean enableTabletMiniPlayer(boolean original) {
        return SettingsEnum.ENABLE_TABLET_MINIPLAYER.getBoolean() || original;
    }

    public static boolean hideAutoCaptions() {
        return SettingsEnum.HIDE_AUTO_CAPTIONS.getBoolean() && !PlayerType.getCurrent().isNoneOrHidden();
    }

    public static boolean hideAutoPlayerPopupPanels() {
        return SettingsEnum.HIDE_AUTO_PLAYER_POPUP_PANELS.getBoolean();
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

    public static boolean hideFloatingMicrophone(boolean original) {
        return SettingsEnum.HIDE_FLOATING_MICROPHONE.getBoolean() || original;
    }

    public static int hideCategoryBarInFeed(int original) {
        return SettingsEnum.HIDE_CATEGORY_BAR_IN_FEED.getBoolean() ? 0 : original;
    }

    public static void hideCategoryBarInRelatedVideo(View view) {
        if (SettingsEnum.HIDE_CATEGORY_BAR_IN_RELATED_VIDEO.getBoolean())
            AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static int hideCategoryBarInSearchResults(int original) {
        return SettingsEnum.HIDE_CATEGORY_BAR_IN_SEARCH_RESULTS.getBoolean() ? 0 : original;
    }

    public static void hideChannelListSubMenu(View view) {
        if (SettingsEnum.HIDE_CHANNEL_LIST_SUBMENU.getBoolean())
            view.setVisibility(View.GONE);
    }

    public static int enablePremiumHeader(int originalValue) {
        if (SettingsEnum.ENABLE_PREMIUM_HEADER.getBoolean()) {
            originalValue = identifier(PREMIUM_HEADER_NAME, ResourceType.ATTR);
        }
        return originalValue;
    }

}
