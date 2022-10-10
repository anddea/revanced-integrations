package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class HideDonationCompanionPatch {

    public static void HideDonationCompanion(View view) {
        if (SettingsEnum.DONATION_COMPANION_SHOWN.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }
}
