package app.revanced.integrations.youtube.patches.misc;

import android.content.Intent;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class UpdateScreenPatch {

    @Nullable
    public static Intent disableUpdateScreen(final Intent intent) {
        return SettingsEnum.DISABLE_UPDATE_SCREEN.getBoolean() ? null : intent;
    }
}
