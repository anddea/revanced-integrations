package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;
import static app.revanced.integrations.utils.VideoHelpers.setTitle;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.settings.SettingsEnum;

public class SeekbarLayoutPatch {

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
        if (SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean()) {
            try {
                colorValue = Color.parseColor(SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR_VALUE.getString());
            } catch (Exception ignored) {
                showToastShort(str("color_invalid"));
            }
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
