package app.revanced.music.returnyoutubedislike;

import static app.revanced.music.utils.StringRef.str;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.icu.text.CompactDecimalFormat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.revanced.music.returnyoutubedislike.requests.RYDVoteData;
import app.revanced.music.returnyoutubedislike.requests.ReturnYouTubeDislikeApi;
import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

/**
 * Because Litho creates spans using multiple threads, this entire class supports multithreading as well.
 */
public class ReturnYouTubeDislike {

    /**
     * Maximum amount of time to block the UI from updates while waiting for network call to complete.
     * <p>
     * Must be less than 5 seconds, as per:
     * <a href="https://developer.android.com/topic/performance/vitals/anr"/>
     */
    private static final long MAX_MILLISECONDS_TO_BLOCK_UI_WAITING_FOR_FETCH = 4000;
    /**
     * Unique placeholder character, used to detect if a segmented span already has dislikes added to it.
     * Can be any almost any non-visible character.
     */
    private static final char MIDDLE_SEPARATOR_CHARACTER = '\u2009'; // 'narrow space' character
    /**
     * Cached lookup of RYD fetches.
     */
    @GuardedBy("videoIdLockObject")
    private static final Map<String, RYDCachedFetch> futureCache = new HashMap<>();
    /**
     * Used to send votes, one by one, in the same order the user created them.
     */
    private static final ExecutorService voteSerialExecutor = Executors.newSingleThreadExecutor();
    /**
     * Used to guard {@link #currentVideoId} and {@link #voteFetchFuture}.
     */
    private static final Object videoIdLockObject = new Object();
    @Nullable
    @GuardedBy("videoIdLockObject")
    private static String currentVideoId;
    /**
     * Stores the results of the vote api fetch, and used as a barrier to wait until fetch completes.
     */
    @Nullable
    @GuardedBy("videoIdLockObject")
    private static Future<RYDVoteData> voteFetchFuture;
    /**
     * Optional current vote status of the UI.  Used to apply a user vote that was done on a previous video viewing.
     */
    @Nullable
    @GuardedBy("videoIdLockObject")
    private static Vote userVote;
    /**
     * Original dislike span, before modifications.
     */
    @Nullable
    @GuardedBy("videoIdLockObject")
    private static Spanned originalDislikeSpan;
    /**
     * Replacement like/dislike span that includes formatted dislikes.
     * Used to prevent recreating the same span multiple times.
     */
    @Nullable
    @GuardedBy("videoIdLockObject")
    private static SpannableString replacementLikeDislikeSpan;
    /**
     * For formatting dislikes as number.
     */
    @GuardedBy("ReturnYouTubeDislike.class") // not thread safe
    private static CompactDecimalFormat dislikeCountFormatter;
    /**
     * For formatting dislikes as percentage.
     */
    @GuardedBy("ReturnYouTubeDislike.class")
    private static NumberFormat dislikePercentageFormatter;

    private ReturnYouTubeDislike() {
    } // only static methods

    /**
     * Should be called after a user dislikes, or if the user changes settings for dislikes appearance.
     */
    public static void clearCache() {
        synchronized (videoIdLockObject) {
            replacementLikeDislikeSpan = null;
        }
    }

    @Nullable
    private static String getCurrentVideoId() {
        synchronized (videoIdLockObject) {
            return currentVideoId;
        }
    }

    public static void setCurrentVideoId(@Nullable String videoId) {
        synchronized (videoIdLockObject) {
            final long now = System.currentTimeMillis();
            futureCache.values().removeIf(value -> value.isExpired(now));
            currentVideoId = videoId;
            userVote = null;
            voteFetchFuture = null;
            originalDislikeSpan = null;
            replacementLikeDislikeSpan = null;
        }
    }

    @Nullable
    private static Future<RYDVoteData> getVoteFetchFuture() {
        synchronized (videoIdLockObject) {
            return voteFetchFuture;
        }
    }

    public static void newVideoLoaded(@NonNull String videoId) {
        Objects.requireNonNull(videoId);

        synchronized (videoIdLockObject) {
            if (videoId.equals(currentVideoId)) {
                return; // already loaded
            }
            if (ReVancedUtils.isNetworkConnected()) { // must do network check after verifying it's a new video id
                setCurrentVideoId(null);
                return;
            }
            setCurrentVideoId(videoId);

            RYDCachedFetch entry = futureCache.get(videoId);
            if (entry != null && entry.futureInProgressOrFinishedSuccessfully()) {
                voteFetchFuture = entry.future;
                return;
            }
            voteFetchFuture = ReVancedUtils.submitOnBackgroundThread(() -> ReturnYouTubeDislikeApi.fetchVotes(videoId));
            futureCache.put(videoId, new RYDCachedFetch(voteFetchFuture, videoId));
        }
    }

    public static Spanned onComponentCreated(Spanned span) {
        try {
            if (SettingsEnum.RYD_ENABLED.getBoolean()) {
                return waitForFetchAndUpdateReplacementSpan(span);
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "onShortsComponentCreated failure", ex);
        }
        return span;
    }

    // Alternatively, this could check if the span contains one of the custom created spans, but this is simple and quick.
    private static boolean isPreviouslyCreatedSegmentedSpan(@NonNull Spanned span) {
        return span.toString().indexOf(MIDDLE_SEPARATOR_CHARACTER) != -1;
    }

    @NonNull
    private static Spanned waitForFetchAndUpdateReplacementSpan(@NonNull Spanned oldSpannable) {
        try {
            Future<RYDVoteData> fetchFuture = getVoteFetchFuture();
            if (fetchFuture == null) {
                return oldSpannable;
            }
            // Absolutely cannot be holding any lock during get().
            RYDVoteData votingData = fetchFuture.get(MAX_MILLISECONDS_TO_BLOCK_UI_WAITING_FOR_FETCH, TimeUnit.MILLISECONDS);
            if (votingData == null) {
                return oldSpannable;
            }

            // Must check against existing replacements, after the fetch,
            // otherwise concurrent threads can create the same replacement same multiple times.
            // Also do the replacement comparison and creation in a single synchronized block.
            synchronized (videoIdLockObject) {
                if (originalDislikeSpan != null && replacementLikeDislikeSpan != null) {
                    if (spansHaveEqualTextAndColor(oldSpannable, replacementLikeDislikeSpan)) {
                        return oldSpannable;
                    }
                    if (spansHaveEqualTextAndColor(oldSpannable, originalDislikeSpan)) {
                        return replacementLikeDislikeSpan;
                    }
                }
                if (isPreviouslyCreatedSegmentedSpan(oldSpannable)) {
                    // need to recreate using original, as oldSpannable has prior outdated dislike values
                    if (originalDislikeSpan == null) {
                        return oldSpannable;
                    }
                    oldSpannable = originalDislikeSpan;
                }

                // No replacement span exist, create it now.

                if (userVote != null) {
                    votingData.updateUsingVote(userVote);
                }
                originalDislikeSpan = oldSpannable;
                replacementLikeDislikeSpan = createDislikeSpan(oldSpannable, votingData);

                return replacementLikeDislikeSpan;
            }
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogHelper.printException(ReturnYouTubeDislike.class, "waitForFetchAndUpdateReplacementSpan failure", e); // should never happen
        }
        return oldSpannable;
    }

    public static void sendVote(@NonNull Vote vote) {
        ReVancedUtils.verifyOnMainThread();
        Objects.requireNonNull(vote);
        try {
            // Must make a local copy of videoId, since it may change between now and when the vote thread runs.
            String videoIdToVoteFor = getCurrentVideoId();
            if (videoIdToVoteFor == null) {
                return;
            }

            voteSerialExecutor.execute(() -> {
                try { // must wrap in try/catch to properly log exceptions
                    String userId = getUserId();
                    if (userId != null) {
                        ReturnYouTubeDislikeApi.sendVote(videoIdToVoteFor, userId, vote);
                    }
                } catch (Exception ex) {
                    LogHelper.printException(ReturnYouTubeDislike.class, "Failed to send vote", ex);
                }
            });

            setUserVote(vote);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "Error trying to send vote", ex);
        }
    }

    public static void setUserVote(@NonNull Vote vote) {
        Objects.requireNonNull(vote);
        try {
            // Update the downloaded vote data.
            Future<RYDVoteData> future = getVoteFetchFuture();
            if (future != null && future.isDone()) {
                RYDVoteData voteData;
                try {
                    voteData = future.get(MAX_MILLISECONDS_TO_BLOCK_UI_WAITING_FOR_FETCH, TimeUnit.MILLISECONDS);
                } catch (ExecutionException | InterruptedException | TimeoutException ex) {
                    // Should never happen
                    LogHelper.printException(ReturnYouTubeDislike.class, "Could not update vote data", ex);
                    return;
                }
                if (voteData == null) {
                    // RYD fetch failed
                    return;
                }

                voteData.updateUsingVote(vote);
            } // Else, vote will be applied after vote data is received

            synchronized (videoIdLockObject) {
                if (userVote != vote) {
                    userVote = vote;
                    clearCache(); // UI needs updating
                }
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "setUserVote failure", ex);
        }
    }

    /**
     * Must call off main thread, as this will make a network call if user is not yet registered.
     *
     * @return ReturnYouTubeDislike user ID. If user registration has never happened
     * and the network call fails, this returns NULL.
     */
    @Nullable
    private static String getUserId() {
        ReVancedUtils.verifyOffMainThread();

        String userId = SettingsEnum.RYD_USER_ID.getString();
        if (!userId.isEmpty()) {
            return userId;
        }

        userId = ReturnYouTubeDislikeApi.registerAsNewUser();
        if (userId != null) {
            SettingsEnum.RYD_USER_ID.saveValue(userId);
        }
        return userId;
    }

    @NonNull
    private static SpannableString createDislikeSpan(@NonNull Spanned oldSpannable, @NonNull RYDVoteData voteData) {
        // Note: Some locales use right to left layout (arabic, hebrew, etc),
        // and care must be taken to retain the existing RTL encoding character on the likes string,
        // otherwise text will incorrectly show as left to right.
        // If making changes to this code, change device settings to a RTL language and verify layout is correct.
        String oldLikesString = oldSpannable.toString();

        // YouTube creators can hide the like count on a video,
        // and the like count appears as a device language specific string that says 'Like'.
        // Check if the string contains any numbers.
        if (!stringContainsNumber(oldLikesString)) {
            // Likes are hidden.
            // RYD does not provide usable data for these types of videos,
            // and the API returns bogus data (zero likes and zero dislikes)
            // discussion about this: https://github.com/Anarios/return-youtube-dislike/discussions/530
            //
            // example video: https://www.youtube.com/watch?v=UnrU5vxCHxw
            // RYD data: https://returnyoutubedislikeapi.com/votes?videoId=UnrU5vxCHxw
            //
            // Change the "Likes" string to show that likes and dislikes are hidden.
            String hiddenMessageString = str("revanced_ryd_video_likes_hidden_by_video_owner");
            return newSpanUsingStylingOfAnotherSpan(oldSpannable, hiddenMessageString);
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("\u2009\u2009");
        final boolean compactLayout = SettingsEnum.RYD_COMPACT_LAYOUT.getBoolean();
        final int separatorColor = 0x58AAAAAA; // light gray
        DisplayMetrics dp = Objects.requireNonNull(ReVancedUtils.getContext()).getResources().getDisplayMetrics();

        if (!compactLayout) {
            // left separator
            final Rect leftSeparatorBounds = new Rect(0, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f, dp),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, dp));
            String leftSeparatorString = ReVancedUtils.isRightToLeftTextLayout()
                    ? "\u200F    "  // u200F = right to left character
                    : "\u200E    "; // u200E = left to right character
            Spannable leftSeparatorSpan = new SpannableString(leftSeparatorString);
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
            shapeDrawable.getPaint().setColor(separatorColor);
            shapeDrawable.setBounds(leftSeparatorBounds);
            leftSeparatorSpan.setSpan(new VerticallyCenteredImageSpan(shapeDrawable), 1, 2,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // drawable cannot overwrite RTL or LTR character
            builder.append(leftSeparatorSpan);
        }

        // likes
        builder.append(newSpanUsingStylingOfAnotherSpan(oldSpannable, oldLikesString));

        // middle separator
        String middleSeparatorString = compactLayout
                ? "  " + MIDDLE_SEPARATOR_CHARACTER + "  "
                : "  \u2009" + MIDDLE_SEPARATOR_CHARACTER + "\u2009  "; // u2009 = 'narrow space' character
        final int shapeInsertionIndex = middleSeparatorString.length() / 2;
        final int middleSeparatorSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.7f, dp);
        final Rect middleSeparatorBounds = new Rect(0, 0, middleSeparatorSize, middleSeparatorSize);
        Spannable middleSeparatorSpan = new SpannableString(middleSeparatorString);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(separatorColor);
        shapeDrawable.setBounds(middleSeparatorBounds);
        middleSeparatorSpan.setSpan(new VerticallyCenteredImageSpan(shapeDrawable), shapeInsertionIndex, shapeInsertionIndex + 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(middleSeparatorSpan);

        // dislikes
        builder.append(newSpannableWithDislikes(oldSpannable, voteData));

        return new SpannableString(builder);
    }

    /**
     * Correctly handles any unicode numbers (such as Arabic numbers).
     *
     * @return if the string contains at least 1 number.
     */
    private static boolean stringContainsNumber(@NonNull String text) {
        for (int index = 0, length = text.length(); index < length; index++) {
            if (Character.isDigit(text.codePointAt(index))) {
                return true;
            }
        }
        return false;
    }

    private static boolean spansHaveEqualTextAndColor(@NonNull Spanned one, @NonNull Spanned two) {
        // Cannot use equals on the span, because many of the inner styling spans do not implement equals.
        // Instead, compare the underlying text and the text color to handle when dark mode is changed.
        // Cannot compare the status of device dark mode, as Litho components are updated just before dark mode status changes.
        if (!one.toString().equals(two.toString())) {
            return false;
        }
        ForegroundColorSpan[] oneColors = one.getSpans(0, one.length(), ForegroundColorSpan.class);
        ForegroundColorSpan[] twoColors = two.getSpans(0, two.length(), ForegroundColorSpan.class);
        final int oneLength = oneColors.length;
        if (oneLength != twoColors.length) {
            return false;
        }
        for (int i = 0; i < oneLength; i++) {
            if (oneColors[i].getForegroundColor() != twoColors[i].getForegroundColor()) {
                return false;
            }
        }
        return true;
    }

    private static SpannableString newSpannableWithDislikes(@NonNull Spanned sourceStyling, @NonNull RYDVoteData voteData) {
        return newSpanUsingStylingOfAnotherSpan(sourceStyling,
                SettingsEnum.RYD_DISLIKE_PERCENTAGE.getBoolean()
                        ? formatDislikePercentage(voteData.getDislikePercentage())
                        : formatDislikeCount(voteData.getDislikeCount()));
    }

    private static SpannableString newSpanUsingStylingOfAnotherSpan(@NonNull Spanned sourceStyle, @NonNull CharSequence newSpanText) {
        newSpanText = ReVancedUtils.isRightToLeftTextLayout()
                ? "\u2066" + newSpanText + "\u2069"
                : newSpanText;
        SpannableString destination = new SpannableString(newSpanText);
        Object[] spans = sourceStyle.getSpans(0, sourceStyle.length(), Object.class);
        for (Object span : spans) {
            destination.setSpan(span, 0, destination.length(), sourceStyle.getSpanFlags(span));
        }
        return destination;
    }

    private static String formatDislikeCount(long dislikeCount) {
        synchronized (ReturnYouTubeDislike.class) { // number formatter is not thread safe, must synchronize
            if (dislikeCountFormatter == null) {
                // Note: Java number formatters will use the locale specific number characters.
                // such as Arabic which formats "1.234" into "۱,۲۳٤"
                // But YouTube disregards locale specific number characters
                // and instead shows english number characters everywhere.
                Locale locale = Objects.requireNonNull(ReVancedUtils.getContext()).getResources().getConfiguration().locale;
                dislikeCountFormatter = CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT);
            }
            return dislikeCountFormatter.format(dislikeCount);
        }

        // will never be reached, as the oldest supported YouTube app requires Android N or greater
    }

    private static String formatDislikePercentage(float dislikePercentage) {
        synchronized (ReturnYouTubeDislike.class) { // number formatter is not thread safe, must synchronize
            if (dislikePercentageFormatter == null) {
                Locale locale = Objects.requireNonNull(ReVancedUtils.getContext()).getResources().getConfiguration().locale;
                dislikePercentageFormatter = NumberFormat.getPercentInstance(locale);
            }
            if (dislikePercentage >= 0.01) { // at least 1%
                dislikePercentageFormatter.setMaximumFractionDigits(0); // show only whole percentage points
            } else {
                dislikePercentageFormatter.setMaximumFractionDigits(1); // show up to 1 digit precision
            }
            return dislikePercentageFormatter.format(dislikePercentage);
        }
    }

    public enum Vote {
        LIKE(1),
        DISLIKE(-1),
        LIKE_REMOVE(0);

        public final int value;

        Vote(int value) {
            this.value = value;
        }
    }

    /**
     * Simple wrapper to cache a Future.
     */
    private static class RYDCachedFetch {
        /**
         * How long to retain cached RYD fetches.
         */
        static final long CACHE_TIMEOUT_MILLISECONDS = 4 * 60 * 1000; // 4 Minutes

        @NonNull
        final Future<RYDVoteData> future;
        final String videoId;
        final long timeFetched;

        RYDCachedFetch(@NonNull Future<RYDVoteData> future, @NonNull String videoId) {
            this.future = Objects.requireNonNull(future);
            this.videoId = Objects.requireNonNull(videoId);
            this.timeFetched = System.currentTimeMillis();
        }

        boolean isExpired(long now) {
            return (now - timeFetched) > CACHE_TIMEOUT_MILLISECONDS;
        }

        boolean futureInProgressOrFinishedSuccessfully() {
            try {
                return !future.isDone() || future.get(MAX_MILLISECONDS_TO_BLOCK_UI_WAITING_FOR_FETCH, TimeUnit.MILLISECONDS) != null;
            } catch (ExecutionException | InterruptedException | TimeoutException ex) {
                LogHelper.printException(ReturnYouTubeDislike.class, "failed to lookup cache", ex); // will never happen
            }
            return false;
        }
    }
}

class VerticallyCenteredImageSpan extends ImageSpan {
    public VerticallyCenteredImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize(@NonNull Paint paint, @NonNull CharSequence text,
                       int start, int end, @Nullable Paint.FontMetricsInt fontMetrics) {
        Drawable drawable = getDrawable();
        Rect bounds = drawable.getBounds();
        if (fontMetrics != null) {
            Paint.FontMetricsInt paintMetrics = paint.getFontMetricsInt();
            final int fontHeight = paintMetrics.descent - paintMetrics.ascent;
            final int drawHeight = bounds.bottom - bounds.top;
            final int yCenter = paintMetrics.ascent + fontHeight / 2;

            fontMetrics.ascent = yCenter - drawHeight / 2;
            fontMetrics.top = fontMetrics.ascent;
            fontMetrics.bottom = yCenter + drawHeight / 2;
            fontMetrics.descent = fontMetrics.bottom;
        }
        return bounds.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        Paint.FontMetricsInt paintMetrics = paint.getFontMetricsInt();
        final int fontHeight = paintMetrics.descent - paintMetrics.ascent;
        final int yCenter = y + paintMetrics.descent - fontHeight / 2;
        final Rect drawBounds = drawable.getBounds();
        final int translateY = yCenter - (drawBounds.bottom - drawBounds.top) / 2;
        canvas.translate(x, translateY);
        drawable.draw(canvas);
        canvas.restore();
    }
}