package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class PlayerFilter extends Filter {

    public PlayerFilter() {
        final var infoCard = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_CARDS,
                "info_card_teaser_overlay.eml"
        );

        this.pathFilterGroups.addAll(
                infoCard
        );
    }
}
