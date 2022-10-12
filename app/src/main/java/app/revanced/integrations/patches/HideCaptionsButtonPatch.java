package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class HideCaptionsButtonPatch {

    public static boolean hideCaptionsButton() {
        return SettingsEnum.CAPTIONS_BUTTON_SHOWN.getBoolean();
    }
}
