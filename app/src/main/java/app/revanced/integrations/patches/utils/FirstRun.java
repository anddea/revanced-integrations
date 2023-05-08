package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.sponsorblock.SegmentPlaybackController.initialize;
import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.settings.SettingsEnum;

public class FirstRun {
    private static final String PREFERENCE_KEY = "integrations";

    public static void initializationSB(@NonNull Context context) {
        if (SettingsEnum.SB_FIRST_RUN.getBoolean()) return;
        initialize(null);
        SettingsEnum.SB_FIRST_RUN.saveValue(true);
    }

    public static void initializationRVX(@NonNull Context context) {
        var integrationVersion = getString(YOUTUBE, PREFERENCE_KEY, null);

        if (!Objects.equals(integrationVersion, BuildConfig.VERSION_NAME))
            saveString(YOUTUBE, PREFERENCE_KEY, BuildConfig.VERSION_NAME);

        if (integrationVersion != null) return;

        Activity activity = (Activity) context;

        new AlertDialog.Builder(activity)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) ->
                        runOnMainThreadDelayed(() ->{
                            activity.finishAffinity();
                            activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
                            System.exit(0);
                            }, 1000L)
                )
                .setNegativeButton(str("sign_in_cancel"), null)
                .setCancelable(false)
                .show();
    }

}