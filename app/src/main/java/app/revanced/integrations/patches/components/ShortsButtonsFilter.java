package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
final class ShortsButtonsFilter extends Filter {

    public ShortsButtonsFilter() {
        final StringFilterGroup shortsDislikeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_DISLIKE_BUTTON,
                "shorts_dislike_button"
        );

        final StringFilterGroup shortsLikeButton = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_PLAYER_LIKE_BUTTON,
                "shorts_like_button"
        );

        pathFilterGroupList.addAll(shortsDislikeButton, shortsLikeButton);
    }
}
