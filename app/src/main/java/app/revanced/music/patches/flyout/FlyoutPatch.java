package app.revanced.music.patches.flyout;

import app.revanced.music.settings.SettingsEnum;

public class FlyoutPatch {

    public static int enableCompactDialog(int original) {
        return SettingsEnum.ENABLE_COMPACT_DIALOG.getBoolean() && original < 600 ? 600 : original;
    }

    public static boolean enableSleepTimer() {
        return SettingsEnum.ENABLE_SLEEP_TIMER.getBoolean();
    }

}
