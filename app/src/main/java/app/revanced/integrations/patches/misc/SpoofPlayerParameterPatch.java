package app.revanced.integrations.patches.misc;

import static app.revanced.integrations.utils.ReVancedUtils.containsAny;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.LogHelper;

public class SpoofPlayerParameterPatch {

    /**
     * Target player parameters.
     */
    private static final String[] PLAYER_PARAMETER_WHITELIST = {
            "YAHI", // Autoplay in feed
            "SAFg"  // Autoplay in scrim
    };

    /**
     * Player parameters parameters used in autoplay in scrim
     * Prepend this parameter to mute video playback (for autoplay in feed)
     */
    private static final String PLAYER_PARAMETER_SCRIM = "SAFgAXgB";

    /**
     * Player parameters used in incognito mode's visitor data.
     * Known issue: ambient mode may not work.
     * Known issue: downloading videos may not work.
     * Known issue: seekbar thumbnails are hidden.
     */
    private static final String PLAYER_PARAMETER_INCOGNITO = "CgIQBg==";

    /**
     * Injection point.
     *
     * @param originalValue originalValue player parameter
     */
    public static String overridePlayerParameter(String originalValue) {
        try {
            if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean() || originalValue.startsWith("8AEB")) {
                return originalValue;
            }

            LogHelper.printDebug(SpoofPlayerParameterPatch.class, "Original protobuf parameter value: " + originalValue);

            boolean isPlayingFeed = containsAny(originalValue, PLAYER_PARAMETER_WHITELIST) && PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL;
            if (isPlayingFeed) {
                // Videos in feed won't autoplay with sound.
                return PLAYER_PARAMETER_SCRIM + PLAYER_PARAMETER_INCOGNITO;
            } else {
                // Spoof the parameter to prevent playback issues.
                return PLAYER_PARAMETER_INCOGNITO;
            }
        } catch (Exception ex) {
            LogHelper.printException(SpoofPlayerParameterPatch.class, "overrideProtobufParameter failure", ex);
        }

        return originalValue;
    }


    /**
     * Injection point.
     */
    public static boolean getSeekbarThumbnailOverrideValue() {
        return SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean();
    }

    /**
     * Injection point.
     *
     * @param view seekbar thumbnail view.  Includes both shorts and regular videos.
     */
    public static void seekbarImageViewCreated(ImageView view) {
        try {
            if (SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean()) {
                view.setVisibility(View.GONE);
                // Also hide the white border around the thumbnail (otherwise a 1 pixel wide bordered frame is visible).
                ViewGroup parentLayout = (ViewGroup) view.getParent();
                parentLayout.setPadding(0, 0, 0, 0);
            }
        } catch (Exception ex) {
            LogHelper.printException(SpoofPlayerParameterPatch.class, "seekbarImageViewCreated failure", ex);
        }
    }
}
