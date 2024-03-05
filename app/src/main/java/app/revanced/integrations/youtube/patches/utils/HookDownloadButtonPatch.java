package app.revanced.integrations.youtube.patches.utils;

import static app.revanced.integrations.youtube.utils.VideoHelpers.download;

import androidx.annotation.Nullable;
import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

public class HookDownloadButtonPatch {    
    /**
     * Injection point.
     *
     * @param videoId id of the video that user want to download
     */
    public static void startVideoDownloadActivity(@Nullable String videoId) {
        if (videoId == null || !shouldHookDownloadButton())
            return;
        download(ReVancedUtils.getContext(), videoId, false);
    }

    /**
     * Injection point.
     *
     * @param playlistId id of the playlist that user want to download
     */
    public static void startPlaylistDownloadActivity(@Nullable String playlistId) {
        if (playlistId == null || !shouldHookDownloadButton())
            return;
        download(ReVancedUtils.getContext(), playlistId, true);
    }
    
    /**
     * Injection point.
     */
    public static boolean shouldHookDownloadButton() {
        return SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean();
    }
}