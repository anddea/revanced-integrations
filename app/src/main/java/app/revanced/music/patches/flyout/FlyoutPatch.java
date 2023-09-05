package app.revanced.music.patches.flyout;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.VideoHelpers;

public class FlyoutPatch {
    private static ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.SRC_ATOP);

    public static int enableCompactDialog(int original) {
        return SettingsEnum.ENABLE_COMPACT_DIALOG.getBoolean() && original < 600 ? 600 : original;
    }

    public static boolean enableSleepTimer() {
        return SettingsEnum.ENABLE_SLEEP_TIMER.getBoolean();
    }

    public static void hideImageView(@NonNull View view) {
        if (view instanceof ImageView) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setFlyoutButtonContainer(@NonNull View view) {
        if (!(view instanceof ViewGroup viewGroup))
            return;

        final ViewGroup flyoutButtonContainers = (ViewGroup) viewGroup.getChildAt(0);
        final ImageView playbackSpeedButton = (ImageView) flyoutButtonContainers.getChildAt(0);
        final View dislikeButton = flyoutButtonContainers.getChildAt(1);
        final View likeButton = flyoutButtonContainers.getChildAt(2);

        playbackSpeedButton.setOnClickListener(imageView -> VideoHelpers.playbackSpeedDialogListener(imageView.getContext()));
        playbackSpeedButton.setColorFilter(cf);

        final boolean showPlaybackSpeedButton = SettingsEnum.ENABLE_FLYOUT_PANEL_PLAYBACK_SPEED.getBoolean();
        final boolean hideLikeDislikeButton = SettingsEnum.HIDE_FLYOUT_PANEL_LIKE_DISLIKE.getBoolean();

        if (!showPlaybackSpeedButton && !hideLikeDislikeButton) {
            // download: hidden, like/dislike: shown
            hideImageView(playbackSpeedButton);
        } else if (showPlaybackSpeedButton && hideLikeDislikeButton) {
            // download: shown, like/dislike: hidden
            hideImageView(dislikeButton);
            hideImageView(likeButton);
        } else if (!showPlaybackSpeedButton) {
            // download: hidden, like/dislike: hidden
            hideImageView(playbackSpeedButton);
            hideImageView(dislikeButton);
            hideImageView(likeButton);
        }
    }
}
