package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike.Vote;

import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicReference;

import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class ReturnYouTubeDislikePatch {
    private static String LIKE_TAG = "like";
    private static String DISLIKE_TAG = "dislike";


    /**
     * Injection point.
     */
    public static void newVideoLoaded(String videoId) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) return;
            ReturnYouTubeDislike.newVideoLoaded(videoId);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "newVideoLoaded failure", ex);
        }
    }

    /**
     * Injection point.
     * <p>
     * Called when a litho text component is initially created,
     * and also when a Span is later reused again (such as scrolling off/on screen).
     * <p>
     * This method is sometimes called on the main thread, but it usually is called _off_ the main thread.
     * This method can be called multiple times for the same UI element (including after dislikes was added).
     *
     * @param textRef Cache reference to the like/dislike char sequence,
     *                which may or may not be the same as the original span parameter.
     *                If dislikes are added, the atomic reference must be set to the replacement span.
     * @param original Original span that was created or reused by Litho.
     * @return The original span (if nothing should change), or a replacement span that contains dislikes.
     */
    @NonNull
    public static CharSequence onLithoTextLoaded(@NonNull Object conversionContext,
                                                 @NonNull AtomicReference<CharSequence> textRef,
                                                 @NonNull CharSequence original) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return original;
            }
            SpannableString replacement = ReturnYouTubeDislike.getDislikeSpanForContext(conversionContext, original);
            if (replacement != null) {
                textRef.set(replacement);
                return replacement;
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "onLithoTextLoaded failure", ex);
        }
        return original;
    }

    /**
     * Injection point.
     * <p>
     * Called when a Shorts dislike Spanned is created.
     */
    public static Spanned onShortsComponentCreated(Spanned original) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return original;
            }
            SpannableString replacement = ReturnYouTubeDislike.getDislikeSpanForShort(original);
            if (replacement != null) {
                return replacement;
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "onShortsComponentCreated failure", ex);
        }
        return original;
    }

    public static void setLikeTag(@NonNull View view, boolean active) {
        if (!SettingsEnum.RYD_ENABLED.getBoolean()) return;
        if (active)
            sendVote(1);
        view.setTag(LIKE_TAG);
    }

    public static void setDislikeTag(@NonNull View view, boolean active) {
        if (!SettingsEnum.RYD_ENABLED.getBoolean()) return;
        if (active)
            sendVote(-1);
        view.setTag(DISLIKE_TAG);
    }

    public static CharSequence onSetText(@NonNull View view, @NonNull CharSequence originalText) {
        if (!SettingsEnum.RYD_ENABLED.getBoolean()) return originalText;
        try {
            CharSequence tag = (CharSequence) view.getTag();
            if (tag == null || tag.equals(LIKE_TAG))
                return originalText;
            else if (tag.equals(DISLIKE_TAG)) {
                return ReturnYouTubeDislike.handleOnSetText(originalText);
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "Error while handling the setText", ex);
        }
        return originalText;
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
