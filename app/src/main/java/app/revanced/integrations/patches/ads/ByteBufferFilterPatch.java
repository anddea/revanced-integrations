package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;


public class ByteBufferFilterPatch {
    private static final List<String> generalWhiteList = List.of(
            "avatar",
            "comment_thread",
            "creation_sheet_menu",
            "metadata",
            "thumbnail",
            "|comment.",
            "-button",
            "-count",
            "-space"
    );
    private static final List<String> mixBufferBlockList = List.of(
            "&list="
    );
    private static int count;

    public static boolean filters(String value, ByteBuffer buffer) {
        if (value == null || value.isEmpty() || generalWhiteList.stream().anyMatch(value::contains))
            return false;

        if (mixBufferBlockList.stream().anyMatch(new String(buffer.array(), StandardCharsets.UTF_8)::contains))
            return hideMixPlaylists(buffer);

        count = 0;

        hideFlyoutPanels(value, buffer);
        hideGeneralAds(value, buffer);
        hideShortsComponent(value);
        hideSuggestedActions(value);

        return count > 0;
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

        if (value.contains("post_base_wrapper")) {
            if (SettingsEnum.ADREMOVER_COMMUNITY_POSTS_HOME.getBoolean() &&
                    value.contains("horizontalCollectionSwipeProtector=null")) count++;
            else if (SettingsEnum.ADREMOVER_COMMUNITY_POSTS_SUBSCRIPTIONS.getBoolean() &&
                    value.contains("heightConstraint=null")) count++;
        }

        if (SettingsEnum.ADREMOVER_BROWSE_STORE_BUTTON.getBoolean() &&
                value.contains("|button")
        ) {
            int bufferIndex = indexOf(buffer.array(), "header_store_button".getBytes());
            if (bufferIndex > 0 && bufferIndex < 2000) count++;
        }

        if (SettingsEnum.ADREMOVER_SUGGESTIONS.getBoolean() &&
                value.contains("horizontal_video_shelf") &&
                !value.contains("activeStateScrollSelectionController=com")
        ) count++;

        if (SettingsEnum.ADREMOVER_FEED_SURVEY.getBoolean() &&
                value.contains("_survey")) count++;
    }

    private static boolean hideMixPlaylists(ByteBuffer buffer) {
        final List<String> mixBufferWhiteList = List.of("description", "share", "|ContainerType|ContainerType|");
        final List<String> musicBufferList = List.of("YouTube Music");
        final List<String> imageBufferList = List.of("ggpht.com");

        if (!SettingsEnum.HIDE_MIX_PLAYLISTS.getBoolean() ||
                mixBufferWhiteList.stream().anyMatch(new String(buffer.array(), StandardCharsets.UTF_8)::contains))
            return false;

        if (musicBufferList.stream().anyMatch(new String(buffer.array(), StandardCharsets.UTF_8)::contains))
            return true;

        return imageBufferList.stream().noneMatch(new String(buffer.array(), StandardCharsets.UTF_8)::contains);
    }

    private static void hideShortsComponent(String value) {
        if (!PatchStatus.ShortsComponent()) return;

        List<String> blockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SHORTS_PLAYER_THANKS_BUTTON.getBoolean())
            blockList.add("suggested_action");

        if (value.contains("reel_channel_bar")) {
            if (SettingsEnum.HIDE_SHORTS_PLAYER_SUBSCRIPTIONS_BUTTON.getBoolean())
                blockList.add("subscribe_button");

            if (SettingsEnum.HIDE_SHORTS_PLAYER_JOIN_BUTTON.getBoolean())
                blockList.add("sponsor_button");
        }

        if (blockList.stream().anyMatch(value::contains)) count++;
    }

    private static void hideSuggestedActions(String value) {
        if (!PatchStatus.SuggestedActions()) return;

        List<String> blockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean() &&
                !PlayerType.getCurrent().isNoneOrHidden())
            blockList.add("suggested_action");

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
