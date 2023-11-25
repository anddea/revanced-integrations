package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

public final class AdsFilter extends Filter {

    public AdsFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_MUSIC_ADS,
                        "statement_banner"
                )
        );
    }
}
