package app.revanced.integrations.patches.misc;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class ProtobufSpoofPatch {
    /**
     * Protobuf parameters used by the player.
     * Known issue: YouTube client recognizes generic player as shorts video
     */
    private static final String GENERAL_PROTOBUF_PARAMETER = "CgIQBg";

    /**
     * Protobuf parameters used for general fixes.
     * Known issue: thumbnails not showing when tapping the seekbar
     */
    private static final String SHORTS_PROTOBUF_PARAMETER = "8AEB";

    /**
     * Target Protobuf parameters.
     * Used by the generic player.
     */
    private static final String TARGET_PROTOBUF_PARAMETER = "YADI";

    private static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";


    public static String getProtobufOverride(String original) {
        if (!SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean()
                || Objects.equals(ReVancedUtils.getContext().getPackageName(), YOUTUBE_PACKAGE_NAME))
            return original;

        if (original.startsWith(TARGET_PROTOBUF_PARAMETER)
                || original.equals(""))
            original = SettingsEnum.SPOOFING_TYPE.getBoolean() ? SHORTS_PROTOBUF_PARAMETER : GENERAL_PROTOBUF_PARAMETER;

        return original;
    }
}
