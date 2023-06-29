package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class NewPlayerFlyoutPanelsDetectPatch {
    private static final String TARGET_VERSION_NAME = "18.18.39";
    private static String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(str("revanced_new_player_flyout_panel_alert_dialog_message_header"));
        sb.append("\n\n");

        if (PatchStatus.CustomVideoSpeed()) {
            sb.append("• ");
            sb.append(str("revanced_enable_custom_video_speed_title"));
            sb.append("\n");
        }

        if (PatchStatus.OldQualityLayout()) {
            sb.append("• ");
            sb.append(str("revanced_enable_old_quality_layout_title"));
            sb.append("\n");
        }

        sb.append("\n");
        sb.append(str("revanced_new_player_flyout_panel_alert_dialog_message_footer", TARGET_VERSION_NAME, TARGET_VERSION_NAME));

        return sb.toString();
    }

    public static void showAlertDialog(@NonNull Context context) {
        SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DETECTED.saveValue(true);

        if (SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DIALOG_SHOWN.getBoolean())
            return;

        Activity activity = (Activity) context;

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);

        builder.setTitle(str("revanced_new_player_flyout_panel_alert_dialog_title"));
        builder.setMessage(getMessage());
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            runOnMainThreadDelayed(() -> {
                SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DETECTED.saveValue(false);
                SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DIALOG_SHOWN.saveValue(true);
                SettingsEnum.SPOOF_APP_VERSION.saveValue(true);
                SettingsEnum.SPOOF_APP_VERSION_TARGET.saveValue(TARGET_VERSION_NAME);
                showToastShort(context ,str("revanced_reboot_first_run"));
            }, 0L);

            runOnMainThreadDelayed(() -> {
                activity.finishAffinity();
                activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
                System.exit(0);
            }, 1000L);

            dialog.dismiss();
        });

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(view -> {
            runOnMainThreadDelayed(() -> SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DIALOG_SHOWN.saveValue(true), 0L);
            dialog.dismiss();
        });

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = Objects.requireNonNull(dialog.getWindow()).getAttributes();
        params.width = (int) (size.x * 0.8);
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
