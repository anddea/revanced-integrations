package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class HideEndscreenOverlayPatch {

    public static boolean getEndscreenOverlay() {
        return SettingsEnum.ENDSCREEN_OVERLAY.getBoolean();
    }
}