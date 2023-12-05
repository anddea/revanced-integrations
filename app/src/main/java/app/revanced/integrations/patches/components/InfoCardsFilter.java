package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;

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
