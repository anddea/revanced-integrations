package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.SegmentPlaybackController;
import app.revanced.integrations.utils.ReVancedHelper;

public class InitializationPatch {
    private static final String PREFERENCE_KEY = "integrations";

    private static void buildDialog(@NonNull Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) ->
                        runOnMainThreadDelayed(() -> {
                                    activity.finishAffinity();
                                    activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
                                    System.exit(0);
                                }, 1500
                        )
                )
                .setNegativeButton(str("sign_in_cancel"), null)
                .setCancelable(false)
                .show();
    }

    /**
     * The new layout is not loaded when the app is first installed.
     * (Also reproduced on unPatched YouTube)
     * <p>
     * Side effects when new layout is not loaded:
     * - Button container's layout is broken
     * - Zoom to fill screen not working
     * - 8X zoom not working in fullscreen
     * <p>
     * To fix this, show the reboot dialog when the app is installed for the first time.
     * <p>
     * The version of the current integrations is saved to YouTube's SharedPreferences to identify if the app was first installed.
     */
    public static void initializeReVancedSettings(@NonNull Context context) {
        final String integrationVersion = getString(YOUTUBE, PREFERENCE_KEY, "");

        if (!integrationVersion.equals(BuildConfig.VERSION_NAME))
            saveString(YOUTUBE, PREFERENCE_KEY, BuildConfig.VERSION_NAME);

        if (!integrationVersion.isEmpty())
            return;

        Activity activity = (Activity) context;

        runOnMainThreadDelayed(() -> buildDialog(activity), 1500);

        // set spoof player parameter default value
        SettingsEnum.SPOOF_PLAYER_PARAMETER.saveValue(!activity.getPackageName().equals("com.google.android.youtube"));
    }

    /**
     * For some reason, when I first install the app, my SponsorBlock settings are not initialized.
     * To solve this, forcibly initialize SponsorBlock.
     */
    public static void initializeSponsorBlockSettings(@NonNull Context context) {
        if (SettingsEnum.SB_INITIALIZED.getBoolean())
            return;
        SegmentPlaybackController.initialize(null);
        SettingsEnum.SB_INITIALIZED.saveValue(true);
    }

    public static void setDeviceInformation(@NonNull Context context) {
        ReVancedHelper.setApplicationLabel(context);
        ReVancedHelper.setIsTablet(context);
        ReVancedHelper.setPackageName(context);
        ReVancedHelper.setVersionCode(context);
        ReVancedHelper.setVersionName(context);
    }
}