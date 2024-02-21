package app.revanced.integrations.youtube.patches.overlaybutton;

import static app.revanced.integrations.youtube.utils.ResourceUtils.anim;
import static app.revanced.integrations.youtube.utils.ResourceUtils.findView;
import static app.revanced.integrations.youtube.utils.ResourceUtils.integer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.VideoHelpers;

public class PlaylistFromChannelVideos {
    volatile static boolean isButtonEnabled;
    volatile static boolean isShowing;
    volatile static boolean isScrubbed;
    static WeakReference<ImageView> buttonView = new WeakReference<>(null);
    @SuppressLint("StaticFieldLeak")
    static ConstraintLayout constraintLayout;
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;

    static final ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#ff3ea6ff"), PorterDuff.Mode.SRC_ATOP);
    public static void initialize(Object obj) {
        try {
            constraintLayout = (ConstraintLayout) obj;
            isButtonEnabled = setValue();
            ImageView imageView = findView(constraintLayout, "play_all_channel_button");

            imageView.setOnClickListener(view -> {
                if (imageView.getColorFilter() == cf)
                    return;

                VideoHelpers.playlistFromChannelVideosListener(view.getContext(), true);
                imageView.setColorFilter(cf);
            });
            imageView.setOnLongClickListener(view -> {
                if (imageView.getColorFilter() != cf)
                    return true;

                VideoHelpers.playlistFromChannelVideosListener(view.getContext(), false);
                imageView.clearColorFilter();
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
            isScrubbed = false;
            changeVisibility(false);

        } catch (Exception ex) {
            LogHelper.printException(() -> "Unable to set FrameLayout", ex);
        }
    }

    public static void changeVisibility(boolean currentVisibility) {
        ImageView imageView = buttonView.get();

        if (isShowing == currentVisibility || constraintLayout == null || imageView == null)
            return;

        isShowing = currentVisibility;

        if (isScrubbed && isButtonEnabled) {
            isScrubbed = false;
            imageView.setVisibility(View.VISIBLE);
            return;
        }

        if (currentVisibility && isButtonEnabled) {
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(fadeIn);
        } else if (imageView.getVisibility() == View.VISIBLE) {
            imageView.startAnimation(fadeOut);
            imageView.setVisibility(View.GONE);
        }
    }

    public static void changeVisibilityNegatedImmediate(boolean isUserScrubbing) {
        ImageView imageView = buttonView.get();

        if (constraintLayout == null || imageView == null || !isUserScrubbing)
            return;

        isShowing = false;
        isScrubbed = true;
        imageView.setVisibility(View.GONE);
    }

    public static void refreshVisibility() {
        isButtonEnabled = setValue();
    }

    private static boolean setValue() {
        return SettingsEnum.OVERLAY_BUTTON_PLAYLIST.getBoolean();
    }
}
