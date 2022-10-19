package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class FullscreenButtonContainerRemoverPatch {

    public static void HideFullscreenButtonContainer(View view) {
        if (SettingsEnum.FULLSCREEN_BUTTON_CONTAINER_SHOWN.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }
}
