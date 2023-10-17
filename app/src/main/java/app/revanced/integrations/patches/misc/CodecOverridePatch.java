package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;

public class CodecOverridePatch {

    public static String getManufacturer(String manufacturer) {
        if (!SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean())
            return manufacturer;

        return SettingsEnum.ENABLE_VIDEO_CODEC_TYPE.getBoolean() ? "Samsung" : "Google";
    }

    public static String getBrand(String brand) {
        if (!SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean())
            return brand;

        return SettingsEnum.ENABLE_VIDEO_CODEC_TYPE.getBoolean() ? "samsung" : "google";
    }

    public static String getModel(String model) {
        if (!SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean())
            return model;

        return SettingsEnum.ENABLE_VIDEO_CODEC_TYPE.getBoolean() ? "SM-G955W" : "Pixel 7 Pro";
    }

    public static boolean shouldForceOpus() {
        return SettingsEnum.ENABLE_OPUS_CODEC.getBoolean();
    }

    public static boolean shouldForceCodec(boolean original) {
        return SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() || original;
    }

    public static int overrideMinHeight(int original) {
        return SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() ? 64 : original;
    }

    public static int overrideMaxHeight(int original) {
        return SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() ? 4096 : original;
    }

    public static int overrideMinWidth(int original) {
        return SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() ? 64 : original;
    }

    public static int overrideMaxWidth(int original) {
        return SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() ? 4096 : original;
    }
}
