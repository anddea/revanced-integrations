package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class AdsFilter extends Filter {

    public AdsFilter() {
        final StringFilterGroup statementBanner = new StringFilterGroup(
                SettingsEnum.HIDE_GENERAL_ADS,
                "statement_banner"
        );

        pathFilterGroupList.addAll(statementBanner);
    }
}
