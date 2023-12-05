package app.revanced.music.patches.ads;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class PremiumRenewalPatch {

    public static void hidePremiumRenewal(LinearLayout buttonContainerView) {
        if (!SettingsEnum.HIDE_PREMIUM_RENEWAL.getBoolean())
            return;

        buttonContainerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            try {
                ReVancedUtils.runOnMainThreadDelayed(() -> {
                            if (buttonContainerView.getChildAt(0) instanceof ViewGroup closeButtonParentView) {
                                final View closeButtonView = closeButtonParentView.getChildAt(0);
                                if (closeButtonView != null) {
                                    closeButtonView.setSoundEffectsEnabled(false);
                                    closeButtonView.performClick();
                                }
                                ReVancedUtils.hideViewByLayoutParams((View) buttonContainerView.getParent());
                            }
                        }, 0
                );
            } catch (Exception ex) {
                LogHelper.printException(() -> "hidePremiumRenewal failure", ex);
            }
        });
    }
}
