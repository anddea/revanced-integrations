package app.revanced.integrations.utils;

import static app.revanced.integrations.patches.video.VideoSpeedPatch.overrideSpeed;
import static app.revanced.integrations.patches.video.VideoSpeedPatch.userChangedSpeed;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.WindowManager;

import java.time.Duration;
import java.util.Arrays;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;

public class VideoHelpers {

    public static float currentSpeed;

    public static void copyUrl(Context context, Boolean withTimestamp) {
        try {
            String url = String.format("https://youtu.be/%s", VideoInformation.getVideoId());
            if (withTimestamp) {
                long seconds = VideoInformation.getVideoTime() / 1000;
                url += String.format("?t=%s", seconds);
            }

            setClipboard(context, url);
            showToastShort(context, str("share_copy_url_success"));
        } catch (Exception e) {
            LogHelper.printException(VideoHelpers.class, "Failed to generate video url", e);
        }
    }

    public static void copyTimeStamp(Context context) {
        try {
            long videoTime = VideoInformation.getVideoTime();

            Duration duration = Duration.ofMillis(videoTime);

            long h = duration.toHours();
            long m = duration.toMinutes() % 60;
            long s = duration.getSeconds() % 60;

            @SuppressLint("DefaultLocale") String timeStamp = h > 0 ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);

            setClipboard(context, timeStamp);
            showToastShort(context, str("revanced_copytimestamp_success") + ": " + timeStamp);
        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Couldn't generate video url", ex);
        }
    }

    public static void download(Context context) {
        try {
            var downloaderPackageName = SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString();
            if (downloaderPackageName == null) downloaderPackageName = "ussr.razar.youtube_dl";

            boolean packageEnabled = context.getPackageManager().getApplicationInfo(downloaderPackageName, 0).enabled;

            if (!packageEnabled) {
                showToastShort(context, getDownloaderName(context, downloaderPackageName) + " " + str("revanced_downloader_not_installed"));
                return;
            }
            var content = String.format("https://youtu.be/%s", VideoInformation.getVideoId());

            var intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage(downloaderPackageName);
            intent.putExtra("android.intent.extra.TEXT", content);
            context.startActivity(intent);

        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Failed to launch the downloader intent", ex);
        }
    }

    private static String getDownloaderName(Context context, String DownloaderPackageName) {
        try {
            final var DOWNLOADER_LABEL_PREFERENCE_KEY = "revanced_downloader_label";
            final var DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY = "revanced_downloader_package_name";

            String[] labelArray = context.getResources().getStringArray(identifier(DOWNLOADER_LABEL_PREFERENCE_KEY, ResourceType.ARRAY));
            String[] packageNameArray = context.getResources().getStringArray(identifier(DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY, ResourceType.ARRAY));

            int findIndex = Arrays.binarySearch(packageNameArray, DownloaderPackageName);

            return findIndex >= 0 ? labelArray[findIndex] : DownloaderPackageName;
        } catch (Exception e) {
            LogHelper.printException(VideoHelpers.class, "Unable to set DownloaderName", e);
        }
        return DownloaderPackageName;
    }

    public static void videoSpeedDialogListener(Context context) {
        final var CUSTOM_SPEED_ENTRY_ARRAY_KEY = "revanced_custom_video_speed_entry";
        final var CUSTOM_SPEED_ENTRY_VALUE_ARRAY_KEY = "revanced_custom_video_speed_entry_value";
        final var DEFAULT_SPEED_ENTRY_ARRAY_KEY = "revanced_default_video_speed_entry";
        final var DEFAULT_SPEED_ENTRY_VALUE_ARRAY_KEY = "revanced_default_video_speed_entry_value";

        boolean isCustomSpeedEnabled = SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
        String entriesKey = isCustomSpeedEnabled ? CUSTOM_SPEED_ENTRY_ARRAY_KEY : DEFAULT_SPEED_ENTRY_ARRAY_KEY;
        String entriesValueKey = isCustomSpeedEnabled ? CUSTOM_SPEED_ENTRY_VALUE_ARRAY_KEY : DEFAULT_SPEED_ENTRY_VALUE_ARRAY_KEY;

        String[] speedEntries = getListArray(context, entriesKey);
        String[] speedEntriesValues = getListArray(context, entriesValueKey);

        AlertDialog speedDialog = new AlertDialog.Builder(context)
                .setTitle(setTitle(str("revanced_whitelisting_speed_button")))
                .setItems(speedEntries, (dialog, index) -> overrideSpeedBridge(Float.parseFloat(speedEntriesValues[index] + "f")))
                .show();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = speedDialog.getWindow().getAttributes();
        params.width = (int)(size.x * 0.5);
        speedDialog.getWindow().setAttributes(params);
        speedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static String setTitle(String prefix) {
        String speed = ReVancedUtils.isRightToLeftTextLayout()
                ? "\u2066x\u2069" + currentSpeed  // u202E = right to left character
                : currentSpeed + "x"; // u202D = left to right character
        if (prefix == null) return speed;
        return String.format("%s\u2009•\u2009%s", prefix, speed);
    }

    public static String[] getListArray(Context context, String key) {
        return Arrays.stream(context.getResources().getStringArray(identifier(key, ResourceType.ARRAY))).skip(1).toArray(String[]::new);
    }

    private static void overrideSpeedBridge(final float speed) {
        overrideSpeed(speed);
        userChangedSpeed(speed);
    }

    private static void setClipboard(Context context, String text) {
         android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
         android.content.ClipData clip = android.content.ClipData.newPlainText("link", text);
         clipboard.setPrimaryClip(clip);
    }

    public static float getCurrentSpeed() {
        return currentSpeed;
    }
}
