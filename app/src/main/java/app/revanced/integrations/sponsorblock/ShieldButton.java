package app.revanced.integrations.sponsorblock;

import static app.revanced.integrations.patches.video.VideoInformation.isVideoEnd;
import static app.revanced.integrations.utils.ResourceUtils.anim;
import static app.revanced.integrations.utils.ResourceUtils.findView;
import static app.revanced.integrations.utils.ResourceUtils.integer;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class ShieldButton {
    @SuppressLint("StaticFieldLeak")
    static RelativeLayout youtubeControlsLayout;
    static WeakReference<ImageView> buttonView = new WeakReference<>(null);
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    public static boolean isButtonEnabled;
    static boolean isShowing;

    public static void initialize(Object viewStub) {
        try {
            youtubeControlsLayout = (RelativeLayout) viewStub;
            isButtonEnabled = setValue();
            ImageView imageView = findView(ShieldButton.class, youtubeControlsLayout, "sponsorblock_button");

            imageView.setOnClickListener(SponsorBlockUtils.shieldButtonListener);
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
            LogHelper.printException(ShieldButton.class, "Unable to set RelativeLayout", ex);
        }
    }

    public static void changeVisibility(boolean currentVisibility) {
        ImageView imageView = buttonView.get();
        if (isShowing == currentVisibility || youtubeControlsLayout == null || imageView == null) return;

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
        return SettingsEnum.SB_ENABLED.getBoolean() && SettingsEnum.SB_NEW_SEGMENT_ENABLED.getBoolean() && !isVideoEnd();
    }
}
