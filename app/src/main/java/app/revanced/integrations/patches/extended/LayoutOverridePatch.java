package app.revanced.integrations.patches.extended;

import app.revanced.integrations.settings.SettingsEnum;

public class LayoutOverridePatch {

    public static int getLayoutOverride(int original) {
        if (SettingsEnum.ENABLE_TABLET_LAYOUT.getBoolean()) return 720;
        else if (SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean()) return 480;
        return original;
    }
}
