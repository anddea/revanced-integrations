package app.revanced.music.patches.ads;

import android.view.View;
import android.widget.LinearLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class PremiumRenewalPatch {

    public static void hidePremiumRenewal(LinearLayout linearLayout, View closeButtonView) {
        if (!SettingsEnum.HIDE_PREMIUM_RENEWAL.getBoolean())
            return;

        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            try {
                if (closeButtonView != null && closeButtonView.getVisibility() == View.VISIBLE) {
                    closeButtonView.setSoundEffectsEnabled(false);
                    closeButtonView.performClick();
                } else {
                    ReVancedUtils.hideViewByLayoutParams(linearLayout);
                }
            } catch (Exception ex) {
                LogHelper.printException(() -> "hidePremiumRenewal failure", ex);
            }
        });
    }
}
