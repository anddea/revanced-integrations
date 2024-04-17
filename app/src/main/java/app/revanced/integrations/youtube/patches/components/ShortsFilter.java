package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.NavigationBar;
import app.revanced.integrations.youtube.shared.NavigationBar.NavigationButton;
import app.revanced.integrations.youtube.shared.PlayerType;
import app.revanced.integrations.youtube.utils.StringTrieSearch;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
public final class ShortsFilter extends Filter {
    private static final String REEL_CHANNEL_BAR_PATH = "reel_channel_bar.eml";
    private static final String REEL_METAPANEL_PATH = "reel_metapanel.eml";

    private static final String SHORTS_SHELF_HEADER_CONVERSION_CONTEXT = "horizontalCollectionSwipeProtector=null";

    private final StringFilterGroup shortsCompactFeedVideoPath;
    private final ByteArrayAsStringFilterGroup shortsCompactFeedVideoBuffer;

    private final StringTrieSearch exceptions = new StringTrieSearch();
    private final StringFilterGroup reelSoundMetadata;
    private final StringFilterGroup videoTitle;
    private final StringFilterGroup videoLinkLabel;
    private final StringFilterGroup infoPanel;
    private final StringFilterGroup joinButton;

    private final StringFilterGroup shelfHeader;
    private final StringFilterGroup subscribeButton;

    private final StringFilterGroup videoActionButton;
    private final StringFilterGroup suggestedAction;
    private final ByteArrayFilterGroupList suggestedActionsGroupList =  new ByteArrayFilterGroupList();
    private final ByteArrayFilterGroupList videoActionButtonGroupList = new ByteArrayFilterGroupList();


    public ShortsFilter() {
        exceptions.addPatterns(
                "lock_mode_suggested_action"
        );

        // Feed Shorts shelf header.
        // Use a different filter group for this pattern, as it requires an additional check after matching.
        shelfHeader = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shelf_header.eml"
        );

        final StringFilterGroup shorts = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shorts_shelf",
                "inline_shorts",
                "shorts_grid",
                "shorts_video_cell"
        );


        identifierFilterGroupList.addAll(
                shelfHeader,
                shorts
        );

        // Shorts that appear in the feed/search when the device is using tablet layout.
        shortsCompactFeedVideoPath = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "compact_video.eml"
        );

        // Filter out items that use the 'frame0' thumbnail.
        // This is a valid thumbnail for both regular videos and Shorts,
        // but it appears these thumbnails are used only for Shorts.
        shortsCompactFeedVideoBuffer = new ByteArrayAsStringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "/frame0.jpg"
        );

        joinButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON,
                "sponsor_button"
        );

        reelSoundMetadata = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_SOUND_METADATA_LABEL,
                "reel_sound_metadata"
        );

        StringFilterGroup pausedOverlayButtons = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PAUSED_OVERLAY_BUTTONS,
                "shorts_paused_state"
        );

        infoPanel = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_INFO_PANEL,
                "reel_multi_format_link",
                "reel_sound_metadata",
                "shorts_info_panel_overview"
        );

        suggestedAction = new StringFilterGroup(
                null,
                "suggested_action.eml"
        );

        subscribeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SUBSCRIBE_BUTTON,
                "subscribe_button"
        );

        videoActionButton = new StringFilterGroup(
                null,
                "shorts_video_action_button"
        );

        videoLinkLabel = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_VIDEO_LINK_LABEL,
                "reel_multi_format_link"
        );

        videoTitle = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_VIDEO_TITLE,
                "shorts_video_title_item"
        );

        pathFilterGroupList.addAll(
                shortsCompactFeedVideoPath,
                joinButton,
                reelSoundMetadata,
                pausedOverlayButtons,
                suggestedAction,
                infoPanel,
                subscribeButton,
                videoActionButton,
                videoLinkLabel,
                videoTitle
        );

        final ByteArrayAsStringFilterGroup shortsDislikeButton =
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_PLAYER_DISLIKE_BUTTON,
                        "reel_dislike_button",
                        "reel_dislike_toggled_button"
                );

        final ByteArrayAsStringFilterGroup shortsLikeButton =
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_PLAYER_LIKE_BUTTON,
                        "reel_like_button",
                        "reel_like_toggled_button"
                );

        final ByteArrayAsStringFilterGroup shortsCommentButton =
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_PLAYER_COMMENTS_BUTTON,
                        "reel_comment_button"
                );

        final ByteArrayAsStringFilterGroup shortsRemixButton =
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_PLAYER_REMIX_BUTTON,
                        "reel_remix_button"
                );

        final ByteArrayAsStringFilterGroup shortsShareButton =
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_PLAYER_SHARE_BUTTON,
                        "reel_share_button"
                );

        videoActionButtonGroupList.addAll(
                shortsCommentButton,
                shortsDislikeButton,
                shortsLikeButton,
                shortsRemixButton,
                shortsShareButton
        );

        //
        // Suggested actions.
        //
        suggestedActionsGroupList.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_SHOP_BUTTON,
                        "yt_outline_bag_"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_TAGGED_PRODUCTS,
                        // Product buttons show pictures of the products, and does not have any unique icons to identify.
                        // Instead, use a unique identifier found in the buffer.
                        "PAproduct_listZ"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_LOCATION_LABEL,
                        "yt_outline_location_point_"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_SAVE_SOUND_BUTTON,
                        "yt_outline_list_add_"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHORTS_SEARCH_SUGGESTIONS,
                        "yt_outline_search_"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path))
            return false;

        if (matchedList == pathFilterGroupList) {
            if (matchedGroup == subscribeButton || matchedGroup == joinButton) {
                // Filter only when reelChannelBar or reelMetapanel is visible to avoid false positives.
                if (path.startsWith(REEL_CHANNEL_BAR_PATH) || path.startsWith(REEL_METAPANEL_PATH)) {
                    return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
                }
                return false;
            }

            if (matchedGroup == infoPanel || matchedGroup == videoLinkLabel ||
                    matchedGroup == videoTitle || matchedGroup == reelSoundMetadata) {
                // Always filter if matched.
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
            } else if (matchedGroup == shortsCompactFeedVideoPath) {
                if (shouldHideShortsFeedItems() && matchedIndex == 0
                        && shortsCompactFeedVideoBuffer.check(protobufBufferArray).isFiltered())
                    return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
                return false;
            } else if (matchedGroup == videoActionButton) {
                // Video action buttons have the same path.
                return videoActionButtonGroupList.check(protobufBufferArray).isFiltered();
            } else if (matchedGroup == suggestedAction) {
                // Suggested actions can be at the start or in the middle of a path.
                if (suggestedActionsGroupList.check(protobufBufferArray).isFiltered())
                    return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
            } else {
                // Filter other path groups from pathFilterGroupList, only when reelChannelBar is visible
                // to avoid false positives.
                return path.startsWith(REEL_CHANNEL_BAR_PATH);
            }
        } else {
            // Feed/search path components.
            if (matchedGroup == shelfHeader) {
                // Because the header is used in watch history and possibly other places, check for the index,
                // which is 0 when the shelf header is used for Shorts.
                if (matchedIndex != 0) return false;
            }

            if (!shouldHideShortsFeedItems()) return false;
        }

        // Super class handles logging.
        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }

    private static boolean shouldHideShortsFeedItems() {
        final boolean hideHome = SettingsEnum.HIDE_SHORTS_HOME.getBoolean();
        final boolean hideSubscriptions = SettingsEnum.HIDE_SHORTS_SUBSCRIPTIONS.getBoolean();
        final boolean hideSearch = SettingsEnum.HIDE_SHORTS_SEARCH.getBoolean();

        if (hideHome && hideSubscriptions && hideSearch) {
            // Shorts suggestions can load in the background if a video is opened and
            // then immediately minimized before any suggestions are loaded.
            // In this state the player type will show minimized, which makes it not possible to
            // distinguish between Shorts suggestions loading in the player and between
            // scrolling through search/home/subscription tabs while a player is minimized.
            //
            // To avoid this situation for users that never want to show Shorts (all hide Shorts options are enabled)
            // then hide all Shorts everywhere including the Library history and Library playlists.
            return true;
        }

        // Must check player type first, as search bar can be active behind the player.
        if (PlayerType.getCurrent().isMaximizedOrFullscreen()) {
            // For now, consider the under video results the same as the home feed.
            return hideHome;
        }

        if (NavigationBar.isSearchBarActive()) { // Must check search first.
            return hideSearch;
        }

        // Avoid checking navigation button status if all other Shorts should show.
        if (!hideHome && !hideSubscriptions) {
            return false;
        }

        NavigationButton selectedNavButton = NavigationButton.getSelectedNavigationButton();
        if (selectedNavButton == null) {
            return hideHome; // Unknown tab, treat the same as home.
        }

        if (selectedNavButton == NavigationButton.HOME) {
            return hideHome;
        }

        if (selectedNavButton == NavigationButton.SUBSCRIPTIONS) {
            return hideSubscriptions;
        }

        // User must be in the library tab. Don't hide the history or any playlists here.
        return false;
    }
}