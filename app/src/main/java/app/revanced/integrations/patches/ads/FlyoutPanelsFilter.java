package app.revanced.integrations.patches.ads;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;

import app.revanced.integrations.settings.SettingsEnum;

final class FlyoutPanelsFilter extends Filter {
    private final StringFilterGroup flyoutPanelRule;
    private final String[] exceptions;

    public FlyoutPanelsFilter() {
        flyoutPanelRule = new StringFilterGroup(
                null,
                "overflow_menu_item"
        );

        exceptions = new String[]{
                "comment",
                "horizontal_video_shelf",
                "library_recent_shelf",
                "playlist_add_to_option_wrapper",
                "video_with_context"
        };

        protobufBufferFilterGroups.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUALITY_MENU,
                        "yt_outline_gear"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_CAPTIONS_MENU,
                        "yt_outline_closed_caption"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_LOOP_MENU,
                        "yt_outline_arrow_repeat_1_"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_AMBIENT_MENU,
                        "yt_outline_screen_light"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_REPORT_MENU,
                        "yt_outline_flag"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_HELP_MENU,
                        "yt_outline_question_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_MORE_MENU,
                        "yt_outline_info_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_SPEED_MENU,
                        "yt_outline_play_arrow_half_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_LISTENING_CONTROLS_MENU,
                        "yt_outline_adjust"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_AUDIO_TRACK_MENU,
                        "yt_outline_person_radar"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_WATCH_IN_VR_MENU,
                        "yt_outline_vr"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_NERDS_MENU,
                        "yt_outline_statistics_graph"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_YT_MUSIC_MENU,
                        "yt_outline_open_new"
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
        if (containsAny(path, exceptions) || containsAny(object, exceptions) || !containsAny(object, "overflow_menu_item")) return false;

        if (isEveryFilterGroupEnabled())
            if (flyoutPanelRule.check(identifier).isFiltered()) return true;

        return super.isFiltered(path, identifier, object, _protobufBufferArray);
    }
}
