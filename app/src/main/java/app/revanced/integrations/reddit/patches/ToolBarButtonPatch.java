package app.revanced.integrations.reddit.patches;

import android.view.View;

import app.revanced.integrations.reddit.settings.Settings;

@SuppressWarnings("unused")
public class ToolBarButtonPatch {

    public static void hideToolBarButton(View view) {
        if (!Settings.HIDE_TOOLBAR_BUTTON.get())
            return;

        view.setVisibility(View.GONE);
    }
}
