package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;
import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThread;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;
import static app.revanced.integrations.utils.SharedPrefHelper.saveBoolean;
import static app.revanced.integrations.utils.StringRef.str;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.LogHelper;

public class ProtobufSpoofPatch {
    private static final String PREFERENCE_KEY = "auto_spoofing_enabled";
    private static boolean isPlayingShorts;

    /**
     * Target Protobuf parameters.
     */
    private static final String[] PROTOBUF_PARAMETER_WHITELIST = {
            "YAHI", // Autoplay in feed
            "SAFg"  // Autoplay in scrim
    };

    /**
     * Protobuf parameters used in autoplay in scrim
     * Prepend this parameter to mute video playback (for autoplay in feed)
     */
    private static final String PROTOBUF_PARAMETER_SCRIM = "SAFgAXgB";

    /**
     * Protobuf parameters used in shorts and stories.
     */
    private static final String PROTOBUF_PARAMETER_SHORTS = "8AEB";


    public static String overrideProtobufParameter(String protobufParameter) {
        return SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() ? setProtobufParameter(protobufParameter) : protobufParameter;
    }

    public static String setProtobufParameter(String protobufParameter) {
        // video is Short or Story
        isPlayingShorts = protobufParameter.contains(PROTOBUF_PARAMETER_SHORTS);

        if (isPlayingShorts)
            return protobufParameter;

        boolean isPlayingFeed = containsAny(protobufParameter, PROTOBUF_PARAMETER_WHITELIST)
                && PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL;

        return isPlayingFeed
                ? PROTOBUF_PARAMETER_SCRIM + PROTOBUF_PARAMETER_SHORTS  // autoplay in feed should not play a sound
                : PROTOBUF_PARAMETER_SHORTS;
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
            if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() || getBoolean(YOUTUBE, PREFERENCE_KEY, false)) return;

            SettingsEnum.ENABLE_PROTOBUF_SPOOF.saveValue(true);
            saveBoolean(YOUTUBE, PREFERENCE_KEY, true);
            runOnMainThread(() -> showToastShort(str("revanced_protobuf_spoof_notice")));

        } catch (Exception ex) {
            LogHelper.printException(ProtobufSpoofPatch.class, "onResponse failure", ex);
        }
    }


    /**
     * Injection point.  Overrides values passed into SubtitleWindowSettings constructor.
     *
     * @param ap anchor position. A bitmask with 6 bit fields, that appears to indicate the layout position on screen
     * @param ah anchor horizontal. A percentage [0, 100], that appears to be a horizontal text anchor point
     * @param av anchor vertical. A percentage [0, 100], that appears to be a vertical text anchor point
     * @param vs appears to indicate if subtitles exist, and the value is always true.
     * @param sd function is not entirely clear
     */
    public static int[] getSubtitleWindowSettingsOverride(int ap, int ah, int av, boolean vs, boolean sd) {
        // Videos with custom captions that specify screen positions appear to always have correct screen positions (even with spoofing).
        // But for auto generated and most other captions, the spoof incorrectly gives various default Shorts caption settings.
        // Check for these known default shorts captions parameters, and replace with the known correct values.
        if (SettingsEnum.ENABLE_PROTOBUF_SPOOF.getBoolean() && !isPlayingShorts) { // video is not a Short or Story
            for (SubtitleWindowReplacementSettings setting : SubtitleWindowReplacementSettings.values()) {
                if (setting.match(ap, ah, av, vs, sd))
                    return setting.replacementSetting();
            }
            // Parameters are either subtitles with custom positions, or a set of unidentified (and incorrect) default parameters.
            // The subtitles could be forced to the bottom no matter what, but that would override custom screen positions.
            // For now, just return the original parameters.
        }

        // No matches, pass back the original values
        return new int[]{ap, ah, av};
    }


    /**
     * Known incorrect default Shorts subtitle parameters, and the corresponding correct (non-Shorts) values.
     */
    private enum SubtitleWindowReplacementSettings {
        DEFAULT_SHORTS_PARAMETERS_1(10, 50, 0, true, false,
                34, 50, 95),
        DEFAULT_SHORTS_PARAMETERS_2(9, 20, 0, true, false,
                34, 50, 90),
        DEFAULT_SHORTS_PARAMETERS_3(9, 20, 0, true, true,
                33, 20, 100);

        // original values
        final int ap, ah, av;
        final boolean vs, sd;

        // replacement values
        final int replacementAp, replacementAh, replacementAv;

        SubtitleWindowReplacementSettings(int ap, int ah, int av, boolean vs, boolean sd,
                                          int replacementAp, int replacementAh, int replacementAv) {
            this.ap = ap;
            this.ah = ah;
            this.av = av;
            this.vs = vs;
            this.sd = sd;
            this.replacementAp = replacementAp;
            this.replacementAh = replacementAh;
            this.replacementAv = replacementAv;
        }

        boolean match(int ap, int ah, int av, boolean vs, boolean sd) {
            return this.ap == ap && this.ah == ah && this.av == av && this.vs == vs && this.sd == sd;
        }

        int[] replacementSetting() {
            return new int[]{replacementAp, replacementAh, replacementAv};
        }
    }

}
