package app.revanced.music.utils;

import static app.revanced.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.Objects;

import app.revanced.music.patches.misc.PlaybackSpeedPatch;
import app.revanced.music.patches.utils.VideoInformation;
import app.revanced.music.settings.SettingsEnum;

public class VideoHelpers {

    public static float currentSpeed;
    private static final String[] playbackSpeedEntries = {"0.25x", "0.5x", "0.75x", str("offline_audio_quality_normal"), "1.25x", "1.5x", "1.75x", "2.0x"};
    private static final String[] playbackSpeedEntryValues = {"0.25", "0.5", "0.75", "1.0", "1.25", "1.50", "1.75", "2.0"};

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

    public static void playbackSpeedDialogListener(Context context) {
        AlertDialog speedDialog = new AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setTitle(currentSpeed + "x")
                .setItems(playbackSpeedEntries, (dialog, index) -> overrideSpeedBridge(Float.parseFloat(playbackSpeedEntryValues[index] + "f")))
                .show();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = Objects.requireNonNull(speedDialog.getWindow()).getAttributes();
        params.width = (int) (size.x * 0.5);
        params.height = (int) (size.y * 0.55);
        speedDialog.getWindow().setAttributes(params);
    }

    private static void overrideSpeedBridge(final float speed) {
        PlaybackSpeedPatch.overrideSpeed(speed);
        PlaybackSpeedPatch.userChangedSpeed(speed);
    }
}
