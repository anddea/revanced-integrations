package app.revanced.integrations.patches.video;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

/**
 * Hooking class for the current playing video.
 */
public final class VideoInformation {
    private static final String SEEK_METHOD_NAME = "seekTo";

    private static WeakReference<Object> playerControllerRef;
    private static Method seekMethod;

    @NonNull
    private static String videoId = "";

    private static long videoLength = 0;
    private static volatile long videoTime = -1; // must be volatile. Value is set off main thread from high precision patch hook

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

            seekMethod = playerController.getClass().getMethod(SEEK_METHOD_NAME, Long.TYPE);
            seekMethod.setAccessible(true);
        } catch (Exception ex) {
            LogHelper.printException(VideoInformation.class, "Failed to initialize", ex);
        }
    }

    /**
     * Seek on the current video.
     * <b>Currently this does not function for Shorts playback.</b>
     * <p>
     * Caution: If called from a videoTimeHook() callback,
     * this will cause a recursive call into the same videoTimeHook() callback.
     *
     * @param millisecond The millisecond to seek the video to.
     * @return if the seek was successful
     */
    public static boolean seekTo(long millisecond) {
        ReVancedUtils.verifyOnMainThread();
        if (seekMethod == null) {
            LogHelper.printException(VideoInformation.class, "seekMethod was null");
            return false;
        }

        try {
            return (Boolean) seekMethod.invoke(playerControllerRef.get(), millisecond);
        } catch (Exception ex) {
            LogHelper.printException(VideoInformation.class, "Failed to seek", ex);
            return false;
        }
    }

    public static void seekToRelative(long millisecondsRelative) {
        seekTo(videoTime + millisecondsRelative);
    }

    public static boolean shouldAlwaysRepeat() {
        if (SettingsEnum.ALWAYS_REPEAT.getBoolean())
            return seekTo(0);
        else
            return false;
    }

    /**
     * Id of the current video playing.
     * <b>Currently this does not function for Shorts playback.</b>
     *
     * @return The id of the video. Empty string if not set yet.
     */
    @NonNull
    public static String getVideoId() {
        return videoId;
    }

    /**
     * Injection point.
     *
     * @param newlyLoadedVideoId id of the current video
     */
    public static void setVideoId(@NonNull String newlyLoadedVideoId) {
        if (!videoId.equals(newlyLoadedVideoId)) {
            videoId = newlyLoadedVideoId;
        }
    }

    /**
     * Length of the current video playing.
     * Includes Shorts and YouTube Stories.
     *
     * @return The length of the video in milliseconds, or zero  if video is not yet loaded.
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
     * Playback time of the current video playing.
     * Value can lag up to approximately 100ms behind the actual current video playback time.
     * <p>
     * Note: Code inside a videoTimeHook patch callback
     * should use the callback video time and avoid using this method
     * (in situations of recursive hook callbacks, the value returned here may be outdated).
     * <p>
     * Includes Shorts playback.
     *
     * @return The time of the video in milliseconds. -1 if not set yet.
     */
    public static long getVideoTime() {
        return videoTime;
    }

    /**
     * Injection point.
     * Called off the main thread approximately every 50ms to 100ms
     *
     * @param time The current playback time of the video in milliseconds.
     */
    public static void setVideoTime(final long time) {
        videoTime = time;
    }

    /**
     * @return If the playback is at the end of the video
     */
    public static boolean isAtEndOfVideo() {
        return videoTime > 0 && videoLength > 0 && videoTime >= videoLength;
    }
}
