package app.revanced.integrations.sponsorblock;

import static app.revanced.integrations.sponsorblock.player.ui.SponsorBlockView.hideSkipButton;
import static app.revanced.integrations.sponsorblock.player.ui.SponsorBlockView.showSkipButton;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;

import app.revanced.integrations.sponsorblock.objects.SponsorSegment;

public class SkipSegmentView {

    private static SponsorSegment lastNotifiedSegment;

    public static void show() {
        showSkipButton();
    }

    public static void hide() {
        hideSkipButton();
    }

    public static void notifySkipped(SponsorSegment segment) {
        if (segment == lastNotifiedSegment) return;

        lastNotifiedSegment = segment;
        String skipMessage = segment.category.getSkipMessage().toString();
        showToastShort(skipMessage);
    }
}
