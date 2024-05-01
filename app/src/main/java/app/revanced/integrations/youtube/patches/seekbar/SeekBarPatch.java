package app.revanced.integrations.youtube.patches.seekbar;

import android.graphics.Color;
import android.view.View;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.VideoHelpers;

@SuppressWarnings("unused")
public class SeekBarPatch {
    /**
     * Default color of seekbar.
     */
    public static final int ORIGINAL_SEEKBAR_COLOR = 0xFFFF0000;

    public static String appendTimeStampInformation(String original) {
        if (!SettingsEnum.APPEND_TIME_STAMP_INFORMATION.getBoolean()) return original;

        String appendString = SettingsEnum.APPEND_TIME_STAMP_INFORMATION_TYPE.getBoolean()
                ? VideoHelpers.getFormattedQualityString(null)
                : VideoHelpers.getFormattedSpeedString(null);

        // Encapsulate the entire appendString with bidi control characters
        appendString = "\u2066" + appendString + "\u2069";

        // Format the original string with the appended time stamp information
        return String.format(
                "%s\u2009•\u2009%s", // Add the separator and the appended information
                original, appendString
        );
    }

    public static boolean enableNewThumbnailPreview() {
        return SettingsEnum.ENABLE_NEW_THUMBNAIL_PREVIEW.getBoolean();
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

    public static void setContainerClickListener(View view) {
        if (!SettingsEnum.APPEND_TIME_STAMP_INFORMATION.getBoolean()) return;

        if (!(view.getParent() instanceof View containerView)) return;

        final SettingsEnum appendTypeSetting = SettingsEnum.APPEND_TIME_STAMP_INFORMATION_TYPE;
        final boolean previousBoolean = appendTypeSetting.getBoolean();

        containerView.setOnLongClickListener(timeStampContainerView -> {
            appendTypeSetting.saveValue(!previousBoolean);
            return true;
        });
    }

    /**
     * Injection point.
     */
    public static int getSeekbarClickedColorValue(final int colorValue) {
        return colorValue == ORIGINAL_SEEKBAR_COLOR ? overrideSeekbarColor(colorValue) : colorValue;
    }

    /**
     * Injection point.
     */
    public static int resumedProgressBarColor(final int colorValue) {
        return SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean() ? getSeekbarClickedColorValue(colorValue) : colorValue;
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
            return SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR.getBoolean() ? Color.parseColor(SettingsEnum.ENABLE_CUSTOM_SEEKBAR_COLOR_VALUE.getString()) : colorValue;
        } catch (Exception ignored) {
        }
        return colorValue;
    }

}
