package app.revanced.reddit.patches;

import app.revanced.reddit.settings.SettingsEnum;

public final class SanitizeUrlQueryPatch {

    public static boolean stripQueryParameters() {
        return SettingsEnum.SANITIZE_URL_QUERY.getBoolean();
    }

}
