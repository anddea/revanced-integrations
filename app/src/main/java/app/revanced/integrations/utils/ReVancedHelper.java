package app.revanced.integrations.utils;

import android.content.pm.PackageManager;

public class ReVancedHelper {

    private ReVancedHelper() {
    } // utility class

    public static boolean isTablet() {
        var context = ReVancedUtils.getContext();
        assert context != null;
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static String getVersionName() {
        try {
            var context = ReVancedUtils.getContext();
            assert context != null;
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "17.49.37";
    }
}