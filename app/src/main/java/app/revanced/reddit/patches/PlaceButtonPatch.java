package app.revanced.reddit.patches;

import android.view.View;

import app.revanced.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public class PlaceButtonPatch {

    public static void hidePlaceButton(View view) {
        if (!SettingsEnum.HIDE_PLACE_BUTTON.getBoolean())
            return;

        view.setVisibility(View.GONE);
    }
}
