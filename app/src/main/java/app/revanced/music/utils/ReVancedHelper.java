package app.revanced.music.utils;

import static app.revanced.music.utils.ResourceUtils.identifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReVancedHelper {
    private static final int LAST_BUILD_2023_VERSION_CODE = 63155230;  // 6.31.55
    public static String applicationLabel = "RVX_Music";
    public static String packageName = "app.rvx.android.apps.youtube.music";
    public static String appVersionName = "6.21.52";

    private ReVancedHelper() {
    } // utility class

    @Nullable
    private static PackageInfo getPackageInfo(@NonNull Context context) {
        try {
            final PackageManager packageManager = getPackageManager(context);
            final String packageName = context.getPackageName();
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    ? packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                    : packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogHelper.printException(() -> "Failed to get package Info!" + e);
        }
        return null;
    }

    @NonNull
    private static PackageManager getPackageManager(@NonNull Context context) {
        return context.getPackageManager();
    }

    public static boolean isLastBuild2023(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null && packageInfo.versionCode >= LAST_BUILD_2023_VERSION_CODE;
    }

    public static boolean isPackageEnabled(@NonNull Context context, @NonNull String packageName) {
        try {
            return getPackageManager(context).getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return false;
    }

    public static void setApplicationLabel(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            applicationLabel = (String) packageInfo.applicationInfo.loadLabel(getPackageManager(context));
        }
    }

    public static void setPackageName(@NonNull Context context) {
        packageName = context.getPackageName();
    }

    public static void setVersionName(@NonNull Context context) {
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            appVersionName = packageInfo.versionName;
        }
    }

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static AlertDialog.Builder getDialogBuilder(@NonNull Context context) {
        return new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
    }

    public static FrameLayout.LayoutParams getLayoutParams(@NonNull Activity activity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int left_margin = dpToPx(20, activity.getResources());
        int top_margin = dpToPx(10, activity.getResources());
        int right_margin = dpToPx(20, activity.getResources());
        int bottom_margin = dpToPx(4, activity.getResources());
        params.setMargins(left_margin, top_margin, right_margin, bottom_margin);

        return params;
    }

    @NonNull
    public static String[] getStringArray(@NonNull Context context, @NonNull String key) {
        return context.getResources().getStringArray(identifier(key, ResourceType.ARRAY));
    }
}