package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.VideoHelpers.download;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class HookDownloadButtonPatch {

    public static boolean shouldHookDownloadButton() {
        return SettingsEnum.HOOK_DOWNLOAD_BUTTON.getBoolean();
    }

    public static void startDownloadActivity() {
        download(ReVancedUtils.getContext());
    }
}