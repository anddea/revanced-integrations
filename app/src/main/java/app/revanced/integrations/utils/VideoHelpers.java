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
        String speed = currentSpeed + "x";
        if (prefix == null) return speed;

        String middleSeparatorString = "\u2009" + "•" + "\u2009";  // u2009 = "half space" character

        return prefix +
                middleSeparatorString +
                speed;
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
}
