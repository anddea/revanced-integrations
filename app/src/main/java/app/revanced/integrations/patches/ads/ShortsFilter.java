package app.revanced.integrations.patches.ads;

import static app.revanced.integrations.patches.utils.PatchStatus.ShortsComponent;

import app.revanced.integrations.patches.utils.NavBarIndexPatch;
import app.revanced.integrations.settings.SettingsEnum;

public final class ShortsFilter extends Filter {

    private final StringFilterGroup reelChannelBar = new StringFilterGroup(
            null,
            "reel_channel_bar"
    );

    public ShortsFilter() {
        final var thanksButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON,
                "suggested_action"
        );

        final var subscribeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON,
                "subscribe_button"
        );

        final var joinButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON,
                "sponsor_button"
        );

        final var shorts = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shelf_header", // shorts shelf header is not blocked in the 'shorts_video_cell'
                "shorts_shelf",
                "inline_shorts",
                "shorts_grid",
                "shorts_video_cell"
        );

        this.pathFilterGroups.addAll(joinButton, subscribeButton);
        this.identifierFilterGroups.addAll(shorts, thanksButton);
    }

    @Override
    boolean isFiltered(final String path, final String identifier, final String object, final byte[] protobufBufferArray) {
        // 'shelf_header' is also used in the library tab, so use [NavBarIndexPatch] to identify it
        if (!ShortsComponent() || !NavBarIndexPatch.isNotLibraryTab())
            return false;

        if (reelChannelBar.check(path).isFiltered())
            if (this.pathFilterGroups.contains(path))
                return true;

        return this.identifierFilterGroups.contains(identifier);
    }
}
