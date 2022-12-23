package app.revanced.integrations.returnyoutubedislike;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.CompactDecimalFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.text.SpannableString;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import app.revanced.integrations.returnyoutubedislike.requests.RYDVoteData;
import app.revanced.integrations.returnyoutubedislike.requests.ReturnYouTubeDislikeApi;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.SharedPrefHelper;

public class ReturnYouTubeDislike {
    /**
     * Maximum amount of time to block the UI from updates while waiting for dislike network call to complete.
     *
     * Must be less than 5 seconds, as per:
     * https://developer.android.com/topic/performance/vitals/anr
     */
    private static final long MILLISECONDS_TO_BLOCK_UI_WHILE_WAITING_FOR_DISLIKE_FETCH_TO_COMPLETE = 4000;

    /**
     * Used to send votes, one by one, in the same order the user created them
     */
    private static final ExecutorService voteSerialExecutor = Executors.newSingleThreadExecutor();

    // Must be volatile, since this is read/wright from different threads
    private static volatile boolean isEnabled = SettingsEnum.RYD_ENABLED.getBoolean();

    /**
     * Used to guard {@link #currentVideoId} and {@link #voteFetchFuture},
     * as multiple threads access this class.
     */
    private static final Object videoIdLockObject = new Object();

    @GuardedBy("videoIdLockObject")
    private static String currentVideoId;

    /**
     * Stores the results of the vote api fetch, and used as a barrier to wait until fetch completes
     */
    @GuardedBy("videoIdLockObject")
    private static Future<RYDVoteData> voteFetchFuture;

    public enum Vote {
        LIKE(1),
        DISLIKE(-1),
        LIKE_REMOVE(0);

        public final int value;

        Vote(int value) {
            this.value = value;
        }
    }

    private ReturnYouTubeDislike() {
    } // only static methods

    /**
     * Used to format like/dislike count.
     */
    @GuardedBy("ReturnYouTubeDislike.class") // not thread safe
    private static CompactDecimalFormat dislikeCountFormatter;

    /**
     * Used to format like/dislike count.
     */
    @GuardedBy("ReturnYouTubeDislike.class") // not thread safe
    private static DecimalFormat dislikePercentageFormatter;

    public static void onEnabledChange(boolean enabled) {
        isEnabled = enabled;
    }

    private static String getCurrentVideoId() {
        synchronized (videoIdLockObject) {
            return currentVideoId;
        }
    }

    private static Future<RYDVoteData> getVoteFetchFuture() {
        synchronized (videoIdLockObject) {
            return voteFetchFuture;
        }
    }

    // It is unclear if this method is always called on the main thread (since the YouTube app is the one making the call)
    // treat this as if any thread could call this method
    public static void newVideoLoaded(String videoId) {
        if (!isEnabled) return;
        try {
            Objects.requireNonNull(videoId);

            synchronized (videoIdLockObject) {
                currentVideoId = videoId;
                // no need to wrap the fetchDislike call in a try/catch,
                // as any exceptions are propagated out in the later Future#Get call
                voteFetchFuture = ReVancedUtils.submitOnBackgroundThread(() -> ReturnYouTubeDislikeApi.fetchVotes(videoId));
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "Failed to load new video: " + videoId, ex);
        }
    }

    // BEWARE! This method is sometimes called on the main thread, but it usually is called _off_ the main thread!
    public static void onComponentCreated(Object conversionContext, AtomicReference<Object> textRef) {
        if (!isEnabled) return;

        try {
            var conversionContextString = conversionContext.toString();

            boolean isSegmentedButton = false;
            // Check for new component
            if (conversionContextString.contains("|segmented_like_dislike_button.eml|")) {
                isSegmentedButton = true;
            } else if (!conversionContextString.contains("|dislike_button.eml|")) {
                return;
            }

            // Have to block the current thread until fetching is done
            // There's no known way to edit the text after creation yet
            RYDVoteData votingData;
            try {
                Future<RYDVoteData> fetchFuture = getVoteFetchFuture();
                fetchFuture.isDone();
                votingData = fetchFuture.get(MILLISECONDS_TO_BLOCK_UI_WHILE_WAITING_FOR_DISLIKE_FETCH_TO_COMPLETE, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                return;
            }
            if (votingData == null) {
                return;
            }

            updateDislike(textRef, isSegmentedButton, votingData);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "Error while trying to set dislikes text", ex);
        }
    }

    public static void sendVote(Vote vote) {
        if (!isEnabled) return;
        try {
            Objects.requireNonNull(vote);
            Context context = Objects.requireNonNull(ReVancedUtils.getContext());
            if (SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.YOUTUBE, "user_signed_out", true)) {
                return;
            }

            // Must make a local copy of videoId, since it may change between now and when the vote thread runs
            String videoIdToVoteFor = getCurrentVideoId();

            voteSerialExecutor.execute(() -> {
                // must wrap in try/catch to properly log exceptions
                try {
                    String userId = getUserId();
                    if (userId != null) {
                        ReturnYouTubeDislikeApi.sendVote(videoIdToVoteFor, userId, vote);
                    }
                } catch (Exception ex) {
                    LogHelper.printException(ReturnYouTubeDislike.class, "Failed to send vote", ex);
                }
            });
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislike.class, "Error while trying to send vote", ex);
        }
    }

    /**
     * Must call off main thread, as this will make a network call if user has not yet been registered
     *
     * @return ReturnYouTubeDislike user ID. If user registration has never happened
     * and the network call fails, this will return NULL
     */
    @Nullable
    private static String getUserId() {
        ReVancedUtils.verifyOffMainThread();

        String userId = SettingsEnum.RYD_USER_ID.getString();
        if (userId != null) {
            return userId;
        }

        userId = ReturnYouTubeDislikeApi.registerAsNewUser(); // blocks until network call is completed
        if (userId != null) {
            SettingsEnum.RYD_USER_ID.saveValue(userId);
        }
        return userId;
    }

    private static void updateDislike(AtomicReference<Object> textRef, boolean isSegmentedButton, RYDVoteData voteData) {
        SpannableString oldSpannableString = (SpannableString) textRef.get();

        String newString = SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean()
                ? formatPercentage(voteData.dislikePercentage)
                : formatCount(voteData.dislikeCount);

        if (isSegmentedButton) {
            ReVancedHelper.setRTL();
            String oldString = ReVancedHelper.getOldString(oldSpannableString.toString());
            
            String likesString = formatCount(voteData.likeCount);
            if (!oldString.contains(".")) {
                try {
                    likesString = formatCount(Long.parseLong(oldString));
                } catch (Exception ignored) {}
            }

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
        synchronized (ReturnYouTubeDislike.class) { // number formatter is not thread safe, must synchronize
            if (dislikeCountFormatter == null) {
                Locale locale = ReVancedUtils.getContext().getResources().getConfiguration().locale;
                dislikeCountFormatter = CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT);
            }
            formatted = dislikeCountFormatter.format(dislikeCount);
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
}
