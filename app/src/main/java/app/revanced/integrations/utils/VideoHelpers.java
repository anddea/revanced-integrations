package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.time.Duration;

import app.revanced.integrations.patches.video.VideoInformation;

public class VideoHelpers {

    public static void copyVideoUrlToClipboard(Context context) {
        generateVideoUrl(context, false);
    }

    public static void copyVideoUrlWithTimeStampToClipboard(Context context) {
        generateVideoUrl(context, true);
    }

    public static void copyTimeStampToClipboard(Context context) {
        generateTimeStamp(context);
    }

    private static void generateVideoUrl(Context context, boolean appendTimeStamp) {
        try {
            String videoId = VideoInformation.getCurrentVideoId();
            if (videoId == null || videoId.isEmpty()) return;

            String videoUrl = String.format("https://youtu.be/%s", videoId);
            if (appendTimeStamp) {
                long videoTime = VideoInformation.getCurrentVideoTime();
                videoUrl += String.format("?t=%s", (videoTime / 1000));
            }

            setClipboard(context, videoUrl);

            Toast.makeText(context, str("share_copy_url_success"), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            LogHelper.printException(VideoHelpers.class, "Couldn't generate video url", ex);
        }
    }

    private static void generateTimeStamp(Context context) {
        try {
            long videoTime = VideoInformation.getCurrentVideoTime();

            Duration duration = Duration.ofMillis(videoTime);

            long h = duration.toHours();
            long m = duration.toMinutes() % 60;
            long s = duration.getSeconds() % 60;

            @SuppressLint("DefaultLocale") String timeStamp = h > 0 ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);

            setClipboard(context, timeStamp);

            Toast.makeText(context, str("revanced_copytimestamp_success") + ": " + timeStamp, Toast.LENGTH_SHORT).show();
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
