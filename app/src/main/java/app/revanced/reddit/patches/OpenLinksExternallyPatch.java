package app.revanced.reddit.patches;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import app.revanced.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public class OpenLinksExternallyPatch {

    /**
     * Override 'CustomTabsIntent', in order to open links in the default browser.
     * Instead of doing CustomTabsActivity,
     *
     * @param activity The activity, to start an Intent.
     * @param uri      The URL to be opened in the default browser.
     */
    public static boolean openLinksExternally(Activity activity, Uri uri) {
        if (activity == null || uri == null || !SettingsEnum.OPEN_LINKS_EXTERNALLY.getBoolean())
            return false;

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            activity.startActivity(intent);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }
}
