package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.content.pm.PackageManager;
import android.widget.Toast;

import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class MicroGPatch {
    private static final String MICROG_PACKAGE_NAME = "com.mgoogle.android.gms";

    public static void checkAvailability() {
        var context = ReVancedUtils.getContext();
        assert context != null;
        try {
            context.getPackageManager().getPackageInfo(MICROG_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException exception) {
            LogHelper.printException(ReVancedUtils.class, "MicroG was not found", exception);
            Toast.makeText(context, str("microg_not_installed_warning"), Toast.LENGTH_LONG).show();
            Toast.makeText(context, str("microg_not_installed_notice"), Toast.LENGTH_LONG).show();
        }
    }
}
