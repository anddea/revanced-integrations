package app.revanced.integrations.patches.utils;

import java.util.concurrent.atomic.AtomicReference;
import android.text.Spanned;

import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.settings.SettingsEnum;

/**
 * Used by app.revanced.patches.youtube.layout.returnyoutubedislike.patch.ReturnYouTubeDislikePatch
 */
public class ReturnYouTubeDislikePatch {

    /**
     * Called when the video id changes
     */
    public static void newVideoLoaded(String videoId) {
       ReturnYouTubeDislike.newVideoLoaded(videoId);
    }

    /**
     * Called when a litho text component is created
     */
    public static void onComponentCreated(Object conversionContext, AtomicReference<Object> textRef) {
        ReturnYouTubeDislike.onComponentCreated(conversionContext, textRef);
    }

    /**
     * Called when a Spanned text component is created
     */
    public static Spanned onShortsComponentCreated(Spanned dislike) {
        return ReturnYouTubeDislike.onShortsComponentCreated(dislike);
    }

    /**
     * Called when the like/dislike button is clicked
     *
     * @param vote -1 (dislike), 0 (none) or 1 (like)
     */
    public static void sendVote(int vote) {
        for (ReturnYouTubeDislike.Vote v : ReturnYouTubeDislike.Vote.values()) {
            if (v.value == vote) {
                ReturnYouTubeDislike.sendVote(v);
                return;
            }
        }
    }
}
