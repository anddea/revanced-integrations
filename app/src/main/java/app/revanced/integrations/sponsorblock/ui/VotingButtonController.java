package app.revanced.integrations.sponsorblock.ui;

import static app.revanced.integrations.sponsorblock.SegmentPlaybackController.currentVideoHasSegments;
import static app.revanced.integrations.utils.ResourceUtils.anim;
import static app.revanced.integrations.utils.ResourceUtils.findView;
import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.ResourceUtils.integer;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.SponsorBlockUtils;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.ResourceType;

public class VotingButtonController {
    private static WeakReference<ImageView> buttonReference = new WeakReference<>(null);
    private static Animation fadeIn;
    private static Animation fadeOut;
    private static boolean isShowing;

    /**
     * injection point
     */
    public static void initialize(Object viewStub) {
        try {
            RelativeLayout controlsLayout = (RelativeLayout) viewStub;
            String buttonResourceName = "sb_voting_button";
            ImageView imageView = controlsLayout.findViewById(identifier(buttonResourceName, ResourceType.ID));
            if (imageView == null) return;

            imageView.setOnClickListener(v -> SponsorBlockUtils.onVotingClicked(v.getContext()));
            buttonReference = new WeakReference<>(imageView);

            // Animations
            if (fadeIn == null) {
                fadeIn = anim("fade_in");
                fadeIn.setDuration(integer("fade_duration_fast"));
                fadeOut = anim("fade_out");
                fadeOut.setDuration(integer("fade_duration_scheduled"));
            }
            isShowing = true;
            changeVisibilityImmediate(false);
        } catch (Exception ex) {
            LogHelper.printException(VotingButtonController.class, "Unable to set RelativeLayout", ex);
        }
    }

    public static void changeVisibilityImmediate(boolean visible) {
        changeVisibility(visible, true);
    }

    /**
     * injection point
     */
    public static void changeVisibilityNegatedImmediate(boolean visible) {
        changeVisibility(!visible, true);
    }

    /**
     * injection point
     */
    public static void changeVisibility(boolean visible) {
        changeVisibility(visible, false);
    }

    public static void changeVisibility(boolean visible, boolean immediate) {
        try {
            if (isShowing == visible) return;
            isShowing = visible;

            ImageView iView = buttonReference.get();
            if (iView == null) return;

            if (visible) {
                iView.clearAnimation();
                if (!shouldBeShown()) {
                    return;
                }
                if (!immediate) {
                    iView.startAnimation(fadeIn);
                }
                iView.setVisibility(View.VISIBLE);
                return;
            }

            if (iView.getVisibility() == View.VISIBLE) {
                iView.clearAnimation();
                if (!immediate) {
                    iView.startAnimation(fadeOut);
                }
                iView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            LogHelper.printException(VotingButtonController.class, "changeVisibility failure", ex);
        }
    }

    private static boolean shouldBeShown() {
        return SettingsEnum.SB_ENABLED.getBoolean() && SettingsEnum.SB_VOTING_ENABLED.getBoolean()
                && !VideoInformation.isAtEndOfVideo() && currentVideoHasSegments();
    }

    public static void hide() {
        if (!isShowing) {
            return;
        }
        ReVancedUtils.verifyOnMainThread();
        View v = buttonReference.get();
        if (v == null) {
            return;
        }
        v.setVisibility(View.GONE);
        isShowing = false;
    }
}
