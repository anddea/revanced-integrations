package app.revanced.music.patches.misc;

import static app.revanced.music.utils.VideoHelpers.downloadMusic;

import android.annotation.SuppressLint;
import android.content.Context;

import app.revanced.music.settings.MusicSettingsEnum;

public class HookShareButtonPatch {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static boolean isShareButtonClicked = false;

    public static Context dismissContext(Context appContext) {
        return MusicSettingsEnum.HOOK_SHARE_BUTTON.getBoolean() ? null : appContext;
    }

    public static boolean overrideSharePanel() {
        final boolean shouldHook = MusicSettingsEnum.HOOK_SHARE_BUTTON.getBoolean();
        if (shouldHook) {
            if (MusicSettingsEnum.HOOK_TYPE.getBoolean())
                isShareButtonClicked = true;
            else
                downloadMusic();
        }

        return shouldHook;
    }
}