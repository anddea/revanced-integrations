package app.revanced.integrations.patches.video;

import androidx.annotation.NonNull;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;

public class ReloadVideoPatch {
    @NonNull
    public static String videoId = "";

    /**
     * Injection point.
     *
     * @param newlyLoadedVideoId id of the current video
     */
    public static void setVideoId(@NonNull String newlyLoadedVideoId) {
        if (!SettingsEnum.SKIP_PRELOADED_BUFFER.getBoolean() || PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL) {
            return;
        }

        if (videoId.equals(newlyLoadedVideoId))
            return;

        videoId = newlyLoadedVideoId;
        VideoInformation.reloadVideo();
    }
}
