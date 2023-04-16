package app.revanced.integrations.patches.music;

import static app.revanced.integrations.settings.MusicSettings.hookShareButton;
import static app.revanced.integrations.utils.VideoHelpers.downloadMusic;

import android.annotation.SuppressLint;
import android.content.Context;

public class HookShareButtonPatch {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context dismissContext(Context appContext) {
        return hookShareButton() ? null : appContext;
    }

    public static boolean overrideSharePanel() {
        return onClick(hookShareButton());
    }

    private static boolean onClick(boolean isDownloadEnabled) {
        if (isDownloadEnabled)
            downloadMusic(context);

        return isDownloadEnabled;
    }
}