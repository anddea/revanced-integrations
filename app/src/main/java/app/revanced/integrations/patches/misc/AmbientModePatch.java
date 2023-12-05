package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class AmbientModePatch {

    public static boolean bypassPowerSaveModeRestrictions(boolean original) {
        return (!SettingsEnum.BYPASS_AMBIENT_MODE_RESTRICTIONS.getBoolean() && original) || SettingsEnum.DISABLE_AMBIENT_MODE.getBoolean();
    }
}
