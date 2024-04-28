package app.revanced.integrations.youtube.utils;

import static app.revanced.integrations.youtube.patches.video.PlaybackSpeedPatch.overrideSpeed;
import static app.revanced.integrations.youtube.patches.video.PlaybackSpeedPatch.userChangedSpeed;
import static app.revanced.integrations.youtube.utils.ReVancedHelper.getStringArray;
import static app.revanced.integrations.youtube.utils.ReVancedHelper.isPackageEnabled;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.youtube.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.Duration;
import java.util.Arrays;

import app.revanced.integrations.youtube.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.integrations.youtube.patches.video.VideoInformation;
import app.revanced.integrations.youtube.patches.video.VideoQualityPatch;
import app.revanced.integrations.youtube.settings.SettingsEnum;

public class VideoHelpers {
    /**
     * Injection point.
     */
    public static float currentSpeed;
    /**
     * Injection point.
     */
    public static String qualityAutoString = "Auto";
    private static volatile boolean isPiPAvailable = true;

    public static String currentQuality = "";

    /**
     * Injection point.
     *
     * @param quality Current video quality in human readable form: 1080p
     */
    public static void onQualityChanges(String quality) {
        if (currentQuality == quality)
            return;
        currentQuality = quality;
        VideoQualityPatch.overideDefaultVideoQuality();
    }

    public static void copyUrl(boolean withTimestamp) {
        StringBuilder builder = new StringBuilder("https://youtu.be/");
        builder.append(VideoInformation.getVideoId());
        final long currentVideoTimeInSeconds = VideoInformation.getVideoTime() / 1000;
        if (withTimestamp && currentVideoTimeInSeconds > 0) {
            builder.append("?t=");
            builder.append(currentVideoTimeInSeconds);
        }

        ReVancedUtils.setClipboard(builder.toString(), withTimestamp
                ? str("revanced_share_copy_url_timestamp_success")
                : str("revanced_share_copy_url_success")
        );
    }

    @TargetApi(26)
    @SuppressLint("DefaultLocale")
    public static void copyTimeStamp() {
        final long currentVideoTime = VideoInformation.getVideoTime();
        final Duration duration = Duration.ofMillis(currentVideoTime);

        final long h = duration.toHours();
        final long m = duration.toMinutes() % 60;
        final long s = duration.getSeconds() % 60;

        final String timeStamp = h > 0
                ? String.format("%02d:%02d:%02d", h, m, s)
                : String.format("%02d:%02d", m, s);

        ReVancedUtils.setClipboard(timeStamp, str("revanced_share_copy_timestamp_success", timeStamp));
    }

    public static void download(@NonNull Context context) {
        download(context, true, VideoInformation.getVideoId(), false);
    }

    public static void download(@NonNull Context context, boolean isActivityContext, @NonNull String videoId, boolean isPlaylistDownload) {
        String downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.getString().trim();

        if (downloaderPackageName.isEmpty()) {
            SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.resetToDefault();
            downloaderPackageName = SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.defaultValue.toString();
        }

        if (!isPackageEnabled(context, downloaderPackageName)) {
            showToastShort(str("revanced_external_downloader_not_installed_warning", getExternalDownloaderName(context, downloaderPackageName)));
            return;
        }

        isPiPAvailable = false;
        startDownloaderActivity(context, isActivityContext, downloaderPackageName, 
                String.format(isPlaylistDownload ? "https://youtu.be/playlist?list=%s": "https://youtu.be/%s", videoId)
        );
        ReVancedUtils.runOnMainThreadDelayed(() -> isPiPAvailable = true, 500L);
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
            LogHelper.printException(() -> "Failed to set ExternalDownloaderName", e);
        }
        return packageName;
    }

    public static void startDownloaderActivity(@NonNull Context context, boolean isActivityContext, @NonNull String downloaderPackageName, @NonNull String content) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setPackage(downloaderPackageName);
        intent.putExtra("android.intent.extra.TEXT", content);
        if (!isActivityContext) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void playlistFromChannelVideosListener(@NonNull Context context, boolean activated) {
        String baseUri = "vnd.youtube://" + VideoInformation.getVideoId() + "?start=" + VideoInformation.getVideoTime() / 1000;
        if (activated) {
            baseUri += "&list=UL" + VideoInformation.getVideoId();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUri));
        context.startActivity(intent);
    }

    public static void playbackSpeedDialogListener(@NonNull Context context) {
        final String[] playbackSpeedWithAutoEntries = CustomPlaybackSpeedPatch.getListEntries();
        final String[] playbackSpeedWithAutoEntryValues = CustomPlaybackSpeedPatch.getListEntryValues();

        final String[] playbackSpeedEntries = Arrays.copyOfRange(playbackSpeedWithAutoEntries, 1, playbackSpeedWithAutoEntries.length);
        final String[] playbackSpeedEntryValues = Arrays.copyOfRange(playbackSpeedWithAutoEntryValues, 1, playbackSpeedWithAutoEntryValues.length);

        final int index = Arrays.binarySearch(playbackSpeedEntryValues, String.valueOf(currentSpeed));

        new AlertDialog.Builder(context)
                .setSingleChoiceItems(playbackSpeedEntries, index, (mDialog, mIndex) -> {
                    overrideSpeedBridge(Float.parseFloat(playbackSpeedEntryValues[mIndex] + "f"));
                    mDialog.dismiss();
                })
                .show();
    }

    public static String getFormattedQualityString(@Nullable String prefix) {
        final String qualityString = getQualityString();

        return prefix == null ? qualityString : String.format("%s\u2009•\u2009%s", prefix, qualityString);
    }

    public static String getFormattedSpeedString(@Nullable String prefix) {
        final String speedString = ReVancedUtils.isRightToLeftTextLayout()
                ? "\u2066x\u2069" + currentSpeed
                : currentSpeed + "x";

        return prefix == null ? speedString : String.format("%s\u2009•\u2009%s", prefix, speedString);
    }

    private static void overrideSpeedBridge(final float speed) {
        overrideSpeed(speed);
        userChangedSpeed(speed);
    }

    public static float getCurrentSpeed() {
        return currentSpeed;
    }

    public static boolean isPiPAvailable(boolean original) {
        return original && isPiPAvailable;
    }

    public static int getCurrentQuality(int original) {
        try {
            return Integer.parseInt(currentQuality.split("p")[0]);
        } catch (Exception ignored) {
        }
        return original;
    }

    public static String getQualityString() {
        if (currentQuality.isEmpty()) {
            VideoQualityPatch.overrideQuality(720);
            return qualityAutoString;
        } else if (currentQuality.equals(qualityAutoString)) {
            return qualityAutoString;
        }

        return currentQuality.split("p")[0] + "p";
    }
}
