package app.revanced.integrations.music.utils;

import static app.revanced.integrations.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.integrations.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.music.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.integrations.music.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.integrations.music.patches.video.PlaybackSpeedPatch;
import app.revanced.integrations.music.patches.video.VideoInformation;
import app.revanced.integrations.music.settings.SettingsEnum;

public class VideoHelpers {
    /**
     * Injection point.
     */
    public static String currentQuality = "";
    /**
     * Injection point.
     */
    public static float currentSpeed = 1.0f;

    public static void downloadMusic(@NonNull Context context) {
        String downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.getString();

        if (downloaderPackageName.isEmpty()) {
            SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.resetToDefault();
            downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.defaultValue.toString();
        }

        if (!ReVancedHelper.isPackageEnabled(context, downloaderPackageName)) {
            showToastShort(str("revanced_external_downloader_not_installed_warning", downloaderPackageName));
            return;
        }

        startDownloaderActivity(context, downloaderPackageName, String.format("https://music.youtube.com/watch?v=%s", VideoInformation.getVideoId()));
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
        final String[] playbackSpeedEntries = CustomPlaybackSpeedPatch.getListEntries();
        final String[] playbackSpeedEntryValues = CustomPlaybackSpeedPatch.getListEntryValues();

        final int index = Arrays.binarySearch(playbackSpeedEntryValues, String.valueOf(currentSpeed));

        AlertDialog speedDialog = getDialogBuilder(context)
                .setSingleChoiceItems(playbackSpeedEntries, index, (mDialog, mIndex) -> {
                    overrideSpeedBridge(Float.parseFloat(playbackSpeedEntryValues[mIndex] + "f"));
                    mDialog.dismiss();
                })
                .show();

        final Window window = speedDialog.getWindow();
        if (CustomPlaybackSpeedPatch.getLength(7) > 7 && window != null) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            WindowManager.LayoutParams params = window.getAttributes();
            params.height = (int) (size.y * 0.45);

            window.setAttributes(params);
        }
    }

    @SuppressLint("IntentReset")
    public static void openInYouTube(@NonNull Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        final String videoId = VideoInformation.getVideoId();
        if (videoId.isEmpty()) {
            showToastShort(str("revanced_watch_on_youtube_warning"));
            return;
        }

        if (audioManager != null) {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        String url = String.format("vnd.youtube://%s", videoId);
        if (SettingsEnum.REPLACE_FLYOUT_PANEL_DISMISS_QUEUE_CONTINUE_WATCH.getBoolean()) {
            long seconds = VideoInformation.getVideoTime() / 1000;
            url += String.format("?t=%s", seconds);
        }

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openInMusic(@NonNull Context context, @NonNull String songId) {
        final String url = String.format("vnd.youtube.music://%s", songId);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(ReVancedHelper.packageName);
        context.startActivity(intent);
    }

    private static void overrideSpeedBridge(final float speed) {
        PlaybackSpeedPatch.overrideSpeed(speed);
        PlaybackSpeedPatch.userChangedSpeed(speed);
    }

    public static float getCurrentSpeed() {
        return currentSpeed;
    }

    public static int getCurrentQuality(int original) {
        try {
            return Integer.parseInt(currentQuality.split("p")[0]);
        } catch (Exception ignored) {
        }
        return original;
    }
}
