package app.revanced.integrations.youtube.patches.misc;

import static app.revanced.integrations.youtube.patches.misc.requests.StoryboardRendererRequester.getStoryboardRenderer;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.containsAny;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.submitOnBackgroundThread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.revanced.integrations.youtube.patches.video.VideoInformation;
import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.PlayerType;
import app.revanced.integrations.youtube.utils.LogHelper;

/**
 * @noinspection ALL
 */
public class SpoofPlayerParameterPatch {
    private static final boolean spoofParameter = SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean();
    private static final boolean spoofParameterInFeed = SettingsEnum.SPOOF_PLAYER_PARAMETER_IN_FEED.getBoolean();

    /**
     * Parameter (also used by
     * <a href="https://github.com/yt-dlp/yt-dlp/blob/81ca451480051d7ce1a31c017e005358345a9149/yt_dlp/extractor/youtube.py#L3602">yt-dlp</a>)
     * to fix playback issues.
     */
    private static final String INCOGNITO_PARAMETERS = "CgIQBg==";

    /**
     * Parameters used when playing clips.
     */
    private static final String CLIPS_PARAMETERS = "kAIB";

    /**
     * Parameters causing playback issues.
     */
    private static final String[] AUTOPLAY_PARAMETERS = {
            "YAHI", // Autoplay in feed.
            "SAFg"  // Autoplay in scrim.
    };

    /**
     * Parameter used for autoplay in scrim.
     * Prepend this parameter to mute video playback (for autoplay in feed).
     */
    private static final String SCRIM_PARAMETER = "SAFgAXgB";

    /**
     * Last video id loaded. Used to prevent reloading the same spec multiple times.
     */
    @Nullable
    private static volatile String lastPlayerResponseVideoId;

    @Nullable
    private static volatile Future<StoryboardRenderer> rendererFuture;

    private static volatile boolean useOriginalStoryboardRenderer;

    @Nullable
    private static StoryboardRenderer getRenderer(boolean waitForCompletion) {
        Future<StoryboardRenderer> future = rendererFuture;
        if (future != null) {
            try {
                if (waitForCompletion || future.isDone()) {
                    return future.get(20000, TimeUnit.MILLISECONDS); // Any arbitrarily large timeout.
                } // else, return null.
            } catch (TimeoutException ex) {
                LogHelper.printDebug(() -> "Could not get renderer (get timed out)");
            } catch (ExecutionException | InterruptedException ex) {
                // Should never happen.
                LogHelper.printException(() -> "Could not get renderer", ex);
            }
        }
        return null;
    }

    /**
     * Injection point.
     * <p>
     * {@link VideoInformation#getVideoId()} cannot be used because it is injected after PlayerResponse.
     * Therefore, we use the videoId called from PlaybackStartDescriptor.
     *
     * @param videoId    Original video id value.
     * @param parameters Original player parameter value.
     */
    public static String spoofParameter(@NonNull String videoId, @Nullable String parameters, boolean isShortAndOpeningOrPlaying) {
        try {
            LogHelper.printDebug(() -> "Original player parameter value: " + parameters);

            if (!spoofParameter) {
                return parameters;
            }

            // Shorts do not need to be spoofed.
            if (useOriginalStoryboardRenderer = VideoInformation.playerParametersAreShort(parameters)) {
                return parameters;
            }

            // Clip's player parameters contain important information such as where the video starts, where it ends, and whether it loops.
            // Clips are 60 seconds or less in length, so no spoofing.
            if (useOriginalStoryboardRenderer = parameters.length() > 150 || containsAny(parameters, CLIPS_PARAMETERS)) {
                return parameters;
            }

            final boolean isPlayingFeed = PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL
                    && containsAny(parameters, AUTOPLAY_PARAMETERS);
            if (isPlayingFeed) {
                //noinspection AssignmentUsedAsCondition
                if (useOriginalStoryboardRenderer = !spoofParameterInFeed) {
                    // Don't spoof the feed video playback. This will cause video playback issues,
                    // but only if user continues watching for more than 1 minute.
                    return parameters;
                }
                // Spoof the feed video.  Video will show up in watch history and video subtitles are missing.
                fetchStoryboardRenderer(videoId);
                return SCRIM_PARAMETER + INCOGNITO_PARAMETERS;
            }

            fetchStoryboardRenderer(videoId);
        } catch (Exception ex) {
            LogHelper.printException(() -> "spoofParameter failure", ex);
        }
        return INCOGNITO_PARAMETERS;
    }

    private static void fetchStoryboardRenderer(@NonNull String videoId) {
        if (!videoId.equals(lastPlayerResponseVideoId)) {
            rendererFuture = submitOnBackgroundThread(() -> getStoryboardRenderer(videoId));
            lastPlayerResponseVideoId = videoId;
        }
        // Block until the renderer fetch completes.
        // This is desired because if this returns without finishing the fetch
        // then video will start playback but the storyboard is not ready yet.
        getRenderer(true);
    }

    private static String getStoryboardRendererSpec(String originalStoryboardRendererSpec,
                                                    boolean returnNullIfLiveStream) {
        if (spoofParameter && !useOriginalStoryboardRenderer) {
            final StoryboardRenderer renderer = getRenderer(false);
            if (renderer != null) {
                if (returnNullIfLiveStream && renderer.isLiveStream()) {
                    return null;
                }
                String spec = renderer.getSpec();
                if (spec != null) {
                    return spec;
                }
            }
        }

        return originalStoryboardRendererSpec;
    }

    /**
     * Injection point.
     * Called from background threads and from the main thread.
     */
    @Nullable
    public static String getStoryboardRendererSpec(String originalStoryboardRendererSpec) {
        return getStoryboardRendererSpec(originalStoryboardRendererSpec, false);
    }

    /**
     * Injection point.
     * Uses additional check to handle live streams.
     * Called from background threads and from the main thread.
     */
    @Nullable
    public static String getStoryboardDecoderRendererSpec(String originalStoryboardRendererSpec) {
        return getStoryboardRendererSpec(originalStoryboardRendererSpec, true);
    }

    /**
     * Injection point.
     */
    public static int getRecommendedLevel(int originalLevel) {
        if (spoofParameter && !useOriginalStoryboardRenderer) {
            final StoryboardRenderer renderer = getRenderer(false);
            if (renderer != null) {
                Integer recommendedLevel = renderer.getRecommendedLevel();
                if (recommendedLevel != null) return recommendedLevel;
            }
        }

        return originalLevel;
    }

    /**
     * Injection point.  Forces seekbar to be shown for paid videos or
     * if {@link SettingsEnum#SPOOF_PLAYER_PARAMETER} is not enabled.
     */
    public static boolean getSeekbarThumbnailOverrideValue() {
        if (!spoofParameter) {
            return false;
        }
        final StoryboardRenderer renderer = getRenderer(false);
        if (renderer == null) {
            // Spoof storyboard renderer is turned off,
            // video is paid, or the storyboard fetch timed out.
            // Show empty thumbnails so the seek time and chapters still show up.
            return true;
        }
        return renderer.getSpec() != null;
    }
}
