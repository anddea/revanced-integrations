package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedUtils.showToastLong;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;

public class MicroGPatch {
    private static final String MICROG_VENDOR = "com.mgoogle";
    private static final String MICROG_PACKAGE_NAME = MICROG_VENDOR + ".android.gms";
    private static final Uri MICROG_PROVIDER = Uri.parse("content://" + MICROG_VENDOR + ".android.gsf.gservices/prefix");

    public static void checkAvailability(@NonNull Context context) {
        try {
            context.getPackageManager().getPackageInfo(MICROG_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException exception) {
            showToastShort(context, str("microg_not_installed_warning"));
            showToastShort(context, str("microg_not_installed_notice"));

            System.exit(0);
        }

        try (var client = context.getContentResolver().acquireContentProviderClient(MICROG_PROVIDER)) {
            if (client != null) return;
            showToastLong(context, str("microg_not_running_warning"));
        }
    }
}
