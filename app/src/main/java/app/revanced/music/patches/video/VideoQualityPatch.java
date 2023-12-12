package app.revanced.music.patches.video;

import static app.revanced.music.utils.StringRef.str;
import static app.revanced.music.utils.VideoHelpers.getCurrentQuality;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class VideoQualityPatch {
    private static final SettingsEnum mobileQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;
    private static final SettingsEnum wifiQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI;

    /**
     * The available qualities of the current video in human readable form: [1080, 720, 480]
     */
    @Nullable
    private static List<Integer> videoQualities;

    private static void changeDefaultQuality(final int defaultQuality) {
        final ReVancedUtils.NetworkType networkType = ReVancedUtils.getNetworkType();

        switch (networkType) {
            case NONE -> {
                ReVancedUtils.showToastShort(str("revanced_save_video_quality_none"));
                return;
            }
            case MOBILE -> mobileQualitySetting.saveValue(defaultQuality);
            default -> wifiQualitySetting.saveValue(defaultQuality);
        }

        ReVancedUtils.showToastShort(str("revanced_save_video_quality_" + networkType.getName(), defaultQuality + "p"));
    }

    public static void overrideQuality(final int qualityValue) {
        LogHelper.printDebug(() -> "Quality changed to: " + qualityValue);
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
                LogHelper.printDebug(() -> "videoQualities: " + videoQualities);
            }
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to set quality list", ex);
        }
    }

    private static void setVideoQuality(int preferredQuality) {
        if (videoQualities != null) {
            int qualityToUse = videoQualities.get(0); // first element is automatic mode
            for (Integer quality : videoQualities) {
                if (quality <= preferredQuality && qualityToUse < quality) {
                    qualityToUse = quality;
                }
            }
            preferredQuality = qualityToUse;
        }
        overrideQuality(preferredQuality);
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

        ReVancedUtils.runOnMainThreadDelayed(() -> setVideoQuality(preferredQuality), 500);
    }

    /**
     * Injection point.
     *
     * @param selectedQualityIndex user selected quality index
     */
    public static void userChangedQuality(final int selectedQualityIndex) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean() || videoQualities == null)
            return;

        final int selectedQuality = videoQualities.get(selectedQualityIndex);

        ReVancedUtils.runOnMainThreadDelayed(() ->
                        changeDefaultQuality(getCurrentQuality(selectedQuality)),
                300
        );
    }
}