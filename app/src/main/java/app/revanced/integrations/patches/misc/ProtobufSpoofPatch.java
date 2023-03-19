package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;

import app.revanced.integrations.settings.SettingsEnum;

public class ProtobufSpoofPatch {
    /**
     * Target Protobuf parameters.
     */
    private static final String[] TARGET_PROTOBUF_PARAMETER = {
            "YADI", // Generic player
            "wgIG", // Play video in notification
            "6AQB", // Open video from external app (e.g. PlayStore, Google News)
            "sAQB" // Play videos from YouTube Premium users only content
    };

    /**
     * Protobuf parameters used for general fixes.
     * Known issue: thumbnails not showing when tapping the seekbar
     */
    private static final String PROTOBUF_PARAMETER_GENERAL = "CgIQBg";

    /**
     * Protobuf parameters used by the player.
     * Known issue: captions are positioned above the player
     */
    private static final String PROTOBUF_PARAMETER_SHORTS = "8AEB";


    public static String getProtobufOverride(String original) {
        if (!SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean())
            return original;

        if (containsAny(original, TARGET_PROTOBUF_PARAMETER)
                || original.isEmpty())
            original = SettingsEnum.SPOOFING_TYPE.getBoolean() ? PROTOBUF_PARAMETER_SHORTS : PROTOBUF_PARAMETER_GENERAL;

        return original;
    }
}
