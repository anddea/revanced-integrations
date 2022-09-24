package app.revanced.integrations.sponsorblock.dialog;

import static app.revanced.integrations.sponsorblock.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.settingsmenu.ReVancedSettingsFragment;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class Dialogs {
    // Inject call from YT to this
    public static void showDialogsAtStartup(Activity activity) {
		vxFirstRun(activity);
    }

    private static void vxFirstRun(Activity activity) {
        Context context = ReVancedUtils.getContext();
        boolean hintShownRVX = SettingsEnum.REVANCED_EXTENDED_HINT_SHOWN.getBoolean();
        // if the version is newer than v17.32.39 (1531051456) -> true
        boolean after33 = ReVancedSettingsFragment.getVersionCode() > 1531051456 ? true : false;
        String msg = "\n\n" + str("settings") + " > " + str("revanced_settings") + " > " + str("revanced_misc_title") + " > " + str("revanced_force_fullscreen_rotation_title");

        // If SB is enabled or hint has been shown, exit
        if (hintShownRVX || !after33) {
            return;
        }

        // set dummy video-id to initialize Sponsorblock: TeamVanced's Sponsorblock tutorial (https://www.youtube.com/watch?v=sE2IzSn-hHU)
        PlayerController.setCurrentVideoId("sE2IzSn-hHU");
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(str("rvx_dialog_title"));
        builder.setIcon(ReVancedUtils.getIdentifier("ic_rvx_logo", "drawable"));
        builder.setCancelable(false);
        builder.setMessage(str("rvx_dialog_message") + msg);
        builder.setPositiveButton(str("playback_control_close"),
                (dialog, id) -> {
                    SettingsEnum.REVANCED_EXTENDED_HINT_SHOWN.saveValue(true);
                    SettingsEnum.SB_ENABLED.saveValue(true);
                    dialog.dismiss();
                });

        builder.setNeutralButton(str("mdx_pref_use_tv_code_learn_more"), null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set learn more action (set here so clicking it doesn't dismiss the dialog)
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
            Uri uri = Uri.parse("https://t.me/revanced_mod_archive/111");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        });
    }
}