package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class ExternalBrowserPatch {

    public static String enableExternalBrowser(final String original) {
        if (!Settings.ENABLE_EXTERNAL_BROWSER.get())
            return original;

        return "";
    }
}
