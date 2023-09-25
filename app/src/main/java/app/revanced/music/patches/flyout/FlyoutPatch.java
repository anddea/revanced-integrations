package app.revanced.music.patches.flyout;

import static app.revanced.music.utils.ResourceUtils.identifier;
import static app.revanced.music.utils.StringRef.str;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;
import app.revanced.music.utils.ResourceType;
import app.revanced.music.utils.VideoHelpers;

public class FlyoutPatch {

    private static final ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.SRC_ATOP);

    public static int enableCompactDialog(int original) {
        if (!SettingsEnum.ENABLE_COMPACT_DIALOG.getBoolean())
            return original;

        return Math.max(original, 600);
    }

    public static boolean enableSleepTimer() {
        return SettingsEnum.ENABLE_SLEEP_TIMER.getBoolean();
    }

    public static boolean hideFlyoutPanels(@Nullable Enum<?> flyoutPanelEnum) {
        if (flyoutPanelEnum == null)
            return false;

        final String flyoutPanelName = flyoutPanelEnum.name();

        LogHelper.printDebug(FlyoutPatch.class, flyoutPanelName);

        for (FlyoutPanelComponent component : FlyoutPanelComponent.values())
            if (component.name.equals(flyoutPanelName) && component.enabled)
                return true;

        return false;
    }

    /**
     * This method is called before the original method
     * So even if we define TextView and ImageView, TextView and ImageView are redefined in the original method
     * To prevent this, define the TextView and ImageView in a new thread
     *
     * @param flyoutPanelEnum Enum in menu
     * @param textView        TextView in menu
     * @param imageView       ImageView in menu
     */
    public static void replaceDismissQueue(@Nullable Enum<?> flyoutPanelEnum, @NonNull TextView textView, @NonNull ImageView imageView) {
        if (flyoutPanelEnum == null || !SettingsEnum.REPLACE_FLYOUT_PANEL_DISMISS_QUEUE.getBoolean())
            return;

        final String flyoutPanelName = flyoutPanelEnum.name();

        if (!flyoutPanelName.equals("DISMISS_QUEUE"))
            return;

        ViewGroup clickAbleArea = (ViewGroup) textView.getParent();

        ReVancedUtils.runOnMainThreadDelayed(() -> {
                    textView.setText(str("revanced_flyout_panel_watch_on_youtube"));
                    imageView.setImageResource(identifier("yt_outline_youtube_logo_icon_black_24", ResourceType.DRAWABLE, clickAbleArea.getContext()));
                    imageView.setColorFilter(cf);
                    clickAbleArea.setOnClickListener(viewGroup -> VideoHelpers.openInYouTube(viewGroup.getContext()));
                }, 0L
        );
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

    private enum FlyoutPanelComponent {
        SAVE_EPISODE_FOR_LATER("BOOKMARK_BORDER", SettingsEnum.HIDE_FLYOUT_PANEL_SAVE_EPISODE_FOR_LATER.getBoolean()),
        SHUFFLE("SHUFFLE", SettingsEnum.HIDE_FLYOUT_PANEL_SHUFFLE.getBoolean()),
        RADIO("MIX", SettingsEnum.HIDE_FLYOUT_PANEL_START_RADIO.getBoolean()),
        EDIT_PLAYLIST("EDIT", SettingsEnum.HIDE_FLYOUT_PANEL_EDIT_PLAYLIST.getBoolean()),
        PLAY_NEXT("QUEUE_PLAY_NEXT", SettingsEnum.HIDE_FLYOUT_PANEL_PLAY_NEXT.getBoolean()),
        ADD_TO_QUEUE("QUEUE_MUSIC", SettingsEnum.HIDE_FLYOUT_PANEL_ADD_TO_QUEUE.getBoolean()),
        SAVE_TO_LIBRARY("LIBRARY_ADD", SettingsEnum.HIDE_FLYOUT_PANEL_SAVE_TO_LIBRARY.getBoolean()),
        REMOVE_FROM_LIBRARY("LIBRARY_REMOVE", SettingsEnum.HIDE_FLYOUT_PANEL_REMOVE_FROM_LIBRARY.getBoolean()),
        DOWNLOAD("OFFLINE_DOWNLOAD", SettingsEnum.HIDE_FLYOUT_PANEL_DOWNLOAD.getBoolean()),
        SAVE_TO_PLAYLIST("ADD_TO_PLAYLIST", SettingsEnum.HIDE_FLYOUT_PANEL_SAVE_TO_PLAYLIST.getBoolean()),
        GO_TO_EPISODE("INFO", SettingsEnum.HIDE_FLYOUT_PANEL_GO_TO_EPISODE.getBoolean()),
        GO_TO_PODCAST("BROADCAST", SettingsEnum.HIDE_FLYOUT_PANEL_GO_TO_PODCAST.getBoolean()),
        GO_TO_ALBUM("ALBUM", SettingsEnum.HIDE_FLYOUT_PANEL_GO_TO_ALBUM.getBoolean()),
        GO_TO_ARTIST("ARTIST", SettingsEnum.HIDE_FLYOUT_PANEL_GO_TO_ARTIST.getBoolean()),
        VIEW_SONG_CREDIT("PEOPLE_GROUP", SettingsEnum.HIDE_FLYOUT_PANEL_VIEW_SONG_CREDIT.getBoolean()),
        SHARE("SHARE", SettingsEnum.HIDE_FLYOUT_PANEL_SHARE.getBoolean()),
        DISMISS_QUEUE("DISMISS_QUEUE", SettingsEnum.HIDE_FLYOUT_PANEL_DISMISS_QUEUE.getBoolean()),
        DISMISS_QUEUE_LEGACY("DELETE", SettingsEnum.HIDE_FLYOUT_PANEL_DISMISS_QUEUE.getBoolean()),
        REPORT("FLAG", SettingsEnum.HIDE_FLYOUT_PANEL_REPORT.getBoolean()),
        QUALITY("SETTINGS_MATERIAL", SettingsEnum.HIDE_FLYOUT_PANEL_QUALITY.getBoolean()),
        CAPTIONS("CAPTIONS", SettingsEnum.HIDE_FLYOUT_PANEL_CAPTIONS.getBoolean()),
        STATS_FOR_NERDS("PLANNER_REVIEW", SettingsEnum.HIDE_FLYOUT_PANEL_STATS_FOR_NERDS.getBoolean()),
        SLEEP_TIMER("MOON_Z", SettingsEnum.HIDE_FLYOUT_PANEL_SLEEP_TIMER.getBoolean());

        private final boolean enabled;
        private final String name;

        FlyoutPanelComponent(String name, boolean enabled) {
            this.enabled = enabled;
            this.name = name;
        }
    }
}
