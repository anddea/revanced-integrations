package app.revanced.integrations.reddit.patches;

import app.revanced.integrations.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class SanitizeUrlQueryPatch {

    public static boolean stripQueryParameters() {
        return SettingsEnum.SANITIZE_URL_QUERY.getBoolean();
    }

}
