package app.revanced.integrations.youtube.whitelist;

import static app.revanced.integrations.youtube.utils.StringRef.str;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.settings.SharedPrefCategory;

public enum WhitelistType {
    SPEED(SharedPrefCategory.REVANCED, SettingsEnum.SPEED_WHITELIST.path),
    SPONSORBLOCK(SharedPrefCategory.REVANCED, SettingsEnum.SB_WHITELIST.path);

    private final String friendlyName;
    private final String preferencesName;
    private final String preferenceEnabledName;
    private final SharedPrefCategory name;

    WhitelistType(SharedPrefCategory name, String preferenceEnabledName) {
        this.friendlyName = str("revanced_whitelisting_" + name().toLowerCase());
        this.name = name;
        this.preferencesName = "whitelist_" + name();
        this.preferenceEnabledName = preferenceEnabledName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public SharedPrefCategory getSharedPreferencesName() {
        return name;
    }

    public String getPreferencesName() {
        return preferencesName;
    }

    public String getPreferenceEnabledName() {
        return preferenceEnabledName;
    }
}