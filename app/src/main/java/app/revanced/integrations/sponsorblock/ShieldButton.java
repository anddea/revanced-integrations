package app.revanced.integrations.sponsorblock;

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
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.utils.LogHelper;

public class ShieldButton {
    @SuppressLint("StaticFieldLeak")
    static RelativeLayout youtubeControlsLayout;
    static WeakReference<ImageView> buttonview = new WeakReference<>(null);
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    static boolean isShowing;

    public static void initialize(Object viewStub) {
        try {
            youtubeControlsLayout = (RelativeLayout) viewStub;

            ImageView imageView = findView(ShieldButton.class, youtubeControlsLayout, "sponsorblock_button");

            if (imageView == null) return;
            imageView.setOnClickListener(SponsorBlockUtils.shieldButtonListener);
            buttonview = new WeakReference<>(imageView);

            fadeDurationFast = integer("fade_duration_fast");
            fadeDurationScheduled = integer("fade_duration_scheduled");

            fadeIn = anim("fade_in");
            fadeIn.setDuration(fadeDurationFast);

            fadeOut = anim("fade_out");
            fadeOut.setDuration(fadeDurationScheduled);
            isShowing = true;
            changeVisibilityImmediate(false);
        } catch (Exception ex) {
            LogHelper.printException(ShieldButton.class, "Unable to set RelativeLayout", ex);
        }
    }

    public static void changeVisibilityImmediate(boolean visible) {
        changeVisibility(visible, true);
    }

    public static void changeVisibilityNegatedImmediate(boolean visible) {
        changeVisibility(!visible, true);
    }

    public static void changeVisibility(boolean visible) {
        changeVisibility(visible, false);
    }

    public static void changeVisibility(boolean visible, boolean immediate) {
        ImageView imageView = buttonview.get();
        if (isShowing == visible || youtubeControlsLayout == null || imageView == null) return;

        isShowing = visible;
        if (visible && shouldBeShown()) {
            if (PlayerController.lastKnownVideoTime >= PlayerController.lastKnownVideoLength) return;
            imageView.setVisibility(View.VISIBLE);
            if (!immediate)
                imageView.startAnimation(fadeIn);
            return;
        }

        if (imageView.getVisibility() == View.VISIBLE) {
            if (!immediate)
                imageView.startAnimation(fadeOut);
            imageView.setVisibility(shouldBeShown() ? View.INVISIBLE : View.GONE);
        }
    }

    static boolean shouldBeShown() {
        return SettingsEnum.SB_ENABLED.getBoolean() && SettingsEnum.SB_NEW_SEGMENT_ENABLED.getBoolean();
    }
}
