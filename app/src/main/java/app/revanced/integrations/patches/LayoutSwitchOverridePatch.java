package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class LayoutSwitchOverridePatch {

    public static int getLayoutSwitchOverride(int original) {
        if (SettingsEnum.TABLET_LAYOUT.getBoolean()) return 720;
        if (SettingsEnum.PHONE_LAYOUT.getBoolean()) return 480;
        return original;
    }
}
