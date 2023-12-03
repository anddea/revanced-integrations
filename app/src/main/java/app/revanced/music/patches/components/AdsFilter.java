package app.revanced.music.patches.components;

import app.revanced.music.patches.utils.InterstitialBannerPatch;
import app.revanced.music.settings.SettingsEnum;

public final class AdsFilter extends Filter {
    private final StringFilterGroup interstitialBanner;
    private final StringFilterGroup statementBanner;

    public AdsFilter() {
        interstitialBanner = new StringFilterGroup(
                SettingsEnum.CLOSE_INTERSTITIAL_ADS,
                "_interstitial"
        );

        statementBanner = new StringFilterGroup(
                SettingsEnum.HIDE_GENERAL_ADS,
                "statement_banner"
        );

        pathFilterGroupList.addAll(interstitialBanner, statementBanner);
    }

    @Override
    boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == statementBanner) {
            return super.isFiltered(path, matchedList, matchedGroup, matchedIndex);
        } else if (matchedGroup == interstitialBanner){
            // If you hide the entire banner, the layout is not loaded,
            // So only the empty gray screen is displayed.
            // https://github.com/ReVanced/revanced-integrations/pull/355

            // Therefore, instead of hiding the entire banner,
            // If the banner is detected, just press the back button.
            InterstitialBannerPatch.onBackPressed();
        }
        return false;
    }
}
