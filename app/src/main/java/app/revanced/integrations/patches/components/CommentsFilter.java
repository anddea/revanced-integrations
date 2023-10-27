package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.StringTrieSearch;

final class CommentsFilter extends Filter {
    private static final String COMMENT_COMPOSER_PATH = "comment_composer.eml";
    private static final String VIDEO_METADATA_CAROUSEL_PATH = "video_metadata_carousel.eml";

    private final StringFilterGroup commentsPreviewDots;
    private final StringFilterGroup emojiPicker;
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
                VIDEO_METADATA_CAROUSEL_PATH,
                "comments_"
        );

        commentsPreviewDots = new StringFilterGroup(
                SettingsEnum.HIDE_PREVIEW_COMMENT,
                "|ContainerType|ContainerType|ContainerType|"
        );

        emojiPicker = new StringFilterGroup(
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
                "|ContainerType|ContainerType|ContainerType|super_thanks_button.eml"
        );


        pathFilterGroupList.addAll(
                channelGuidelines,
                comments,
                commentsPreviewDots,
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

        if (matchedGroup == emojiPicker) {
            if (!path.startsWith(COMMENT_COMPOSER_PATH))
                return false;
        } else if (matchedGroup == commentsPreviewDots) {
            if (!path.startsWith(VIDEO_METADATA_CAROUSEL_PATH))
                return false;
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
