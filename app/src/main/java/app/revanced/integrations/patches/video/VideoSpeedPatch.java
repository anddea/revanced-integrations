package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class VideoSpeedPatch {
    private static float selectedSpeed = -1.0f;
    private static String currentContentCpn;

    public static void newVideoStarted(final String contentCpn, final boolean isLive) {
        if (contentCpn.isEmpty() || Objects.equals(currentContentCpn, contentCpn))
            return;

        currentContentCpn = contentCpn;

        if (SettingsEnum.DISABLE_DEFAULT_VIDEO_SPEED_LIVE.getBoolean() && isLive)
            return;

        selectedSpeed = SettingsEnum.DEFAULT_VIDEO_SPEED.getFloat();

        overrideSpeed(selectedSpeed);
    }

    public static void userChangedSpeed(final float speed) {
        selectedSpeed = speed;

        if (SettingsEnum.ENABLE_SAVE_VIDEO_SPEED.getBoolean()) {
            SettingsEnum.DEFAULT_VIDEO_SPEED.saveValue(speed);
            showToastShort(str("revanced_save_video_speed") + speed + "x");
        }
    }

    public static void overrideSpeed(final float speedValue) {
        if (speedValue != selectedSpeed)
            selectedSpeed = speedValue;
    }
}
