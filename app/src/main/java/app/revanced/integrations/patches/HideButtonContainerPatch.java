package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HideButtonContainerPatch {
    //Used by app.revanced.patches.youtube.ad.general.patch.GeneralAdsRemovalPatch
    public static boolean ContainerLithoView(String inflatedTemplate, ByteBuffer buffer) {
        return InflatedLithoView(inflatedTemplate, buffer);
    }

    private static boolean InflatedLithoView(String inflatedTemplate, ByteBuffer buffer) {
        String readableBuffer = new String(buffer.array(), StandardCharsets.UTF_8);

        //Action Bar Buttons
        List<String> actionButtonsBlockList = new ArrayList<>();

        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_message_bubble_overlap");
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_flag");
        }
        if (SettingsEnum.HIDE_CREATE_SHORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_youtube_shorts_plus");
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_dollar_sign_heart");
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_scissors");
        }

        if (!SettingsEnum.IS_NEWLAYOUT.getBoolean() && containsAny(inflatedTemplate, "ContainerType|ContainerType|video_action_button") &&
            anyMatch(actionButtonsBlockList, readableBuffer::contains)) {
            return true;
        }

        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean() &&
            containsAny(inflatedTemplate, "ContainerType|ContainerType|download_button")) {
            return true;
        }

        //Comments Teasers
        List<String> commentsTeaserBlockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SPOILER_COMMENT.getBoolean()) {
            commentsTeaserBlockList.add("ContainerType|comments_entry_point_teaser");
        }
        if (SettingsEnum.HIDE_EXTERNAL_COMMENT_BOX.getBoolean()) {
            commentsTeaserBlockList.add("ContainerType|comments_entry_point_simplebox");
        }

        if (anyMatch(commentsTeaserBlockList, inflatedTemplate::contains)) {
            return true;
        }

        return false;
    }

    private static boolean containsAny(String value, String... targets) {
        for (String string : targets)
            if (value.contains(string)) return true;
        return false;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            builder.append(String.format("%02x", b));
        return builder.toString();
    }

    private static <T> boolean anyMatch(List<T> value, APredicate<? super T> predicate) {
        for (T t : value) {
            if (predicate.test(t)) return true;
        }
        return false;
    }

    @FunctionalInterface
    public interface APredicate<T> {
        boolean test(T t);
    }
}
