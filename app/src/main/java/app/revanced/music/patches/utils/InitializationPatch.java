package app.revanced.music.patches.utils;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.settingsmenu.ReVancedSettingsFragment;
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

        getDialogBuilder(context)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(android.R.string.ok, (dialog, id) ->
                        runOnMainThreadDelayed(() -> ReVancedSettingsFragment.reboot(mActivity), 1000L)
                )
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();

        // set save playback speed default value
        SettingsEnum.ENABLE_SAVE_PLAYBACK_SPEED.saveValue(PatchStatus.RememberPlaybackSpeed());
    }

    public static void setDeviceInformation(@NonNull Context context) {
        ReVancedHelper.setApplicationLabel(context);
        ReVancedHelper.setPackageName(context);
        ReVancedHelper.setVersionName(context);
    }
}