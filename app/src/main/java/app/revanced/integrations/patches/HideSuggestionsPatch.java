package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;

public class HideSuggestionsPatch {
	
    public static int hideSuggestions() {
        return SettingsEnum.SUGGESTIONS_SHOWN.getBoolean() ? 0 : 8;
    }
}
