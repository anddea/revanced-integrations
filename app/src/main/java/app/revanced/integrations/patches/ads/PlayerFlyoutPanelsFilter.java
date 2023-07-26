package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class PlayerFlyoutPanelsFilter extends Filter {
    public PlayerFlyoutPanelsFilter() {
        protobufBufferFilterGroups.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_QUALITY,
                        "yt_outline_gear"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_CAPTIONS,
                        "closed_caption"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_LOOP,
                        "yt_outline_arrow_repeat_1_"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_AMBIENT,
                        "yt_outline_screen_light"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_REPORT,
                        "yt_outline_flag"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_HELP,
                        "yt_outline_question_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_MORE,
                        "yt_outline_info_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_SPEED,
                        "yt_outline_play_arrow_half_circle"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_LISTENING_CONTROLS,
                        "yt_outline_adjust"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_AUDIO_TRACK,
                        "yt_outline_person_radar"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_WATCH_IN_VR,
                        "yt_outline_vr"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_NERDS,
                        "yt_outline_statistics_graph"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_YT_MUSIC,
                        "yt_outline_open_new"
                )
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String allValue, final byte[] _protobufBufferArray) {
        if (identifier != null && identifier.startsWith("overflow_menu_item.eml|"))
            return super.isFiltered(path, identifier, allValue, _protobufBufferArray);

        return false;
    }
}
