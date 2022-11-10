package app.revanced.integrations.patches;

import app.revanced.integrations.settings.SettingsEnum;

public class HideInfocardsPatch {

    public static int hideInfoCard() {
        return SettingsEnum.INFO_CARDS_SHOWN.getBoolean() ? 0 : 8;
    }

}
