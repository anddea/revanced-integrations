package app.revanced.integrations.sponsorblock;

import static app.revanced.integrations.sponsorblock.SponsorBlockUtils.timeWithoutSegments;
import static app.revanced.integrations.sponsorblock.SponsorBlockUtils.videoHasSegments;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.objects.SponsorSegment;
import app.revanced.integrations.sponsorblock.requests.SBRequester;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.whitelist.Whitelist;

public class PlayerController {

    private static final Timer sponsorTimer = new Timer("sponsor-skip-timer");
    public static WeakReference<Activity> playerActivity = new WeakReference<>(null);
    public static SponsorSegment[] sponsorSegmentsOfCurrentVideo;
    private static long allowNextSkipRequestTime = 0L;
    public static String currentVideoId;
    public static long lastKnownVideoTime = -1L;
    public static long lastKnownVideoLength = 1L;
    private static final Runnable findAndSkipSegmentRunnable = () -> {
        findAndSkipSegment(false);
    };
    private static float sponsorBarLeft = 1f;
    private static float sponsorBarRight = 1f;
    private static float sponsorBarThickness = 2f;
    private static TimerTask skipSponsorTask = null;

    public static String getCurrentVideoId() {
        return currentVideoId;
    }

    public static void setCurrentVideoId(final String videoId) {
        if (videoId == null || !SettingsEnum.SB_ENABLED.getBoolean()) {
            currentVideoId = null;
            sponsorSegmentsOfCurrentVideo = null;
            return;
        }

        if (videoId.equals(currentVideoId)) return;

        SponsorBlockSettings.update(ReVancedUtils.getContext());

        currentVideoId = videoId;
        sponsorSegmentsOfCurrentVideo = null;

        sponsorTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeDownloadSegments(currentVideoId);
            }
        }, 0);
    }

    /**
     * Called when creating some kind of youtube internal player controlled, every time when new video starts to play
     */
    public static void initialize() {
        lastKnownVideoTime = 0;
        SkipSegmentView.hide();
        NewSegmentHelperLayout.hide();
    }

    public static void executeDownloadSegments(String videoId) {
        videoHasSegments = false;
        timeWithoutSegments = "";
        if (Whitelist.isChannelSBWhitelisted())
            return;

        SponsorSegment[] segments = SBRequester.getSegments(videoId);
        Arrays.sort(segments);

        sponsorSegmentsOfCurrentVideo = segments;
    }

    /**
     * Called when it's time to update the UI with new second, about once per second, only when playing, also in background
     */
    public static void setCurrentVideoTime(long millis) {
        if (!SettingsEnum.SB_ENABLED.getBoolean()) return;
        lastKnownVideoTime = millis;
        if (millis <= 0) return;

        if (millis == lastKnownVideoLength) {
            SponsorBlockUtils.hideShieldButton();
            SponsorBlockUtils.hideVoteButton();
            return;
        }

        SponsorSegment[] segments = sponsorSegmentsOfCurrentVideo;
        if (segments == null || segments.length == 0) return;

        final long START_TIMER_BEFORE_SEGMENT_MILLIS = 1200;
        final long startTimerAtMillis = millis + START_TIMER_BEFORE_SEGMENT_MILLIS;

        for (final SponsorSegment segment : segments) {
            if (segment.start > millis) {
                if (segment.start > startTimerAtMillis) break; // it's more then START_TIMER_BEFORE_SEGMENT_MILLIS far away
                if (!segment.category.getBehaviour().getSkip()) break;

                if (skipSponsorTask == null) {
                    skipSponsorTask = new TimerTask() {
                        @Override
                        public void run() {
                            skipSponsorTask = null;
                            lastKnownVideoTime = segment.start + 1;
                            VideoInformation.lastKnownVideoTime = lastKnownVideoTime;
                            ReVancedUtils.runOnMainThread(findAndSkipSegmentRunnable);
                        }
                    };
                    sponsorTimer.schedule(skipSponsorTask, segment.start - millis);
                }

                break;
            }

            if (segment.end < millis) continue;

            // we are in the segment!
            final var behaviour = segment.category.getBehaviour();
            if (behaviour.getSkip() && !(behaviour.getKey().equals("skip-once") && segment.hasAutoSkipped)) {
                sendViewRequestAsync(millis, segment);
                skipSegment(segment, false);
                break;
            } else {
                SkipSegmentView.show();
                return;
            }
        }
        SkipSegmentView.hide();
    }

    private static void sendViewRequestAsync(final long millis, final SponsorSegment segment) {
        if (segment.category != SponsorBlockSettings.SegmentInfo.UNSUBMITTED) {
            long newSkippedTime = SettingsEnum.SB_SKIPPED_SEGMENTS_TIME.getLong() + (segment.end - segment.start);
            SettingsEnum.SB_SKIPPED_SEGMENTS.saveValue(SettingsEnum.SB_SKIPPED_SEGMENTS.getInt() + 1);
            SettingsEnum.SB_SKIPPED_SEGMENTS_TIME.saveValue(newSkippedTime);
        }
        if (SettingsEnum.SB_COUNT_SKIPS.getBoolean() &&
                segment.category != SponsorBlockSettings.SegmentInfo.UNSUBMITTED &&
                millis - segment.start < 2000) {
            // Only skips from the start should count as a view
            new Thread(() -> SBRequester.sendViewCountRequest(segment)).start();
        }
    }

    /**
     * Called very high frequency (once every about 100ms), also in background. It sometimes triggers when a video is paused (couple times in the row with the same value)
     */
    public static void setCurrentVideoTimeHighPrecision(final long millis) {
        if ((millis < lastKnownVideoTime && lastKnownVideoTime >= lastKnownVideoLength) || millis == 0) {
            SponsorBlockUtils.showShieldButton(); // skipping from end to the video will show the buttons again
            SponsorBlockUtils.showVoteButton();
        }
        if (lastKnownVideoTime > 0) {
            lastKnownVideoTime = millis;
        } else setCurrentVideoTime(millis);
    }

    public static long getCurrentVideoLength() {
        return VideoInformation.getCurrentVideoLength();
    }

    public static long getLastKnownVideoTime() {
        return lastKnownVideoTime;
    }

    public static void setSponsorBarAbsoluteLeft(final Rect rect) {
        sponsorBarLeft = rect.left;
    }

    public static void setSponsorBarRect(final Object self) {
        try {
            Field field = self.getClass().getDeclaredField("replaceMeWithsetSponsorBarRect");
            field.setAccessible(true);
            Rect rect = (Rect) field.get(self);
            if (rect != null) {
                setSponsorBarAbsoluteLeft(rect);
                setSponsorBarAbsoluteRight(rect);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSponsorBarAbsoluteRight(final Rect rect) {
        sponsorBarRight = rect.right;
    }

    public static void setSponsorBarThickness(final int thickness) {
        sponsorBarThickness = (float) thickness;
    }

    public static void onSkipSponsorClicked() {
        findAndSkipSegment(true);
    }


    public static void addSkipSponsorView15(final View view) {
        playerActivity = new WeakReference<>((Activity) view.getContext());

        ReVancedUtils.runDelayed(() -> {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(2);
            NewSegmentHelperLayout.context = viewGroup.getContext();
        }, 500L);
    }

    /**
     * Called when it's time to draw time bar
     */
    public static void drawSponsorTimeBars(final Canvas canvas, final float posY) throws InterruptedException {
        if (sponsorBarThickness < 0.1) return;
        if (sponsorSegmentsOfCurrentVideo == null) return;

        final float thicknessDiv2 = sponsorBarThickness / 2;
        final float top = posY - thicknessDiv2;
        final float bottom = posY + thicknessDiv2;
        final float absoluteLeft = sponsorBarLeft;
        final float absoluteRight = sponsorBarRight;

        final float tmp1 = 1f / (float) lastKnownVideoLength * (absoluteRight - absoluteLeft);
        for (SponsorSegment segment : sponsorSegmentsOfCurrentVideo) {
            float left = segment.start * tmp1 + absoluteLeft;
            float right = segment.end * tmp1 + absoluteLeft;
            canvas.drawRect(left, top, right, bottom, segment.category.getPaint());
        }
    }

    public static void skipRelativeMilliseconds(int millisRelative) {
        skipToMillisecond(lastKnownVideoTime + millisRelative);
    }

    public static boolean skipToMillisecond(long millisecond) {
        // in 15.x if sponsor clip hits the end, then it crashes the app, because of too many function invocations
        // I put this block so that skip can be made only once per some time
        long now = System.currentTimeMillis();
        if (now < allowNextSkipRequestTime) {
            return false;
        }
        allowNextSkipRequestTime = now + 100;


        try {
            lastKnownVideoTime = millisecond;
            VideoInformation.seekTo(millisecond);
        } catch (Exception e) {
            LogHelper.printException(PlayerController.class, "Cannot skip to millisecond", e);
        }

        return true;
    }


    private static void findAndSkipSegment(boolean wasClicked) {
        if (sponsorSegmentsOfCurrentVideo == null)
            return;

        final long millis = lastKnownVideoTime;

        for (SponsorSegment segment : sponsorSegmentsOfCurrentVideo) {
            if (segment.start > millis) break;

            if (segment.end < millis) continue;

            SkipSegmentView.show();

            final var behaviour = segment.category.getBehaviour();
            if (!((behaviour.getSkip() && !(behaviour.getKey().equals("skip-once") && segment.hasAutoSkipped)) || wasClicked))
                return;

            sendViewRequestAsync(millis, segment);
            skipSegment(segment, wasClicked);
            break;
        }

        SkipSegmentView.hide();
    }

    private static void skipSegment(SponsorSegment segment, boolean wasClicked) {
        if (SettingsEnum.SB_SHOW_TOAST_WHEN_SKIP.getBoolean()) SkipSegmentView.notifySkipped(segment);

        boolean didSucceed = skipToMillisecond(segment.end + 2);
        if (didSucceed && !wasClicked) {
            segment.hasAutoSkipped = true;
        }
        SkipSegmentView.hide();
        if (segment.category == SponsorBlockSettings.SegmentInfo.UNSUBMITTED) {
            SponsorSegment[] newSegments = new SponsorSegment[sponsorSegmentsOfCurrentVideo.length - 1];
            int i = 0;
            for (SponsorSegment sponsorSegment : sponsorSegmentsOfCurrentVideo) {
                if (sponsorSegment != segment)
                    newSegments[i++] = sponsorSegment;
            }
            sponsorSegmentsOfCurrentVideo = newSegments;
        }
    }
}
