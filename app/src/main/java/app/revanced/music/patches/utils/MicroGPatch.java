package app.revanced.music.patches.utils;

import static app.revanced.music.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;

import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public class MicroGPatch {
    private static final String DONT_KILL_MY_APP_LINK = "https://dontkillmyapp.com";
    private static final String MICROG_VENDOR = "com.mgoogle";
    private static final String MICROG_PACKAGE_NAME = MICROG_VENDOR + ".android.gms";
    private static final String MICROG_DOWNLOAD_LINK = "https://github.com/inotia00/VancedMicroG/releases/latest";
    private static final Uri MICROG_PROVIDER = Uri.parse("content://" + MICROG_VENDOR + ".android.gsf.gservices/prefix");

    private static void startIntent(Context context, String uriString, String... message) {
        for (String string : message) {
            ReVancedUtils.showToastLong(string);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    public static void checkAvailability(@NonNull Context context) {
        try {
            context.getPackageManager().getPackageInfo(MICROG_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException exception) {
            LogHelper.printInfo(() -> "MicroG was not found", exception);
            startIntent(context, MICROG_DOWNLOAD_LINK, str("microg_not_installed_warning"), str("microg_not_installed_notice"));

            final Activity activity = (Activity) context;
            runOnMainThreadDelayed(activity::finish, 1000L);
            return;
        }

        try (final ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(MICROG_PROVIDER)) {
            if (client != null)
                return;
            LogHelper.printInfo(() -> "MicroG is not running in the background");
            startIntent(context, DONT_KILL_MY_APP_LINK, str("microg_not_running_warning"));
        }
    }
}
