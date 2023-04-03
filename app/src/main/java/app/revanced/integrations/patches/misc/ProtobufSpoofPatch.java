package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;
import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThread;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.StringRef.str;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.LogHelper;

public class ProtobufSpoofPatch {
    /**
     * Target Protobuf parameters.
     */
    private static final String[] PROTOBUF_PARAMETER_WHITELIST = {
            "8AEB", // Play video in shorts and stories
            "YAHIAQ", // Autoplay in feed
            "SAFgAxgB" // Autoplay in scrim
    };

    /**
     * Protobuf parameters used by the player.
     */
    private static final String PROTOBUF_PARAMETER_SHORTS = "8AEB";


    public static String overrideProtobufParameter(String original) {
        if (!SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean()
                || containsAny(original, PROTOBUF_PARAMETER_WHITELIST))
            return original;

        return PROTOBUF_PARAMETER_SHORTS;
    }

    /**
     * Injection point. Runs off the main thread.
     * <p>
     * This method is called when returning a 403 response, and switches Protobuf Spoof.
     *
     */
    public static void switchProtobufSpoof() {
        try {
            // already enabled or autoplay in the feed
            if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() || PlayerType.getCurrent().isNoneOrHidden()) return;

            SettingsEnum.ENABLE_PROTOBUF_SPOOF.saveValue(true);
            runOnMainThread(() -> {
                showToastShort(str("revanced_protobuf_spoof_notice"));
                showToastShort(str("sb_switching_success"));
            });

        } catch (Exception ex) {
            LogHelper.printException(ProtobufSpoofPatch.class, "onResponse failure", ex);
        }
    }

    /**
     * Injection point.  Overrides values passed into SubtitleWindowSettings constructor.
     *
     * @param anchorPosition       bitmask with 6 bit fields, that appears to be indicate the layout position on screen
     * @param anchorHorizontal     percentage [0, 100], that appears to be a horizontal text anchor point
     * @param anchorVertical       percentage [0, 100], that appears to be a vertical text anchor point
     * @param vs                   appears to indicate is subtitles exist, and value is always true.
     * @param sd                   appears to indicate if video has non standard aspect ratio (4:3, or a rotated orientation)
     *                             Always true for Shorts playback.
     */
    public static int[] getSubtitleWindowSettingsOverride(int anchorPosition, int anchorHorizontal, int anchorVertical,
                                                          boolean vs, boolean sd) {
        int[] override = {anchorPosition, anchorHorizontal, anchorVertical};

        // Videos with custom captions that specify screen positions appear to always have correct screen positions (even with spoofing).
        // But for auto generated and most other captions, the spoof incorrectly gives Shorts caption settings for all videos.
        // Override the parameters if the video is not a Short but it has Short caption settings.
        if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean()
                && !PlayerType.getCurrent().isNoneOrHidden()) {
            if (sd) {
                // values observed during playback
                override[0] = 33;
                override[1] = 20;
                override[2] = 100;
            } else {
                // Default values used for regular (non Shorts) playback of videos with a standard aspect ratio
                // Values are found in SubtitleWindowSettings static field
                override[0] = 34;
                override[1] = 50;
                override[2] = 95;
            }
        }

        return override;
    }

}
