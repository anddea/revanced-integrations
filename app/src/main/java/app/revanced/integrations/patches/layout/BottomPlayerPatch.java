package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.patches.ads.ByteBufferFilterPatch.indexOf;

import java.nio.ByteBuffer;

import app.revanced.integrations.settings.SettingsEnum;

public class BottomPlayerPatch {

    public static boolean hideActionButtons(Object object, ByteBuffer buffer) {
        String value = object.toString();
        if (value == null || value.isEmpty() || !value.contains("video_action_button"))
            return false;

        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_message_bubble_overlap".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_share".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_SHOP_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_bag".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_flag".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_REMIX_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_youtube_shorts_plus".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_dollar_sign_heart".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_scissors".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2500) return true;
        }

        return false;
    }
}
