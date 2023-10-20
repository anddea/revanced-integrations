package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public class ReVancedHelper {
    public static String applicationLabel = "ReVanced_Extended";
    public static boolean isTablet = false;
    public static String packageName = "app.rvx.android.youtube";
    public static int versionCode = 1540361664; // 18.39.41
    public static String versionName = "18.39.41";

    private ReVancedHelper() {
    } // utility class

    @Nullable
    private static PackageInfo getPackageInfo(@NonNull Context context) {
        try {
            return getPackageManager(context).getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogHelper.printException(ReVancedHelper.class, "Failed to get package Info!" + e);
        }
        return null;
    }

    @NonNull
    private static PackageManager getPackageManager(@NonNull Context context) {
        return context.getPackageManager();
    }

    @NonNull
    public static String[] getStringArray(@NonNull Context context, @NonNull String key) {
        return context.getResources().getStringArray(identifier(key, ResourceType.ARRAY));
    }

    public static boolean isAdditionalSettingsEnabled() {
        boolean additionalSettingsEnabled = true;
        final SettingsEnum[] additionalSettings = {
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_AMBIENT,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_HELP,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_LOOP,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_PREMIUM_CONTROLS,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_STATS_FOR_NERDS,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_WATCH_IN_VR,
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_YT_MUSIC,
        };
        for (SettingsEnum s : additionalSettings) {
            additionalSettingsEnabled &= s.getBoolean();
        }
        return additionalSettingsEnabled;
    }

    public static boolean isFullscreenHidden() {
        boolean isFullscreenHidden = isTablet &&
                !SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();
        final SettingsEnum[] hideFullscreenSettings = {
                SettingsEnum.ENABLE_TABLET_LAYOUT,
                SettingsEnum.HIDE_QUICK_ACTIONS,
                SettingsEnum.HIDE_FULLSCREEN_PANELS
        };
        for (SettingsEnum s : hideFullscreenSettings) {
            isFullscreenHidden |= s.getBoolean();
        }
        return isFullscreenHidden;
    }

    public static boolean isPackageEnabled(@NonNull Context context, @NonNull String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return false;
    }

    public static boolean isSpoofedTargetVersionGez(@NonNull String versionName) {
        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return false;

        final int spoofedVersion = Integer.parseInt(SettingsEnum.SPOOF_APP_VERSION_TARGET.getString().replaceAll("\\.", ""));
        final int targetVersion = Integer.parseInt(versionName.replaceAll("\\.", ""));
        return spoofedVersion >= targetVersion;
    }

    public static boolean isSpoofedTargetVersionLez(@NonNull String versionName) {
        if (!SettingsEnum.SPOOF_APP_VERSION.getBoolean())
            return false;

        final int spoofedVersion = Integer.parseInt(SettingsEnum.SPOOF_APP_VERSION_TARGET.getString().replaceAll("\\.", ""));
        final int targetVersion = Integer.parseInt(versionName.replaceAll("\\.", ""));
        return spoofedVersion <= targetVersion;
    }

    public static void setApplicationLabel(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            applicationLabel = (String) packageInfo.applicationInfo.loadLabel(getPackageManager(context));
        }
    }

    public static void setIsTablet(@NonNull Context context) {
        isTablet = context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void setPackageName(@NonNull Context context) {
        packageName = context.getPackageName();
    }

    public static void setVersionCode(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
    }

    public static void setVersionName(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
        }
    }
}