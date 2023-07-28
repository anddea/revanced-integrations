package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public final class LayoutComponentsFilter extends Filter {
    private final String[] exceptions;

    private final CustomFilterGroup custom;

    public LayoutComponentsFilter() {
        exceptions = new String[]{
                "related_video_with_context",
                "comment_thread", // skip blocking anything in the comments
                "|comment.", // skip blocking anything in the comments replies
                "library_recent_shelf"
        };

        custom = new CustomFilterGroup(
                SettingsEnum.CUSTOM_FILTER,
                SettingsEnum.CUSTOM_FILTER_STRINGS
        );

        final var albumCard = new StringFilterGroup(
                SettingsEnum.HIDE_ALBUM_CARDS,
                "browsy_bar",
                "official_card"
        );

        final var audioTrackButton = new StringFilterGroup(
                SettingsEnum.HIDE_AUDIO_TRACK_BUTTON,
                "multi_feed_icon_button"
        );

        final var channelMemberShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_MEMBER_SHELF,
                "member_recognition_shelf"
        );

        final var chipsShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CHIPS_SHELF,
                "chips_shelf"
        );

        final var expandableMetadata = new StringFilterGroup(
                SettingsEnum.HIDE_EXPANDABLE_CHIP,
                "inline_expander"
        );

        final var feedSurvey = new StringFilterGroup(
                SettingsEnum.HIDE_FEED_SURVEY,
                "feed_nudge",
                "in_feed_survey",
                "slimline_survey"
        );

        final var graySeparator = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_SEPARATOR,
                "cell_divider"
        );

        final var infoPanel = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_PANEL,
                "compact_banner",
                "publisher_transparency_panel",
                "single_item_information_panel"
        );

        final var joinMembership = new StringFilterGroup(
                SettingsEnum.HIDE_JOIN_BUTTON,
                "compact_sponsor_button"
        );

        final var latestPosts = new StringFilterGroup(
                SettingsEnum.HIDE_LATEST_POSTS,
                "post_shelf"
        );

        final var medicalPanel = new StringFilterGroup(
                SettingsEnum.HIDE_MEDICAL_PANEL,
                "emergency_onebox",
                "medical_panel"
        );

        final var movieShelf = new StringFilterGroup(
                SettingsEnum.HIDE_MOVIE_SHELF,
                "compact_movie",
                "horizontal_movie_shelf",
                "movie_and_show_upsell_card",
                "compact_tvfilm_item",
                "offer_module_root"
        );

        final var ticketShelf = new StringFilterGroup(
                SettingsEnum.HIDE_TICKET_SHELF,
                "ticket_horizontal_shelf",
                "ticket_shelf"
        );

        final var timedReactions = new StringFilterGroup(
                SettingsEnum.HIDE_TIMED_REACTIONS,
                "emoji_control_panel",
                "timed_reaction"
        );

        this.pathFilterGroups.addAll(
                albumCard,
                audioTrackButton,
                channelMemberShelf,
                expandableMetadata,
                feedSurvey,
                infoPanel,
                joinMembership,
                latestPosts,
                medicalPanel,
                movieShelf,
                ticketShelf,
                timedReactions
        );

        this.identifierFilterGroups.addAll(
                chipsShelf,
                graySeparator
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String object, final byte[] _protobufBufferArray) {
        if (custom.isEnabled() && custom.check(path).isFiltered())
            return true;

        if (ReVancedUtils.containsAny(path, exceptions))
            return false;

        return super.isFiltered(path, identifier, object, _protobufBufferArray);
    }
}
