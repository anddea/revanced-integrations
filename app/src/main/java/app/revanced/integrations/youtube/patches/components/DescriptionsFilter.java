package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.StringTrieSearch;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
final class DescriptionsFilter extends Filter {

    private final StringTrieSearch exceptions = new StringTrieSearch();
    private final StringFilterGroup shoppingLinks;

    private final StringFilterGroup videoAttributesFilterPath;
    private final ByteArrayFilterGroupList bufferFilterGroupList = new ByteArrayFilterGroupList();

    public DescriptionsFilter() {
        exceptions.addPatterns(
                "compact_channel",
                "description",
                "grid_video",
                "inline_expander",
                "metadata"
        );

        // Video attributes section includes both Games and Music sections for new YT versions
        videoAttributesFilterPath = new StringFilterGroup(
                null,
                "video_attributes_section"
        );

        final StringFilterGroup chapterSection = new StringFilterGroup(
                SettingsEnum.HIDE_CHAPTERS,
                "macro_markers_carousel."
        );

        final StringFilterGroup infoCardsSection = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_CARDS_SECTION,
                "infocards_section"
        );

        final StringFilterGroup gameSection = new StringFilterGroup(
                SettingsEnum.HIDE_GAME_SECTION,
                "gaming_section"
        );

        final StringFilterGroup musicSection = new StringFilterGroup(
                SettingsEnum.HIDE_MUSIC_SECTION,
                "music_section"
        );

        final StringFilterGroup placeSection = new StringFilterGroup(
                SettingsEnum.HIDE_PLACE_SECTION,
                "place_section"
        );

        final StringFilterGroup podcastSection = new StringFilterGroup(
                SettingsEnum.HIDE_PODCAST_SECTION,
                "playlist_section"
        );

        shoppingLinks = new StringFilterGroup(
                SettingsEnum.HIDE_SHOPPING_LINKS,
                "expandable_list"
        );

        final StringFilterGroup transcriptSection = new StringFilterGroup(
                SettingsEnum.HIDE_TRANSCIPT_SECTION,
                "transcript_section"
        );

        pathFilterGroupList.addAll(
                chapterSection,
                infoCardsSection,
                gameSection,
                musicSection,
                placeSection,
                podcastSection,
                shoppingLinks,
                transcriptSection,
                videoAttributesFilterPath
        );

        // Buffer filter for video attributes section
        // If one of the options is enabled header will be hidden for both of them,
        // first solution that comes to mind is either just join two options into one,
        // or use pattern checking: games header has one "sans-serif" string, music - 2.
        bufferFilterGroupList.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_GAME_SECTION,
                        "eml.shelf_header", // header
                        "GamC", // Game info
                        "yt_outline_gaming" // footer button
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_MUSIC_SECTION,
                        "eml.shelf_header", // header
                        "overflow_button", // Music info
                        "yt_outline_audio" // footer button
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path))
            return false;

        // Check for the index because of likelihood of false positives.
        if (matchedGroup == shoppingLinks && matchedIndex != 0)
            return false;

        if (matchedGroup == videoAttributesFilterPath) {
            return bufferFilterGroupList.check(protobufBufferArray).isFiltered();
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
