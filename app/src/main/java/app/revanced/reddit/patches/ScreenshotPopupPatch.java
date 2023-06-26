package app.revanced.reddit.patches;

import app.revanced.reddit.settings.SettingsEnum;

public class ScreenshotPopupPatch {

    public static boolean disableScreenshotPopup() {
        return SettingsEnum.DISABLE_SCREENSHOT_POPUP.getBoolean();
    }
}
