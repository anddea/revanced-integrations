package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;


public class ByteBufferFilterPatch {
    private static final List<String> blockList = List.of(
            "overflow_menu_item"
    );
    private static final List<String> whiteList = List.of(
            "comment_thread", // skip blocking anything in the comments
            "|comment.", // skip blocking anything in the comments replies
            "menu",
            "-button",
            "-count"
    );
    private static int count;

    public static boolean filters(String value, ByteBuffer buffer) {
        if (value == null || value.isEmpty()) return false;
        if ((whiteList.stream().anyMatch(value::contains) && blockList.stream().noneMatch(value::contains))) return false;
        if (value.contains("ScrollableContainerType|ContainerType|ContainerType|video_action_button"))
            return hideActionButton(buffer);
        count = 0;

        hideActionBar(value);
        hideFlyoutPanels(value, buffer);
        hideGeneralAds(value, buffer);
        hideMixPlaylist(value, buffer);
        hideShortsComponent(value);

        return count > 0;
    }

    private static void hideActionBar(String value) {
        if (!value.contains("video_action_bar")) return;

        List<String> rawStringList = new ArrayList<>();

        if (SettingsEnum.HIDE_LIKE_BUTTON.getBoolean()) {
            rawStringList.add("|like_button");
        }
        if (SettingsEnum.HIDE_DISLIKE_BUTTON.getBoolean()) {
            rawStringList.add("|dislike_button");
            rawStringList.add("|segmented_like_dislike_button");
        }
        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean()) {
            rawStringList.add("download_button");
        }
        if (SettingsEnum.HIDE_PLAYLIST_BUTTON.getBoolean()) {
            rawStringList.add("save_to_playlist_button");
        }
        if (rawStringList.stream().anyMatch(value::contains)) count++;
    }

    private static boolean hideActionButton(ByteBuffer buffer) {
        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_share".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) return true;
        }
        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_message_bubble_overlap".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) return true;
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_flag".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) return true;
        }
        if (SettingsEnum.HIDE_CREATE_SHORT_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_youtube_shorts_plus".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) return true;
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_dollar_sign_heart".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) return true;
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            int bufferIndex = indexOf(buffer.array(), "yt_outline_scissors".getBytes());
            return bufferIndex > 0 && bufferIndex < 2000;
        }
        return false;
    }

    private static void hideFlyoutPanels(String value, ByteBuffer buffer) {
        List<byte[]> byteBufferList = new ArrayList<>();
        if (!value.contains("overflow_menu_item")) return;

        if (SettingsEnum.HIDE_CAPTIONS_MENU.getBoolean()) {
            byteBufferList.add("_caption".getBytes());
            byteBufferList.add("_closed".getBytes());
        }
        if (SettingsEnum.HIDE_LOOP_MENU.getBoolean()) {
            byteBufferList.add("_1_".getBytes());
        }
        if (SettingsEnum.HIDE_AMBIENT_MENU.getBoolean()) {
            byteBufferList.add("_screen".getBytes());
        }
        if (SettingsEnum.HIDE_REPORT_MENU.getBoolean()) {
            byteBufferList.add("_flag".getBytes());
        }
        if (SettingsEnum.HIDE_HELP_MENU.getBoolean()) {
            byteBufferList.add("_question".getBytes());
        }
        if (SettingsEnum.HIDE_MORE_MENU.getBoolean()) {
            byteBufferList.add("_info".getBytes());
        }
        if (SettingsEnum.HIDE_SPEED_MENU.getBoolean()) {
            byteBufferList.add("_half".getBytes());
        }
        if (SettingsEnum.HIDE_LISTENING_CONTROLS_MENU.getBoolean()) {
            byteBufferList.add("_adjust".getBytes());
        }
        if (SettingsEnum.HIDE_AUDIO_TRACK_MENU.getBoolean()) {
            byteBufferList.add("_person".getBytes());
        }
        if (SettingsEnum.HIDE_WATCH_IN_VR_MENU.getBoolean()) {
            byteBufferList.add("_vr".getBytes());
        }
        if (SettingsEnum.HIDE_NERDS_MENU.getBoolean()) {
            byteBufferList.add("_statistic".getBytes());
        }
        if (SettingsEnum.HIDE_YT_MUSIC_MENU.getBoolean()) {
            byteBufferList.add("_open".getBytes());
        }

        indexOfBuffer(byteBufferList, buffer);
    }

    private static void hideGeneralAds(String value, ByteBuffer buffer) {
        if (!PatchStatus.GeneralAds()) return;

        List<String> blockList = new ArrayList<>();
        List<byte[]> genericBufferList = new ArrayList<>();

        if (value.contains("post_base_wrapper")) {
            if (SettingsEnum.ADREMOVER_COMMUNITY_POSTS_HOME.getBoolean() &&
                    !value.contains("heightConstraint=null") &&
                    value.contains("horizontalCollectionSwipeProtector=null")) count++;
            else if (SettingsEnum.ADREMOVER_COMMUNITY_POSTS_SUBSCRIPTIONS.getBoolean() &&
                    value.contains("heightConstraint=null")) count++;
        }

        if (SettingsEnum.ADREMOVER_BROWSE_STORE_BUTTON.getBoolean() &&
                value.contains("|button")
        ) {
            genericBufferList.add("header_store_button".getBytes());
            indexOfBuffer(genericBufferList, buffer);
        }

        if (SettingsEnum.ADREMOVER_FEED_SURVEY.getBoolean() &&
                value.contains("_survey")) count++;

        if (SettingsEnum.ADREMOVER_SUGGESTIONS.getBoolean() &&
                value.contains("horizontal_video_shelf") &&
                !value.contains("activeStateScrollSelectionController=com")
        ) count++;

        if (SettingsEnum.ADREMOVER_VIEW_PRODUCTS.getBoolean()) {
            blockList.add("product_item");
            blockList.add("products_in_video");
        }

        if (SettingsEnum.ADREMOVER_CHAPTER_TEASER.getBoolean() &&
                value.contains("expandable_metadata")) count++;

        if (blockList.stream().anyMatch(value::contains)) count++;
    }

    private static void hideMixPlaylist(String value, ByteBuffer buffer) {
        if (!SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean() ||
                !value.contains("video_with_context.") ||
                value.contains("|ContainerType|ContainerType|"))
            return;

        List<byte[]> byteBufferList = new ArrayList<>();

        byteBufferList.add("for you".getBytes());
        byteBufferList.add("mix-watch".getBytes());
        byteBufferList.add("list=".getBytes());
        byteBufferList.add("rellist".getBytes());

        // home feed & search result
        if ((value.contains("home_video_with_context.") ||
                value.contains("search_video_with_context.")) &&
                !value.contains("|ContainerType|ContainerType|"))
            indexOfBuffer(byteBufferList, buffer);

        // related video
        if (value.contains("related_video_with_context.") &&
                !value.contains("|ContainerType|related_video_with_context_inner"))
            indexOfBuffer(byteBufferList, buffer);
    }

    private static void hideShortsComponent(String value) {
        if (!PatchStatus.ShortsComponent()) return;

        List<String> blockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SHORTS_SHELF.getBoolean()) {
            blockList.add("inline_shorts");
            blockList.add("reels_player_overlay");
            blockList.add("shorts_grid");
            blockList.add("shorts_shelf");
        }

        if (SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON.getBoolean()) {
            blockList.add("suggested_action");
        }

        if (blockList.stream().anyMatch(value::contains)) count++;
        blockList.clear();

        if (!value.contains("reel_channel_bar")) return;

        if (SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean())
            blockList.add("subscribe_button");

        if (SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON.getBoolean())
            blockList.add("sponsor_button");

        if (blockList.stream().anyMatch(value::contains)) count++;
    }

    private static void indexOfBuffer(List<byte[]> bufferList, ByteBuffer buffer) {
        for (byte[] b: bufferList) {
            int bufferIndex = indexOf(buffer.array(), b);
            if (bufferIndex > 0 && bufferIndex < 2000) {
                count++;
                break;
            }
        }
    }

    private static int indexOf(byte[] array, byte[] target) {
        if (target.length == 0) return 0;

        for (int i = 0; i < array.length - target.length + 1; i++) {
            boolean targetFound = true;
            for (int j = 0; j < target.length; j++) {
                if (array[i+j] != target[j]) {
                    targetFound = false;
                    break;
                }
            }
            if (targetFound) return i;
        }
        return -1;
    }
}
