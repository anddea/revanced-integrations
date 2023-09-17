package app.revanced.integrations.utils;

import static app.revanced.integrations.patches.video.PlaybackSpeedPatch.overrideSpeed;
import static app.revanced.integrations.patches.video.PlaybackSpeedPatch.userChangedSpeed;
import static app.revanced.integrations.utils.ReVancedHelper.getStringArray;
import static app.revanced.integrations.utils.ReVancedHelper.isPackageEnabled;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

import app.revanced.integrations.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;

public class VideoHelpers {

    public static float currentSpeed;

    public static void copyUrl(@NonNull Context context, Boolean withTimestamp) {
        String url = String.format("https://youtu.be/%s", VideoInformation.getVideoId());
        if (withTimestamp) {
            long seconds = VideoInformation.getVideoTime() / 1000;
            url += String.format("?t=%s", seconds);
        }

        ReVancedUtils.setClipboard(url);
        showToastShort(context, str("share_copy_url_success"));
    }

    @SuppressLint("DefaultLocale")
    public static void copyTimeStamp(@NonNull Context context) {
        final long videoTime = VideoInformation.getVideoTime();

        final Duration duration = Duration.ofMillis(videoTime);

        long h = duration.toHours();
        long m = duration.toMinutes() % 60;
        long s = duration.getSeconds() % 60;

        final String timeStamp = h > 0
                ? String.format(":\u2009%02d:%02d:%02d", h, m, s)
                : String.format(":\u2009%02d:%02d", m, s);

        ReVancedUtils.setClipboard(timeStamp);
        showToastShort(context, str("revanced_copy_video_timestamp_success") + timeStamp);
    }

    public static void download(@NonNull Context context) {
        String downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.getString().trim();

        if (downloaderPackageName.isEmpty()) {
            final String defaultValue = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.defaultValue.toString();
            SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.saveValue(defaultValue);
            downloaderPackageName = defaultValue;
        }

        if (!isPackageEnabled(context, downloaderPackageName)) {
            showToastShort(str("revanced_external_downloader_not_installed_warning", getExternalDownloaderName(context, downloaderPackageName)));
            return;
        }

        startDownloaderActivity(context, downloaderPackageName, String.format("https://youtu.be/%s", VideoInformation.getVideoId()));
    }

    @NonNull
    private static String getExternalDownloaderName(@NonNull Context context, @NonNull String packageName) {
        try {
            final String EXTERNAL_DOWNLOADER_LABEL_PREFERENCE_KEY = "revanced_external_downloader_label";
            final String EXTERNAL_DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY = "revanced_external_downloader_package_name";

            final String[] labelArray = getStringArray(context, EXTERNAL_DOWNLOADER_LABEL_PREFERENCE_KEY);
            final String[] packageNameArray = getStringArray(context, EXTERNAL_DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY);

            final int findIndex = Arrays.binarySearch(packageNameArray, packageName);

            return findIndex >= 0 ? labelArray[findIndex] : packageName;
        } catch (Exception e) {
            LogHelper.printException(VideoHelpers.class, "Failed to set ExternalDownloaderName", e);
        }
        return packageName;
    }

    public static void startDownloaderActivity(@NonNull Context context, @NonNull String downloaderPackageName, @NonNull String content) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setPackage(downloaderPackageName);
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void playbackSpeedDialogListener(@NonNull Context context) {
        final String[] speedEntries = CustomPlaybackSpeedPatch.getListEntries();
        final String[] speedEntryValues = CustomPlaybackSpeedPatch.getListEntryValues();

        AlertDialog speedDialog = new AlertDialog.Builder(context)
                .setTitle(setTitle(str("camera_speed_button_label")))
                .setItems(speedEntries, (dialog, index) -> overrideSpeedBridge(Float.parseFloat(speedEntryValues[index] + "f")))
                .show();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = Objects.requireNonNull(speedDialog.getWindow()).getAttributes();
        params.width = (int) (size.x * 0.5);
        speedDialog.getWindow().setAttributes(params);
        speedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static String setTitle(@Nullable String prefix) {
        final String speed = ReVancedUtils.isRightToLeftTextLayout()
                ? "\u2066x\u2069" + currentSpeed  // u202E = right to left character
                : currentSpeed + "x"; // u202D = left to right character

        if (prefix == null)
            return speed;

        return String.format("%s\u2009•\u2009%s", prefix, speed);
    }

    private static void overrideSpeedBridge(final float speed) {
        overrideSpeed(speed);
        userChangedSpeed(speed);
    }

    public static float getCurrentSpeed() {
        return currentSpeed;
    }
}
