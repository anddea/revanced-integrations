package app.revanced.music.utils;

import static app.revanced.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.music.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.music.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.music.patches.video.PlaybackSpeedPatch;
import app.revanced.music.patches.video.VideoInformation;
import app.revanced.music.settings.SettingsEnum;

public class VideoHelpers {
    public static float currentSpeed = 1.0f;

    public static void downloadMusic(@NonNull Context context) {
        String downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.getString();

        if (downloaderPackageName.isEmpty()) {
            final String defaultValue = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.defaultValue.toString();
            SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.saveValue(defaultValue);
            downloaderPackageName = defaultValue;
        }

        if (!ReVancedHelper.isPackageEnabled(context, downloaderPackageName)) {
            showToastShort(str("revanced_external_downloader_not_installed_warning", downloaderPackageName));
            return;
        }

        startDownloaderActivity(context, downloaderPackageName, String.format("https://youtu.be/%s", VideoInformation.getVideoId()));
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

        AlertDialog speedDialog = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setTitle(currentSpeed + "x")
                .setItems(playbackSpeedEntries, (dialog, index) -> overrideSpeedBridge(Float.parseFloat(playbackSpeedEntryValues[index] + "f")))
                .show();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = Objects.requireNonNull(speedDialog.getWindow()).getAttributes();
        params.width = (int) (size.x * 0.5);

        if (CustomPlaybackSpeedPatch.getLength(7) > 7)
            params.height = (int) (size.y * 0.55);

        speedDialog.getWindow().setAttributes(params);
    }

    @SuppressLint("IntentReset")
    public static void openInYouTube(@NonNull Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            // noinspection deprecation
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        String url = String.format("vnd.youtube://%s", VideoInformation.getVideoId());
        if (SettingsEnum.REPLACE_FLYOUT_PANEL_DISMISS_QUEUE_CONTINUE_WATCH.getBoolean()) {
            long seconds = VideoInformation.getVideoTime() / 1000;
            url += String.format("?t=%s", seconds);
        }

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void overrideSpeedBridge(final float speed) {
        PlaybackSpeedPatch.overrideSpeed(speed);
        PlaybackSpeedPatch.userChangedSpeed(speed);
    }

    public static float getCurrentSpeed() {
        return currentSpeed;
    }
}
