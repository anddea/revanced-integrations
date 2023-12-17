package app.revanced.integrations.patches.bottomplayer;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class BottomPlayerPatch {

    public static boolean enableBottomPlayerGestures() {
        return SettingsEnum.ENABLE_BOTTOM_PLAYER_GESTURES.getBoolean();
    }

}