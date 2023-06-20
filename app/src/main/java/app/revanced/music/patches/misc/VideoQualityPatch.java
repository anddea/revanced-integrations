package app.revanced.music.patches.misc;

import static app.revanced.music.utils.ReVancedUtils.NetworkType;
import static app.revanced.music.utils.ReVancedUtils.getNetworkType;
import static app.revanced.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.music.utils.StringRef.str;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class VideoQualityPatch {
    private static final int AUTOMATIC_VIDEO_QUALITY_VALUE = -2;
    private static final SettingsEnum wifiQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI;
    private static final SettingsEnum mobileQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;
    public static String qIndexMethod;
    private static boolean qualityNeedsUpdating;
    @Nullable
    private static String currentVideoId;
    private static boolean userChangedDefaultQuality;
    private static int userSelectedQualityIndex;
    @Nullable
    private static List<Integer> videoQualities;

    private static void changeDefaultQuality(int defaultQuality) {
        var networkType = getNetworkType();
        var toastMessage = str("revanced_save_video_quality_" + networkType.getName());
        var changedMessage = toastMessage + "\u2009" + defaultQuality + "p";

        if (networkType == NetworkType.NONE) {
            showToastShort(str("revanced_save_video_quality_none"));
            return;
        }
        if (networkType == NetworkType.MOBILE) {
            mobileQualitySetting.saveValue(defaultQuality);
        } else {
            wifiQualitySetting.saveValue(defaultQuality);
        }
        showToastShort(changedMessage);
    }

    public static int setVideoQuality(Object[] qualities, final int originalQualityIndex, Object qInterface) {
        try {
            if (!(qualityNeedsUpdating || userChangedDefaultQuality) || qInterface == null) {
                return originalQualityIndex;
            }
            qualityNeedsUpdating = false;

            final int preferredQuality;
            if (getNetworkType() == NetworkType.MOBILE) {
                preferredQuality = mobileQualitySetting.getInt();
            } else {
                preferredQuality = wifiQualitySetting.getInt();
            }
            if (!userChangedDefaultQuality && preferredQuality == AUTOMATIC_VIDEO_QUALITY_VALUE) {
                return originalQualityIndex; // nothing to do
            }

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
                LogHelper.printDebug(VideoQualityPatch.class, "VideoId: " + currentVideoId + " videoQualities: " + videoQualities);
            }

            if (userChangedDefaultQuality) {
                userChangedDefaultQuality = false;
                final int quality = videoQualities.get(userSelectedQualityIndex);
                LogHelper.printDebug(VideoQualityPatch.class, "User changed default quality to: " + quality);
                changeDefaultQuality(quality);
                return userSelectedQualityIndex;
            }

            // find the highest quality that is equal to or less than the preferred
            int qualityToUse = videoQualities.get(0); // first element is automatic mode
            int qualityIndexToUse = 0;
            int i = 0;
            for (Integer quality : videoQualities) {
                if (quality <= preferredQuality && qualityToUse < quality) {
                    qualityToUse = quality;
                    qualityIndexToUse = i;
                }
                i++;
            }
            if (qualityIndexToUse == originalQualityIndex) {
                LogHelper.printDebug(VideoQualityPatch.class, "Video is already preferred quality: " + preferredQuality);
                return originalQualityIndex;
            }

            final int qualityToUseLog = qualityToUse;
            LogHelper.printDebug(VideoQualityPatch.class, "Quality changed from: "
                    + videoQualities.get(originalQualityIndex) + " to: " + qualityToUseLog);

            Method m = qInterface.getClass().getMethod(qIndexMethod, Integer.TYPE);
            m.invoke(qInterface, qualityToUse);
            return qualityIndexToUse;
        } catch (Exception ex) {
            LogHelper.printException(VideoQualityPatch.class, "Failed to set quality", ex);
            return originalQualityIndex;
        }
    }

    public static void userChangedQuality(int selectedQuality) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) return;

        userSelectedQualityIndex = selectedQuality;
        userChangedDefaultQuality = true;
    }

    public static void newVideoStarted(@NonNull String videoId) {
        qualityNeedsUpdating = true;

        if (!videoId.equals(currentVideoId)) {
            currentVideoId = videoId;
            videoQualities = null;
        }
    }
}