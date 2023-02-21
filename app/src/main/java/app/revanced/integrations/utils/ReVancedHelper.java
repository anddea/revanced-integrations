package app.revanced.integrations.utils;

import static app.revanced.integrations.settingsmenu.ReVancedSettingsFragment.rebootDialogStatic;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.settings.SettingsEnum;

public class ReVancedHelper {
    private static final String PREFERENCE_KEY = "integrations";

    private ReVancedHelper() {
    } // utility class

    public static boolean isTablet() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

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

    public static void setBuildVersion(Context context) {
        var savedVersion = getString(context, YOUTUBE, PREFERENCE_KEY, null);
        var currentVersion = BuildConfig.VERSION_NAME;

        if (savedVersion == null) resetPreference(context, currentVersion);
        else saveString(context, YOUTUBE, PREFERENCE_KEY, currentVersion);
    }

    public static void resetPreference(Context context, String currentVersion) {
        try {
            SharedPreferences.Editor prefEdit = SharedPrefHelper.getPreferences(context, REVANCED).edit();
            prefEdit.clear();
            prefEdit.apply();
            saveString(context, YOUTUBE, PREFERENCE_KEY, currentVersion);
        } catch (Exception ignored) {
        }
    }

    /*
     * fix: https://github.com/inotia00/ReVanced_Extended/issues/276
     */
    public static void versionSpoof(Context context) {
        SettingsEnum initialSpoof = SettingsEnum.INITIAL_SPOOF;

        if (initialSpoof.getBoolean()) return;

        Activity activity = (Activity) context;

        new AlertDialog.Builder(activity)
                .setMessage(str("revanced_initial_spoof"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) -> {
                    initialSpoof.saveValue(true);
                    rebootDialogStatic(context, str("pref_refresh_config"));
                    dialog.dismiss();
                })
                .setNeutralButton(str("mdx_pref_use_tv_code_learn_more"), (dialog, id) -> {
                    Uri uri = Uri.parse("https://github.com/inotia00/ReVanced_Extended/issues/276");
                    var intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }
}