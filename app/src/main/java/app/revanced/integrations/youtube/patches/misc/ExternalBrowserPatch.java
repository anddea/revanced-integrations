package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
public class ExternalBrowserPatch {

    public static String enableExternalBrowser(final String original) {
        if (!SettingsEnum.ENABLE_EXTERNAL_BROWSER.getBoolean())
            return original;

        return "";
    }
}
