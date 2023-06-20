package app.revanced.integrations.patches.ads;

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
                )
        );
    }

    private boolean isEveryFilterGroupEnabled() {
        for (StringFilterGroup rule : pathFilterGroups)
            if (!rule.isEnabled()) return false;

        return true;
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String object, final byte[] _protobufBufferArray) {
        if (isEveryFilterGroupEnabled())
            if (actionBarRule.check(identifier).isFiltered()) return true;

        return super.isFiltered(path, identifier, object, _protobufBufferArray);
    }
}
