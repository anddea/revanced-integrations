package app.revanced.integrations.patches;

import android.view.View;
import app.revanced.integrations.settings.SettingsEnum;

public class SuggestedActionsPatch {
    
    public static void hideSuggestedActions(View view) {
        view.setVisibility(SettingsEnum.SUGGESTED_ACTION.getBoolean() ? View.GONE : View.VISIBLE);
    }
}
