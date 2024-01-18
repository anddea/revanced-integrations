package app.revanced.integrations.youtube.patches.utils;

import static app.revanced.integrations.youtube.utils.VideoHelpers.download;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

public class HookDownloadButtonPatch {

    public static boolean shouldHookDownloadButton() {
        return SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean();
    }

    public static void startDownloadActivity() {
        download(ReVancedUtils.getContext());
    }
}