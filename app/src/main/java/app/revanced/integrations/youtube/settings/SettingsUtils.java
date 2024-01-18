package app.revanced.integrations.youtube.settings;

import static app.revanced.integrations.youtube.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.youtube.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SettingsUtils {

    public static void restartApp(@NonNull Activity activity) {
        final Intent intent = Objects.requireNonNull(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
        final Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());

        activity.finishAffinity();
        activity.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    public static void showRestartDialog(@NonNull Activity activity) {
        showRestartDialog(activity, "revanced_restart_message", 0);
    }

    public static void showRestartDialog(@NonNull Activity activity, @NonNull String message, long delay) {
        new AlertDialog.Builder(activity)
                .setMessage(str(message))
                .setPositiveButton(android.R.string.ok, (dialog, id) -> runOnMainThreadDelayed(() -> restartApp(activity), delay))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}