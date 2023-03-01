package app.revanced.integrations.patches.button;

import static app.revanced.integrations.patches.video.VideoSpeedPatch.overrideSpeed;
import static app.revanced.integrations.utils.ResourceUtils.anim;
import static app.revanced.integrations.utils.ResourceUtils.findView;
import static app.revanced.integrations.utils.ResourceUtils.integer;
import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.VideoHelpers;

public class Speed {
    static WeakReference<ImageView> buttonView = new WeakReference<>(null);
    @SuppressLint("StaticFieldLeak")
    static ConstraintLayout constraintLayout;
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    public static boolean isButtonEnabled;
    static boolean isShowing;

    public static void initialize(Object obj) {
        try {
            constraintLayout = (ConstraintLayout) obj;
            isButtonEnabled = setValue();
            ImageView imageView = findView(Speed.class, constraintLayout, "speed_button");

            imageView.setOnClickListener(view -> VideoHelpers.videoSpeedDialogListener(view.getContext()));
            imageView.setOnLongClickListener(view -> {
                overrideSpeed(1.0f);
                Toast.makeText(view.getContext(), str("revanced_overlay_button_speed_reset"), Toast.LENGTH_SHORT).show();
                return true;
            });
            buttonView = new WeakReference<>(imageView);

            fadeDurationFast = integer("fade_duration_fast");
            fadeDurationScheduled = integer("fade_duration_scheduled");

            fadeIn = anim("fade_in");
            fadeIn.setDuration(fadeDurationFast);

            fadeOut = anim("fade_out");
            fadeOut.setDuration(fadeDurationScheduled);

            isShowing = true;
            changeVisibility(false);

        } catch (Exception ex) {
            LogHelper.printException(Speed.class, "Unable to set FrameLayout", ex);
        }
    }

    public static void changeVisibility(boolean currentVisibility) {
        ImageView imageView = buttonView.get();
        if (isShowing == currentVisibility || constraintLayout == null || imageView == null) return;

        isShowing = currentVisibility;
        if (currentVisibility && isButtonEnabled) {
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(fadeIn);
        } else if (imageView.getVisibility() == View.VISIBLE) {
            imageView.startAnimation(fadeOut);
            imageView.setVisibility(View.GONE);
        }
    }

    public static void refreshVisibility() {
        isButtonEnabled = setValue();
    }

    private static boolean setValue() {
        return SettingsEnum.OVERLAY_BUTTON_SPEED.getBoolean();
    }
}
