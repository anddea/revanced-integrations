package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.patches.layout.PlayerPatch.playPauseButtonView;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class AlwaysRepeatPatch {

    public static boolean enableAlwaysRepeat(boolean original) {
        return !SettingsEnum.ALWAYS_REPEAT.getBoolean() && original;
    }

    public static void shouldRepeatAndPause() {
        View view = playPauseButtonView;
        if (view == null || SettingsEnum.ALWAYS_REPEAT_PAUSE.getBoolean())
            return;

        view.performClick();
    }
}
