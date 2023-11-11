package app.revanced.integrations.patches.components;

import static app.revanced.integrations.utils.ReVancedHelper.isSpoofedTargetVersionLez;
import static app.revanced.integrations.utils.ReVancedUtils.hideViewBy0dpUnderCondition;

import android.view.View;

import androidx.annotation.Nullable;

import app.revanced.integrations.patches.utils.BrowseIdPatch;
import app.revanced.integrations.settings.SettingsEnum;

public final class SuggestionsShelfFilter extends Filter {
    private static final String DEFAULT_BROWSE_ID = "FEwhat_to_watch";

    private final StringFilterGroup horizontalShelf;
    private final StringFilterGroup searchResult;

    public SuggestionsShelfFilter() {
        horizontalShelf = new StringFilterGroup(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF,
                "horizontal_tile_shelf.eml",
                "horizontal_video_shelf.eml"
        );

        searchResult = new StringFilterGroup(
                null,
                "compact_channel.eml",
                "search_video_with_context.eml"
        );

        identifierFilterGroupList.addAll(searchResult);
        pathFilterGroupList.addAll(horizontalShelf);
    }

    /**
     * Injection point.
     * <p>
     * Only used to tablet layout and the old UI components.
     */
    public static void hideBreakingNewsShelf(View view) {
        hideViewBy0dpUnderCondition(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF.getBoolean()
                        && !isSpoofedTargetVersionLez("17.31.00"),
                view
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == horizontalShelf) {
            return BrowseIdPatch.isHomeFeed();
        } else if (matchedGroup == searchResult) {
            // In search results, [BrowseId] is not set.
            // To avoid the issue of [BrowseId] not being updated in search results,
            // Manually set the default [BrowseId].
            BrowseIdPatch.setBrowseIdToField(DEFAULT_BROWSE_ID);
        }
        return false;
    }
}
