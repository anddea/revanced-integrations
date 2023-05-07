package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.VideoHelpers.setTitle;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.settings.SettingsEnum;

public class SeekBarPatch {
    private static final int ORIGINAL_SEEKBAR_CLICKED_COLOR = 0xFFFF0000;

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
        return colorValue == ORIGINAL_SEEKBAR_CLICKED_COLOR
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
     * Points where errors occur when playing videos on the PlayStore (ROOT Build)
     */
    public static int overrideSeekbarColor(final int colorValue) {
        try {
            return SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean()
                    ? Color.parseColor(SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR_VALUE.getString())
                    : colorValue;
        } catch (Exception ignored) {}
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
