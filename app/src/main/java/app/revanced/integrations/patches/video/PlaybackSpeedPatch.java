package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class PlaybackSpeedPatch {
    private static String currentContentCpn;
    private static float currentPlaybackSpeed = 1.0f;

    public static void newVideoStarted(final String contentCpn, final boolean isLiveStream) {
        try {
            if (contentCpn.isEmpty() || Objects.equals(currentContentCpn, contentCpn))
                return;

            currentContentCpn = contentCpn;

            if (SettingsEnum.DISABLE_DEFAULT_PLAYBACK_SPEED_LIVE.getBoolean() && isLiveStream)
                return;

            currentPlaybackSpeed = SettingsEnum.DEFAULT_PLAYBACK_SPEED.getFloat();

            overrideSpeed(currentPlaybackSpeed);
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to setDefaultPlaybackSpeed", ex);
        }
    }

    public static void userChangedSpeed(final float playbackSpeed) {
        currentPlaybackSpeed = playbackSpeed;

        if (!SettingsEnum.ENABLE_SAVE_PLAYBACK_SPEED.getBoolean())
            return;

        SettingsEnum.DEFAULT_PLAYBACK_SPEED.saveValue(playbackSpeed);
        showToastShort(str("revanced_save_playback_speed", playbackSpeed + "x"));
    }

    public static void overrideSpeed(final float playbackSpeed) {
        if (playbackSpeed != currentPlaybackSpeed)
            currentPlaybackSpeed = playbackSpeed;
    }
}
