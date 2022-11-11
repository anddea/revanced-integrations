package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class HideShortsRemixButtonPatch {

    public static void hideShortsRemixButton(View view) {
        if (SettingsEnum.SHORTS_REMIX_BUTTON.getBoolean()) return;
        view.setVisibility(View.GONE);
    }
}