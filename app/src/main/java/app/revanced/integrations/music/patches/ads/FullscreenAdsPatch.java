package app.revanced.integrations.music.patches.ads;

import static app.revanced.integrations.shared.utils.Utils.hideViewBy0dpUnderCondition;

import android.view.View;

import app.revanced.integrations.music.settings.Settings;

@SuppressWarnings("unused")
public class FullscreenAdsPatch {

    public static boolean hideFullscreenAds() {
        return Settings.HIDE_FULLSCREEN_ADS.get();
    }

    public static void hideFullscreenAds(View view) {
        hideViewBy0dpUnderCondition(
                Settings.HIDE_FULLSCREEN_ADS.get(),
                view
        );
    }

    //
    // public static void confirmDialog(final Button button) {
    //     if (Settings.HIDE_FULLSCREEN_ADS.get() && button != null) {
    //         button.setSoundEffectsEnabled(false);
    //         button.performClick();
    //     }
    // }

}