package app.revanced.reddit.patches;

import android.net.Uri;

import app.revanced.reddit.settings.SettingsEnum;
import app.revanced.reddit.utils.LogHelper;

public final class OpenLinksDirectlyPatch {

    /**
     * Parses the given Reddit redirect uri by extracting the redirect query.
     *
     * @param uri The Reddit redirect uri.
     * @return The redirect query.
     */
    public static Uri parseRedirectUri(Uri uri) {
        try {
            if (!SettingsEnum.OPEN_LINKS_DIRECTLY.getBoolean()) return uri;
            var parsedUri = uri.getQueryParameter("url");
            if (parsedUri == null)
                return uri;

            return Uri.parse(parsedUri);
        } catch (Exception e) {
            LogHelper.printException(OpenLinksDirectlyPatch.class, "Can not parse URL", e);
            return uri;
        }
    }

}
