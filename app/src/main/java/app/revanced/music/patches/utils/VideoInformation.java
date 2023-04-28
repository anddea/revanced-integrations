package app.revanced.music.patches.utils;

import androidx.annotation.NonNull;

import app.revanced.music.utils.LogHelper;

/**
 * Hooking class for the current playing video.
 */
public final class VideoInformation {
    @NonNull
    private static String videoId = "";


    /**
     * Injection point.
     *
     * @param newlyLoadedVideoId id of the current video
     */
    public static void setVideoId(@NonNull String newlyLoadedVideoId) {
        if (!videoId.equals(newlyLoadedVideoId)) {
            LogHelper.printDebug(VideoInformation.class, "New video id: " + newlyLoadedVideoId);
            videoId = newlyLoadedVideoId;
        }
    }

    /**
     * Id of the current video playing.  Includes Shorts and YouTube Stories.
     *
     * @return The id of the video. Empty string if not set yet.
     */
    @NonNull
    public static String getVideoId() {
        return videoId;
    }

}
