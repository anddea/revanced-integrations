package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;
import static app.revanced.integrations.utils.VideoHelpers.setTitle;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.settings.SettingsEnum;

public class SeekBarPatch {

    public static boolean enableSeekbarTapping() {
        return SettingsEnum.ENABLE_SEEKBAR_TAPPING.getBoolean();
    }

    public static boolean hideTimeStamp() {
        return SettingsEnum.HIDE_TIME_STAMP.getBoolean();
    }

    public static boolean hideSeekbar() {
        return SettingsEnum.HIDE_SEEKBAR.getBoolean();
    }

    public static int enableCustomSeekbarColor(int colorValue) {
        return overrideSeekbarColor(colorValue, false);
    }

    public static int enableCustomSeekbarColorDarkMode(int colorValue) {
        return overrideSeekbarColor(colorValue, true);
    }

    private static int overrideSeekbarColor(int colorValue) {
        try {
            colorValue = Color.parseColor(SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR_VALUE.getString());
        } catch (Exception ignored) {
            showToastShort(str("color_invalid"));
        }
        return colorValue;
    }

    private static int overrideSeekbarColor(int colorValue, boolean isDarkMode) {
        if (SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean() &&
                (isDarkMode || colorValue == -65536))
            return overrideSeekbarColor(colorValue);
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
