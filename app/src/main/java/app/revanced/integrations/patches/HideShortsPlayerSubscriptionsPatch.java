package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class HideShortsPlayerSubscriptionsPatch {

    public static void hideSubscriptions(View view) {
        if (!SettingsEnum.SHORTS_PLAYER_SUBSCRIPTIONS.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }
}
