package app.revanced.integrations.reddit.patches;

import android.net.Uri;

import app.revanced.integrations.reddit.settings.SettingsEnum;
import app.revanced.integrations.reddit.utils.LogHelper;

@SuppressWarnings("unused")
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
            LogHelper.printException(() -> "Can not parse URL", e);
            return uri;
        }
    }

}
