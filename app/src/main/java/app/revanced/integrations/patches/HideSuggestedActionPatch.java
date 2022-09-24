package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class HideSuggestedActionPatch {
	
    public static int hideSuggestedactions() {
        return SettingsEnum.SUGGESTED_ACTION_SHOWN.getBoolean() ? 0 : 8;
    }
}
