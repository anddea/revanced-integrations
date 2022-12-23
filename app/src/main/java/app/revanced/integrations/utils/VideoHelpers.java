package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.time.Duration;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class VideoHelpers {

    public static void copyVideoUrlToClipboard() {
        generateVideoUrl(false);
    }

    public static void copyVideoUrlWithTimeStampToClipboard() {
        generateVideoUrl(true);
    }

    public static void copyTimeStampToClipboard() {
        generateTimeStamp();
    }

    private static void generateVideoUrl(boolean appendTimeStamp) {
        try {
            String videoId = VideoInformation.getCurrentVideoId();
            if (videoId == null || videoId.isEmpty()) return;

            String videoUrl = String.format("https://youtu.be/%s", videoId);
            if (appendTimeStamp) {
                long videoTime = PlayerController.getLastKnownVideoTime() < 0L ? VideoInformation.getCurrentVideoTime() : PlayerController.lastKnownVideoTime;
                videoUrl += String.format("?t=%s", (videoTime / 1000));
            }

            setClipboard(ReVancedUtils.getContext(), videoUrl);

            Toast.makeText(ReVancedUtils.getContext(), str("share_copy_url_success"), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Couldn't generate video url", ex);
        }
    }

    private static void generateTimeStamp() {
        try {
            long videoTime = PlayerController.getLastKnownVideoTime() < 0L ? VideoInformation.getCurrentVideoTime() : PlayerController.lastKnownVideoTime;

            Duration duration = Duration.ofMillis(videoTime);

            long h = duration.toHours();
            long m = duration.toMinutes() % 60;
            long s = duration.getSeconds() % 60;

            @SuppressLint("DefaultLocale") String timeStamp = h > 0 ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);

            setClipboard(ReVancedUtils.getContext(), timeStamp);

            Toast.makeText(ReVancedUtils.getContext(), str("revanced_copytimestamp_success") + ": " + timeStamp, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Couldn't generate video url", ex);
        }
    }

    private static void setClipboard(Context context, String text) {
         android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
         android.content.ClipData clip = android.content.ClipData.newPlainText("link", text);
         clipboard.setPrimaryClip(clip);
    }
}
