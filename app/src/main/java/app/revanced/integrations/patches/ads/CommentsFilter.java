package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.StringTrieSearch;

final class CommentsFilter extends Filter {
    private final StringTrieSearch exceptions = new StringTrieSearch();

    public CommentsFilter() {
        exceptions.addPatterns(
                "macro_markers_list_item"
        );

        final var channelGuidelines = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_GUIDELINES,
                "channel_guidelines_entry_banner",
                "community_guidelines",
                "sponsorships_comments_upsell"
        );

        final var comments = new StringFilterGroup(
                SettingsEnum.HIDE_COMMENTS_SECTION,
                "video_metadata_carousel",
                "_comments"
        );

        final var emojiPicker = new StringFilterGroup(
                SettingsEnum.HIDE_EMOJI_PICKER,
                "|CellType|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|"
        );

        final var previewComment = new StringFilterGroup(
                SettingsEnum.HIDE_PREVIEW_COMMENT,
                "|carousel_item",
                "|carousel_listener",
                "comments_entry_point_teaser",
                "comments_entry_point_simplebox"
        );

        final var thanksButton = new StringFilterGroup(
                SettingsEnum.HIDE_COMMENTS_THANKS_BUTTON,
                "super_thanks_button"
        );


        this.pathFilterGroupList.addAll(
                channelGuidelines,
                comments,
                emojiPicker,
                previewComment,
                thanksButton
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path))
            return false;

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
