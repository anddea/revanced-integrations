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
    public static void startDownloadActivity(@Nullable String videoId) {
        if (videoId == null || !shouldHookDownloadButton())
            return;
        download(ReVancedUtils.getContext(), videoId);
    }
    
    /**
     * Injection point.
     */
    public static boolean shouldHookDownloadButton() {
        return SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean();
    }
}