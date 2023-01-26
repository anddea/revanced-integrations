package app.revanced.integrations.patches.video;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import android.widget.Toast;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.utils.ReVancedUtils;

public class VideoInformation {
    public static String currentVideoId;
    public static String channelName;
    public static long lastKnownVideoTime = -1L;
    public static long lastKnownVideoLength = 1L;

    private static WeakReference<Object> Controller;
    private static Method seekMethod;

    private static final String SEEK_METHOD_NAME = "seekTo";

    // Call hook in the YT code when the video changes
    public static void setCurrentVideoId(final String videoId) {
        if (videoId == null) {
            channelName = null;
            return;
        }

        if (videoId.equals(currentVideoId)) return;
        else
            currentVideoId = videoId;
    }


    public static void setCurrentVideoTime(final long time) {
        lastKnownVideoTime = time;
        PlayerController.setCurrentVideoTime(time);
    }

    public static void setCurrentVideoTimeHighPrecision(final long time) {
        PlayerController.setCurrentVideoTimeHighPrecision(time);
    }

    public static void setCurrentVideoLength(final long length) {
        lastKnownVideoLength = length;
        PlayerController.lastKnownVideoLength = lastKnownVideoLength;
    }

    public static void setChannelName(String name) {
        channelName = name;
    }

    public static void onCreate(final Object object) {
        Controller = new WeakReference<>(object);
        lastKnownVideoTime = -1L;
        lastKnownVideoLength = 1L;
        PlayerController.initialize();

        try {
            seekMethod = object.getClass().getMethod(SEEK_METHOD_NAME, Long.TYPE);
            seekMethod.setAccessible(true);
        } catch (NoSuchMethodException ignored) {}
    }

    // Call hook in the YT code when the video ends
    public static boolean videoEnded() {
        if (SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean()) {
            seekTo(0);
            return true;
        }
        return false;
    }

    public static void seekTo(final long millisecond) {
        if (seekMethod == null) return;
        ReVancedUtils.runOnMainThread(() -> {
            try {
                seekMethod.invoke(Controller.get(), millisecond);
            } catch (Exception ignored) {}
        });
    }

    public static long getCurrentVideoTime() {
        return lastKnownVideoTime;
    }

    public static long getCurrentVideoLength() {
        return lastKnownVideoLength;
    }

    public static String getCurrentVideoId() {
        return currentVideoId;
    }

    public static String getChannelName() {
        return channelName;
    }
}
