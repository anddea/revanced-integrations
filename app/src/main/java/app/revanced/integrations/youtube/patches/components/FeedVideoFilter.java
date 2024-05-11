package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.shared.patches.components.ByteArrayFilterGroup;
import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.shared.RootView;

/**
 * @noinspection ALL
 */
public final class FeedVideoFilter extends Filter {
    private final ByteArrayFilterGroup feedRecommendations = new ByteArrayFilterGroup(
            null,
            "endorsement_header_footer" // videos with gray descriptions
    );
    private final ByteArrayFilterGroup homeFeedRecommendations = new ByteArrayFilterGroup(
            null,
            "g-highZ",  // videos with less than 1000 views
            "high-ptsZ" // videos for membership only
    );
    // In search results, vertical video with shorts labels mostly include videos with gray descriptions.
    // Filters without check process.
    private final StringFilterGroup inlineShorts;
    private final StringFilterGroup feedVideo;
    private final StringFilterGroup homeFeedVideoFilter;


    public FeedVideoFilter() {
        homeFeedVideoFilter = new StringFilterGroup(
                Settings.HIDE_RECOMMENDED_VIDEO,
                "home_video_with_context.eml"
        );
        feedVideo = new StringFilterGroup(
                Settings.HIDE_RECOMMENDED_VIDEO,
                "video_with_context.eml"
        );
        inlineShorts = new StringFilterGroup(
                Settings.HIDE_RECOMMENDED_VIDEO,
                "inline_shorts.eml" // vertical video with shorts label
        );

        // Paths.
        addPathCallbacks(
                inlineShorts,
                homeFeedVideoFilter,
                feedVideo
        );
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        if (matchedGroup == inlineShorts) {
            if (contentIndex != 0 || !RootView.isSearchBarActive()) {
                return false;
            }
        } else {
            if (matchedGroup == feedVideo && !feedRecommendations.check(protobufBufferArray).isFiltered()) {
                return false;
            }
            if (matchedGroup == homeFeedVideoFilter) {
                if (contentIndex != 0 || !homeFeedRecommendations.check(protobufBufferArray).isFiltered()) {
                    return false;
                }
            }
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
    }
}
