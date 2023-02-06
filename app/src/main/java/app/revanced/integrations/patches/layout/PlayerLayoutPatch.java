package app.revanced.integrations.patches.layout;

import android.view.View;
import android.widget.ImageView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ThemeHelper;

public class PlayerLayoutPatch {

    public static int hideCastButton(int original) {
        return SettingsEnum.HIDE_CAST_BUTTON.getBoolean() ? View.GONE : original;
    }

    public static boolean hideAutoPlayButton() {
        return SettingsEnum.HIDE_AUTOPLAY_BUTTON.getBoolean();
    }

    public static void hideCaptionsButton(ImageView imageView) {
        imageView.setVisibility(SettingsEnum.HIDE_CAPTIONS_BUTTON.getBoolean() ? ImageView.GONE : ImageView.VISIBLE);
    }

    public static void hideEndscreen(View view) {
        if (SettingsEnum.HIDE_ENDSCREEN_CARDS.getBoolean()) {
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

    public static boolean hidePlayerOverlayFilter() {
        return SettingsEnum.HIDE_PALYER_OVERLAY_FILTER.getBoolean();
    }

    public static int hidePlayerButton(View view, int originalValue) {
        boolean hidden = (SettingsEnum.HIDE_LIVE_CHATS_BUTTON.getBoolean() && ThemeHelper.isLiveChat(view))
                || (SettingsEnum.HIDE_NEXT_BUTTON.getBoolean() && ThemeHelper.isNextButton(view))
                || (SettingsEnum.HIDE_PREV_BUTTON.getBoolean() && ThemeHelper.isPrevButton(view));
        return hidden ? 8 : originalValue;
    }
}
