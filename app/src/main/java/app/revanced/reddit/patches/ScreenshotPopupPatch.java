package app.revanced.reddit.patches;

import app.revanced.reddit.settings.SettingsEnum;

public class ScreenshotPopupPatch {

    public static boolean hideScreenshotPopup() {
        return SettingsEnum.HIDE_SCREENSHOT_POPUP.getBoolean();
    }
}
