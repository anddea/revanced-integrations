package app.revanced.music.utils;

import static app.revanced.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.music.utils.StringRef.str;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import app.revanced.music.patches.utils.VideoInformation;
import app.revanced.music.settings.SettingsEnum;

public class VideoHelpers {

    public static void downloadMusic(Context context) {
        try {
            if (context == null) {
                showToastShort("Context is null!");
                return;
            }
            var downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.getString();

            boolean packageEnabled = false;
            try {
                packageEnabled = context.getPackageManager().getApplicationInfo(downloaderPackageName, 0).enabled;
            } catch (PackageManager.NameNotFoundException error) {
                showToastShort(str("revanced_external_downloader_not_installed_warning", downloaderPackageName));
            }

            if (!packageEnabled) {
                showToastShort(str("revanced_external_downloader_not_installed_warning", downloaderPackageName));
                return;
            }
            var content = String.format("https://youtu.be/%s", VideoInformation.getVideoId());

            startDownloaderActivity(context, downloaderPackageName, content);
        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Failed to launch the downloader intent", ex);
        }
    }

    public static void startDownloaderActivity(Context context, String downloaderPackageName, String content) {
        try {
            var intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage(downloaderPackageName);
            intent.putExtra("android.intent.extra.TEXT", content);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            LogHelper.printException(VideoHelpers.class, "Unable to start DownloaderActivity", e);
        }
    }
}
