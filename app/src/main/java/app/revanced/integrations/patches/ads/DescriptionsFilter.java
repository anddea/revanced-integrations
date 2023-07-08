package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class DescriptionsFilter extends Filter {

    public DescriptionsFilter() {
        final var infoCardsSection = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_CARDS_SECTION,
                "infocards_section"
        );

        final var gameSection = new StringFilterGroup(
                SettingsEnum.HIDE_GAME_SECTION,
                "gaming_section"
        );

        final var musicSection = new StringFilterGroup(
                SettingsEnum.HIDE_MUSIC_SECTION,
                "music_section"
        );

        final var placeSection = new StringFilterGroup(
                SettingsEnum.HIDE_PLACE_SECTION,
                "place_section"
        );

        final var transcriptSection = new StringFilterGroup(
                SettingsEnum.HIDE_TRANSCIPT_SECTION,
                "transcript_section"
        );


        this.pathFilterGroups.addAll(
                infoCardsSection,
                gameSection,
                musicSection,
                placeSection,
                transcriptSection
        );
    }
}
