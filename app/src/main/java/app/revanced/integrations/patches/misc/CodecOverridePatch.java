package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class CodecOverridePatch {

    public static String getManufacturer(String manufacturer) {
        if (SettingsEnum.ENABLE_VP9_CODEC.getBoolean()) {
            return "Google";
        } else if (SettingsEnum.ENABLE_HDR_CODEC.getBoolean()) {
            return "Samsung";
        }
        return manufacturer;
    }

    public static String getBrand(String brand) {
        if (SettingsEnum.ENABLE_VP9_CODEC.getBoolean()) {
            return "google";
        } else if (SettingsEnum.ENABLE_HDR_CODEC.getBoolean()) {
            return "samsung";
        }
        return brand;
    }

    public static String getModel(String model) {
        if (SettingsEnum.ENABLE_VP9_CODEC.getBoolean()) {
            return "Pixel 7 Pro";
        } else if (SettingsEnum.ENABLE_HDR_CODEC.getBoolean()) {
            return "SM-G955W";
        }
        return model;
    }

    private static boolean shouldForceVideoCodec() {
        return SettingsEnum.ENABLE_VP9_CODEC.getBoolean() || SettingsEnum.ENABLE_HDR_CODEC.getBoolean();
    }

    public static boolean shouldForceOpus() {
        return SettingsEnum.ENABLE_OPUS_CODEC.getBoolean();
    }

    public static boolean shouldForceCodec(boolean original) {
        return shouldForceVideoCodec() || original;
    }

    public static int overrideMinHeight(int original) {
        return shouldForceVideoCodec() ? 64 : original;
    }

    public static int overrideMaxHeight(int original) {
        return shouldForceVideoCodec() ? 4096 : original;
    }

    public static int overrideMinWidth(int original) {
        return shouldForceVideoCodec() ? 64 : original;
    }

    public static int overrideMaxWidth(int original) {
        return shouldForceVideoCodec() ? 4096 : original;
    }
}
