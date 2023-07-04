package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.StringRef.str;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class CustomVideoSpeedPatch {
    /**
     * Maximum playback speed, exclusive value.  Custom speeds must be less than this value.
     */
    public static final float MAXIMUM_PLAYBACK_SPEED = 10;

    /**
     * Custom playback speeds.
     */
    public static float[] videoSpeeds;

    public static String[] customSpeedEntries;
    public static String[] customSpeedEntryValues;
    public static String[] defaultSpeedEntries = {str("quality_auto"), "0.25x", "0.5x", "0.75x", str("shorts_speed_control_normal_label"), "1.25x", "1.5x", "1.75x", "2.0x"};
    public static String[] defaultSpeedEntryValues = {"-2.0", "0.25", "0.5", "0.75", "1.0", "1.25", "1.50", "1.75", "2.0"};

    static {
        loadSpeeds();
    }

    public static float[] getArray(float[] original) {
        return isCustomVideoSpeedEnabled() ? videoSpeeds : original;
    }

    public static int getLength(int original) {
        return isCustomVideoSpeedEnabled() ? videoSpeeds.length : original;
    }

    public static int getSize(int original) {
        return isCustomVideoSpeedEnabled() ? 0 : original;
    }

    private static void resetCustomSpeeds(@NonNull String toastMessage) {
        ReVancedUtils.showToastLong(toastMessage);
        SettingsEnum.CUSTOM_VIDEO_SPEEDS.saveValue(SettingsEnum.CUSTOM_VIDEO_SPEEDS.defaultValue);
    }

    private static void loadSpeeds() {
        try {
            if (!isCustomVideoSpeedEnabled()) return;

            String[] speedStrings = SettingsEnum.CUSTOM_VIDEO_SPEEDS.getString().split("\\s+");
            Arrays.sort(speedStrings);
            if (speedStrings.length == 0) {
                throw new IllegalArgumentException();
            }
            videoSpeeds = new float[speedStrings.length];
            for (int i = 0, length = speedStrings.length; i < length; i++) {
                final float speed = Float.parseFloat(speedStrings[i]);
                if (speed <= 0 || arrayContains(videoSpeeds, speed)) {
                    throw new IllegalArgumentException();
                }
                if (speed >= MAXIMUM_PLAYBACK_SPEED) {
                    resetCustomSpeeds(str("revanced_custom_video_speeds_warning", MAXIMUM_PLAYBACK_SPEED + ""));
                    loadSpeeds();
                    return;
                }
                videoSpeeds[i] = speed;
            }

            if (customSpeedEntries != null) return;

            customSpeedEntries = new String[videoSpeeds.length + 1];
            customSpeedEntryValues = new String[videoSpeeds.length + 1];
            customSpeedEntries[0] = str("quality_auto");
            customSpeedEntryValues[0] = "-2.0";

            int i = 1;
            for (float speed : videoSpeeds) {
                String speedString = String.valueOf(speed);
                customSpeedEntries[i] = speed != 1.0f
                        ? speedString + "x"
                        : str("shorts_speed_control_normal_label");
                customSpeedEntryValues[i] = speedString;
                i++;
            }
        } catch (Exception ex) {
            LogHelper.printException(CustomVideoSpeedPatch.class, "parse error", ex);
            resetCustomSpeeds(str("revanced_custom_video_speeds_error"));
            loadSpeeds();
        }
    }

    private static boolean arrayContains(float[] array, float value) {
        for (float arrayValue : array) {
            if (arrayValue == value) return true;
        }
        return false;
    }

    public static String[] getListEntries() {
        return isCustomVideoSpeedEnabled()
                ? customSpeedEntries
                : defaultSpeedEntries;
    }

    public static String[] getListEntryValues() {
        return isCustomVideoSpeedEnabled()
                ? customSpeedEntryValues
                : defaultSpeedEntryValues;
    }

    private static boolean isCustomVideoSpeedEnabled() {
        return SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
    }

}
