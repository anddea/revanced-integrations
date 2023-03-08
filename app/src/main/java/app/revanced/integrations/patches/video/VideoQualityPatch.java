package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.getNetworkType;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getInt;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class VideoQualityPatch {

    public static int selectedQuality1 = -2;
    private static boolean newVideo = false;
    private static boolean userChangedQuality = false;

    public static void changeDefaultQuality(int defaultQuality) {
        if (SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) {
            var networkType = getNetworkType();
            var preferenceKey = "revanced_default_video_quality_" + networkType.getName();
            var toastMessage = str("revanced_save_video_quality_" + networkType.getName());
            var changedMessage = toastMessage + "\u2009" + defaultQuality + "p";

            if (networkType == ReVancedUtils.NetworkType.NONE) {
                showToastShort(toastMessage);
            } else {
                saveString(REVANCED, preferenceKey, defaultQuality + "");
                showToastShort(changedMessage);
            }
        }
        userChangedQuality = false;
    }

    public static int setVideoQuality(Object[] qualities, int quality, Object qInterface, String qIndexMethod) {
        int defaultQuality;

        if (!(newVideo || userChangedQuality) || qInterface == null) return quality;

        Class<?> intType = Integer.TYPE;
        ArrayList<Integer> iStreamQualities = new ArrayList<>();
        try {
            for (Object streamQuality : qualities) {
                for (Field field : streamQuality.getClass().getFields()) {
                    if (field.getType().isAssignableFrom(intType)) {  // converts quality index to actual readable resolution
                        int value = field.getInt(streamQuality);
                        if (field.getName().length() <= 2) {
                            iStreamQualities.add(value);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        Collections.sort(iStreamQualities);
        int index = 0;
        if (userChangedQuality) {
            for (int convertedQuality : iStreamQualities) {
                int selectedQuality2 = qualities.length - selectedQuality1 + 1;
                index++;
                if (selectedQuality2 == index) {
                    changeDefaultQuality(convertedQuality);
                    return selectedQuality2;
                }
            }
        }

        newVideo = false;
        var networkType = getNetworkType();
        var preferenceKey = "revanced_default_video_quality_" + networkType.getName();

        if (networkType == ReVancedUtils.NetworkType.NONE) return quality;
        else defaultQuality = getInt(REVANCED, preferenceKey, -2);

        if (defaultQuality == -2) return quality;

        for (int ignored : iStreamQualities) {
            index++;
        }
        for (Integer iStreamQuality : iStreamQualities) {
            int streamQuality3 = iStreamQuality;
            if (streamQuality3 <= defaultQuality) {
                quality = streamQuality3;
            }
        }
        if (quality == -2) return quality;

        int qualityIndex = iStreamQualities.indexOf(quality);
        try {
            Class<?> cl = qInterface.getClass();
            Method m = cl.getMethod(qIndexMethod, Integer.TYPE);
            m.invoke(qInterface, iStreamQualities.get(qualityIndex));
            return qualityIndex;
        } catch (Exception ex) {
            LogHelper.printException(VideoQualityPatch.class, "Failed to set quality", ex);
            showToastShort(str("revanced_save_video_quality_common_error"));
            return qualityIndex;
        }
    }

    public static void userChangedQuality(int selectedQuality) {
        selectedQuality1 = selectedQuality;
        userChangedQuality = true;
    }

    public static void newVideoStarted(@NonNull String videoId) {
        newVideo = true;
    }
}
