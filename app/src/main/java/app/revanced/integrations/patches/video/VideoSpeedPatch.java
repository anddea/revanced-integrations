package app.revanced.integrations.patches.video;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.SharedPrefHelper;
import app.revanced.integrations.whitelist.Whitelist;

public class VideoSpeedPatch {

    private static Boolean newVideo = false;
    private static Boolean userChangedSpeed = false;
    private static Float defaultSpeed;

    public static int getDefaultSpeed(Object[] speeds, int speed, Object qInterface) {
        int speed2;
        refreshSpeed();

        Exception e;
        if (!(newVideo || userChangedSpeed) || qInterface == null) {
            return speed;
        }
        if (Whitelist.isChannelSPEEDWhitelisted()) defaultSpeed = 1.0f;
        newVideo = false;
        if (defaultSpeed == -2.0f) return speed;
        Class<?> floatType = Float.TYPE;
        ArrayList<Float> iStreamSpeeds = new ArrayList<>();
        try {
            for (Object streamSpeed : speeds) {
                Field[] fields = streamSpeed.getClass().getFields();
                for (Field field : fields) {
                    if (field.getType().isAssignableFrom(floatType)) {
                        float value = field.getFloat(streamSpeed);
                        if (field.getName().length() <= 2) {
                            iStreamSpeeds.add(value);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        int speed3 = -1;
        for (float streamSpeed3 : iStreamSpeeds) {
            if (streamSpeed3 <= defaultSpeed) {
                speed3++;
            }
        }
        if (speed3 == -1) speed2 = 3;
        else speed2 = speed3;
        try {
            Method[] declaredMethods = qInterface.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().length() <= 2) {
                    try {
                        try {
                            method.invoke(qInterface, VideoSpeedEntries.videoSpeed[speed2]);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
                        } catch (Exception e6) {
                            e = e6;
                            LogHelper.printException(VideoSpeedPatch.class, e.getMessage());
                            return speed2;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return speed2;
    }

    public static void userChangedSpeed() {
        userChangedSpeed = true;
        newVideo = false;
    }

    private static void setDefaultSpeed() {
        defaultSpeed = SharedPrefHelper.getFloat(ReVancedUtils.getContext(), SharedPrefHelper.SharedPrefNames.REVANCED, "revanced_default_video_speed", -2.0f);
    }

    public static void refreshSpeed() {
        setDefaultSpeed();
    }

    public static void newVideoStarted(String videoId) {
        setDefaultSpeed();
        newVideo = true;
    }

    public static float getSpeedValue(Object[] speeds, int speed) {
        int i = 0;
        refreshSpeed();

        if (!newVideo || userChangedSpeed) {
            userChangedSpeed = false;
            return -1.0f;
        }
        newVideo = false;
        if (Whitelist.isChannelSPEEDWhitelisted()) defaultSpeed = 1.0f;

        if (defaultSpeed == -2.0f) return -1.0f;
        else if (!isCustomVideoSpeedEnabled() && defaultSpeed >= 2.0f) defaultSpeed = 2.0f;

        Class<?> floatType = Float.TYPE;
        ArrayList<Float> iStreamSpeeds = new ArrayList<>();
        try {
            int length = speeds.length;
            int i2 = 0;
            while (i2 < length) {
                Object streamSpeed = speeds[i2];
                Field[] fields = streamSpeed.getClass().getFields();
                int length2 = fields.length;
                while (i < length2) {
                    Field field = fields[i];
                    if (field.getType().isAssignableFrom(floatType)) {
                        float value = field.getFloat(streamSpeed);
                        if (field.getName().length() <= 2) {
                            iStreamSpeeds.add(value);
                        }
                    }
                    i++;
                }
                i2++;
                i = 0;
            }
        } catch (Exception ignored) {
        }
        return defaultSpeed;
    }

    public static boolean isCustomVideoSpeedEnabled() {
        return SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
    }

}
