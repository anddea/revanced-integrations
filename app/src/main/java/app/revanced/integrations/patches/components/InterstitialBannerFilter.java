package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.patches.utils.InterstitialBannerPatch;
import app.revanced.integrations.settings.SettingsEnum;

final class InterstitialBannerFilter extends Filter {

    public InterstitialBannerFilter() {
        final StringFilterGroup interstitialBanner = new StringFilterGroup(
                SettingsEnum.CLOSE_INTERSTITIAL_ADS,
                "_interstitial"
        );

        pathFilterGroupList.addAll(interstitialBanner);
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        // If you hide the entire banner, the layout is not loaded,
        // So only the empty gray screen is displayed.
        // https://github.com/ReVanced/revanced-integrations/pull/355

        // Therefore, instead of hiding the entire banner,
        // If the banner is detected, just press the back button.
        InterstitialBannerPatch.onBackPressed();

        return false;
    }
}
