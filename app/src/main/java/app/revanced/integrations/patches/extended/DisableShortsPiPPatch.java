package app.revanced.integrations.patches.extended;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;

public class DisableShortsPiPPatch {

    public static boolean disableShortsPlayerPiP(boolean original) {
        return !(SettingsEnum.DISABLE_SHORTS_PLAYER_PIP.getBoolean() && PlayerType.getCurrent() == PlayerType.NONE) && original;
    }

}
