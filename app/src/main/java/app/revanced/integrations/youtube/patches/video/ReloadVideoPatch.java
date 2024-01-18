package app.revanced.integrations.youtube.patches.video;

import androidx.annotation.NonNull;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.PlayerType;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class ReloadVideoPatch {
    @NonNull
    public static String videoId = "";

    /**
     * Injection point.
     *
     * @param newlyLoadedVideoId id of the current video
     */
    public static void setVideoId(@NonNull String newlyLoadedVideoId) {
        if (!SettingsEnum.SKIP_PRELOADED_BUFFER.getBoolean()) {
            return;
        }

        if (PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL) {
            return;
        }

        if (videoId.equals(newlyLoadedVideoId))
            return;

        videoId = newlyLoadedVideoId;

        ReVancedUtils.runOnMainThreadDelayed(VideoInformation::reloadVideo, 700);
    }
}
