package app.revanced.integrations.patches.layout;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class FullscreenPatch {

    public static boolean disableLandScapeMode(boolean original) {
        return SettingsEnum.DISABLE_LANDSCAPE_MODE.getBoolean() || original;
    }

    public static boolean enableCompactControlsOverlay(boolean original) {
        return SettingsEnum.ENABLE_COMPACT_CONTROLS_OVERLAY.getBoolean() || original;
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

    public static boolean showFullscreenTitle() {
        return SettingsEnum.SHOW_FULLSCREEN_TITLE.getBoolean() || !SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean();
    }
}
