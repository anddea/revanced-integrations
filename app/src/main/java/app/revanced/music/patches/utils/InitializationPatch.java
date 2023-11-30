package app.revanced.music.patches.utils;

import static app.revanced.music.settings.SettingsUtils.showRestartDialog;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.ReVancedHelper;

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

        showRestartDialog(mActivity, "revanced_reboot_first_run", 1000);

        // set save playback speed default value
        SettingsEnum.ENABLE_SAVE_PLAYBACK_SPEED.saveValue(PatchStatus.RememberPlaybackSpeed());
    }

    public static void setDeviceInformation(@NonNull Context context) {
        ReVancedHelper.setApplicationLabel(context);
        ReVancedHelper.setPackageName(context);
        ReVancedHelper.setVersionName(context);
    }
}