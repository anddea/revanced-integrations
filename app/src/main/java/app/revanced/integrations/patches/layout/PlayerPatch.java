package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;
import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.StringRef.str;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ResourceHelper;
import app.revanced.integrations.utils.ResourceType;

public class PlayerPatch {

    public static float customSpeedOverlay(final float original) {
        try {
            final String speedValue = SettingsEnum.CUSTOM_SPEED_OVERLAY.getString();
            final float speed = Float.parseFloat(speedValue);

            if (speed == original || speed <= 0)
                return original;

            return speed;
        } catch (Exception ignored) {
            return original;
        }
    }

    public static CharSequence customSpeedOverlay(TextView textView, CharSequence original) {
        if (textView == null)
            return original;

        try {
            final int speedMasterEduTextId = identifier("speedmaster_edu_text", ResourceType.ID);
            final int textViewId = textView.getId();

            final String speedValue = SettingsEnum.CUSTOM_SPEED_OVERLAY.getString();
            final float speed = Float.parseFloat(speedValue);

            if (speed == 2.0f || speed <= 0)
                return original;

            if (speedMasterEduTextId == textViewId)
                return str("revanced_custom_speed_overlay_text", speedValue);
        } catch (Exception ignored) {
            return original;
        }
        return original;
    }

    public static boolean disableChapterVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_CHAPTERS.getBoolean();
    }

    public static boolean disableSeekVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_SEEK.getBoolean();
    }

    public static boolean disableScrubbingVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_SCRUBBING.getBoolean();
    }

    public static boolean disableZoomVibrate() {
        return SettingsEnum.DISABLE_HAPTIC_FEEDBACK_ZOOM.getBoolean();
    }

    public static boolean hideAutoPlayButton() {
        return SettingsEnum.HIDE_AUTOPLAY_BUTTON.getBoolean();
    }

    public static void hideCaptionsButton(ImageView imageView) {
        imageView.setVisibility(SettingsEnum.HIDE_CAPTIONS_BUTTON.getBoolean() ? ImageView.GONE : ImageView.VISIBLE);
    }

    public static int hideCastButton(int original) {
        return SettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static boolean hideChannelWatermark() {
        return !SettingsEnum.HIDE_CHANNEL_WATERMARK.getBoolean();
    }

    public static void hideEndScreen(View view) {
        if (SettingsEnum.HIDE_END_SCREEN_CARDS.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static boolean hideFilmstripOverlay() {
        return SettingsEnum.HIDE_FILMSTRIP_OVERLAY.getBoolean();
    }

    public static boolean hideInfoCard(boolean original) {
        return !SettingsEnum.HIDE_INFO_CARDS.getBoolean() && original;
    }

    public static boolean hideMusicButton() {
        return SettingsEnum.HIDE_YOUTUBE_MUSIC_BUTTON.getBoolean();
    }

    public static int hidePlayerButton(View view, int originalValue) {
        return ResourceHelper.hidePlayerButton(view, originalValue);
    }

    public static void hidePlayerOverlayFilter(ImageView view) {
        if (!SettingsEnum.HIDE_PLAYER_OVERLAY_FILTER.getBoolean()) return;
        view.setImageResource(android.R.color.transparent);
    }

    public static boolean hideSeekMessage() {
        return SettingsEnum.HIDE_SEEK_MESSAGE.getBoolean();
    }

    public static boolean hideSpeedOverlay(boolean original) {
        return !SettingsEnum.HIDE_SPEED_OVERLAY.getBoolean() && original;
    }

    public static void hideSuggestedActions(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean(), view);
    }
}
