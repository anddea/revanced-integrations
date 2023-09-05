package app.revanced.music.patches.utils;

import static app.revanced.music.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.settingsmenu.SharedPreferenceChangeListener;

public class FirstRun {

    /**
     * The new layout is not loaded normally when the app is first installed.
     * (Also reproduced on unPatched YouTube Music)
     * <p>
     * Side effects when new layout is not loaded:
     * - Button container's layout is broken
     * <p>
     * To fix this, show the reboot dialog when the app is installed for the first time.
     */
    public static void initializationRVX(@NonNull Context context) {
        if (SettingsEnum.FIRST_RUN.getBoolean())
            return;

        SettingsEnum.FIRST_RUN.saveValue(true);

        // show dialog
        Activity activity = (Activity) context;

        new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(android.R.string.ok, (dialog, id) ->
                        runOnMainThreadDelayed(() -> SharedPreferenceChangeListener.reboot(activity), 1000L)
                )
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();
    }
}