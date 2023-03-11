package app.revanced.integrations.sponsorblock.player.ui;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.NewSegmentHelperLayout;
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.sponsorblock.SponsorBlockUtils;
import app.revanced.integrations.utils.ResourceType;

public class NewSegmentLayout extends FrameLayout {
    private final int rippleEffectId;
    final int defaultBottomMargin;
    final int ctaBottomMargin;
    final int hiddenBottomMargin;

    public NewSegmentLayout(Context context) {
        this(context, null);
    }

    public NewSegmentLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NewSegmentLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        LayoutInflater.from(context).inflate(identifier("new_segment", ResourceType.LAYOUT, context), this, true);
        Resources resources = context.getResources();

        TypedValue rippleEffect = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, rippleEffect, true);
        rippleEffectId = rippleEffect.resourceId;

        ImageButton rewindButton = findViewById(identifier("new_segment_rewind", ResourceType.ID, context));
        if (rewindButton != null) {
            setClickEffect(rewindButton);
            rewindButton.setOnClickListener(v -> PlayerController.skipRelativeMilliseconds(-SettingsEnum.SB_ADJUST_NEW_SEGMENT_STEP.getInt()));
        }
        ImageButton forwardButton = findViewById(identifier("new_segment_forward", ResourceType.ID, context));
        if (forwardButton != null) {
            setClickEffect(forwardButton);
            forwardButton.setOnClickListener(v -> PlayerController.skipRelativeMilliseconds(SettingsEnum.SB_ADJUST_NEW_SEGMENT_STEP.getInt()));
        }
        ImageButton adjustButton = findViewById(identifier("new_segment_adjust", ResourceType.ID, context));
        if (adjustButton != null) {
            setClickEffect(adjustButton);
            adjustButton.setOnClickListener(v -> SponsorBlockUtils.onMarkLocationClicked(NewSegmentHelperLayout.context));
        }
        ImageButton compareButton = findViewById(identifier("new_segment_compare", ResourceType.ID, context));
        if (compareButton != null) {
            setClickEffect(compareButton);
            compareButton.setOnClickListener(v -> SponsorBlockUtils.onPreviewClicked(NewSegmentHelperLayout.context));
        }
        ImageButton editButton = findViewById(identifier("new_segment_edit", ResourceType.ID, context));
        if (editButton != null) {
            setClickEffect(editButton);
            editButton.setOnClickListener(v -> SponsorBlockUtils.onEditByHandClicked(NewSegmentHelperLayout.context));
        }
        ImageButton publishButton = findViewById(identifier("new_segment_publish", ResourceType.ID, context));
        if (publishButton != null) {
            setClickEffect(publishButton);
            publishButton.setOnClickListener(v -> SponsorBlockUtils.onPublishClicked(NewSegmentHelperLayout.context));
        }

        defaultBottomMargin = resources.getDimensionPixelSize(identifier("brand_interaction_default_bottom_margin", ResourceType.DIMEN, context));
        ctaBottomMargin = resources.getDimensionPixelSize(identifier("brand_interaction_cta_bottom_margin", ResourceType.DIMEN, context));
        hiddenBottomMargin = (int) Math.round((ctaBottomMargin) * 0.5);  // margin when the button container is hidden
    }

    private void setClickEffect(ImageButton btn) {
        btn.setBackgroundResource(rippleEffectId);

        RippleDrawable rippleDrawable = (RippleDrawable) btn.getBackground();

        int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}};
        int[] colors = new int[]{0x33ffffff}; // sets the ripple color to white

        ColorStateList colorStateList = new ColorStateList(states, colors);
        rippleDrawable.setColor(colorStateList);
    }
}
