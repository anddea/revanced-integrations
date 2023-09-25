package app.revanced.integrations.patches.misc;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.LogHelper;

public class SpoofPlayerParameterPatch {

    /**
     * Parameter (also used by
     * <a href="https://github.com/yt-dlp/yt-dlp/blob/81ca451480051d7ce1a31c017e005358345a9149/yt_dlp/extractor/youtube.py#L3602">yt-dlp</a>)
     * to fix playback issues.
     */
    private static final String INCOGNITO_PARAMETERS = "CgIQBg==";

    /**
     * Parameters causing playback issues.
     */
    private static final List<String> AUTOPLAY_PARAMETERS = Arrays.asList(
            "YAHI", // Autoplay in feed
            "SAFg"  // Autoplay in scrim
    );

    /**
     * Parameters used in YouTube Shorts.
     */
    private static final String SHORTS_PLAYER_PARAMETERS = "8AEB";

    private static boolean isPlayingShorts;

    /**
     * Injection point.
     *
     * @param parameters Original player parameter value.
     */
    public static String spoofParameter(String parameters) {
        LogHelper.printDebug(SpoofPlayerParameterPatch.class, "Original player parameter value: " + parameters);

        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean()) {
            return parameters;
        }

        // Shorts do not need to be spoofed.
        // noinspection AssignmentUsedAsCondition
        if (isPlayingShorts = parameters.startsWith(SHORTS_PLAYER_PARAMETERS)) {
            return parameters;
        }

        // Clip's player parameters contain important information such as where the video starts, where it ends, and whether it loops.
        // For this reason, the player parameters of a clip are usually very long (150~300 characters).
        // Clips are 60 seconds or less in length, so no spoofing.
        if (parameters.length() > 150) {
            return parameters;
        }

        final boolean isPlayingFeed = PlayerType.getCurrent() == PlayerType.INLINE_MINIMAL
                && AUTOPLAY_PARAMETERS.stream().anyMatch(parameters::contains);

        if (isPlayingFeed) {
            // In order to prevent videos that are auto-played in feed to be added to history,
            // only spoof the parameter if the video is not playing in the feed.
            // This will cause playback issues in the feed, but it's better than manipulating the history.
            return parameters;
        } else {
            // Spoof the player parameter to prevent playback issues.
            return INCOGNITO_PARAMETERS;
        }
    }

    /**
     * Injection point.
     */
    public static boolean getSeekbarThumbnailOverrideValue() {
        return SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean();
    }

    /**
     * Injection point.
     *
     * @param view seekbar thumbnail view.  Includes both shorts and regular videos.
     */
    public static void seekbarImageViewCreated(ImageView view) {
        if (!SettingsEnum.SPOOF_PLAYER_PARAMETER.getBoolean()) return;
        if (isPlayingShorts) return;

        view.setVisibility(View.GONE);
        // Also hide the border around the thumbnail (otherwise a 1 pixel wide bordered frame is visible).
        ViewGroup parentLayout = (ViewGroup) view.getParent();
        parentLayout.setPadding(0, 0, 0, 0);
    }
}
