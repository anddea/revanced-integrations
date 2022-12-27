package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ResourceType;

public class SeekbarLayoutPatch {
    private static final String OLD_SEEKBAR_COLOR = "yt_brand_red";

    public static boolean enableSeekbarTapping() {
        return SettingsEnum.ENABLE_SEEKBAR_TAPPING.getBoolean();
    }

    public static boolean hideTimeAndSeekbar() {
        return SettingsEnum.HIDE_TIME_AND_SEEKBAR.getBoolean();
    }

    public static int enableOldSeekbarColor(int originalValue) {
        if (SettingsEnum.ENABLE_OLD_SEEKBAR_COLOR.getBoolean()) {
            originalValue = identifier(OLD_SEEKBAR_COLOR, ResourceType.COLOR);
        }
        return originalValue;
    }
}
