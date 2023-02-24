package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getFloat;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import android.widget.Toast;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.whitelist.Whitelist;

public class VideoSpeedPatch {
    private static final String DEFAULT_VIDEO_SPEED_PREFERENCE_KEY = "revanced_default_video_speed";

    private static float selectedSpeed = -1.0f;

    private static boolean newVideo = false;

    public static void userChangedSpeed(final float speed) {
        selectedSpeed = speed;
        var context = Objects.requireNonNull(ReVancedUtils.getContext());

        if (SettingsEnum.ENABLE_SAVE_VIDEO_SPEED.getBoolean()) {
            SettingsEnum.DEFAULT_VIDEO_SPEED.saveValue(speed);
            saveString(context, REVANCED, DEFAULT_VIDEO_SPEED_PREFERENCE_KEY, speed + "");
            Toast.makeText(context, str("revanced_save_video_speed") + speed + "x", Toast.LENGTH_SHORT).show();
		}
    }

    public static void setDefaultSpeed() {
        float defaultSpeed = selectedSpeed;
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        if (newVideo) {
            defaultSpeed = getFloat(context, REVANCED, DEFAULT_VIDEO_SPEED_PREFERENCE_KEY, -1.0f);
            selectedSpeed = defaultSpeed;

            newVideo = false;

            if (Whitelist.isChannelSPEEDWhitelisted()) defaultSpeed = -1.0f;
            else if (!isCustomVideoSpeedEnabled() && defaultSpeed >= 2.0f) defaultSpeed = 2.0f;
        }
        overrideSpeed(defaultSpeed);
    }

    public static void newVideoStarted(String videoId) {
        newVideo = true;
    }

    public static void overrideSpeed(final float speedValue) {
        if (speedValue != selectedSpeed)
            selectedSpeed = speedValue;
    }

    public static boolean isCustomVideoSpeedEnabled() {
        return SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
    }

}
