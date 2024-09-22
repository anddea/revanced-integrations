package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.shared.patches.components.ByteArrayFilterGroup;
import app.revanced.integrations.shared.patches.components.ByteArrayFilterGroupList;
import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;
import app.revanced.integrations.shared.utils.StringTrieSearch;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.shared.PlayerType;

@SuppressWarnings("unused")
public final class PlayerFlyoutMenuFilter extends Filter {
    private final ByteArrayFilterGroupList flyoutFilterGroupList = new ByteArrayFilterGroupList();

    private final ByteArrayFilterGroup byteArrayException;
    private final StringTrieSearch pathBuilderException = new StringTrieSearch();
    private final StringTrieSearch playerFlyoutMenuFooter = new StringTrieSearch();
    private final StringFilterGroup playerFlyoutMenu;
    private final StringFilterGroup qualityHeader;

    public PlayerFlyoutMenuFilter() {
        byteArrayException = new ByteArrayFilterGroup(
                null,
                "quality_sheet"
        );
        pathBuilderException.addPattern(
                "bottom_sheet_list_option"
        );
        playerFlyoutMenuFooter.addPatterns(
                "captions_sheet_content.eml",
                "quality_sheet_content.eml"
        );

        final StringFilterGroup captionsFooter = new StringFilterGroup(
                Settings.HIDE_PLAYER_FLYOUT_MENU_CAPTIONS_FOOTER,
                "|ContainerType|ContainerType|ContainerType|TextType|",
                "|divider.eml|"
        );

        final StringFilterGroup qualityFooter = new StringFilterGroup(
                Settings.HIDE_PLAYER_FLYOUT_MENU_QUALITY_FOOTER,
                "quality_sheet_footer.eml",
                "|divider.eml|"
        );

        qualityHeader = new StringFilterGroup(
                Settings.HIDE_PLAYER_FLYOUT_MENU_QUALITY_HEADER,
                "quality_sheet_header.eml"
        );

        playerFlyoutMenu = new StringFilterGroup(null, "overflow_menu_item.eml|");

        // Using pathFilterGroupList due to new flyout panel(A/B)
        addPathCallbacks(
                captionsFooter,
                qualityFooter,
                qualityHeader,
                playerFlyoutMenu
        );

        flyoutFilterGroupList.addAll(
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_AMBIENT,
                        "yt_outline_screen_light"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_AUDIO_TRACK,
                        "yt_outline_person_radar"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_CAPTIONS,
                        "closed_caption"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_HELP,
                        "yt_outline_question_circle"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_LOCK_SCREEN,
                        "yt_outline_lock"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_LOOP,
                        "yt_outline_arrow_repeat_1_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_MORE,
                        "yt_outline_info_circle"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_PIP,
                        "yt_fill_picture_in_picture",
                        "yt_outline_picture_in_picture"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_PLAYBACK_SPEED,
                        "yt_outline_play_arrow_half_circle"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_PREMIUM_CONTROLS,
                        "yt_outline_adjust"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_ADDITIONAL_SETTINGS,
                        "yt_outline_gear"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_REPORT,
                        "yt_outline_flag"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_STABLE_VOLUME,
                        "volume_stable"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_SLEEP_TIMER,
                        "yt_outline_moon_z_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_STATS_FOR_NERDS,
                        "yt_outline_statistics_graph"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_WATCH_IN_VR,
                        "yt_outline_vr"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_PLAYER_FLYOUT_MENU_YT_MUSIC,
                        "yt_outline_open_new"
                )
        );
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        if (matchedGroup == playerFlyoutMenu) {
            // Overflow menu is always the start of the path.
            if (contentIndex != 0) {
                return false;
            }
            // Shorts also use this player flyout panel
            if (PlayerType.getCurrent().isNoneOrHidden() || byteArrayException.check(protobufBufferArray).isFiltered()) {
                return false;
            }
            if (flyoutFilterGroupList.check(protobufBufferArray).isFiltered()) {
                // Super class handles logging.
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }
        } else if (matchedGroup == qualityHeader) {
            // Quality header is always the start of the path.
            if (contentIndex != 0) {
                return false;
            }
            // Super class handles logging.
            return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
        } else {
            // Components other than the footer separator are not filtered.
            if (pathBuilderException.matches(path) || !playerFlyoutMenuFooter.matches(path)) {
                return false;
            }
            // Super class handles logging.
            return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
        }

        return false;
    }
}
