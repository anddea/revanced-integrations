package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class LayoutComponentsUniversalFilter extends Filter {

    public LayoutComponentsUniversalFilter() {
        final var expandableMetadata = new StringFilterGroup(
                SettingsEnum.HIDE_EXPANDABLE_CHIP,
                "inline_expander"
        );

        final var feedSurvey = new StringFilterGroup(
                SettingsEnum.HIDE_FEED_SURVEY,
                "feed_nudge",
                "in_feed_survey",
                "slimline_survey"
        );

        final var grayDescription = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_DESCRIPTION,
                "endorsement_header_footer"
        );


        this.pathFilterGroups.addAll(
                expandableMetadata,
                feedSurvey,
                grayDescription
        );
    }
}
