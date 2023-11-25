package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.StringTrieSearch;

public final class ShortsFilter extends Filter {
    private static final String REEL_CHANNEL_BAR_PATH = "reel_channel_bar.eml";
    private static final String SHORTS_SHELF_HEADER_CONVERSION_CONTEXT = "horizontalCollectionSwipeProtector=null";

    private final StringTrieSearch exceptions = new StringTrieSearch();
    private final StringFilterGroup infoPanel;
    private final StringFilterGroup shelfHeader;

    private final StringFilterGroup videoActionButton;
    private final ByteArrayFilterGroupList videoActionButtonGroupList = new ByteArrayFilterGroupList();


    public ShortsFilter() {
        exceptions.addPatterns(
                "lock_mode_suggested_action"
        );

        final StringFilterGroup thanksButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON,
                "suggested_action"
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
                shorts,
                shelfHeader,
                thanksButton
        );

        final StringFilterGroup joinButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON,
                "sponsor_button"
        );

        final StringFilterGroup subscribeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON,
                "shorts_paused_state",
                "subscribe_button"
        );

        infoPanel = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_INFO_PANEL,
                "reel_multi_format_link",
                "reel_sound_metadata",
                "shorts_info_panel_overview"
        );

        videoActionButton = new StringFilterGroup(
                null,
                "shorts_video_action_button"
        );

        pathFilterGroupList.addAll(
                joinButton,
                subscribeButton,
                infoPanel,
                videoActionButton
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
                shortsRemixButton,
                shortsShareButton
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path))
            return false;

        if (matchedList == pathFilterGroupList) {
            // Always filter if matched.
            if (matchedGroup == infoPanel)
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);

            // Video action buttons have the same path.
            if (matchedGroup == videoActionButton) {
                if (videoActionButtonGroupList.check(protobufBufferArray).isFiltered())
                    return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
                return false;
            }

            // Filter other path groups from pathFilterGroupList, only when reelChannelBar is visible
            // to avoid false positives.
            if (!path.startsWith(REEL_CHANNEL_BAR_PATH))
                return false;
        } else if (matchedGroup == shelfHeader) {
            // Check ConversationContext to not hide shelf header in channel profile
            // This value does not exist in the shelf header in the channel profile
            if (!allValue.contains(SHORTS_SHELF_HEADER_CONVERSION_CONTEXT))
                return false;
        }

        // Super class handles logging.
        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
