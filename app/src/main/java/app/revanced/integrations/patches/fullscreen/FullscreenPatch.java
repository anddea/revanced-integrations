package app.revanced.integrations.patches.fullscreen;

import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.FrameLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class FullscreenPatch {
    private static final int DEFAULT_MARGIN_TOP = (int) SettingsEnum.QUICK_ACTIONS_MARGIN_TOP.defaultValue;
    @SuppressLint("StaticFieldLeak")
    public static Activity watchDescriptorActivity;
    private static boolean isLandScapeVideo = true;

    public static boolean disableAmbientMode() {
        return !SettingsEnum.DISABLE_AMBIENT_MODE_IN_FULLSCREEN.getBoolean();
    }

    public static boolean disableLandScapeMode(boolean original) {
        return SettingsEnum.DISABLE_LANDSCAPE_MODE.getBoolean() || original;
    }

    public static boolean enableCompactControlsOverlay(boolean original) {
        return SettingsEnum.ENABLE_COMPACT_CONTROLS_OVERLAY.getBoolean() || original;
    }

    public static boolean forceFullscreen(boolean original) {
        if (!SettingsEnum.FORCE_FULLSCREEN.getBoolean())
            return original;

        ReVancedUtils.runOnMainThreadDelayed(FullscreenPatch::setOrientation, 1000);
        return true;
    }

    public static boolean hideAutoPlayPreview() {
        return SettingsEnum.HIDE_AUTOPLAY_PREVIEW.getBoolean() || SettingsEnum.HIDE_AUTOPLAY_BUTTON.getBoolean();
    }

    public static boolean hideEndScreenOverlay() {
        return SettingsEnum.HIDE_END_SCREEN_OVERLAY.getBoolean();
    }

    public static int hideFullscreenPanels() {
        return SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean() ? 8 : 0;
    }

    public static void hideFullscreenPanels(CoordinatorLayout coordinatorLayout) {
        if (!SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean()) return;
        coordinatorLayout.setVisibility(View.GONE);
    }

    public static void hideQuickActions(View view) {
        ReVancedUtils.hideViewBy0dpUnderCondition(
                SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean() || SettingsEnum.HIDE_QUICK_ACTIONS.getBoolean(),
                view
        );
    }

    private static void setOrientation() {
        final int requestedOrientation = isLandScapeVideo
                ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : watchDescriptorActivity.getRequestedOrientation();

        watchDescriptorActivity.setRequestedOrientation(requestedOrientation);
    }

    public static void setQuickActionMargin(FrameLayout frameLayout) {
        int marginTop = SettingsEnum.QUICK_ACTIONS_MARGIN_TOP.getInt();

        if (marginTop < 0 || marginTop > 100) {
            ReVancedUtils.showToastShort(str("revanced_quick_actions_margin_top_warning"));
            SettingsEnum.CUSTOM_PLAYER_OVERLAY_OPACITY.saveValue(DEFAULT_MARGIN_TOP);
            marginTop = DEFAULT_MARGIN_TOP;
        }

        if (!(frameLayout.getLayoutParams() instanceof FrameLayout.MarginLayoutParams marginLayoutParams))
            return;
        marginLayoutParams.setMargins(
                marginLayoutParams.leftMargin,
                marginTop,
                marginLayoutParams.rightMargin,
                marginLayoutParams.bottomMargin
        );
        frameLayout.requestLayout();
    }

    public static void setVideoPortrait(int width, int height) {
        if (!SettingsEnum.FORCE_FULLSCREEN.getBoolean())
            return;

        isLandScapeVideo = width > height;
    }

    public static boolean showFullscreenTitle() {
        return SettingsEnum.SHOW_FULLSCREEN_TITLE.getBoolean() || !SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean();
    }
}
