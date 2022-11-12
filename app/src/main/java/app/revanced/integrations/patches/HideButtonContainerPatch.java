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
        List<byte[]> actionButtonsBlockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_share".getBytes());
        }
        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_message_bubble_overlap".getBytes());
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_flag".getBytes());
        }
        if (SettingsEnum.HIDE_CREATE_SHORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_youtube_shorts_plus".getBytes());
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_dollar_sign_heart".getBytes());
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_scissors".getBytes());
        }

        if (containsAny(inflatedTemplate, "video_action_button")) {
            for (byte[] b: actionButtonsBlockList) {
                int bufferIndex = indexOf(buffer.array(), b);
                if (bufferIndex > 0 && bufferIndex < 2000) return true;
            }
        }

        if (SettingsEnum.HIDE_LIKE_BUTTON.getBoolean() &&
        containsAny(inflatedTemplate, "ContainerType|ContainerType|like_button")) {
            return true;
        }

        if (SettingsEnum.HIDE_DISLIKE_BUTTON.getBoolean() &&
        (containsAny(inflatedTemplate, "ContainerType|ContainerType|dislike_button") ||
        containsAny(inflatedTemplate, "ContainerType|ContainerType|segmented_like_dislike_button"))) {
            return true;
        }

        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean() &&
            containsAny(inflatedTemplate, "download_button")) {
            return true;
        }

        if (SettingsEnum.HIDE_PLAYLIST_BUTTON.getBoolean() &&
            containsAny(inflatedTemplate, "save_to_playlist_button")) {
            return true;
        }

        List<byte[]> genericBufferList = new ArrayList<>();

        if (!SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean()) {
            genericBufferList.add("for you".getBytes());
            genericBufferList.add("mix-watch".getBytes());
        }

        if (containsAny(inflatedTemplate, "related_video_with_context", "search_video_with_context")) {
            for (byte[] b: genericBufferList) {
                if (indexOf(buffer.array(), b) > 0) return true;
            }
        }

        List<String> newBlockList = new ArrayList<>();

        if (SettingsEnum.VIEW_PRODUCTS.getBoolean()) {
            newBlockList.add("product_item");
            newBlockList.add("products_in_video");
        }

        if (SettingsEnum.TIMED_REACTIONS.getBoolean()) {
            newBlockList.add("emoji_control_panel");
            newBlockList.add("timed_reaction_player_animation");
            newBlockList.add("timed_reaction_live_player_overlay");
        }

        if (SettingsEnum.CUTOUT_GLITCH.getBoolean()) {
            newBlockList.add("endorsement_header_footer");
        }

        if (SettingsEnum.EXPANSION_CARD.getBoolean()) {
            newBlockList.add("expandable_metadata");
        }

        if (SettingsEnum.WEB_SEARCH_PANELS.getBoolean()) {
            newBlockList.add("web_link_panel");
        }

        if (!SettingsEnum.SHORTS_PLAYER_THANKS.getBoolean()) {
            newBlockList.add("suggested_action");
        }

        if (anyMatch(newBlockList, inflatedTemplate::contains)) {
            return true;
        }

        return false;
    }

    private static boolean containsAny(String value, String... targets) {
        for (String string : targets)
            if (value.contains(string)) return true;
        return false;
    }

    private static <T> boolean anyMatch(List<T> value, APredicate<? super T> predicate) {
        for (T t : value) {
            if (predicate.test(t)) return true;
        }
        return false;
    }

    public static int indexOf(byte[] array, byte[] target) {
        if (target.length == 0) {
            return 0;
        }

        for (int i = 0; i < array.length - target.length + 1; i++) {
            boolean targetFound = true;
            for (int j = 0; j < target.length; j++) {
                if (array[i+j] != target[j]) {
                    targetFound = false;
                    break;
                }
            }
            if (targetFound) {
                return i;
            }
        }
        return -1;
    }

    @FunctionalInterface
    public interface APredicate<T> {
        boolean test(T t);
    }
}
