package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.NetworkType;
import static app.revanced.integrations.utils.ReVancedUtils.getNetworkType;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class VideoQualityPatch {
    private static final int AUTOMATIC_VIDEO_QUALITY_VALUE = -2;
    private static final SettingsEnum wifiQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI;
    private static final SettingsEnum mobileQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;

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

    public static int setVideoQuality(Object[] qualities, final int originalQualityIndex, Object qInterface, String qIndexMethod) {
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
            }

            if (userChangedDefaultQuality) {
                userChangedDefaultQuality = false;
                final int quality = videoQualities.get(userSelectedQualityIndex);
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
            if (qualityIndexToUse == originalQualityIndex)
                return originalQualityIndex;

            Method m = qInterface.getClass().getMethod(qIndexMethod, Integer.TYPE);
            m.invoke(qInterface, qualityToUse);
            return qualityIndexToUse;
        } catch (Exception ex) {
            LogHelper.printException(VideoQualityPatch.class, str("revanced_save_video_quality_none"), ex);
            return originalQualityIndex;
        }
    }

    public static void userChangedQuality(int selectedQuality) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) return;

        userSelectedQualityIndex = selectedQuality;
        userChangedDefaultQuality = true;
    }

    public static void userChangedQualityInNewFlyoutPanels(int selectedQuality) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) return;

        userSelectedQualityIndex = selectedQuality;
        userChangedDefaultQuality = true;

        changeDefaultQuality(selectedQuality);
    }

    public static void newVideoStarted(@NonNull String videoId) {
        if (!videoId.equals(currentVideoId)) {
            currentVideoId = videoId;
            qualityNeedsUpdating = true;
            videoQualities = null;
        }
    }
}