package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class ReVancedHelper {
    private static final String DEFAULT_APP_NAME = "ReVanced_Extended";
    private static final int DEFAULT_VERSION_CODE = 1537867200; // 18.20.39
    private static final String DEFAULT_VERSION_NAME = "18.20.39";
    private static final int HOOK_DOWNLOAD_BUTTON_TARGET_VERSION_CODE = 1538379200; // 18.24.33

    private ReVancedHelper() {
    } // utility class

    @NonNull
    public static String getAppName(@NonNull Context context) {
        var packageInfo = getPackageInfo(context);
        return packageInfo == null
                ? DEFAULT_APP_NAME
                : (String) packageInfo.applicationInfo.loadLabel(getPackageManager(context));
    }

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
    private static int getVersionCode(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null
                ? DEFAULT_VERSION_CODE
                : packageInfo.versionCode;
    }

    @NonNull
    public static String getVersionName(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null
                ? DEFAULT_VERSION_NAME
                : packageInfo.versionName;
    }

    @NonNull
    public static String[] getStringArray(@NonNull Context context, @NonNull String key) {
        return context.getResources().getStringArray(identifier(key, ResourceType.ARRAY));
    }

    public static boolean isFullscreenHidden() {
        boolean isFullscreenHidden = isTablet() &&
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

    public static boolean isSupportHookDownloadButton(@NonNull Context context) {
        return isSpoofedTargetVersionGez("18.24.00") || getVersionCode(context) >= HOOK_DOWNLOAD_BUTTON_TARGET_VERSION_CODE;
    }

    public static boolean isTablet() {
        final Context context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
}