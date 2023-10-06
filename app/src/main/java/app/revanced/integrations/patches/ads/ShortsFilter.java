package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public final class ShortsFilter extends Filter {
    private static final String REEL_CHANNEL_BAR_PATH = "reel_channel_bar.eml";

    private final StringFilterGroup shelfHeader;

    public ShortsFilter() {
        final var thanksButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON,
                "suggested_action"
        );

        final var shorts = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shorts_shelf",
                "inline_shorts",
                "shorts_grid",
                "shorts_pivot_item",
                "shorts_video_cell"
        );

        shelfHeader = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shelf_header.eml"
        );

        final var joinButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON,
                "sponsor_button"
        );

        final var subscribeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON,
                "subscribe_button"
        );

        identifierFilterGroupList.addAll(shorts, shelfHeader, thanksButton);
        pathFilterGroupList.addAll(joinButton, subscribeButton);
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedList == pathFilterGroupList) {
            // Filter other path groups from pathFilterGroupList, only when reelChannelBar is visible
            // to avoid false positives.
            if (!path.startsWith(REEL_CHANNEL_BAR_PATH))
                return false;
        } else if (matchedGroup == shelfHeader) {
            // Because the header is used in watch history and possibly other places, check for the index,
            // which is 0 when the shelf header is used for Shorts.
            if (matchedIndex != 0)
                return false;
        }

        // Super class handles logging.
        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
