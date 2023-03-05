package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.getNetworkInfo;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class VideoQualityPatch {

    public static int selectedQuality1 = -2;
    private static boolean newVideo = false;
    private static boolean userChangedQuality = false;
    private static int defaultQualityWiFi;
    private static int defaultQualityMobile;

    public static void changeDefaultQuality(int defaultQuality) {
        if (SettingsEnum.ENABLE_SAVE_VIDEO_QUALITY.getBoolean()) {
            if (isConnectedWifi()) {
                try {
                    SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI.saveValue(defaultQuality);
                } catch (Exception ex) {
                    LogHelper.printException(VideoQualityPatch.class, "Failed to change default WI-FI quality" + ex);
                    showToastShort(str("revanced_save_video_quality_wifi_error"));
                }
                showToastShort(str("revanced_save_video_quality_wifi") + "" + defaultQuality + "p");
            } else if (isConnectedMobile()) {
                try {
                    SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE.saveValue(defaultQuality);
                } catch (Exception ex) {
                    showToastShort(str("revanced_save_video_quality_mobile_error"));
                }
                showToastShort(str("revanced_save_video_quality_mobile") + "" + defaultQuality + "p");
            } else {
                showToastShort(str("revanced_save_video_quality_internet_error"));
            }
            refreshQuality();
        }
        userChangedQuality = false;
    }

    public static int setVideoQuality(Object[] qualities, int quality, Object qInterface, String qIndexMethod) {
        int defaultQuality;
        refreshQuality();

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
        if (isConnectedWifi())
            defaultQuality = defaultQualityWiFi;
        else if (isConnectedMobile())
            defaultQuality = defaultQualityMobile;
        else
            return quality;

        if (defaultQuality == 1) return defaultQuality;
        if (defaultQuality == -2) return quality;

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

    private static void setDefaultQuality() {
        defaultQualityWiFi = SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI.getInt();
        defaultQualityMobile = SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE.getInt();
    }

    public static void refreshQuality() {
        setDefaultQuality();
    }

    public static void newVideoStarted(@NonNull String videoId) {
        setDefaultQuality();
        newVideo = true;
    }

    private static boolean isConnectedWifi() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isConnected() && info.getType() == 1;
    }

    private static boolean isConnectedMobile() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isConnected() && info.getType() == 0;
    }

}
