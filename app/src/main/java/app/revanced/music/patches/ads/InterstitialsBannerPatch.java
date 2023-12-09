package app.revanced.music.patches.ads;

import static app.revanced.music.utils.ReVancedUtils.hideViewBy0dpUnderCondition;

import android.view.View;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class InterstitialsBannerPatch {

    public static void hideInterstitialsBanner(View view) {
        hideViewBy0dpUnderCondition(
                SettingsEnum.CLOSE_INTERSTITIAL_ADS.getBoolean(),
                view
        );
    }
}