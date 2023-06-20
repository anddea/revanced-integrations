package app.revanced.music.patches.misc;

import static app.revanced.music.utils.VideoHelpers.downloadMusic;

import android.annotation.SuppressLint;
import android.content.Context;

import app.revanced.music.settings.SettingsEnum;

public class HookShareButtonPatch {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context dismissContext(Context appContext) {
        return SettingsEnum.HOOK_SHARE_BUTTON.getBoolean() ? null : appContext;
    }

    public static boolean overrideSharePanel() {
        final boolean shouldHook = SettingsEnum.HOOK_SHARE_BUTTON.getBoolean();
        if (shouldHook)
            downloadMusic();

        return shouldHook;
    }
}