package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.patches.ads.ByteBufferFilterPatch.indexOf;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;

public class BottomPlayerPatch {

    public static boolean hideActionButtons(Object object, ByteBuffer buffer) {
        String value = object.toString();
        if (!value.contains("ContainerType|ContainerType|video_action_button"))
            return false;

        String convertedBuffer = new String(buffer.array(), StandardCharsets.UTF_8);

        List<String> actionButtonsBlockList = new ArrayList<>();

        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_message_bubble_overlap");
        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_share");
        if (SettingsEnum.HIDE_SHOP_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_bag");
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_flag");
        if (SettingsEnum.HIDE_REMIX_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_youtube_shorts_plus");
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_dollar_sign_heart");
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean())
            actionButtonsBlockList.add("yt_outline_scissors");

        return actionButtonsBlockList.stream().anyMatch(convertedBuffer::contains);
    }
}
