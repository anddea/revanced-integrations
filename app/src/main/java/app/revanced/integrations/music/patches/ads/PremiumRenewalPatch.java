package app.revanced.integrations.music.patches.ads;

import static app.revanced.integrations.music.utils.StringRef.str;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.utils.LogHelper;
import app.revanced.integrations.music.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class PremiumRenewalPatch {

    public static void hidePremiumRenewal(LinearLayout buttonContainerView) {
        if (!SettingsEnum.HIDE_PREMIUM_RENEWAL.getBoolean())
            return;

        buttonContainerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            try {
                ReVancedUtils.runOnMainThreadDelayed(() -> {
                            if (!(buttonContainerView.getChildAt(0) instanceof ViewGroup closeButtonParentView))
                                return;

                            if (!(closeButtonParentView.getChildAt(0) instanceof TextView closeButtonView))
                                return;

                            if (closeButtonView.getText().toString().equals(str("dialog_got_it_text"))) {
                                closeButtonView.setSoundEffectsEnabled(false);
                                closeButtonView.performClick();
                            } else {
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
