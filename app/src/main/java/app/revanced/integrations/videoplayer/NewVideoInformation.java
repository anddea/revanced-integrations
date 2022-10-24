package app.revanced.integrations.videoplayer;

import app.revanced.integrations.utils.LogHelper;

public class NewVideoInformation {
    public static String currentVideoId;
    public static String channelName;
    public static long lastKnownVideoTime = -1L;

    private static boolean tempInfoSaved = false;
    private static String tempVideoId;

    // Call hook in the YT code when the video changes
    public static void setCurrentVideoId(final String videoId) {
        if (videoId == null) {
            LogHelper.debug(NewVideoInformation.class, "setCurrentVideoId - new id was null - currentVideoId was" + currentVideoId);
            clearInformation(true);
            return;
        }

        // Restore temporary information that was stored from the last watched video
        if (tempInfoSaved) {
            restoreTempInformation();
        }

        if (videoId.equals(currentVideoId)) {
            LogHelper.debug(NewVideoInformation.class, "setCurrentVideoId - new and current video were equal - " + videoId);
            return;
        }

        LogHelper.debug(NewVideoInformation.class, "setCurrentVideoId - video id updated from " + currentVideoId + " to " + videoId);

        currentVideoId = videoId;
    }

    // Call hook in the YT code when the video ends
    public static void videoEnded() {
        saveTempInformation();
        clearInformation(false);
    }

    // Information is cleared once a video ends
    // It's cleared because the setCurrentVideoId isn't called for Shorts
    // so Shorts would otherwise use the information from the last watched video
    private static void clearInformation(boolean full) {
        if (full) {
            currentVideoId = null;
        }
        channelName = null;
    }

    // Temporary information is saved once a video ends
    // so that if the user watches the same video again,
    // the information can be restored without having to fetch again
    private static void saveTempInformation() {
        tempVideoId = currentVideoId;
        tempInfoSaved = true;
    }

    private static void restoreTempInformation() {
        currentVideoId = tempVideoId;
        tempVideoId = null;
        tempInfoSaved = false;
    }
}
