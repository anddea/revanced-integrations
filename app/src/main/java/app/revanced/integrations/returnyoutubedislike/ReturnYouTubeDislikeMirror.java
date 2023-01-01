package app.revanced.integrations.returnyoutubedislike;

import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.icu.text.CompactDecimalFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.text.SpannableString;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import app.revanced.integrations.returnyoutubedislike.requests.ReturnYouTubeDislikeMirrorApi;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class ReturnYouTubeDislikeMirror {
    private static String currentVideoId;
    public static Long likeCount;
    public static Long dislikeCount;
    public static Float dislikePercentage;

    private static CompactDecimalFormat compactNumberFormatter;
    private static DecimalFormat dislikePercentageFormatter;

    private static boolean segmentedButton;

    private static Thread _dislikeFetchThread = null;

    public static void newVideoLoaded(String videoId) {
        if (videoId == null || videoId.equals(currentVideoId)) return;

        likeCount = null;
        dislikeCount = null;
        dislikePercentage = null;

        currentVideoId = videoId;

        try {
            if (_dislikeFetchThread != null && _dislikeFetchThread.getState() != Thread.State.TERMINATED) {
                _dislikeFetchThread.interrupt();
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeMirror.class, "Error in the dislike fetch thread", ex);
        }

        _dislikeFetchThread = new Thread(() -> ReturnYouTubeDislikeMirrorApi.fetchDislikes(videoId));
        _dislikeFetchThread.start();
    }

    public static void onComponentCreated(Object conversionContext, AtomicReference<Object> textRef) {
        try {
            var conversionContextString = conversionContext.toString();

            // Check for new component
            if (conversionContextString.contains("|segmented_like_dislike_button.eml|"))
                segmentedButton = true;
            else if (!conversionContextString.contains("|dislike_button.eml|"))
                return;

            // Have to block the current thread until fetching is done
            // There's no known way to edit the text after creation yet
            if (_dislikeFetchThread != null) _dislikeFetchThread.join();

            if (likeCount == null || dislikeCount == null) return;
            updateDislike(textRef, segmentedButton);

        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeMirror.class, "Error while trying to set dislikes text", ex);
        }
    }

    private static void updateDislike(AtomicReference<Object> textRef, boolean isSegmentedButton) {
        SpannableString oldSpannableString = (SpannableString) textRef.get();

        String newString = SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean()
                ? formatPercentage(dislikePercentage)
                : formatCount(dislikeCount);

        if (isSegmentedButton) {
            ReVancedHelper.setRTL();
            String oldString = ReVancedHelper.getOldString(oldSpannableString.toString());
            String hiddenMessageString = str("revanced_ryd_video_likes_hidden_by_video_owner");

            String likesString = formatCount(likeCount);
            if (!oldString.contains(".")) {
                try {
                    likesString = formatCount(Long.parseLong(oldString));
                } catch (Exception ignored) {}
            }

            if (oldString.contains(hiddenMessageString))
                newString = hiddenMessageString;
            else
                newString = ReVancedHelper.setRTLString(likesString, newString);
        }

        SpannableString newSpannableString = new SpannableString(newString);
        // Copy style (foreground color, etc) to new string
        Object[] spans = oldSpannableString.getSpans(0, oldSpannableString.length(), Object.class);
        for (Object span : spans) {
            newSpannableString.setSpan(span, 0, newString.length(), oldSpannableString.getSpanFlags(span));
        }
        textRef.set(newSpannableString);
    }

    @SuppressLint("NewApi")
    private static String formatCount(long dislikeCount) {
        String formatted;
        synchronized (ReturnYouTubeDislikeMirror.class) { // number formatter is not thread safe, must synchronize
            if (compactNumberFormatter == null) {
                Locale locale = ReVancedUtils.getContext().getResources().getConfiguration().locale;
                compactNumberFormatter = CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT);
            }
            formatted = compactNumberFormatter.format(dislikeCount);
        }
        return formatted;
    }

    @SuppressLint("NewApi")
    private static String formatPercentage(float dislikePercentage) {
        String formatted;
        synchronized (ReturnYouTubeDislikeMirror.class) { // number formatter is not thread safe, must synchronize
            if (dislikePercentageFormatter == null) {
                Locale locale = ReVancedUtils.getContext().getResources().getConfiguration().locale;
                dislikePercentageFormatter = new DecimalFormat("", new DecimalFormatSymbols(locale));
            }
            if (dislikePercentage == 0 || dislikePercentage >= 0.01) { // zero, or at least 1%
                dislikePercentageFormatter.applyLocalizedPattern("0"); // show only whole percentage points
            } else { // between (0, 1)%
                dislikePercentageFormatter.applyLocalizedPattern("0.#"); // show 1 digit precision
            }
            final char percentChar = dislikePercentageFormatter.getDecimalFormatSymbols().getPercent();
            formatted = dislikePercentageFormatter.format(100 * dislikePercentage) + percentChar;
        }
        return formatted;
    }

    public static void setValues(Long like, Long dislike, Float percentage) {
        likeCount = like;
        dislikeCount = dislike;
        dislikePercentage = percentage;
    }
}
