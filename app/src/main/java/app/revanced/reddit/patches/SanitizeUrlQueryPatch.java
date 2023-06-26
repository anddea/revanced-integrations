package app.revanced.reddit.patches;

import java.net.MalformedURLException;
import java.net.URL;

import app.revanced.reddit.settings.SettingsEnum;
import app.revanced.reddit.utils.LogHelper;

public final class SanitizeUrlQueryPatch {

    /**
     * Strip query parameters from a given URL string.
     *
     * @param urlString URL string to strip query parameters from.
     * @return URL string without query parameters if possible, otherwise the original string.
     */
    public static String stripQueryParameters(final String urlString) {
        try {
            if (!SettingsEnum.SANITIZE_URL_QUERY.getBoolean())
                return urlString;

            final var url = new URL(urlString);
            return url.getProtocol() + "://" + url.getHost() + url.getPath();
        } catch (MalformedURLException e) {
            LogHelper.printException(SanitizeUrlQueryPatch.class, "Can not parse URL", e);
            return urlString;
        }
    }

}
