package app.revanced.integrations.music.patches.utils;

import static app.revanced.integrations.music.settings.SettingsUtils.showRestartDialog;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.utils.ReVancedHelper;

@SuppressWarnings("unused")
public class InitializationPatch {

    /**
     * The new layout is not loaded normally when the app is first installed.
     * (Also reproduced on unPatched YouTube Music)
     * <p>
     * Side effects when new layout is not loaded:
     * - Button container's layout is broken
     * <p>
     * To fix this, show the reboot dialog when the app is installed for the first time.
     */
    public static void initializeReVancedSettings(@NonNull Context context) {
        if (SettingsEnum.SETTINGS_INITIALIZED.getBoolean())
            return;

        SettingsEnum.SETTINGS_INITIALIZED.saveValue(true);

        // show dialog
        if (!(context instanceof Activity mActivity))
            return;

        showRestartDialog(mActivity, "revanced_restart_first_run", 1000);

        // set default value
        SettingsEnum.ENABLE_SAVE_PLAYBACK_SPEED.saveValue(PatchStatus.RememberPlaybackSpeed());
    }

    public static void setDeviceInformation(@NonNull Context context) {
        ReVancedHelper.setPackageName(context);
        ReVancedHelper.setApplicationLabel(context);
        ReVancedHelper.setVersionName(context);
    }
}