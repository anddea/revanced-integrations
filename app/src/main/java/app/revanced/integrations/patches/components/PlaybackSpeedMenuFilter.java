package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public final class PlaybackSpeedMenuFilter extends Filter {
    // Must be volatile or synchronized, as litho filtering runs off main thread and this field is then access from the main thread.
    public static volatile boolean isPlaybackSpeedMenuVisible;

    public PlaybackSpeedMenuFilter() {
        pathFilterGroupList.addAll(new StringFilterGroup(
                SettingsEnum.ENABLE_CUSTOM_PLAYBACK_SPEED,
                "playback_speed_sheet_content.eml-js"
        ));
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        isPlaybackSpeedMenuVisible = true;

        return false;
    }
}
