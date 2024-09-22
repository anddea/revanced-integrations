package app.revanced.integrations.youtube.patches.overlaybutton;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.youtube.settings.Settings;

import static app.revanced.integrations.shared.utils.Utils.getActivity;

@SuppressWarnings("unused")
public class SetBrightness extends BottomControlButton {
    @Nullable
    private static SetBrightness instance;
    private static float previousBrightness = -2f;

    private SetBrightness(ViewGroup bottomControlsViewGroup) {
        super(
                bottomControlsViewGroup,
                "set_brightness_button",
                Settings.OVERLAY_BUTTON_SET_BRIGHTNESS,
                view -> {
                    try {
                        Activity activity = getActivity();
                        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
                        Logger.printInfo(() -> "Current Brightness: " + layoutParams.screenBrightness);

                        if (previousBrightness == -2f) {
                            previousBrightness = (float) android.provider.Settings.System.getInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                        }

                        float newBrightness = (layoutParams.screenBrightness == 0f) ? previousBrightness : 0f;
                        layoutParams.screenBrightness = newBrightness;
                        activity.getWindow().setAttributes(layoutParams);

                        if (instance != null) {
                            changeActivated(instance);
                        }

                        Logger.printInfo(() -> "Brightness set to: " + newBrightness);
                    } catch (Exception e) {
                        Logger.printException(() -> "Error toggling brightness", e);
                    }
                },
                view -> {
                    try {
                        Activity activity = getActivity();
                        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
                        if (previousBrightness == -2f) {
                            previousBrightness = (float) android.provider.Settings.System.getInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                        }
                        layoutParams.screenBrightness = 1f;
                        activity.getWindow().setAttributes(layoutParams);

                        if (instance != null) {
                            changeActivated(instance);
                        }

                        Logger.printInfo(() -> "Brightness set to maximum");
                    } catch (Exception e) {
                        Logger.printException(() -> "Error setting brightness to maximum", e);
                    }
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
        try {
            Activity activity = getActivity();
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            boolean isMinBrightness = layoutParams.screenBrightness <= 0.4f;
            instance.changeActivated(isMinBrightness);
        } catch (Exception e) {
            Logger.printException(() -> "Error updating activation state", e);
        }
    }
}
