package app.revanced.integrations.patches.video;

import app.revanced.integrations.settings.SettingsEnum;

public class VideoRestrictionPatch {

    public static int overrideLowerRange(int original) {
        return SettingsEnum.VERTICAL_VIDEO_RESTRICTIONS.getBoolean() ? 64 : original;
    }

    public static int overrideUpperRange(int original) {
        return SettingsEnum.VERTICAL_VIDEO_RESTRICTIONS.getBoolean() ? 4096 : original;
    }
}
