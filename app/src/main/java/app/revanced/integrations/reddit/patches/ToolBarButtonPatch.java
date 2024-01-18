package app.revanced.integrations.reddit.patches;

import android.view.View;

import app.revanced.integrations.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public class ToolBarButtonPatch {

    public static void hideToolBarButton(View view) {
        if (!SettingsEnum.HIDE_TOOLBAR_BUTTON.getBoolean())
            return;

        view.setVisibility(View.GONE);
    }
}
