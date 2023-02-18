package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;

public class ReVancedHelper {
    private static final String PREFERENCE_KEY = "integrations";

    private ReVancedHelper() {
    } // utility class

    public static boolean isTablet() {
        var context = ReVancedUtils.getContext();
        assert context != null;
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static String getAppName() {
        String appName = "ReVanced_Extended";
        try {
            var context = ReVancedUtils.getContext();
            assert context != null;
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            appName = packageInfo.applicationInfo.loadLabel(packageManager) + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
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

    public static void setBuildVersion() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        var savedVersion = getString(context, YOUTUBE, PREFERENCE_KEY, null);
        var currentVersion = BuildConfig.VERSION_NAME;

        if (savedVersion == null) resetPreference(currentVersion);
        else if (!savedVersion.equals(currentVersion))
            saveString(context, YOUTUBE, PREFERENCE_KEY, currentVersion);
    }

    public static void resetPreference(String currentVersion) {
        try {
            var context = Objects.requireNonNull(ReVancedUtils.getContext());
            SharedPreferences.Editor prefEdit = SharedPrefHelper.getPreferences(context, REVANCED).edit();
            prefEdit.clear();
            prefEdit.apply();
            saveString(context, YOUTUBE, PREFERENCE_KEY, currentVersion);
        } catch (Exception ignored) {
        }
    }
}