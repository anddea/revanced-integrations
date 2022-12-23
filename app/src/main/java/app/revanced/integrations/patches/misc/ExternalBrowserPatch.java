package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class ExternalBrowserPatch {

    public static String enableExternalBrowser(String original) {
        if (SettingsEnum.ENABLE_EXTERNAL_BROWSER.getBoolean()) original = "";
        return original;
    }
}
