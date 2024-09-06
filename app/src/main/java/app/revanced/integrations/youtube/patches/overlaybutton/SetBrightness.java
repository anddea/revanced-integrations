package app.revanced.integrations.youtube.patches.overlaybutton;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.youtube.settings.Settings;

import static app.revanced.integrations.shared.utils.Utils.context;

@SuppressWarnings("unused")
public class SetBrightness extends BottomControlButton {
    @Nullable
    private static SetBrightness instance;

    public SetBrightness(ViewGroup bottomControlsViewGroup) {
        super(
                bottomControlsViewGroup,
                "set_brightness_button",
                Settings.OVERLAY_BUTTON_SET_BRIGHTNESS,
                view -> {
                    // between: 0 and 255
                    int brightness = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
                    Logger.printInfo(() -> "Brightness: " + brightness);
                    // TODO: Tap to toggle between minimum and restored brightness
                    //  Restore the previous brightness
                    //  Store the current brightness and set it to minimum
                },
                view -> {
                    // TODO: Long press to set brightness to maximum
                    return true;
                }
        );
    }

    /**
     * Injection point.
     */
    public static void initialize(View bottomControlsViewGroup) {
        try {
            if (bottomControlsViewGroup instanceof ViewGroup viewGroup) {
                instance = new SetBrightness(viewGroup);
            }
        } catch (Exception ex) {
            Logger.printException(() -> "initialize failure", ex);
        }
    }

    /**
     * Injection point.
     */
    public static void changeVisibility(boolean showing, boolean animation) {
        if (instance != null) {
            instance.setVisibility(showing, animation);
            changeActivated(instance);
        }
    }

    public static void changeVisibilityNegatedImmediate() {
        if (instance != null) {
            instance.setVisibilityNegatedImmediate();
            changeActivated(instance);
        }
    }

    private static void changeActivated(SetBrightness instance) {
        // TODO: Make this dynamic based on the current brightness
        instance.changeActivated(true);
    }
}
