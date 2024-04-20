package app.revanced.integrations.youtube.patches.video;

import androidx.annotation.Nullable;
import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedUtils;
import app.revanced.integrations.youtube.utils.ReVancedUtils.NetworkType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static app.revanced.integrations.youtube.utils.StringRef.str;

@SuppressWarnings("unused")
public class RememberVideoQualityPatch {
    private static final int AUTOMATIC_VIDEO_QUALITY_VALUE = -2;
    private static final SettingsEnum mobileQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;
    private static final SettingsEnum wifiQualitySetting = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI;

    private static boolean qualityNeedsUpdating;

    /**
     * If the user selected a new quality from the flyout menu,
     * and {@link SettingsEnum#ENABLE_SAVE_VIDEO_QUALITY} is enabled.
     */
    private static boolean userChangedDefaultQuality;

    /**
     * Index of the video quality chosen by the user from the flyout menu.
     */
    private static int userSelectedQualityIndex;

    /**
     * The available qualities of the current video in human-readable form: [1080, 720, 480]
     */
    @Nullable
    private static List<Integer> videoQualities;

    private static void changeDefaultQuality(int defaultQuality) {
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

    /**
     * Injection point.
     *
     * @param qualities            Video qualities available, ordered from largest to smallest, with index 0 being the 'automatic' value of -2
     * @param originalQualityIndex quality index to use, as chosen by YouTube
     */
    public static int setVideoQuality(Object[] qualities, final int originalQualityIndex, Object qInterface, String qIndexMethod) {
        try {
            if (!(qualityNeedsUpdating || userChangedDefaultQuality) || qInterface == null) {
                return originalQualityIndex;
            }
            qualityNeedsUpdating = false;

            final int preferredQuality;
            if (ReVancedUtils.getNetworkType() == NetworkType.MOBILE) {
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
                        if (field.getType().isAssignableFrom(Integer.TYPE) && field.getName().length() <= 2) {
                            videoQualities.add(field.getInt(streamQuality));
                        }
                    }
                }
                LogHelper.printDebug(() -> "videoQualities: " + videoQualities);
            }

            if (userChangedDefaultQuality) {
                userChangedDefaultQuality = false;
                final int quality = videoQualities.get(userSelectedQualityIndex);
                LogHelper.printDebug(() -> "User changed default quality to: " + quality);
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

            // If the desired quality index is equal to the original index,
            // then the video is already set to the desired default quality.
            //
            // The method could return here, but the UI video quality flyout will still
            // show 'Auto' (ie: Auto (480p))
            // It appears that "Auto" picks the resolution on video load,
            // and it does not appear to change the resolution during playback.
            //
            // To prevent confusion, set the video index anyway (even if it matches the existing index)
            // As that will force the UI picker to not display "Auto" which may confuse the user.
            if (qualityIndexToUse == originalQualityIndex) {
                LogHelper.printDebug(() -> "Video is already preferred quality: " + preferredQuality);
            } else {
                final int qualityToUseLog = qualityToUse;
                LogHelper.printDebug(() -> "Quality changed from: " + videoQualities.get(originalQualityIndex) + " to: " + qualityToUseLog);
            }

            Method m = qInterface.getClass().getMethod(qIndexMethod, Integer.TYPE);
            m.invoke(qInterface, qualityToUse);
            return qualityIndexToUse;
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to set quality", ex);
            return originalQualityIndex;
        }
    }

    /**
     * Injection point.
     * <p>
     * The remaining code will be implemented by patch.
     */
    public static void overrideQuality(final int qualityValue) {
        LogHelper.printDebug(() -> "Quality changed to: " + qualityValue);
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
                        if (field.getType().isAssignableFrom(Integer.TYPE) && field.getName().length() <= 2) {
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

    private static final Boolean useCustomQuality = false;
    private static int customQuality;

    private static void setQuality(int preferredQuality) {
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

    public static void overideDefaultVideoQuality() {
        final int preferredQuality = ReVancedUtils.getNetworkType() == ReVancedUtils.NetworkType.MOBILE ? mobileQualitySetting.getInt() : wifiQualitySetting.getInt();

        if (preferredQuality == -2) return;

        ReVancedUtils.runOnMainThreadDelayed(() -> setQuality((useCustomQuality) ? customQuality : preferredQuality), 300);
    }

    /**
     * Injection point.  Old quality menu.
     */
    public static void userChangedQuality(int selectedQualityIndex) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) return;

        userSelectedQualityIndex = selectedQualityIndex;
        userChangedDefaultQuality = true;
    }

    /**
     * Injection point.  New quality menu.
     */
    public static void userChangedQualityInNewFlyout(int selectedQuality) {
        if (!SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) return;

        changeDefaultQuality(selectedQuality); // Quality is human-readable resolution (ie: 1080).
    }

    /**
     * Injection point.
     */
    public static void newVideoStarted(Object ignoredPlayerController) {
        LogHelper.printDebug(() -> "newVideoStarted");
        qualityNeedsUpdating = true;
        videoQualities = null;
    }
}
