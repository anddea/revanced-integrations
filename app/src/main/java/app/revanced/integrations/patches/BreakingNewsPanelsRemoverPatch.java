package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class BreakingNewsPanelsRemoverPatch {

    public static void HideBreakingNewsPanels(View view) {
        if (SettingsEnum.BREAKING_NEWS_PANELS.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }
}
