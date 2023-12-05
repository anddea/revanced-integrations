package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class ExternalBrowserPatch {

    public static String enableExternalBrowser(final String original) {
        if (!SettingsEnum.ENABLE_EXTERNAL_BROWSER.getBoolean())
            return original;

        return "";
    }
}
