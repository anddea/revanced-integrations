package app.revanced.integrations.sponsorblock.ui;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.sponsorblock.objects.SponsorSegment;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.ResourceType;

public class SponsorBlockViewController {
    private static WeakReference<RelativeLayout> inlineSponsorOverlayRef = new WeakReference<>(null);
    private static WeakReference<ViewGroup> youtubeOverlaysLayoutRef = new WeakReference<>(null);
    private static WeakReference<SkipSponsorButton> skipSponsorButtonRef = new WeakReference<>(null);
    private static WeakReference<NewSegmentLayout> newSegmentLayoutRef = new WeakReference<>(null);
    private static boolean canShowViewElements = true;
    @Nullable
    private static SponsorSegment skipSegment;

    static {
        PlayerType.getOnChange().addObserver((PlayerType type) -> {
            playerTypeChanged(type);
            return null;
        });
    }

    public static Context getOverLaysViewGroupContext() {
        ViewGroup group = youtubeOverlaysLayoutRef.get();
        if (group == null) {
            return null;
        }
        return group.getContext();
    }

    /**
     * Injection point.
     */
    public static void initialize(Object obj) {
        try {
            RelativeLayout layout = new RelativeLayout(ReVancedUtils.getContext());
            layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            LayoutInflater.from(ReVancedUtils.getContext()).inflate(identifier("inline_sponsor_overlay", ResourceType.LAYOUT), layout);

            inlineSponsorOverlayRef = new WeakReference<>(layout);

            ViewGroup viewGroup = (ViewGroup) obj;
            viewGroup.addView(layout, viewGroup.getChildCount() - 2);
            youtubeOverlaysLayoutRef = new WeakReference<>(viewGroup);

            skipSponsorButtonRef = new WeakReference<>(
                    Objects.requireNonNull(layout.findViewById(identifier("sb_skip_sponsor_button", ResourceType.ID))));

            newSegmentLayoutRef = new WeakReference<>(
                    Objects.requireNonNull(layout.findViewById(identifier("sb_new_segment_view", ResourceType.ID))));
        } catch (Exception ex) {
            LogHelper.printException(SponsorBlockViewController.class, "initialize failure", ex);
        }
    }

    public static void showSkipButton(@NonNull SponsorSegment info) {
        skipSegment = Objects.requireNonNull(info);
        updateSkipButton();
    }

    public static void hideSkipButton() {
        skipSegment = null;
        updateSkipButton();
    }

    private static void updateSkipButton() {
        SkipSponsorButton skipSponsorButton = skipSponsorButtonRef.get();
        if (skipSponsorButton == null) {
            return;
        }
        if (skipSegment == null) {
            setSkipSponsorButtonVisibility(false);
        } else {
            final boolean layoutNeedsUpdating = skipSponsorButton.updateSkipButtonText(skipSegment);
            if (layoutNeedsUpdating) {
                bringLayoutToFront();
            }
            setSkipSponsorButtonVisibility(true);
        }
    }

    public static void showNewSegmentLayout() {
        setNewSegmentLayoutVisibility(true);
    }

    public static void hideNewSegmentLayout() {
        NewSegmentLayout newSegmentLayout = newSegmentLayoutRef.get();
        if (newSegmentLayout == null) {
            return;
        }
        setNewSegmentLayoutVisibility(false);
    }

    public static void toggleNewSegmentLayoutVisibility() {
        NewSegmentLayout newSegmentLayout = newSegmentLayoutRef.get();
        if (newSegmentLayout == null) {
            LogHelper.printException(SponsorBlockViewController.class, "toggleNewSegmentLayoutVisibility failure");
            return;
        }
        setNewSegmentLayoutVisibility(newSegmentLayout.getVisibility() != View.VISIBLE);
    }

    private static void playerTypeChanged(PlayerType playerType) {
        try {
            final boolean isWatchFullScreen = playerType == PlayerType.WATCH_WHILE_FULLSCREEN;
            canShowViewElements = (isWatchFullScreen || playerType == PlayerType.WATCH_WHILE_MAXIMIZED);

            setSkipButtonMargins(isWatchFullScreen);
            setNewSegmentLayoutMargins(isWatchFullScreen);
            updateSkipButton();
        } catch (Exception ex) {
            LogHelper.printException(SponsorBlockViewController.class, "Player type changed error", ex);
        }
    }

    private static void setSkipButtonMargins(boolean fullScreen) {
        SkipSponsorButton skipSponsorButton = skipSponsorButtonRef.get();
        if (skipSponsorButton == null) {
            LogHelper.printException(SponsorBlockViewController.class, "setSkipButtonMargins failure");
            return;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) skipSponsorButton.getLayoutParams();
        if (params == null) {
            LogHelper.printException(SponsorBlockViewController.class, "setSkipButtonMargins failure");
            return;
        }
        params.bottomMargin = fullScreen ? skipSponsorButton.ctaBottomMargin : skipSponsorButton.defaultBottomMargin;
        skipSponsorButton.setLayoutParams(params);
    }

    private static void setSkipSponsorButtonVisibility(boolean visible) {
        SkipSponsorButton skipSponsorButton = skipSponsorButtonRef.get();
        if (skipSponsorButton == null) {
            LogHelper.printException(SponsorBlockViewController.class, "setSkipSponsorButtonVisibility failure");
            return;
        }

        visible &= canShowViewElements;

        final int desiredVisibility = visible ? View.VISIBLE : View.GONE;
        if (skipSponsorButton.getVisibility() != desiredVisibility) {
            skipSponsorButton.setVisibility(desiredVisibility);
            if (visible) {
                bringLayoutToFront();
            }
        }
    }

    private static void setNewSegmentLayoutMargins(boolean fullScreen) {
        NewSegmentLayout newSegmentLayout = newSegmentLayoutRef.get();
        if (newSegmentLayout == null) {
            LogHelper.printException(SponsorBlockViewController.class, "Unable to setNewSegmentLayoutMargins (button is null)");
            return;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newSegmentLayout.getLayoutParams();
        if (params == null) {
            LogHelper.printException(SponsorBlockViewController.class, "Unable to setNewSegmentLayoutMargins (params are null)");
            return;
        }
        params.bottomMargin = fullScreen ? newSegmentLayout.ctaBottomMargin : newSegmentLayout.defaultBottomMargin;
        newSegmentLayout.setLayoutParams(params);
    }

    private static void setNewSegmentLayoutVisibility(boolean visible) {
        NewSegmentLayout newSegmentLayout = newSegmentLayoutRef.get();
        if (newSegmentLayout == null) {
            LogHelper.printException(SponsorBlockViewController.class, "setNewSegmentLayoutVisibility failure");
            return;
        }

        visible &= canShowViewElements;

        final int desiredVisibility = visible ? View.VISIBLE : View.GONE;
        if (newSegmentLayout.getVisibility() != desiredVisibility) {
            newSegmentLayout.setVisibility(desiredVisibility);
            if (visible) {
                bringLayoutToFront();
            }
        }
    }

    private static void bringLayoutToFront() {
        RelativeLayout layout = inlineSponsorOverlayRef.get();
        if (layout != null) {
            // needed to keep skip button overtop end screen cards
            layout.bringToFront();
            layout.requestLayout();
            layout.invalidate();
        }
    }

    /**
     * Injection point.
     */
    public static void endOfVideoReached() {
        try {
            // the buttons automatically set themselves to visible when appropriate,
            // but if buttons are showing when the end of the video is reached then they need
            // to be forcefully hidden
            if (!SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean()) {
                CreateSegmentButtonController.hide();
                VotingButtonController.hide();
            }
        } catch (Exception ex) {
            LogHelper.printException(SponsorBlockViewController.class, "endOfVideoReached failure", ex);
        }
    }
}
