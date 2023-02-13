package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;

public class MinimizedPlaybackPatch {

    public static boolean enableMinimizedPlayback() {
        return SettingsEnum.ENABLE_MINIMIZED_PLAYBACK.getBoolean();
    }

    public static boolean isNotPlayingShorts(boolean isPipEnabled) {
        return !PlayerType.getCurrent().isNoneOrHidden() && isPipEnabled;
    }

}
