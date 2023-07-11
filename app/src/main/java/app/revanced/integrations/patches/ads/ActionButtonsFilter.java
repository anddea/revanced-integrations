package app.revanced.integrations.patches.ads;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import app.revanced.integrations.settings.SettingsEnum;

final class ActionButtonsFilter extends Filter {
    private final StringFilterGroup actionButtonRule;

    private final String[] exceptions;

    public ActionButtonsFilter() {
        exceptions = new String[]{
                "account_link_button",
                "comment",
                "download_button",
                "like_button",
                "save_to_playlist_button",
                "video_with_context"
        };

        actionButtonRule = new StringFilterGroup(
                null,
                "ContainerType|video_action_button",
                "|CellType|CollectionType|CellType|ContainerType|button.eml|"
        );

        protobufBufferFilterGroups.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_CREATE_CLIP_BUTTON,
                        "yt_outline_scissors"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_LIVE_CHAT_BUTTON,
                        "yt_outline_message_bubble_overlap"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_REMIX_BUTTON,
                        "yt_outline_youtube_shorts_plus"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_REPORT_BUTTON,
                        "yt_outline_flag"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHARE_BUTTON,
                        "yt_outline_share"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SHOP_BUTTON,
                        "yt_outline_bag"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_THANKS_BUTTON,
                        "yt_outline_dollar_sign_heart"
                )
        );
    }

    private boolean isEveryFilterGroupEnabled() {
        for (ByteArrayFilterGroup rule : protobufBufferFilterGroups)
            if (!rule.isEnabled()) return false;

        return true;
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String object, final byte[] _protobufBufferArray) {
        if (containsAny(path, exceptions) || path.startsWith("CellType|"))
            return false;

        if (isEveryFilterGroupEnabled())
            if (actionButtonRule.check(identifier).isFiltered()) return true;

        return super.isFiltered(path, identifier, object, _protobufBufferArray);
    }
}
