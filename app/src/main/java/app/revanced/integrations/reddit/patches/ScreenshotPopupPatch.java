package app.revanced.integrations.reddit.patches;

import app.revanced.integrations.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public class ScreenshotPopupPatch {

    public static boolean disableScreenshotPopup() {
        return SettingsEnum.DISABLE_SCREENSHOT_POPUP.getBoolean();
    }
}
