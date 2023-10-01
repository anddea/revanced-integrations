package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedHelper.isSpoofedTargetVersionLez;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewBy0dpUnderCondition;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;
import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.annotation.SuppressLint;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.ResourceType;

public class GeneralPatch {
    private static final String PREMIUM_HEADER_NAME = "ytPremiumWordmarkHeader";
    private static final List<String> horizontalShelf = Arrays.asList(
            "horizontal_tile_shelf.eml",
            "horizontal_video_shelf.eml"
    );
    public static boolean captionsButtonStatus;
    @SuppressLint("StaticFieldLeak")
    public static View compactLink;

    public static boolean disableAutoCaptions() {
        return SettingsEnum.DISABLE_AUTO_CAPTIONS.getBoolean() && !PlayerType.getCurrent().isNoneOrHidden();
    }

    public static int enablePremiumHeader(int originalValue) {
        if (SettingsEnum.ENABLE_PREMIUM_HEADER.getBoolean())
            return identifier(PREMIUM_HEADER_NAME, ResourceType.ATTR);

        return originalValue;
    }

    public static boolean enableTabletMiniPlayer(boolean original) {
        return SettingsEnum.ENABLE_TABLET_MINI_PLAYER.getBoolean() || original;
    }

    public static boolean enableWideSearchBar() {
        return SettingsEnum.ENABLE_WIDE_SEARCH_BAR.getBoolean();
    }

    public static void hideAccountMenu(@NonNull Spanned span) {
        if (!(compactLink instanceof ViewGroup viewGroup) || !SettingsEnum.HIDE_ACCOUNT_MENU.getBoolean())
            return;

        String[] blockList = SettingsEnum.HIDE_ACCOUNT_MENU_FILTER_STRINGS.getString().split("\\n");

        for (String filter : blockList) {
            if (span.toString().contains(filter) && !filter.isEmpty()) {
                if (!(viewGroup.getLayoutParams() instanceof MarginLayoutParams))
                    viewGroup.setLayoutParams(new LayoutParams(0, 0));
            }
        }
    }

    public static boolean hideAutoPlayerPopupPanels() {
        return SettingsEnum.HIDE_AUTO_PLAYER_POPUP_PANELS.getBoolean();
    }

    public static void hideBreakingNewsShelf(View view) {
        hideViewBy0dpUnderCondition(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF.getBoolean()
                        && !isSpoofedTargetVersionLez("17.31.00"),
                view
        );
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

    public static int hideEmailAddress(int originalValue) {
        return SettingsEnum.HIDE_EMAIL_ADDRESS.getBoolean() ? 8 : originalValue;
    }

    public static boolean hideFloatingMicrophone(boolean original) {
        return SettingsEnum.HIDE_FLOATING_MICROPHONE.getBoolean() || original;
    }

    public static void hideLatestVideosButton(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_LATEST_VIDEOS_BUTTON.getBoolean(), view);
    }

    public static void hideLoadMoreButton(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_LOAD_MORE_BUTTON.getBoolean(), view);
    }

    public static void hideMixPlaylists(View view) {
        hideViewBy0dpUnderCondition(SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean(), view);
    }

    public static boolean hideSnackBar() {
        return SettingsEnum.HIDE_SNACK_BAR.getBoolean();
    }

    /**
     * In this method, only subcomponents are created:
     * - horizontal video shelf in feed (horizontal_video_shelf.eml)
     * - video action bar (video_action_bar.eml)
     * <p>
     * Horizontal video shelf used in library tab is not used in this method
     * The header of the suggestion shelf cannot be removed here, so it must be removed from the low-level filter
     *
     * @param object allValue
     * @return whether horizontal video shelf contains
     */
    public static boolean hideSuggestionsShelf(Object object) {
        if (!SettingsEnum.HIDE_SUGGESTIONS_SHELF.getBoolean())
            return false;

        final String allValue = object.toString();
        return horizontalShelf.stream().anyMatch(allValue::contains);
    }

    public static void hideTrendingSearches(ImageView imageView, boolean isTrendingSearches) {
        View parent = (View) imageView.getParent();

        if (SettingsEnum.HIDE_TRENDING_SEARCHES.getBoolean() && isTrendingSearches)
            parent.setVisibility(View.GONE);
        else
            parent.setVisibility(View.VISIBLE);

    }
}
