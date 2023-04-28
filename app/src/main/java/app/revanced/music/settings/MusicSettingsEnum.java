package app.revanced.music.settings;

import static app.revanced.music.settings.MusicSettingsEnum.ReturnType.BOOLEAN;
import static app.revanced.music.settings.MusicSettingsEnum.ReturnType.INTEGER;
import static app.revanced.music.utils.SharedPrefHelper.getPreferences;
import static app.revanced.music.utils.SharedPrefHelper.saveBoolean;
import static app.revanced.music.utils.SharedPrefHelper.saveInteger;
import static app.revanced.music.utils.SharedPrefHelper.saveString;

import androidx.annotation.NonNull;

import java.util.Objects;

public enum MusicSettingsEnum {

    // Ads
    HIDE_MUSIC_ADS("revanced_hide_music_ads", BOOLEAN, true, true),


    // Layout
    DISABLE_AUTO_CAPTIONS("revanced_disable_auto_captions", BOOLEAN, false),
    ENABLE_BLACK_NAVBAR("revanced_enable_black_navbar", BOOLEAN, true),
    ENABLE_COLOR_MATCH_PLAYER("revanced_enable_color_match_player", BOOLEAN, true),
    ENABLE_COMPACT_DIALOG("revanced_enable_compact_dialog", BOOLEAN, true),
    ENABLE_FORCE_MINIMIZED_PLAYER("revanced_enable_force_minimized_player", BOOLEAN, true),
    ENABLE_LANDSCAPE_MODE("revanced_enable_landscape_mode", BOOLEAN, true, true),
    ENABLE_ZEN_MODE("revanced_enable_zen_mode", BOOLEAN, false),
    HIDE_BUTTON_SHELF("revanced_hide_button_shelf", BOOLEAN, false, true),
    HIDE_CAROUSEL_SHELF("revanced_hide_carousel_shelf", BOOLEAN, false, true),
    HIDE_CAST_BUTTON("revanced_hide_cast_button", BOOLEAN, true),
    HIDE_CATEGORY_BAR("revanced_hide_category_bar", BOOLEAN, true, true),
    HIDE_NEW_PLAYLIST_BUTTON("revanced_hide_new_playlist_button", BOOLEAN, false),
    HIDE_PLAYLIST_CARD("revanced_hide_playlist_card", BOOLEAN, false, true),


    // Misc
    ENABLE_DEBUG("revanced_enable_debug", BOOLEAN, false),
    ENABLE_FORCE_SHUFFLE("revanced_enable_force_shuffle", BOOLEAN, true),
    ENABLE_OPUS_CODEC("revanced_enable_opus_codec", BOOLEAN, true, true),
    ENABLE_SAVE_VIDEO_QUALITY("revanced_enable_save_video_quality", BOOLEAN, true),
    DEFAULT_DOWNLOADER("revanced_default_downloader", ReturnType.STRING, "ussr.razar.youtube_dl", true),
    DEFAULT_VIDEO_QUALITY_WIFI("revanced_default_video_quality_wifi", INTEGER, -2),
    DEFAULT_VIDEO_QUALITY_MOBILE("revanced_default_video_quality_mobile", INTEGER, -2),
    HOOK_SHARE_BUTTON("revanced_hook_share_button", BOOLEAN, false, true),
    HOOK_TYPE("revanced_hook_type", BOOLEAN, false, true),
    SPOOF_APP_VERSION("revanced_spoof_app_version", BOOLEAN, false, true);


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

    MusicSettingsEnum(@NonNull String path, @NonNull ReturnType returnType, @NonNull Object defaultValue) {
        this.path = path;
        this.returnType = returnType;
        this.defaultValue = defaultValue;
        this.rebootApp = false;
    }

    MusicSettingsEnum(@NonNull String path, @NonNull ReturnType returnType, @NonNull Object defaultValue, boolean rebootApp) {
        this.path = path;
        this.returnType = returnType;
        this.defaultValue = defaultValue;
        this.rebootApp = rebootApp;
    }

    private static void loadAllSettings() {
        for (MusicSettingsEnum setting : values()) {
            setting.load();
        }
    }

    private void load() {
        switch (returnType) {
            case BOOLEAN:
                value = Objects.requireNonNull(getPreferences()).getBoolean(path, (boolean) defaultValue);
                break;
            case INTEGER:
                value = Objects.requireNonNull(getPreferences()).getInt(path, (Integer) defaultValue);
                break;
            case STRING:
                value = Objects.requireNonNull(getPreferences()).getString(path, (String) defaultValue);
                break;
            default:
                throw new IllegalStateException(name());
        }
    }

    public void saveValue(@NonNull Object newValue) {
        Objects.requireNonNull(newValue);
        switch (returnType) {
            case BOOLEAN:
                saveBoolean(path, (boolean) newValue);
                break;
            case INTEGER:
                saveInteger(path, (Integer) newValue);
                break;
            default:
                saveString(path, newValue.toString());
                break;
        }
        value = newValue;
    }

    public boolean getBoolean() {
        return (boolean) value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public String getString() {
        return (String) value;
    }

    public enum ReturnType {
        BOOLEAN,
        INTEGER,
        STRING,
    }
}
