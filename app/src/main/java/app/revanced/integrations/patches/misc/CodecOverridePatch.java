package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class CodecOverridePatch {

    public static String getManufacturer(String original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? "Google" : original;
    }

    public static String getBrand(String original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? "google" : original;
    }

    public static String getModel(String original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? "Pixel 7 Pro" : original;
    }

    public static boolean shouldForceVP9(boolean original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() || original;
    }

    public static int overrideMinHeight(int original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? 64 : original;
    }

    public static int overrideMaxHeight(int original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? 3840 : original;
    }

    public static int overrideMinWidth(int original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? 64 : original;
    }

    public static int overrideMaxWidth(int original) {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() ? 2160 : original;
    }
}
