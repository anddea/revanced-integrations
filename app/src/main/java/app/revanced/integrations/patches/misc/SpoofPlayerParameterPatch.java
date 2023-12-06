package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.patches.misc.requests.StoryBoardRendererRequester.getStoryboardRenderer;
import static app.revanced.integrations.utils.ReVancedUtils.containsAny;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.LogHelper;

/**
 * @noinspection ALL
 */
public class SpoofPlayerParameterPatch {

    /**
     * Parameter (also used by
     * <a href="https://github.com/yt-dlp/yt-dlp/blob/81ca451480051d7ce1a31c017e005358345a9149/yt_dlp/extractor/youtube.py#L3602">yt-dlp</a>)
     * to fix playback issues.
     */
    private static final String INCOGNITO_PARAMETERS = "CgIQBg==";

    /**
     * Parameters causing playback issues.
     */
    private static final String[] AUTOPLAY_PARAMETERS = {
            "YAHI", // Autoplay in feed.
            "SAFg"  // Autoplay in scrim.
    };

    /**
     * Parameters used in Your Clips.
     */
    private static final String CLIPS_PARAMETERS = "kAIB";

    /**
     * Parameter used for autoplay in scrim.
     * Prepend this parameter to mute video playback (for autoplay in feed).
     */
    private static final String SCRIM_PARAMETER = "SAFgAXgB";

    @Nullable
    private static volatile StoryboardRenderer videoRenderer;

    private static volatile boolean originalStoryboardRenderer;


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
        LogHelper.printDebug(() -> "Original player parameter value: " + parameters);

        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean()) {
            return parameters;
        }

        // Shorts do not need to be spoofed.
        if (originalStoryboardRenderer = VideoInformation.playerParametersAreShort(parameters)) {
            return parameters;
        }

        // Clip's player parameters contain important information such as where the video starts, where it ends, and whether it loops.
        // Clips are 60 seconds or less in length, so no spoofing.
        if (originalStoryboardRenderer = parameters.length() > 150 || parameters.contains(CLIPS_PARAMETERS)) {
            return parameters;
        }

        final boolean isPlayingFeed = PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL
                && containsAny(parameters, AUTOPLAY_PARAMETERS);

        if (!isPlayingFeed) {
            // StoryboardRenderer is always empty when playing video with INCOGNITO_PARAMETERS parameter.
            // Fetch StoryboardRenderer without parameter.
            fetchStoryboardRenderer(videoId);
            // Spoof the player parameter to prevent playback issues.
            return INCOGNITO_PARAMETERS;
        }

        if (originalStoryboardRenderer = !SettingsEnum.SPOOF_PLAYER_PARAMETER_IN_FEED.getBoolean()) {
            return parameters;
        } else {
            // StoryboardRenderer is always empty when playing video with INCOGNITO_PARAMETERS parameter.
            // Fetch StoryboardRenderer without parameter.
            fetchStoryboardRenderer(videoId);
            // Spoof the player parameter to prevent playback issues.
            return SCRIM_PARAMETER + INCOGNITO_PARAMETERS;
        }
    }

    private static void fetchStoryboardRenderer(@NonNull String videoId) {
        // Allow duplicate fetches of StoryBoard.
        // This is a temporary workaround for StoryBoard not updating for some reason.
        videoRenderer = getStoryboardRenderer(videoId);
    }

    /**
     * Injection point.  Forces seekbar to be shown for paid videos or
     * if {@link SettingsEnum#SPOOF_PLAYER_PARAMETER} is not enabled.
     */
    public static boolean getSeekbarThumbnailOverrideValue() {
        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean()) {
            return false;
        }
        final StoryboardRenderer renderer = videoRenderer;
        // Spoof storyboard renderer is turned off,
        // video is paid, or the storyboard fetch timed out.
        // Show empty thumbnails so the seek time and chapters still show up.
        return renderer == null || renderer.getSpec() != null;
    }

    /**
     * Injection point.
     * Called from background threads and from the main thread.
     */
    @Nullable
    public static String getStoryboardRendererSpec() {
        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean() || originalStoryboardRenderer)
            return null;

        StoryboardRenderer renderer = videoRenderer;
        if (renderer != null)
            return renderer.getSpec();

        return null;
    }

    /**
     * Injection point.
     * <p>
     * This method is only injected into methods that create storyboards in the live stream.
     * In this case, this value should be null in the live stream.
     */
    @Nullable
    public static String getStoryboardRendererSpec(String originalStoryboardRendererSpec) {
        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean() || originalStoryboardRenderer)
            return originalStoryboardRendererSpec;

        StoryboardRenderer renderer = videoRenderer;
        if (renderer != null) {
            return renderer.isLiveStream() ? null : renderer.getSpec();
        }

        return originalStoryboardRendererSpec;
    }

    /**
     * Injection point.
     */
    public static int getRecommendedLevel(int originalLevel) {
        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean() || originalStoryboardRenderer)
            return originalLevel;

        StoryboardRenderer renderer = videoRenderer;
        if (renderer != null) {
            Integer recommendedLevel = renderer.getRecommendedLevel();
            if (recommendedLevel != null)
                return recommendedLevel;
        }

        return originalLevel;
    }
}
