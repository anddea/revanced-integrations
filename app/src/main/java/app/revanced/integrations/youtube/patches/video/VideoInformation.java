package app.revanced.integrations.youtube.patches.video;

import static app.revanced.integrations.youtube.utils.StringRef.str;
import static app.revanced.integrations.youtube.patches.video.PlaybackSpeedPatch.overrideSpeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Objects;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.VideoState;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedUtils;
import app.revanced.integrations.youtube.utils.VideoHelpers;
import app.revanced.integrations.youtube.whitelist.Whitelist;

/**
 * Hooking class for the current playing video.
 */
@SuppressWarnings("unused")
public final class VideoInformation {
    private static final String SEEK_METHOD_NAME = "seekTo";
    /**
     * Prefix present in all Short player parameters signature.
     */
    private static final String SHORTS_PLAYER_PARAMETERS = "8AEB";

    /**
     * Injection point.
     */
    public static boolean isLiveStream = false;

    private static WeakReference<Object> playerControllerRef;
    private static Method seekMethod;

    @NonNull
    private static String channelName = "";

    @NonNull
    private static String videoId = "";

    private static long videoLength = 0;
    private static long videoTime = -1;

    @NonNull
    private static volatile String playerResponseVideoId = "";
    private static volatile boolean playerResponseVideoIdIsShort;
    private static volatile boolean videoIdIsShort;

    /**
     * Injection point.
     *
     * @param playerController player controller object.
     */
    public static void initialize(@NonNull Object playerController) {
        try {
            playerControllerRef = new WeakReference<>(Objects.requireNonNull(playerController));
            videoLength = 0;
            videoTime = -1;
            channelName = "";

            seekMethod = playerController.getClass().getMethod(SEEK_METHOD_NAME, Long.TYPE);
            seekMethod.setAccessible(true);
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to initialize", ex);
        }
    }

    /**
     * Seek on the current video.
     * Does not function for playback of Shorts.
     * <p>
     * Caution: If called from a videoTimeHook() callback,
     * this will cause a recursive call into the same videoTimeHook() callback.
     *
     * @param seekTime The seekTime to seek the video to.
     * @return true if the seek was successful.
     */
    public static boolean seekTo(final long seekTime) {
        ReVancedUtils.verifyOnMainThread();
        try {
            final long videoTime = getVideoTime();
            final long videoLength = getVideoLength();

            // Prevent issues such as play/ pause button or autoplay not working.
            final long adjustedSeekTime = Math.min(seekTime, videoLength - 250);
            if (videoTime <= seekTime && videoTime >= adjustedSeekTime) {
                // Both the current video time and the seekTo are in the last 250ms of the video.
                // Ignore this seek call, otherwise if a video ends with multiple closely timed segments
                // then seeking here can create an infinite loop of skip attempts.
                LogHelper.printDebug(() -> "Ignoring seekTo call as video playback is almost finished. "
                        + " videoTime: " + videoTime + " videoLength: " + videoLength + " seekTo: " + seekTime);
                return false;
            }

            LogHelper.printDebug(() -> "Seeking to " + adjustedSeekTime);
            //noinspection DataFlowIssue
            return (Boolean) seekMethod.invoke(playerControllerRef.get(), adjustedSeekTime);
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to seek", ex);
        }
        return false;
    }

    public static void seekToRelative(long millisecondsRelative) {
        seekTo(videoTime + millisecondsRelative);
    }

    public static void reloadVideo() {
        if (videoLength < 10000 || isLiveStream)
            return;

        final long lastVideoTime = videoTime;
        final float playbackSpeed = VideoHelpers.getCurrentSpeed();
        final long speedAdjustedTimeThreshold = (long) (playbackSpeed * 1000);
        seekTo(10000);
        seekTo(lastVideoTime + speedAdjustedTimeThreshold);

        if (!SettingsEnum.SKIP_PRELOADED_BUFFER_TOAST.getBoolean())
            return;

        ReVancedUtils.showToastShort(str("revanced_skipped_preloaded_buffer"));
    }

    public static boolean videoEnded() {
        if (!SettingsEnum.ALWAYS_REPEAT.getBoolean())
            return false;

        ReVancedUtils.runOnMainThreadDelayed(() -> seekTo(0), 0);

        return true;
    }

    /**
     * Id of the last video opened.  Includes Shorts.
     *
     * @return The id of the video, or an empty string if no videos have been opened yet.
     */
    @NonNull
    public static String getVideoId() {
        return videoId;
    }

    /**
     * Channel name of the current video playing.
     * <b>Currently this does not function for Shorts playback.</b>
     *
     * @return The channel name of the video. Empty string if not set yet.
     */
    @NonNull
    public static String getChannelName() {
        return channelName;
    }

    /**
     * Injection point.
     *
     * @param newlyLoadedVideoId id of the current video
     */
    public static void setVideoId(@NonNull String newlyLoadedVideoId) {
        if (videoId.equals(newlyLoadedVideoId))
            return;

        videoId = newlyLoadedVideoId;
    }

    /**
     * Injection point.
     *
     * @param newlyLoadedChannelName channel name of the current video
     */
    public static void setChannelName(@NonNull String newlyLoadedChannelName) {
        if (channelName.equals(newlyLoadedChannelName))
            return;

        channelName = newlyLoadedChannelName;
        if (Whitelist.isChannelSPEEDWhitelisted())
            overrideSpeed(1.0f);
    }

    /**
     * Differs from {@link #videoId} as this is the video id for the
     * last player response received, which may not be the last video opened.
     * <p>
     * If Shorts are loading the background, this commonly will be
     * different from the Short that is currently on screen.
     * <p>
     * For most use cases, you should instead use {@link #getVideoId()}.
     *
     * @return The id of the last video loaded, or an empty string if no videos have been loaded yet.
     */
    @NonNull
    public static String getPlayerResponseVideoId() {
        return playerResponseVideoId;
    }


    /**
     * @return If the last player response video id was a Short.
     * Includes Shorts shelf items appearing in the feed that are not opened.
     * @see #lastVideoIdIsShort()
     */
    public static boolean lastPlayerResponseIsShort() {
        return playerResponseVideoIdIsShort;
    }

    /**
     * @return If the last player response video id _that was opened_ was a Short.
     */
    public static boolean lastVideoIdIsShort() {
        return videoIdIsShort;
    }

    /**
     * @return If the player parameters are for a Short.
     */
    public static boolean playerParametersAreShort(@Nullable String playerParameter) {
        return playerParameter != null && playerParameter.startsWith(SHORTS_PLAYER_PARAMETERS);
    }

    /**
     * Injection point.
     */
    @Nullable
    public static String newPlayerParameter(@NonNull String videoId, @Nullable String playerParameter, boolean isShortAndOpeningOrPlaying) {
        final boolean isShort = playerParametersAreShort(playerParameter);
        playerResponseVideoIdIsShort = isShort;
        if (!isShort || isShortAndOpeningOrPlaying) {
            if (videoIdIsShort != isShort) {
                videoIdIsShort = isShort;
            }
        }
        return playerParameter; // Return the original value since we are observing and not modifying.
    }

    /**
     * Injection point.  Called off the main thread.
     *
     * @param videoId The id of the last video loaded.
     */
    public static void setPlayerResponseVideoId(@NonNull String videoId, boolean isShortAndOpeningOrPlaying) {
        if (!playerResponseVideoId.equals(videoId)) {
            playerResponseVideoId = videoId;
        }
    }

    /**
     * Length of the current video playing.  Includes Shorts.
     *
     * @return The length of the video in milliseconds.
     * If the video is not yet loaded, or if the video is playing in the background with no video visible,
     * then this returns zero.
     */
    public static long getVideoLength() {
        return videoLength;
    }

    /**
     * Injection point.
     *
     * @param length The length of the video in milliseconds.
     */
    public static void setVideoLength(final long length) {
        videoLength = length;
    }

    /**
     * Playback time of the current video playing.  Includes Shorts.
     * <p>
     * Value will lag behind the actual playback time by a variable amount based on the playback speed.
     * <p>
     * If playback speed is 2.0x, this value may be up to 2000ms behind the actual playback time.
     * If playback speed is 1.0x, this value may be up to 1000ms behind the actual playback time.
     * If playback speed is 0.5x, this value may be up to 500ms behind the actual playback time.
     * Etc.
     *
     * @return The time of the video in milliseconds. -1 if not set yet.
     */
    public static long getVideoTime() {
        return videoTime;
    }

    /**
     * Injection point.
     * Called on the main thread every 1000ms.
     *
     * @param time The current playback time of the video in milliseconds.
     */
    public static void setVideoTime(final long time) {
        videoTime = time;
    }

    /**
     * @return If the playback is not at the end of the video.
     * <p>
     * If video is playing in the background with no video visible,
     * this always returns false (even if the video is not actually at the end).
     * <p>
     * This is equivalent to checking for {@link VideoState#ENDED},
     * but can give a more up to date result for code calling from some hooks.
     * @see VideoState
     */
    public static boolean isNotAtEndOfVideo() {
        return videoTime < videoLength && videoLength > 0;
    }
}
