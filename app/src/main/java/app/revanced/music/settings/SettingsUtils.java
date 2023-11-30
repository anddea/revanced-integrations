package app.revanced.music.settings;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
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
        showRestartDialog(activity, "revanced_reboot_message", 0);
    }

    public static void showRestartDialog(@NonNull Activity activity, @NonNull String message, long delay) {
        getDialogBuilder(activity)
                .setMessage(str(message))
                .setPositiveButton(android.R.string.ok, (dialog, id) -> runOnMainThreadDelayed(() -> restartApp(activity), delay))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}