package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;


public class ExtendedLithoFilterPatch {
    private static final List<String> bufferWhiteList = List.of(
        "metadata",
        "decorated_avatar"
    );
    private static final List<String> generalWhiteList = List.of(
        "library_recent_shelf"
    );

    public static boolean InflatedLithoView(String value, ByteBuffer buffer) {
        if (value == null || value.isEmpty() || generalWhiteList.stream().anyMatch(value::contains)) return false;

        List<byte[]> actionButtonsBlockList = new ArrayList<>();
        List<byte[]> menuItemBlockList = new ArrayList<>();
        List<byte[]> genericBufferList = new ArrayList<>();
        List<byte[]> bufferBlockList = new ArrayList<>();
        List<String> generalBlockList = new ArrayList<>();
        int count = 0;

        if (SettingsEnum.HIDE_LIKE_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("id.video.like.button".getBytes());
            generalBlockList.add("segmented_like_dislike_button");
        }
        if (SettingsEnum.HIDE_DISLIKE_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("id.video.dislike.button".getBytes());
        }
        if (SettingsEnum.HIDE_LIVE_CHAT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_message_bubble_overlap".getBytes());
            actionButtonsBlockList.add("live-chat-item-section".getBytes());
        }
        if (SettingsEnum.HIDE_SHARE_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_share".getBytes());
            actionButtonsBlockList.add("id.video.share.button".getBytes());
        }
        if (SettingsEnum.HIDE_REPORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_flag".getBytes());
        }
        if (SettingsEnum.HIDE_CREATE_SHORT_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_youtube_shorts_plus".getBytes());
            actionButtonsBlockList.add("shorts-creation-on-vod-watch".getBytes());
        }
        if (SettingsEnum.HIDE_THANKS_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_dollar_sign_heart".getBytes());
            actionButtonsBlockList.add("watch-supervod-button".getBytes());
        }
        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean()) {
            generalBlockList.add("download_button");
        }
        if (SettingsEnum.HIDE_CREATE_CLIP_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("yt_outline_scissors".getBytes());
            actionButtonsBlockList.add("create-clip-button".getBytes());
        }
        if (SettingsEnum.HIDE_PLAYLIST_BUTTON.getBoolean()) {
            actionButtonsBlockList.add("id.video.add_to.button".getBytes());
            generalBlockList.add("save_to_playlist_button");
        }

        if (value.contains("|video_action_button")) {
            for (byte[] b: actionButtonsBlockList) {
                int bufferIndex = indexOf(buffer.array(), b);
                if (bufferIndex > 0 && bufferIndex < 3000) count++;
            }
        }

        if (SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean()) {
            genericBufferList.add("mix-watch".getBytes());
            genericBufferList.add("list=".getBytes());
            genericBufferList.add("rellist".getBytes());
        }

        if (SettingsEnum.ADREMOVER_GENERAL_ADS.getBoolean()) {
            genericBufferList.add("Premium".getBytes());
            genericBufferList.add("/promos/".getBytes());
            genericBufferList.add("yt_outline_x_".getBytes());
        }

        if (value.contains("video_with_context") &&
        bufferWhiteList.stream().noneMatch(value::contains)) {
            for (byte[] b: genericBufferList) {
                int bufferIndex = indexOf(buffer.array(), b);
                if (bufferIndex > 0 && bufferIndex < 3000) count++;
            }
        }

        if (SettingsEnum.HIDE_CAPTIONS_MENU.getBoolean()) {
            menuItemBlockList.add("_caption".getBytes());
            menuItemBlockList.add("_closed".getBytes());
        }
        if (SettingsEnum.HIDE_LOOP_MENU.getBoolean()) {
            menuItemBlockList.add("_1_".getBytes());
        }
        if (SettingsEnum.HIDE_AMBIENT_MENU.getBoolean()) {
            menuItemBlockList.add("_screen".getBytes());
        }
        if (SettingsEnum.HIDE_REPORT_MENU.getBoolean()) {
            menuItemBlockList.add("_flag".getBytes());
        }
        if (SettingsEnum.HIDE_HELP_MENU.getBoolean()) {
            menuItemBlockList.add("_question".getBytes());
        }
        if (SettingsEnum.HIDE_MORE_MENU.getBoolean()) {
            menuItemBlockList.add("_info".getBytes());
        }
        if (SettingsEnum.HIDE_SPEED_MENU.getBoolean()) {
            menuItemBlockList.add("_half".getBytes());
        }
        if (SettingsEnum.HIDE_LISTENING_CONTROLS_MENU.getBoolean()) {
            menuItemBlockList.add("_adjust".getBytes());
        }
        if (SettingsEnum.HIDE_AUDIO_TRACK_MENU.getBoolean()) {
            menuItemBlockList.add("_person".getBytes());
        }
        if (SettingsEnum.HIDE_WATCH_IN_VR_MENU.getBoolean()) {
            menuItemBlockList.add("_vr".getBytes());
        }
        if (SettingsEnum.HIDE_NERDS_MENU.getBoolean()) {
            menuItemBlockList.add("_statistic".getBytes());
        }
        if (SettingsEnum.HIDE_YT_MUSIC_MENU.getBoolean()) {
            menuItemBlockList.add("_open".getBytes());
        }

        if (value.contains("overflow_menu_item")) {
            for (byte[] b: menuItemBlockList) {
                int bufferIndex = indexOf(buffer.array(), b);
                if (bufferIndex > 0 && bufferIndex < 3000) count++;
            }
        }

        if (PatchStatus.GeneralAds()) {
            if (SettingsEnum.ADREMOVER_BROWSE_STORE_BUTTON.getBoolean()) {
                bufferBlockList.add("header_store_button".getBytes());
                if (value.contains("|button")) {
                    for (byte[] b: bufferBlockList) {
                        int bufferIndex = indexOf(buffer.array(), b);
                        if (bufferIndex > 0 && bufferIndex < 3000) count++;
                    }
                }
            }

            if (SettingsEnum.ADREMOVER_FEED_SURVEY.getBoolean() &&
                value.contains("slimline_survey")) count++;

            if (SettingsEnum.ADREMOVER_SUGGESTIONS.getBoolean() &&
                value.contains("horizontal_video_shelf") &&
                !value.contains("activeStateScrollSelectionController=com")
            ) count++;

            if (SettingsEnum.ADREMOVER_VIEW_PRODUCTS.getBoolean()) {
                generalBlockList.add("product_item");
                generalBlockList.add("products_in_video");
            }

            if (SettingsEnum.ADREMOVER_CHAPTER_TEASER.getBoolean() &&
                    value.contains("expandable_metadata")) count++;
        }

        if (PatchStatus.ShortsComponent()) {
            if (SettingsEnum.HIDE_SHORTS_SHELF.getBoolean()) {
                generalBlockList.add("inline_shorts");
                generalBlockList.add("reels_player_overlay");
                generalBlockList.add("shorts_grid");
                generalBlockList.add("shorts_shelf");
            }

            if (SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON.getBoolean()) {
                generalBlockList.add("suggested_action");
            }

            if (SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean() &&
                    value.contains("reel_channel_bar")
            ) {
                generalBlockList.add("subscribe_button");
            }

            if (SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON.getBoolean() &&
                    value.contains("reel_channel_bar")
            ) {
                generalBlockList.add("sponsor_button");
            }
        }

        return generalBlockList.stream().anyMatch(value::contains) || count > 0;
    }

    public static int indexOf(byte[] array, byte[] target) {
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
