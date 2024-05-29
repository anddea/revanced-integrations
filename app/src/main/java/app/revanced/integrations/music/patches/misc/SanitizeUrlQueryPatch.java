package app.revanced.integrations.music.patches.misc;

import app.revanced.integrations.music.settings.Settings;

@SuppressWarnings("unused")
public final class SanitizeUrlQueryPatch {
    /**
     * This tracking parameter is mainly used.
     */
    private static final String NEW_TRACKING_REGEX = ".si=.+";
    /**
     * This tracking parameter is outdated.
     * Used when patching old versions or enabling spoof app version.
     */
    private static final String OLD_TRACKING_REGEX = ".feature=.+";

    /**
     * Strip query parameters from a given URL string.
     *
     * @param urlString URL string to strip query parameters from.
     * @return URL string without query parameters if possible, otherwise the original string.
     */
    public static String stripQueryParameters(final String urlString) {
        if (!Settings.SANITIZE_SHARING_LINKS.get())
            return urlString;

        return urlString.replaceAll(NEW_TRACKING_REGEX, "").replaceAll(OLD_TRACKING_REGEX, "");
    }

}
