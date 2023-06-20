package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.VideoHelpers.setTitle;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.settings.SettingsEnum;

public class SeekBarPatch {
    /**
     * Default color of seekbar.
     */
    public static final int ORIGINAL_SEEKBAR_COLOR = 0xFFFF0000;

    public static boolean enableNewThumbnailPreview(boolean original) {
        return SettingsEnum.ENABLE_NEW_THUMBNAIL_PREVIEW.getBoolean() || original;
    }

    public static boolean enableSeekbarTapping() {
        return SettingsEnum.ENABLE_SEEKBAR_TAPPING.getBoolean();
    }

    public static boolean hideTimeStamp() {
        return SettingsEnum.HIDE_TIME_STAMP.getBoolean();
    }

    public static boolean hideSeekbar() {
        return SettingsEnum.HIDE_SEEKBAR.getBoolean();
    }

    /**
     * Injection point.
     */
    public static int getSeekbarClickedColorValue(final int colorValue) {
        return colorValue == ORIGINAL_SEEKBAR_COLOR
                ? overrideSeekbarColor(colorValue)
                : colorValue;
    }

    /**
     * Injection point.
     */
    public static int resumedProgressBarColor(final int colorValue) {
        return SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean()
                ? getSeekbarClickedColorValue(colorValue)
                : colorValue;
    }

    /**
     * Injection point.
     * <p>
     * Overrides all Litho components that use the YouTube seekbar color.
     * Used only for the video thumbnails seekbar.
     * <p>
     * If {@link SettingsEnum#HIDE_SEEKBAR_THUMBNAIL} is enabled, this returns a fully transparent color.
     */
    public static int getLithoColor(int colorValue) {
        if (colorValue == ORIGINAL_SEEKBAR_COLOR) {
            if (SettingsEnum.HIDE_SEEKBAR_THUMBNAIL.getBoolean()) {
                return 0x00000000;
            }
            return overrideSeekbarColor(ORIGINAL_SEEKBAR_COLOR);
        }
        return colorValue;
    }

    /**
     * Points where errors occur when playing videos on the PlayStore (ROOT Build)
     */
    public static int overrideSeekbarColor(final int colorValue) {
        try {
            return SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean()
                    ? Color.parseColor(SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR_VALUE.getString())
                    : colorValue;
        } catch (Exception ignored) {
        }
        return colorValue;
    }

    public static String enableTimeStampSpeed(String totalTime) {
        if (SettingsEnum.ENABLE_TIME_STAMP_SPEED.getBoolean()) {
            var regex = "\\((.*?)\\)";
            Matcher matcher = Pattern.compile(regex).matcher(totalTime);
            if (matcher.find())
                totalTime = totalTime.replaceAll(regex, "") + String.format("\u2009(%s)", setTitle(matcher.group(1)));
            else
                totalTime = totalTime + String.format("\u2009(%s)", setTitle(null));
        }
        return totalTime;
    }
}
