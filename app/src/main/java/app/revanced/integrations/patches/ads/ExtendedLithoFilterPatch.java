package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.patches.utils.PatchStatus;


public class ExtendedLithoFilterPatch {
    private static final List<String> excludedBlockingList = List.of(
        "home_video_with_context",
        "related_video_with_context",
        "search_video_with_context"
    );
    private static final List<String> whiteList = List.of(
        "|comment.",
        "comment_thread",
        "library_recent_shelf"
    );
    private static final int excludedBlockingListSize = excludedBlockingList.size() - 1;

    public static boolean InflatedLithoView(String value, ByteBuffer buffer) {
        if (value == null || value.isEmpty() || whiteList.stream().anyMatch(value::contains)) return false;

        List<byte[]> actionButtonsBlockList = new ArrayList<>();
        List<byte[]> menuItemBlockList = new ArrayList<>();
        List<byte[]> genericBufferList = new ArrayList<>();
        List<String> generalBlockList = new ArrayList<>();

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

        if (value.contains("CellType|ScrollableContainerType|ContainerType|ContainerType|video_action_button")) {
            for (byte[] b: actionButtonsBlockList) {
                int bufferIndex = indexOf(buffer.array(), b);
                if (bufferIndex > 0 && bufferIndex < 2000) return true;
            }
        }

        if (SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean()) {
            genericBufferList.add("mix-watch".getBytes());    
            genericBufferList.add("&list=".getBytes());
            genericBufferList.add("rellist".getBytes());
        }

        if (containsAnyString(value)) {
            for (byte[] b: genericBufferList) {
                if (indexOf(buffer.array(), b) > 0) return true;
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
                if (bufferIndex > 0 && bufferIndex < 2000) return true;
            }
        }

        if (SettingsEnum.ADREMOVER_FEED_SURVEY.getBoolean() && value.contains("slimline_survey"))
            return true;

        if (SettingsEnum.HIDE_LIKE_BUTTON.getBoolean()) {
            generalBlockList.add("ContainerType|ContainerType|like_button");
        }

        if (SettingsEnum.HIDE_DISLIKE_BUTTON.getBoolean()) {
            generalBlockList.add("ContainerType|ContainerType|dislike_button");
            generalBlockList.add("ContainerType|ContainerType|segmented_like_dislike_button");
        }

        if (SettingsEnum.HIDE_DOWNLOAD_BUTTON.getBoolean()) {
            generalBlockList.add("download_button");
        }

        if (SettingsEnum.HIDE_PLAYLIST_BUTTON.getBoolean()) {
            generalBlockList.add("save_to_playlist_button");
        }

        if (PatchStatus.GeneralAds()) {
            if (SettingsEnum.ADREMOVER_VIEW_PRODUCTS.getBoolean()) {
                generalBlockList.add("product_item");
                generalBlockList.add("products_in_video");
            }

            if (SettingsEnum.ADREMOVER_TIMED_REACTIONS.getBoolean()) {
                generalBlockList.add("emoji_control_panel");
                generalBlockList.add("timed_reaction_player_animation");
                generalBlockList.add("timed_reaction_live_player_overlay");
            }

            if (SettingsEnum.ADREMOVER_CHAPTER_TEASER.getBoolean()) {
                generalBlockList.add("expandable_metadata");
            }

            if (SettingsEnum.ADREMOVER_WEB_SEARCH_PANEL.getBoolean()) {
                generalBlockList.add("web_link_panel");
            }

            if (SettingsEnum.ADREMOVER_GRAY_SEPARATOR.getBoolean()) {
                generalBlockList.add("endorsement_header_footer");
            }
        }

        if (PatchStatus.ShortsComponent()) {
            if (SettingsEnum.HIDE_SHORTS_SHELF.getBoolean()) {
                generalBlockList.add("inline_shorts");
                generalBlockList.add("reels_player_overlay");
                generalBlockList.add("shelf_header");
                generalBlockList.add("shorts_grid");
                generalBlockList.add("shorts_shelf");
            }

            if (SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON.getBoolean()) {
                generalBlockList.add("suggested_action");
            }
        }

        if (generalBlockList.stream().anyMatch(value::contains)) return true;

        return false;
    }

    private static boolean containsAnyString(String value) {
        for (int i = 1; i <= ExtendedLithoFilterPatch.excludedBlockingListSize; i++) {
            if (value.contains(ExtendedLithoFilterPatch.excludedBlockingList.get(i))) return true;
        }
        return false;
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
