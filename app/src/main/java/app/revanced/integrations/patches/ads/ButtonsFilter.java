package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

final class ButtonsFilter extends Filter {
    private final StringFilterGroup actionBarRule;

    public ButtonsFilter() {
        actionBarRule = new StringFilterGroup(
                null,
                "video_action_bar"
        );

        pathFilterGroups.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_LIKE_BUTTON,
                        "|CellType|ContainerType|like_button",
                        "|CellType|ContainerType|segmented_like_dislike_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_DISLIKE_BUTTON,
                        "|CellType|ContainerType|dislike_button",
                        "|CellType|ContainerType|segmented_like_dislike_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_DOWNLOAD_BUTTON,
                        "download_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_CREATE_CLIP_BUTTON,
                        "|clip_button.eml|"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_PLAYLIST_BUTTON,
                        "save_to_playlist_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_REWARDS_BUTTON,
                        "account_link_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_ACTION_BUTTON,
                        "ContainerType|video_action_button",
                        "|CellType|CollectionType|CellType|ContainerType|button.eml|"
                ),
                actionBarRule
        );
    }

    private boolean isEveryFilterGroupEnabled() {
        for (StringFilterGroup rule : pathFilterGroups)
            if (!rule.isEnabled()) return false;

        return true;
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == actionBarRule) {
            return isEveryFilterGroupEnabled();
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
