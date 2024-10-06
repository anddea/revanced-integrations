package app.revanced.integrations.youtube.patches.overlaybutton;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings.System;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.youtube.settings.Settings;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static app.revanced.integrations.shared.utils.Utils.getActivity;

@SuppressWarnings("unused")
public class SetBrightness extends BottomControlButton {
    @Nullable
    private static SetBrightness instance;
    private static int previousBrightness = -1;

    private SetBrightness(ViewGroup bottomControlsViewGroup) {
        super(
                bottomControlsViewGroup,
                "set_brightness_button",
                Settings.OVERLAY_BUTTON_SET_BRIGHTNESS,
                view -> {
                    try {
                        Activity activity = getActivity();
                        ContentResolver contentResolver = activity.getContentResolver();

                        if (!System.canWrite(activity)) {
                            Intent intent = new Intent(ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + ((Context) activity).getPackageName()));
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            ((Context) activity).startActivity(intent);
                            return;
                        }

                        int currentBrightness = System.getInt(contentResolver, SCREEN_BRIGHTNESS);

                        if (previousBrightness == -1)
                            previousBrightness = currentBrightness;

                        Logger.printInfo(() -> "Current brightness: " + currentBrightness);

                        int newBrightness = currentBrightness == 0 ? previousBrightness : 0;

                        System.putInt(contentResolver, SCREEN_BRIGHTNESS, newBrightness);

                        if (instance != null)
                            changeActivated(instance);

                        Logger.printInfo(() -> "Brightness set to: " + newBrightness);

                    } catch (Exception e) {
                        Logger.printException(() -> "Error toggling brightness", e);
                    }
                },
                view -> {
                    try {
                        Activity activity = getActivity();
                        ContentResolver contentResolver = activity.getContentResolver();

                        if (!System.canWrite(activity)) {
                            Intent intent = new Intent(ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + ((Context) activity).getPackageName()));
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            ((Context) activity).startActivity(intent);
                            return true;
                        }

                        int currentBrightness = System.getInt(contentResolver, SCREEN_BRIGHTNESS);

                        // TODO: why previousBrightness is always 0?
                        if (previousBrightness == -1)
                            previousBrightness = currentBrightness;

                        Logger.printInfo(() -> "Current brightness: " + currentBrightness);

                        int newBrightness = currentBrightness == 255 ? previousBrightness : 255;

                        System.putInt(contentResolver, SCREEN_BRIGHTNESS, newBrightness);

                        if (instance != null)
                            changeActivated(instance);

                        Logger.printInfo(() -> "Brightness set to: " + newBrightness);

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
            ContentResolver contentResolver = getActivity().getContentResolver();
            boolean isBrightnessLowEnough = System.getInt(contentResolver, SCREEN_BRIGHTNESS) <= 120; // less than ~50%
            instance.changeActivated(isBrightnessLowEnough);
        } catch (Exception e) {
            Logger.printException(() -> "Error updating activation state", e);
        }
    }
}
