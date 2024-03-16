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
    public static boolean startVideoDownloadActivity(@Nullable String videoId) {
        if (videoId == null || !SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean())
            return false;
        download(ReVancedUtils.getContext(), videoId, false);
        return true;
    }

    /**
     * Injection point.
     *
     * @param playlistId id of the playlist that user want to download
     */
    public static boolean startPlaylistDownloadActivity(@Nullable String playlistId) {
        if (playlistId == null || !SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean())
            return false;
        download(ReVancedUtils.getContext(), playlistId, true);
        return true;
    }
}
