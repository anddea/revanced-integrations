package app.revanced.music.patches.utils;

import static app.revanced.music.returnyoutubedislike.ReturnYouTubeDislike.Vote;

import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.music.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.music.returnyoutubedislike.requests.ReturnYouTubeDislikeApi;
import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

/**
 * Handles all interaction of UI patch components.
 * <p>
 * Does not handle creating dislike spans or anything to do with {@link ReturnYouTubeDislikeApi}.
 */
public class ReturnYouTubeDislikePatch {
    @Nullable
    private static String currentVideoId;

    /**
     * Injection point
     *
     * Called when a Shorts dislike Spannable is created
     */
    public static Spanned onComponentCreated(Spanned like) {
        return ReturnYouTubeDislike.onComponentCreated(like);
    }

    /**
     * Injection point.
     */
    public static void newVideoLoaded(@NonNull String videoId) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) return;

            if (!videoId.equals(currentVideoId)) {
                currentVideoId = videoId;

                ReturnYouTubeDislike.newVideoLoaded(videoId);
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "newVideoLoaded failure", ex);
        }
    }

    /**
     * Injection point.
     * <p>
     * Called when the user likes or dislikes.
     *
     * @param vote int that matches {@link ReturnYouTubeDislike.Vote#value}
     */
    public static void sendVote(int vote) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return;
            }

            for (Vote v : Vote.values()) {
                if (v.value == vote) {
                    ReturnYouTubeDislike.sendVote(v);
                    return;
                }
            }
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "Unknown vote type: " + vote);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "sendVote failure", ex);
        }
    }
}
