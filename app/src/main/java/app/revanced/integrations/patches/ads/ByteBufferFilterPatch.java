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
            "ContainerType|video_action_button",
            "FEhistory",
            "avatar",
            "compact_channel_bar",
            "comment_thread",
            "creation_sheet_menu",
            "metadata",
            "thumbnail",
            "|comment.",
            "-button",
            "-count",
            "-space"
    );
    private static final List<String> mixBufferBlockList = List.of("&list=");
    private static int count;

    public static boolean filter(Object object, ByteBuffer buffer) {
        var value = object.toString();

        if (value.isEmpty() || generalWhiteList.stream().anyMatch(value::contains))
            return false;

        if (mixBufferBlockList.stream().anyMatch(new String(buffer.array(), StandardCharsets.UTF_8)::contains))
            return hideMixPlaylists(buffer);

        count = 0;

        hideGeneralAds(value, buffer);
        hideSuggestedActions(value);

        return count > 0;
    }

    private static void hideGeneralAds(String value, ByteBuffer buffer) {
        if (!PatchStatus.GeneralAds()) return;
        String charset = new String(buffer.array(), StandardCharsets.UTF_8);

        if (SettingsEnum.HIDE_SUGGESTIONS.getBoolean() &&
                value.contains("horizontal_video_shelf") &&
                !value.contains("activeStateScrollSelectionController=com")
        ) count++;

        if (SettingsEnum.HIDE_FEED_SURVEY.getBoolean() &&
                value.contains("_survey")) count++;

        if (SettingsEnum.HIDE_OFFICIAL_HEADER.getBoolean() &&
                charset.contains("shelf_header") &&
                charset.contains("YTSans-SemiBold") &&
                charset.contains("sans-serif-medium")
        ) count++;
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

    private static void hideSuggestedActions(String value) {
        if (!PatchStatus.SuggestedActions()) return;

        List<String> blockList = new ArrayList<>();

        if (SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean() &&
                !PlayerType.getCurrent().isNoneOrHidden())
            blockList.add("suggested_action");

        if (blockList.stream().anyMatch(value::contains)) count++;
    }
}
