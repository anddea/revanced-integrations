package app.revanced.integrations.utils;

import static app.revanced.integrations.sponsorblock.StringRef.str;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.content.pm.PackageManager;
import android.widget.Toast;

import app.revanced.integrations.sponsorblock.player.PlayerType;

public class ReVancedUtils {

    private static PlayerType env;
    public static boolean newVideo = false;

    //Used by Integrations patch
    public static Context context;
    //Used by Integrations patch

    public static void setNewVideo(boolean started) {
        LogHelper.debug(ReVancedUtils.class, "New video started: " + started);
        newVideo = started;
    }
    public static boolean isNewVideoStarted() {
        return newVideo;
    }

    public static Integer getResourceIdByName(Context context, String type, String name) {
        try {
            Resources res = context.getResources();
            return res.getIdentifier(name, type, context.getPackageName());
        } catch (Throwable exception) {
            LogHelper.printException(ReVancedUtils.class, "Resource not found.", exception);
            return null;
        }
    }

    public static void setPlayerType(PlayerType type) {
        env = type;
    }

    public static PlayerType getPlayerType() {
        return env;
    }

    public static int getIdentifier(String name, String defType) {
        Context context = getContext();
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    public static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            LogHelper.printException(ReVancedUtils.class, "Context is null, returning null!");
            return null;
        }
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void checkMicroG() {
        try {
            getContext().getPackageManager().getPackageInfo("com.mgoogle.android.gms", PackageManager.GET_ACTIVITIES);
            LogHelper.debug(ReVancedUtils.class, "MicroG is installed on the device");
        } catch (PackageManager.NameNotFoundException exception) {
            LogHelper.printException(ReVancedUtils.class, "MicroG was not found", exception);
            Toast.makeText(getContext(), str("microg_not_installed_warning"), Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), str("microg_not_installed_notice"), Toast.LENGTH_LONG).show();
        }
    }

}