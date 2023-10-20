package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getFloat;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.StringRef.str;

import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class SpeedOverlayPatch {
    /**
     * Maximum playback speed, exclusive value.
     * Speed overlay value must be less than this value.
     */
    public static final float MAXIMUM_PLAYBACK_SPEED = 8;

    static {
        loadSpeeds();
    }

    private static void resetSpeed(@NonNull String toastMessage) {
        ReVancedUtils.showToastLong(toastMessage);
        SettingsEnum.EDIT_SPEED_OVERLAY_VALUE.saveValue(SettingsEnum.EDIT_SPEED_OVERLAY_VALUE.defaultValue);
        loadSpeeds();
    }

    private static void loadSpeeds() {
        try {
            float speed = getFloat(REVANCED, SettingsEnum.EDIT_SPEED_OVERLAY_VALUE.path, 2.0f);
            if (speed == 2.0f)
                return;

            if (speed <= 0 || speed > MAXIMUM_PLAYBACK_SPEED)
                resetSpeed(str("revanced_custom_playback_speeds_warning", MAXIMUM_PLAYBACK_SPEED + ""));
        } catch (Exception ex) {
            LogHelper.printException(SpeedOverlayPatch.class, "parse error", ex);
            resetSpeed(str("revanced_custom_playback_speeds_invalid"));
        }
    }

    public static float getSpeed(final float original) {
        try {
            return getFloat(REVANCED, SettingsEnum.EDIT_SPEED_OVERLAY_VALUE.path, 2.0f);
        } catch (Exception ignored) {
            return original;
        }
    }

    public static CharSequence getSpeedText(TextView textView, CharSequence original, int speedMasterEduTextId) {
        if (textView == null || textView.getId() != speedMasterEduTextId)
            return original;

        final String speedString = getString(REVANCED, SettingsEnum.EDIT_SPEED_OVERLAY_VALUE.path, "2.0");

        if (speedString.equals("2.0"))
            return original;

        return str("revanced_speed_overlay_text", speedString);
    }

}
