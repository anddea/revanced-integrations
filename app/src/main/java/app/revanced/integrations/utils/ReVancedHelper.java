package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.SwitchPreference;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.settings.SettingsEnum;

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

    public static boolean isFullscreenHidden() {
        boolean isFullscreenHidden = isTablet() &&
                !SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();
        final SettingsEnum[] hideFullscreenSettings = {
                SettingsEnum.ENABLE_TABLET_LAYOUT,
                SettingsEnum.HIDE_FULLSCREEN_BUTTON_CONTAINER,
                SettingsEnum.HIDE_FULLSCREEN_PANELS
        };
        for (SettingsEnum s : hideFullscreenSettings) {
            isFullscreenHidden |= s.getBoolean();
        }
        return isFullscreenHidden;
    }

    public static boolean isTablet() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void setBuildVersion() {
        saveString(YOUTUBE, PREFERENCE_KEY, BuildConfig.VERSION_NAME);
    }

}