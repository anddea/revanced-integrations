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
     * Fix side issue when spoofing with shorts parameter.
     * @param ap anchor position configuration
     * @param ah anchorHorizontal
     * @param av anchorVertical
     */
    public static int overrideAnchorPosition(int ap, int ah, int av) {
        if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() && !PlayerType.getCurrent().isNoneOrHidden() && ah != 0 && av == 0)
            ap = 34;
        return ap;
    }

    public static int overrideAnchorVerticalPosition(int ah, int av) {
        if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() && !PlayerType.getCurrent().isNoneOrHidden() && ah != 0 && av == 0)
            av = 95;
        return av;
    }

}
