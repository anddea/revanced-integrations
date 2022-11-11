package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class PlayerOverlayBackgroundPatch {

    public static boolean getPlayerOverlaybackground() {
        return SettingsEnum.PALYER_OVERLAY_BACKGROUND.getBoolean();
    }
}