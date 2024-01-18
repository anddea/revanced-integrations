package app.revanced.integrations.music.patches.ads;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.utils.LogHelper;

@SuppressWarnings("unused")
public class PremiumPromotionPatch {

    public static void hidePremiumPromotion(View view) {
        if (!SettingsEnum.HIDE_PREMIUM_PROMOTION.getBoolean())
            return;

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            try {
                if (!(view instanceof ViewGroup viewGroup)) {
                    return;
                }
                if (viewGroup.getChildCount() == 0) {
                    return;
                }
                if (!(viewGroup.getChildAt(0) instanceof ViewGroup mealBarLayoutRoot)) {
                    return;
                }
                if (mealBarLayoutRoot.getChildCount() == 0) {
                    return;
                }
                if (!(mealBarLayoutRoot.getChildAt(0) instanceof LinearLayout linearLayout)) {
                    return;
                }
                if (linearLayout.getChildCount() == 0) {
                    return;
                }
                if (!(linearLayout.getChildAt(0) instanceof ImageView imageView)) {
                    return;
                }

                if (imageView.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                LogHelper.printException(() -> "hideGetPremium failure", ex);
            }
        });
    }
}