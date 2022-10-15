package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class HideMyMixPatch {
	
    public static boolean HideMyMix() {
        return SettingsEnum.MY_MIX_SHOWN.getBoolean();
    }
}
