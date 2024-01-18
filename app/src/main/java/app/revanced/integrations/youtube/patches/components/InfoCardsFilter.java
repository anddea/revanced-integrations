package app.revanced.integrations.youtube.patches.components;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
final class InfoCardsFilter extends Filter {

    public InfoCardsFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_INFO_CARDS,
                        "info_card_teaser_overlay.eml"
                )
        );
    }
}
