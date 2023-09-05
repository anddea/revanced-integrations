package app.revanced.music.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static app.revanced.music.settings.SettingsEnum.ReturnType.BOOLEAN;
import static app.revanced.music.settings.SettingsEnum.ReturnType.FLOAT;
import static app.revanced.music.settings.SettingsEnum.ReturnType.INTEGER;
import static app.revanced.music.settings.SettingsEnum.ReturnType.STRING;
import static app.revanced.music.utils.SharedPrefHelper.getPreferences;
import static app.revanced.music.utils.SharedPrefHelper.saveBoolean;
import static app.revanced.music.utils.SharedPrefHelper.saveFloat;
import static app.revanced.music.utils.SharedPrefHelper.saveInteger;
import static app.revanced.music.utils.SharedPrefHelper.saveString;

import androidx.annotation.NonNull;

import java.util.Objects;

public enum SettingsEnum {

    // Ads
    HIDE_MUSIC_ADS("revanced_hide_music_ads", BOOLEAN, TRUE, true),


    // Button Container
    BUTTON_CONTAINER_DOWNLOAD_INDEX("revanced_button_container_download_index", INTEGER, 3),
    BUTTON_CONTAINER_DOWNLOAD_INDEX_FOUND("revanced_button_container_download_index_found", BOOLEAN, FALSE),
    EXTERNAL_DOWNLOADER_PACKAGE_NAME("revanced_external_downloader_package_name", STRING, "com.junkfood.seal", true),
    HIDE_BUTTON_CONTAINER_LABEL("revanced_hide_button_container_label", BOOLEAN, FALSE),
    HIDE_BUTTON_CONTAINER_RADIO("revanced_hide_button_container_radio", BOOLEAN, FALSE),
    HOOK_BUTTON_CONTAINER_DOWNLOAD("revanced_hook_button_container_download", BOOLEAN, FALSE, true),


    // Flyout
    ENABLE_COMPACT_DIALOG("revanced_enable_compact_dialog", BOOLEAN, TRUE),
    ENABLE_SLEEP_TIMER("revanced_enable_sleep_timer", BOOLEAN, TRUE, true),
    ENABLE_FLYOUT_PANEL_PLAYBACK_SPEED("revanced_enable_flyout_panel_playback_speed", BOOLEAN, FALSE, true),

    HIDE_FLYOUT_PANEL_ADD_TO_QUEUE("revanced_hide_flyout_panel_add_to_queue", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_CAPTIONS("revanced_hide_flyout_panel_captions", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_DISMISS_QUEUE("revanced_hide_flyout_panel_dismiss_queue", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_DOWNLOAD("revanced_hide_flyout_panel_download", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_EDIT_PLAYLIST("revanced_hide_flyout_panel_edit_playlist", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_GO_TO_ALBUM("revanced_hide_flyout_panel_go_to_album", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_GO_TO_ARTIST("revanced_hide_flyout_panel_go_to_artist", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_LIKE_DISLIKE("revanced_hide_flyout_panel_like_dislike", BOOLEAN, FALSE, true),
    HIDE_FLYOUT_PANEL_PLAY_NEXT("revanced_hide_flyout_panel_play_next", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_QUALITY("revanced_hide_flyout_panel_quality", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_REMOVE_FROM_LIBRARY("revanced_hide_flyout_panel_remove_from_library", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_REPORT("revanced_hide_flyout_panel_report", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_SAVE_TO_LIBRARY("revanced_hide_flyout_panel_save_to_library", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_SAVE_TO_PLAYLIST("revanced_hide_flyout_panel_save_to_playlist", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_SHARE("revanced_hide_flyout_panel_share", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_SHUFFLE("revanced_hide_flyout_panel_shuffle", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_SLEEP_TIMER("revanced_hide_flyout_panel_sleep_timer", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_START_RADIO("revanced_hide_flyout_panel_start_radio", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_STATS_FOR_NERDS("revanced_hide_flyout_panel_stats_for_nerds", BOOLEAN, FALSE),
    HIDE_FLYOUT_PANEL_VIEW_SONG_CREDIT("revanced_hide_flyout_panel_view_song_credit", BOOLEAN, FALSE),


    // General
    CUSTOM_FILTER("revanced_custom_filter", BOOLEAN, FALSE),
    CUSTOM_FILTER_STRINGS("revanced_custom_filter_strings", STRING, "", true),
    DISABLE_AUTO_CAPTIONS("revanced_disable_auto_captions", BOOLEAN, FALSE),
    ENABLE_LANDSCAPE_MODE("revanced_enable_landscape_mode", BOOLEAN, TRUE, true),
    ENABLE_OLD_STYLE_LIBRARY_SHELF("revanced_enable_old_style_library_shelf", BOOLEAN, FALSE, true),
    HIDE_BUTTON_SHELF("revanced_hide_button_shelf", BOOLEAN, FALSE, true),
    HIDE_CAROUSEL_SHELF("revanced_hide_carousel_shelf", BOOLEAN, FALSE, true),
    HIDE_CAST_BUTTON("revanced_hide_cast_button", BOOLEAN, TRUE),
    HIDE_CATEGORY_BAR("revanced_hide_category_bar", BOOLEAN, TRUE, true),
    HIDE_CHANNEL_GUIDELINES("revanced_hide_channel_guidelines", BOOLEAN, TRUE),
    HIDE_EMOJI_PICKER("revanced_hide_emoji_picker", BOOLEAN, FALSE),
    HIDE_NEW_PLAYLIST_BUTTON("revanced_hide_new_playlist_button", BOOLEAN, FALSE),
    HIDE_PLAYLIST_CARD("revanced_hide_playlist_card", BOOLEAN, FALSE, true),


    // Misc
    ENABLE_DEBUG_LOGGING("revanced_enable_debug_logging", BOOLEAN, FALSE),
    ENABLE_OPUS_CODEC("revanced_enable_opus_codec", BOOLEAN, TRUE, true),
    ENABLE_SAVE_PLAYBACK_SPEED("revanced_enable_save_playback_speed", BOOLEAN, FALSE),
    ENABLE_SAVE_VIDEO_QUALITY("revanced_enable_save_video_quality", BOOLEAN, TRUE),
    DEFAULT_PLAYBACK_SPEED("revanced_default_playback_speed", FLOAT, 1.0f),
    DEFAULT_VIDEO_QUALITY_WIFI("revanced_default_video_quality_wifi", INTEGER, -2),
    DEFAULT_VIDEO_QUALITY_MOBILE("revanced_default_video_quality_mobile", INTEGER, -2),
    SPOOF_APP_VERSION("revanced_spoof_app_version", BOOLEAN, FALSE, true),


    // Navigation
    ENABLE_BLACK_NAVIGATION_BAR("revanced_enable_black_navigation_bar", BOOLEAN, TRUE),
    IS_SAMPLE_BUTTON_SHOWN("revanced_is_sample_button_shown", BOOLEAN, FALSE),
    HIDE_NAVIGATION_LABEL("revanced_hide_navigation_label", BOOLEAN, FALSE, true),
    HIDE_SAMPLE_BUTTON("revanced_hide_sample_button", BOOLEAN, FALSE, true),


    // Player
    ENABLE_COLOR_MATCH_PLAYER("revanced_enable_color_match_player", BOOLEAN, TRUE),
    ENABLE_FORCE_MINIMIZED_PLAYER("revanced_enable_force_minimized_player", BOOLEAN, TRUE),
    ENABLE_FORCE_SHUFFLE("revanced_enable_force_shuffle", BOOLEAN, TRUE),
    ENABLE_NEW_LAYOUT("revanced_enable_new_layout", BOOLEAN, TRUE, true),
    ENABLE_OLD_STYLE_MINI_PLAYER("revanced_enable_old_style_mini_player", BOOLEAN, TRUE, true),
    ENABLE_ZEN_MODE("revanced_enable_zen_mode", BOOLEAN, FALSE);


    static {
        loadAllSettings();
    }

    @NonNull
    public final String path;
    @NonNull
    public final Object defaultValue;
    @NonNull
    public final ReturnType returnType;

    public final boolean rebootApp;

    public Object value;

    SettingsEnum(@NonNull String path, @NonNull ReturnType returnType, @NonNull Object defaultValue) {
        this(path, returnType, defaultValue, false);
    }

    SettingsEnum(@NonNull String path, @NonNull ReturnType returnType, @NonNull Object defaultValue, boolean rebootApp) {
        this.path = path;
        this.returnType = returnType;
        this.defaultValue = defaultValue;
        this.rebootApp = rebootApp;
    }

    private static void loadAllSettings() {
        for (SettingsEnum setting : values()) {
            setting.load();
        }
    }

    private void load() {
        switch (returnType) {
            case BOOLEAN ->
                    value = Objects.requireNonNull(getPreferences()).getBoolean(path, (boolean) defaultValue);
            case INTEGER ->
                    value = Objects.requireNonNull(getPreferences()).getInt(path, (Integer) defaultValue);
            case FLOAT ->
                    value = Objects.requireNonNull(getPreferences()).getFloat(path, (float) defaultValue);
            case STRING ->
                    value = Objects.requireNonNull(getPreferences()).getString(path, (String) defaultValue);
            default -> throw new IllegalStateException(name());
        }
    }

    public void saveValue(@NonNull Object newValue) {
        Objects.requireNonNull(newValue);
        switch (returnType) {
            case BOOLEAN -> saveBoolean(path, (boolean) newValue);
            case INTEGER -> saveInteger(path, (Integer) newValue);
            case FLOAT -> saveFloat(path, (float) newValue);
            default -> saveString(path, newValue.toString());
        }
        value = newValue;
    }

    public boolean getBoolean() {
        return (boolean) value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public float getFloat() {
        return (Float) value;
    }

    public String getString() {
        return (String) value;
    }

    public enum ReturnType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
    }
}
