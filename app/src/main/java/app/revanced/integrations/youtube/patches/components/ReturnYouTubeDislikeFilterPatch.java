package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import app.revanced.integrations.youtube.patches.utils.ReturnYouTubeDislikePatch;
import app.revanced.integrations.youtube.patches.video.VideoInformation;
import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.TrieSearch;

/**
 * Searches for video id's in the proto buffer of Shorts dislike.
 * <p>
 * Because multiple litho dislike spans are created in the background
 * (and also anytime litho refreshes the components, which is somewhat arbitrary),
 * that makes the value of {@link VideoInformation#getVideoId()} and {@link VideoInformation#getPlayerResponseVideoId()}
 * unreliable to determine which video id a Shorts litho span belongs to.
 * <p>
 * But the correct video id does appear in the protobuffer just before a Shorts litho span is created.
 * <p>
 * Once a way to asynchronously update litho text is found, this strategy will no longer be needed.
 */
@SuppressWarnings("unused")
public final class ReturnYouTubeDislikeFilterPatch extends Filter {

    /**
     * Last unique video id's loaded.  Value is ignored and Map is treated as a Set.
     * Cannot use {@link LinkedHashSet} because it's missing #removeEldestEntry().
     */
    @GuardedBy("itself")
    private static final Map<String, Boolean> lastVideoIds = new LinkedHashMap<>() {
        /**
         * Number of video id's to keep track of for searching through the buffer.
         * A minimum value of 3 should be sufficient, but check a few more just in case.
         */
        private static final int NUMBER_OF_LAST_VIDEO_IDS_TO_TRACK = 5;

        @Override
        protected boolean removeEldestEntry(Entry eldest) {
            return size() > NUMBER_OF_LAST_VIDEO_IDS_TO_TRACK;
        }
    };
    private final ByteArrayFilterGroupList videoIdFilterGroup = new ByteArrayFilterGroupList();

    public ReturnYouTubeDislikeFilterPatch() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(SettingsEnum.RYD_SHORTS, "|shorts_dislike_button.eml|")
        );
        // After the dislikes icon name is some binary data and then the video id for that specific short.
        videoIdFilterGroup.addAll(
                // Video was previously disliked before video was opened.
                new ByteArrayAsStringFilterGroup(null, "ic_right_dislike_on_shadowed"),
                // Video was not already disliked.
                new ByteArrayAsStringFilterGroup(null, "ic_right_dislike_off_shadowed")
        );
    }

    /**
     * Injection point.
     */
    @SuppressWarnings("unused")
    public static void newPlayerResponseVideoId(String videoId, boolean isShortAndOpeningOrPlaying) {
        try {
            if (!isShortAndOpeningOrPlaying || !SettingsEnum.RYD_SHORTS.getBoolean()) {
                return;
            }
            synchronized (lastVideoIds) {
                if (lastVideoIds.put(videoId, Boolean.TRUE) == null) {
                    LogHelper.printDebug(() -> "New Short video id: " + videoId);
                }
            }
        } catch (Exception ex) {
            LogHelper.printException(() -> "newPlayerResponseVideoId failure", ex);
        }
    }

    /**
     * This could use {@link TrieSearch}, but since the patterns are constantly changing
     * the overhead of updating the Trie might negate the search performance gain.
     */
    private static boolean byteArrayContainsString(@NonNull byte[] array, @NonNull String text) {
        for (int i = 0, lastArrayStartIndex = array.length - text.length(); i <= lastArrayStartIndex; i++) {
            boolean found = true;
            for (int j = 0, textLength = text.length(); j < textLength; j++) {
                if (array[i + j] != (byte) text.charAt(j)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }
        return false;
    }

    /**
     * @noinspection rawtypes
     */
    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        FilterGroup.FilterGroupResult result = videoIdFilterGroup.check(protobufBufferArray);
        if (result.isFiltered()) {
            String matchedVideoId = findVideoId(protobufBufferArray);
            // Matched video will be null if in incognito mode.
            // Must pass a null id to correctly clear out the current video data.
            // Otherwise, if a Short is opened in non-incognito, then incognito is enabled and another Short is opened,
            // the new incognito Short will show the old prior data.
            ReturnYouTubeDislikePatch.setLastLithoShortsVideoId(matchedVideoId);
        }

        return false;
    }

    @Nullable
    private String findVideoId(byte[] protobufBufferArray) {
        synchronized (lastVideoIds) {
            for (String videoId : lastVideoIds.keySet()) {
                if (byteArrayContainsString(protobufBufferArray, videoId)) {
                    return videoId;
                }
            }
            return null;
        }
    }
}
