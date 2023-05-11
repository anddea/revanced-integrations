package app.revanced.integrations.patches.layout;

import android.view.View;
import android.widget.ImageView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ResourceHelper;

public class PlayerPatch {

    public static boolean hideMusicButton() {
        return SettingsEnum.HIDE_YOUTUBE_MUSIC_BUTTON.getBoolean();
    }

    public static int hideCastButton(int original) {
        return SettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static boolean hideAutoPlayButton() {
        return SettingsEnum.HIDE_AUTOPLAY_BUTTON.getBoolean();
    }

    public static void hideCaptionsButton(ImageView imageView) {
        imageView.setVisibility(SettingsEnum.HIDE_CAPTIONS_BUTTON.getBoolean() ? ImageView.GONE : ImageView.VISIBLE);
    }

    public static void hideEndScreen(View view) {
        if (SettingsEnum.HIDE_END_SCREEN_CARDS.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static boolean hideInfoCard(boolean original) {
        return !SettingsEnum.HIDE_INFO_CARDS.getBoolean() && original;
    }

    public static boolean hideChannelWatermark() {
        return !SettingsEnum.HIDE_CHANNEL_WATERMARK.getBoolean();
    }

    public static void hideSuggestedActions(View view) {
        if (SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean()) {
            view.setVisibility(View.GONE);
        }
    }

    public static void hidePlayerOverlayFilter(ImageView view) {
        if (!SettingsEnum.HIDE_PLAYER_OVERLAY_FILTER.getBoolean()) return;
        view.setImageResource(android.R.color.transparent);
    }

    public static int hidePlayerButton(View view, int originalValue) {
        return ResourceHelper.hidePlayerButton(view) ? 8 : originalValue;
    }

    public static boolean hidePreviousNextButton(boolean original) {
        return !SettingsEnum.HIDE_PREVIOUS_NEXT_BUTTON.getBoolean() && original;
    }
}
