package app.revanced.integrations.sponsorblock.ui;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.integrations.sponsorblock.SegmentPlaybackController;
import app.revanced.integrations.sponsorblock.objects.SponsorSegment;
import app.revanced.integrations.utils.ResourceType;

public class SkipSponsorButton extends FrameLayout {
    private static final boolean highContrast = true;
    final int defaultBottomMargin;
    final int ctaBottomMargin;
    final int hiddenBottomMargin;
    private final LinearLayout skipSponsorBtnContainer;
    private final TextView skipSponsorTextView;
    private final Paint background;
    private final Paint border;
    private SponsorSegment segment;

    public SkipSponsorButton(Context context) {
        this(context, null);
    }

    public SkipSponsorButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SkipSponsorButton(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this(context, attributeSet, defStyleAttr, 0);
    }

    public SkipSponsorButton(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);

        LayoutInflater.from(context).inflate(identifier("skip_sponsor_button", ResourceType.LAYOUT, context), this, true);  // layout:skip_ad_button
        setMinimumHeight(getResources().getDimensionPixelSize(identifier("ad_skip_ad_button_min_height", ResourceType.DIMEN, context)));  // dimen:ad_skip_ad_button_min_height
        skipSponsorBtnContainer = (LinearLayout) Objects.requireNonNull((View) findViewById(identifier("sb_skip_sponsor_button_container", ResourceType.ID, context)));  // id:skip_ad_button_container
        background = new Paint();
        background.setColor(context.getColor(identifier("skip_ad_button_background_color", ResourceType.COLOR, context)));  // color:skip_ad_button_background_color);
        background.setStyle(Paint.Style.FILL);
        border = new Paint();
        border.setColor(context.getColor(identifier("skip_ad_button_border_color", ResourceType.COLOR, context)));  // color:skip_ad_button_border_color);
        border.setStrokeWidth(getResources().getDimension(identifier("ad_skip_ad_button_border_width", ResourceType.DIMEN, context)));  // dimen:ad_skip_ad_button_border_width
        border.setStyle(Paint.Style.STROKE);
        skipSponsorTextView = (TextView) Objects.requireNonNull((View) findViewById(identifier("sb_skip_sponsor_button_text", ResourceType.ID, context)));  // id:sb_skip_sponsor_button_text;
        Resources resources = context.getResources();
        defaultBottomMargin = resources.getDimensionPixelSize(identifier("skip_button_default_bottom_margin", ResourceType.DIMEN, context));  // dimen:skip_button_default_bottom_margin
        ctaBottomMargin = resources.getDimensionPixelSize(identifier("skip_button_cta_bottom_margin", ResourceType.DIMEN, context));  // dimen:skip_button_cta_bottom_margin
        hiddenBottomMargin = (int) Math.round((ctaBottomMargin) * 0.5);  // margin when the button container is hidden

        skipSponsorBtnContainer.setOnClickListener(v -> {
            // The view controller handles hiding this button, but hide it here as well just in case something goofs.
            setVisibility(View.GONE);
            SegmentPlaybackController.onSkipSegmentClicked(segment);
        });
    }

    @Override  // android.view.ViewGroup
    protected final void dispatchDraw(Canvas canvas) {
        final int left = skipSponsorBtnContainer.getLeft();
        final int top = skipSponsorBtnContainer.getTop();
        final int leftPlusWidth = (left + skipSponsorBtnContainer.getWidth());
        final int topPlusHeight = (top + skipSponsorBtnContainer.getHeight());
        canvas.drawRect(left, top, leftPlusWidth, topPlusHeight, background);
        if (!highContrast) {
            canvas.drawLines(new float[]{
                            leftPlusWidth, top, left, top,
                            left, top, left, topPlusHeight,
                            left, topPlusHeight, leftPlusWidth, topPlusHeight},
                    border);
        }

        super.dispatchDraw(canvas);
    }

    /**
     * @return true, if this button state was changed
     */
    public boolean updateSkipButtonText(@NonNull SponsorSegment segment) {
        this.segment = segment;
        CharSequence newText = segment.getSkipButtonText();
        if (newText.equals(skipSponsorTextView.getText())) {
            return false;
        }
        skipSponsorTextView.setText(newText);
        return true;
    }
}
