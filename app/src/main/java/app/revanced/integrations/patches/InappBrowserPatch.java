package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class InappBrowserPatch {

    public static String getInappBrowser(String inappbrowser) {
        if (SettingsEnum.INAPP_BROWSER.getBoolean()){
            return "";
		}
        return inappbrowser;
    }
}
