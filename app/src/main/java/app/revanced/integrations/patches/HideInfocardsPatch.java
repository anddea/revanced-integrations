package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class HideInfocardsPatch {
    public static void hideInfocardsIncognito(View view) {
        if (SettingsEnum.INFO_CARDS_SHOWN.getBoolean()) return;
        view.setVisibility(View.GONE);
    }

    public static boolean hideInfocardsMethodCall() {
        return !SettingsEnum.INFO_CARDS_SHOWN.getBoolean();
    }
}
