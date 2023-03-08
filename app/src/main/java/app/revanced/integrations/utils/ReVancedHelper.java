package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;

public class ReVancedHelper {
    private static final String PREFERENCE_KEY = "integrations";

    private ReVancedHelper() {
    } // utility class

    public static String getAppName() {
        String appName = "ReVanced_Extended";
        try {
            var context = Objects.requireNonNull(ReVancedUtils.getContext());
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
            var context = Objects.requireNonNull(ReVancedUtils.getContext());
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "18.04.43";
    }

    public static boolean isTablet() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void setBuildVersion(Context context) {
        var savedVersion = getString(YOUTUBE, PREFERENCE_KEY, null);
        var currentVersion = BuildConfig.VERSION_NAME;

        if (savedVersion == null) resetPreference(context, currentVersion);
        else saveString(YOUTUBE, PREFERENCE_KEY, currentVersion);
    }

    public static void resetPreference(Context context, String currentVersion) {
        try {
            SharedPreferences.Editor prefEdit = SharedPrefHelper.getPreferences(context, REVANCED).edit();
            prefEdit.clear();
            prefEdit.apply();
            saveString(YOUTUBE, PREFERENCE_KEY, currentVersion);
        } catch (Exception ignored) {
        }
    }

}