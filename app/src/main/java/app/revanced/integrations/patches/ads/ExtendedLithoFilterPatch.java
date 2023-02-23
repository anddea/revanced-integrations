package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;


public class ExtendedLithoFilterPatch {
    private static int count;
    private static final List<String> bufferWhiteList = List.of(
        "metadata",
        "decorated_avatar"
    );

    public static boolean InflatedLithoView(String value, ByteBuffer buffer) {
        if (value == null || value.isEmpty()) return false;

        count = 0;

        hideActionBar(value, buffer);
        hideFlyoutPanels(value, buffer);
        hideGeneralAds(value, buffer);
        hideMixPlaylist(value, buffer);
        hideShortsComponent(value);

        return count > 0;
    }

    private static void hideActionBar(String value, ByteBuffer buffer) {
        List<String> blockList = new ArrayList<>();
        List<byte[]> byteBufferList = new ArrayList<>();

        if (SettingsEnum.HIDE_LIKE_BUTTON.getBoolean()) {
            byteBufferList.add("id.video.like.button".getBytes());
            blockList.add("segmented_like_dislike_button");
        }
        if (SettingsEnum.HIDE_DISLIKE_BUTTON.getBoolean()) {
            byteBufferList.add("id.video.dislike.button".getBytes());
        }
        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_message_bubble_overlap".getBytes());
            byteBufferList.add("live-chat-item-section".getBytes());
        }
        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_share".getBytes());
            byteBufferList.add("id.video.share.button".getBytes());
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_flag".getBytes());
        }
        if (SettingsEnum.HIDE_CREATE_SHORT_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_youtube_shorts_plus".getBytes());
            byteBufferList.add("shorts-creation-on-vod-watch".getBytes());
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_dollar_sign_heart".getBytes());
            byteBufferList.add("watch-supervod-button".getBytes());
        }
        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean()) {
            blockList.add("download_button");
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            byteBufferList.add("yt_outline_scissors".getBytes());
            byteBufferList.add("create-clip-button".getBytes());
        }
        if (SettingsEnum.HIDE_PLAYLIST_BUTTON.getBoolean()) {
            byteBufferList.add("id.video.add_to.button".getBytes());
            blockList.add("save_to_playlist_button");
        }

        if (value.contains("|video_action_button"))
            indexOfBuffer(byteBufferList, buffer);

        if (blockList.stream().anyMatch(value::contains)) count++;
    }

    private static void hideFlyoutPanels(String value, ByteBuffer buffer) {
        List<byte[]> byteBufferList = new ArrayList<>();

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

        if (value.contains("overflow_menu_item"))
            indexOfBuffer(byteBufferList, buffer);
    }

    private static void hideGeneralAds(String value, ByteBuffer buffer) {
        if (!PatchStatus.GeneralAds()) return;

        List<String> blockList = new ArrayList<>();
        List<byte[]> byteBufferList = new ArrayList<>();
        List<byte[]> genericBufferList = new ArrayList<>();

        if (SettingsEnum.ADREMOVER_GENERAL_ADS.getBoolean()) {
            byteBufferList.add("Premium".getBytes());
            byteBufferList.add("/promos/".getBytes());
            if (value.contains("home_video_with_context") &&
                    bufferWhiteList.stream().noneMatch(value::contains))
                indexOfBuffer(byteBufferList, buffer);
        }

        if (SettingsEnum.ADREMOVER_BROWSE_STORE_BUTTON.getBoolean()) {
            genericBufferList.add("header_store_button".getBytes());
            if (value.contains("|button"))
                indexOfBuffer(genericBufferList, buffer);
        }

        if (SettingsEnum.ADREMOVER_FEED_SURVEY.getBoolean() &&
                value.contains("slimline_survey")) count++;

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
        if (!SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean()) return;

        List<byte[]> byteBufferList = new ArrayList<>();

        byteBufferList.add("for you".getBytes());
        byteBufferList.add("mix-watch".getBytes());
        byteBufferList.add("list=".getBytes());
        byteBufferList.add("rellist".getBytes());

        if (value.contains("video_with_context") &&
                bufferWhiteList.stream().noneMatch(value::contains))
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

        if (SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean() &&
                value.contains("reel_channel_bar")
        ) {
            blockList.add("subscribe_button");
        }

        if (SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON.getBoolean() &&
                value.contains("reel_channel_bar")
        ) {
            blockList.add("sponsor_button");
        }

        if (blockList.stream().anyMatch(value::contains)) count++;
    }

    private static void indexOfBuffer(List<byte[]> bufferList, ByteBuffer buffer) {
        for (byte[] b: bufferList) {
            int bufferIndex = indexOf(buffer.array(), b);
            if (bufferIndex > 0 && bufferIndex < 4000) {
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
