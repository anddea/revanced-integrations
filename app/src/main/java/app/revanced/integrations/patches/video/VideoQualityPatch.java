package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.StringRef.str;
import static app.revanced.integrations.utils.VideoHelpers.getCurrentQuality;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class VideoQualityPatch {
    private static final SettingsEnum mobileQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;
    private static final SettingsEnum wifiQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI;

    /**
     * The available qualities of the current video in human readable form: [1080, 720, 480]
     */
    @Nullable
    private static List<Integer> videoQualities;

    private static void changeDefaultQuality(int defaultQuality) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean())
            return;

        ReVancedUtils.NetworkType networkType = ReVancedUtils.getNetworkType();

        switch (networkType) {
            case NONE -> {
                ReVancedUtils.showToastShort(str("revanced_save_video_quality_none"));
                return;
            }
            case MOBILE -> mobileQualitySetting.saveValue(defaultQuality);
            default -> wifiQualitySetting.saveValue(defaultQuality);
        }

        ReVancedUtils.showToastShort(str("revanced_save_video_quality_" + networkType.getName())
                + "\u2009" + defaultQuality + "p");
    }

    /**
     * There is no need to check the array of available qualities
     * The target method finds available quality and applies it.
     *
     * @param qualityValue preferred quality value
     */
    public static void overrideQuality(final int qualityValue) {
        LogHelper.printDebug(VideoQualityPatch.class, "Quality changed to: " + qualityValue);
        // Rest of the implementation added by patch.
    }

    /**
     * Injection point.
     *
     * @param qualities Video qualities available, ordered from largest to smallest, with index 0 being the 'automatic' value of -2
     */
    public static void setVideoQualityList(Object[] qualities) {
        try {
            if (videoQualities == null || videoQualities.size() != qualities.length) {
                videoQualities = new ArrayList<>(qualities.length);
                for (Object streamQuality : qualities) {
                    for (Field field : streamQuality.getClass().getFields()) {
                        if (field.getType().isAssignableFrom(Integer.TYPE)
                                && field.getName().length() <= 2) {
                            videoQualities.add(field.getInt(streamQuality));
                        }
                    }
                }
                LogHelper.printDebug(VideoQualityPatch.class, "videoQualities: " + videoQualities);
            }
        } catch (Exception ex) {
            LogHelper.printException(VideoQualityPatch.class, "Failed to set quality list", ex);
        }
    }

    /**
     * Injection point.
     */
    public static void newVideoStarted(final String ignoredVideoId) {
        final int preferredQuality =
                ReVancedUtils.getNetworkType() == ReVancedUtils.NetworkType.MOBILE
                        ? mobileQualitySetting.getInt()
                        : wifiQualitySetting.getInt();

        if (preferredQuality == -2)
            return;

        overrideQuality(preferredQuality);
    }

    /**
     * Injection point. New quality menu.
     *
     * @param selectedQuality user selected quality
     */
    public static void userChangedQuality(final int selectedQuality) {
        ReVancedUtils.runOnMainThreadDelayed(() ->
                        changeDefaultQuality(getCurrentQuality(selectedQuality)),
                300
        );
    }

    /**
     * Injection point. Old quality menu.
     *
     * @param selectedQualityIndex user selected quality index
     */
    public static void userChangedQualityIndex(final int selectedQualityIndex) {
        if (videoQualities == null)
            return;

        changeDefaultQuality(videoQualities.get(selectedQualityIndex));
    }
}