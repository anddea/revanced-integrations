package app.revanced.integrations.patches.layout;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;

public class BottomPlayerPatch {
    private static final List<String> whiteList = List.of(
            "FEhistory",
            "avatar",
            "channel_bar",
            "comment_thread",
            "creation_sheet_menu",
            "like_button",
            "metadata",
            "thumbnail",
            "home_video_with_context",
            "related_video_with_context",
            "search_video_with_context",
            "menu",
            "-count",
            "-space",
            "-button"
    );


    /**
     * Injection point.
     */
    public static boolean hideActionButton(Object object, ByteBuffer buffer) {
        String value = object.toString();

        if (whiteList.stream().anyMatch(value::contains)
                || buffer == null
                || (!value.contains("ContainerType|video_action_button") && !value.contains("|button.eml|")))
            return false;

        return hideActionButton(new String(buffer.array(), StandardCharsets.UTF_8));
    }

    private static boolean hideActionButton(String charset) {
        List<String> blocklist = new ArrayList<>();

        for (ActionBarButton button : ActionBarButton.values())
            if (button.settings.getBoolean())
                blocklist.add(button.filter);

        return blocklist.stream().anyMatch(charset::contains);
    }

    private enum ActionBarButton {
        CLIP(SettingsEnum.HIDE_CREATE_CLIP_BUTTON, "yt_outline_scissors"),
        LIVECHAT(SettingsEnum.HIDE_LIVE_CHAT_BUTTON, "yt_outline_message_bubble_overlap"),
        REMIX(SettingsEnum.HIDE_REMIX_BUTTON, "yt_outline_youtube_shorts_plus"),
        REPORT(SettingsEnum.HIDE_REPORT_BUTTON, "yt_outline_flag"),
        SHARE(SettingsEnum.HIDE_SHARE_BUTTON, "yt_outline_share"),
        SHOP(SettingsEnum.HIDE_SHOP_BUTTON, "yt_outline_bag"),
        THANKS(SettingsEnum.HIDE_THANKS_BUTTON, "yt_outline_dollar_sign_heart");

        private final SettingsEnum settings;
        private final String filter;

        ActionBarButton(SettingsEnum settings, String filter) {
            this.settings = settings;
            this.filter = filter;
        }
    }
}