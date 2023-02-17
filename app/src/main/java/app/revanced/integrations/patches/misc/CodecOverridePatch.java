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

}
