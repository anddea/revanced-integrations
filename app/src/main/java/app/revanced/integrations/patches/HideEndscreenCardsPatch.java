package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class HideEndscreenCardsPatch {
    //Used by app.revanced.patches.youtube.layout.hideendscreencards.bytecode.patch.HideEndscreenCardsPatch
    public static void hideEndscreen(View view) {
        if (SettingsEnum.ENDSCREEN_CARDS_SHOWN.getBoolean()) return;
        view.setVisibility(View.GONE);
    }
}