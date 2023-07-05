package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class DescriptionsFilter extends Filter {

    public DescriptionsFilter() {
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
                musicSection,
                placeSection,
                transcriptSection
        );
    }
}
