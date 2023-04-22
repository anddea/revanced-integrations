package app.revanced.integrations.patches.layout;

import android.view.View;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;

public class FullscreenPatch {
    private static final List<String> generalWhiteList = List.of(
            "FEhistory",
            "avatar",
            "channel_bar",
            "comment_thread",
            "creation_sheet_menu",
            "thumbnail",
            "home_video_with_context",
            "related_video_with_context",
            "search_video_with_context",
            "-count",
            "-space",
            "-button"
    );

    public static boolean hideQuickActionButtons(Object object, ByteBuffer buffer) {
        String value = object.toString();
        if (generalWhiteList.stream().anyMatch(value::contains) || !value.contains("quick_actions") || PlayerType.getCurrent() != PlayerType.WATCH_WHILE_FULLSCREEN || SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean()) return false;
        if (SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean() || SettingsEnum.HIDE_QUICK_ACTIONS.getBoolean()) return true;

        List<String> actionButtonsBytebufferBlockList = new ArrayList<>();
        List<String> actionButtonsObjectBlockList = new ArrayList<>();

        if (SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON.getBoolean()) {
            actionButtonsObjectBlockList.add("|like_button");
            actionButtonsBytebufferBlockList.add("_thumb_up");
        }
        if (SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON.getBoolean()) {
            actionButtonsObjectBlockList.add("dislike_button");
            actionButtonsBytebufferBlockList.add("_thumb_down");
        }
        if (SettingsEnum.HIDE_QUICK_ACTIONS_COMMENT_BUTTON.getBoolean())
            actionButtonsBytebufferBlockList.add("_message_bubble_right");
        if (SettingsEnum.HIDE_QUICK_ACTIONS_LIVE_CHAT_BUTTON.getBoolean())
            actionButtonsBytebufferBlockList.add("_message_bubble_overlap");
        if (SettingsEnum.HIDE_QUICK_ACTIONS_PLAYLIST_BUTTON.getBoolean())
            actionButtonsBytebufferBlockList.add("yt_outline_library_add");
        if (SettingsEnum.HIDE_QUICK_ACTIONS_SHARE_BUTTON.getBoolean())
            actionButtonsBytebufferBlockList.add("yt_outline_share");
        if (SettingsEnum.HIDE_QUICK_ACTIONS_MORE_BUTTON.getBoolean())
            actionButtonsBytebufferBlockList.add("yt_outline_overflow_horizontal");
        if (SettingsEnum.HIDE_QUICK_ACTIONS_RELATED_VIDEO.getBoolean())
            actionButtonsObjectBlockList.add("fullscreen_related_videos");

        String convertedBuffer = new String(buffer.array(), StandardCharsets.UTF_8);

        return actionButtonsBytebufferBlockList.stream().anyMatch(convertedBuffer::contains)
                || actionButtonsObjectBlockList.stream().anyMatch(value::contains);
    }

    public static void hideFullscreenButtonContainer(View view) {
        if (SettingsEnum.HIDE_FULLSCREEN_BUTTON_CONTAINER.getBoolean() ||
                SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean())
            AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static int hideFullscreenPanels() {
        return SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean() ? 8 : 0;
    }

    public static void hideFullscreenPanels(View view) {
        if (SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

    public static boolean showFullscreenTitle() {
        return SettingsEnum.SHOW_FULLSCREEN_TITLE.getBoolean() || !SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean();
    }

    public static boolean hideAutoPlayPreview() {
        return SettingsEnum.HIDE_AUTOPLAY_PREVIEW.getBoolean() || SettingsEnum.HIDE_AUTOPLAY_BUTTON.getBoolean();
    }

    public static boolean hideEndScreenOverlay() {
        return SettingsEnum.HIDE_ENDSCREEN_OVERLAY.getBoolean();
    }

    public static boolean hideFilmstripOverlay() {
        return SettingsEnum.HIDE_FILMSTRIP_OVERLAY.getBoolean();
    }

    public static boolean disableLandScapeMode(boolean original) {
        return SettingsEnum.DISABLE_LANDSCAPE_MODE.getBoolean() || original;
    }

    public static boolean disableSeekVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_SEEK.getBoolean();
    }

    public static boolean disableScrubbingVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_SCRUBBING.getBoolean();
    }

    public static boolean disableChapterVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_CHAPTERS.getBoolean();
    }

    public static boolean disableZoomVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_ZOOM.getBoolean();
    }
}
