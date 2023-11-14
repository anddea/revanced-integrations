package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.hideViewBy0dpUnderCondition;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.ReVancedUtils;

public class GeneralPatch {
    private static final List<String> toolBarButtonList = Arrays.asList(
            "CREATION_ENTRY",   // Create button (Phone)
            "FAB_CAMERA",       // Create button (Tablet)
            "TAB_ACTIVITY"      // Notification button
    );
    public static boolean captionsButtonStatus;
    private static FrameLayout.LayoutParams layoutParams;
    private static int minimumHeight = 1;
    private static int paddingLeft = 12;
    private static int paddingTop = 0;
    private static int paddingRight = 12;
    private static int paddingBottom = 0;

    public static boolean disableAutoCaptions() {
        return SettingsEnum.DISABLE_AUTO_CAPTIONS.getBoolean() && !PlayerType.getCurrent().isNoneOrHidden();
    }

    public static boolean enableGradientLoadingScreen() {
        return SettingsEnum.ENABLE_GRADIENT_LOADING_SCREEN.getBoolean();
    }

    public static boolean enableMusicSearch() {
        return SettingsEnum.ENABLE_MUSIC_SEARCH.getBoolean();
    }

    public static boolean enableTabletMiniPlayer(boolean original) {
        return SettingsEnum.ENABLE_TABLET_MINI_PLAYER.getBoolean() || original;
    }

    public static boolean enableWideSearchBar() {
        return SettingsEnum.ENABLE_WIDE_SEARCH_BAR.getBoolean();
    }

    public static boolean enableWideSearchBarInYouTab(boolean original) {
        return !SettingsEnum.ENABLE_WIDE_SEARCH_BAR_IN_YOU_TAB.getBoolean() && original;
    }

    public static void hideAccountList(View view, CharSequence charSequence) {
        if (!SettingsEnum.HIDE_ACCOUNT_MENU.getBoolean())
            return;

        if (!(view.getParent().getParent().getParent() instanceof ViewGroup viewGroup))
            return;

        String[] blockList = SettingsEnum.HIDE_ACCOUNT_MENU_FILTER_STRINGS.getString().split("\\n");
        String targetString = charSequence.toString();

        for (String filter : blockList) {
            if (targetString.equals(filter) && !filter.isEmpty()) {
                viewGroup.setLayoutParams(new LayoutParams(0, 0));
            }
        }
    }

    public static void hideAccountMenu(View view, CharSequence charSequence) {
        if (!SettingsEnum.HIDE_ACCOUNT_MENU.getBoolean())
            return;

        if (!(view.getParent().getParent() instanceof ViewGroup viewGroup))
            return;

        String[] blockList = SettingsEnum.HIDE_ACCOUNT_MENU_FILTER_STRINGS.getString().split("\\n");
        String targetString = charSequence.toString();

        for (String filter : blockList) {
            if (targetString.equals(filter) && !filter.isEmpty()) {
                if (viewGroup.getLayoutParams() instanceof MarginLayoutParams)
                    hideAccountMenu(viewGroup);
                else
                    viewGroup.setLayoutParams(new LayoutParams(0, 0));
            }
        }
    }

    private static void hideAccountMenu(ViewGroup viewGroup) {
        viewGroup.setVisibility(View.GONE);
    }

    public static boolean hideAutoPlayerPopupPanels() {
        return SettingsEnum.HIDE_AUTO_PLAYER_POPUP_PANELS.getBoolean();
    }

    public static int hideCategoryBarInFeed(int original) {
        return SettingsEnum.HIDE_CATEGORY_BAR_IN_FEED.getBoolean() ? 0 : original;
    }

    public static void hideCategoryBarInRelatedVideo(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_CATEGORY_BAR_IN_RELATED_VIDEO.getBoolean(), view);
    }

    public static int hideCategoryBarInSearchResults(int original) {
        return SettingsEnum.HIDE_CATEGORY_BAR_IN_SEARCH_RESULTS.getBoolean() ? 0 : original;
    }

    public static void hideChannelListSubMenu(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_CHANNEL_LIST_SUBMENU.getBoolean(), view);
    }

    public static void hideCrowdfundingBox(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_CROWDFUNDING_BOX.getBoolean(), view);
    }

    public static boolean hideFloatingMicrophone(boolean original) {
        return SettingsEnum.HIDE_FLOATING_MICROPHONE.getBoolean() || original;
    }

    public static int hideHandle(int originalValue) {
        return SettingsEnum.HIDE_HANDLE.getBoolean() ? 8 : originalValue;
    }

    public static void hideLatestVideosButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_LATEST_VIDEOS_BUTTON.getBoolean(), view);
    }

    public static void hideLoadMoreButton(View view) {
        if (!SettingsEnum.HIDE_LOAD_MORE_BUTTON.getBoolean())
            return;

        if (!(view instanceof ViewGroup viewGroup))
            return;

        if (!(viewGroup.getChildAt(0) instanceof ViewGroup expandButtonContainer))
            return;

        if (layoutParams == null
                && expandButtonContainer.getLayoutParams() instanceof FrameLayout.LayoutParams lp) {
            layoutParams = lp;
            paddingLeft = view.getPaddingLeft();
            paddingTop = view.getPaddingTop();
            paddingRight = view.getPaddingRight();
            paddingBottom = view.getPaddingBottom();
        }

        ReVancedUtils.runOnMainThreadDelayed(() -> {
                    if (minimumHeight == 1) {
                        minimumHeight = view.getMinimumHeight();
                    }
                    if (expandButtonContainer.getChildAt(0).getVisibility() != View.VISIBLE && layoutParams != null) {
                        view.setMinimumHeight(minimumHeight);
                        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                        expandButtonContainer.setLayoutParams(layoutParams);
                    } else {
                        view.setMinimumHeight(0);
                        view.setPadding(0, 0, 0, 0);
                        expandButtonContainer.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
                    }
                },
                0
        );
    }

    public static boolean hideSearchTermThumbnail() {
        return SettingsEnum.HIDE_SEARCH_TERM_THUMBNAIL.getBoolean();
    }


    public static boolean hideSnackBar() {
        return SettingsEnum.HIDE_SNACK_BAR.getBoolean();
    }

    public static void hideToolBarButton(String enumString, View view) {
        hideViewUnderCondition(
                toolBarButtonList.stream().anyMatch(enumString::contains)
                        && SettingsEnum.HIDE_TOOLBAR_CREATE_NOTIFICATION_BUTTON.getBoolean(),
                view
        );
    }

    public static void hideTrendingSearches(ImageView imageView, boolean isTrendingSearches) {
        View parent = (View) imageView.getParent();

        if (SettingsEnum.HIDE_TRENDING_SEARCHES.getBoolean() && isTrendingSearches)
            parent.setVisibility(View.GONE);
        else
            parent.setVisibility(View.VISIBLE);

    }
}
