package app.revanced.music.patches.video;

import static app.revanced.music.utils.StringRef.str;

import java.util.Arrays;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class CustomPlaybackSpeedPatch {
    /**
     * Maximum playback speed, exclusive value.  Custom speeds must be less than this value.
     */
    private static final float MAXIMUM_PLAYBACK_SPEED = 3;

    /**
     * Custom playback speeds.
     */
    private static float[] customPlaybackSpeeds;

    private static String[] customSpeedEntries;
    private static String[] customSpeedEntryValues;

    static {
        loadCustomSpeeds();
    }

    public static float[] getArray(float[] original) {
        return customPlaybackSpeeds;
    }

    public static int getLength(int original) {
        return customPlaybackSpeeds.length;
    }

    public static int getSize(int original) {
        return 0;
    }

    private static void resetCustomSpeeds(boolean shouldWarning) {
        if (shouldWarning) {
            ReVancedUtils.showToastShort(getWarningMessage());
        }

        ReVancedUtils.showToastShort(str("revanced_custom_playback_speeds_invalid"));
        SettingsEnum.CUSTOM_PLAYBACK_SPEEDS.saveValue(SettingsEnum.CUSTOM_PLAYBACK_SPEEDS.defaultValue);
    }

    public static void loadCustomSpeeds() {
        try {
            String[] speedStrings = SettingsEnum.CUSTOM_PLAYBACK_SPEEDS.getString().split("\\s+");
            Arrays.sort(speedStrings);
            if (speedStrings.length == 0) {
                throw new IllegalArgumentException();
            }
            customPlaybackSpeeds = new float[speedStrings.length];
            for (int i = 0, length = speedStrings.length; i < length; i++) {
                final float speed = Float.parseFloat(speedStrings[i]);
                if (speed <= 0 || arrayContains(customPlaybackSpeeds, speed)) {
                    throw new IllegalArgumentException();
                }
                if (speed > MAXIMUM_PLAYBACK_SPEED) {
                    resetCustomSpeeds(true);
                    loadCustomSpeeds();
                    return;
                }
                customPlaybackSpeeds[i] = speed;
            }

            if (customSpeedEntries != null) return;

            customSpeedEntries = new String[customPlaybackSpeeds.length];
            customSpeedEntryValues = new String[customPlaybackSpeeds.length];

            int i = 0;
            for (float speed : customPlaybackSpeeds) {
                String speedString = String.valueOf(speed);
                customSpeedEntries[i] = speed != 1.0f
                        ? speedString + "x"
                        : str("revanced_playback_speed_normal");
                customSpeedEntryValues[i] = speedString;
                i++;
            }
        } catch (Exception ex) {
            LogHelper.printInfo(() -> "parse error", ex);
            resetCustomSpeeds(false);
            loadCustomSpeeds();
        }
    }

    private static boolean arrayContains(float[] array, float value) {
        for (float arrayValue : array) {
            if (arrayValue == value) return true;
        }
        return false;
    }

    public static String[] getListEntries() {
        return customSpeedEntries;
    }

    public static String[] getListEntryValues() {
        return customSpeedEntryValues;
    }

    public static String getWarningMessage() {
        return str("revanced_custom_playback_speeds_warning", MAXIMUM_PLAYBACK_SPEED + "");
    }

}
