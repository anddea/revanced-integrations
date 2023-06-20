package app.revanced.integrations.sponsorblock.ui;

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

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.SponsorBlockUtils;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ResourceType;

public class NewSegmentLayouttmp extends FrameLayout {
    final int defaultBottomMargin;
    final int ctaBottomMargin;
    final int hiddenBottomMargin;
    private final int rippleEffectId;

    public NewSegmentLayouttmp(Context context) {
        this(context, null);
    }

    public NewSegmentLayouttmp(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NewSegmentLayouttmp(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this(context, attributeSet, defStyleAttr, 0);
    }

    public NewSegmentLayouttmp(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);

        LayoutInflater.from(context).inflate(identifier("new_segment", ResourceType.LAYOUT, context), this, true);

        TypedValue rippleEffect = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, rippleEffect, true);
        rippleEffectId = rippleEffect.resourceId;

        // LinearLayout newSegmentContainer = findViewById(getResourceIdentifier(context, "sb_new_segment_container", "id"));

        ImageButton rewindButton = findViewById(identifier("sb_new_segment_rewind", ResourceType.ID, context));
        if (rewindButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find rewindButton");
        } else {
            setClickEffect(rewindButton);
            rewindButton.setOnClickListener(v -> VideoInformation.seekToRelative(-SettingsEnum.SB_ADJUST_NEW_SEGMENT_STEP.getInt()));
        }
        ImageButton forwardButton = findViewById(identifier("sb_new_segment_forward", ResourceType.ID, context));
        if (forwardButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find forwardButton");
        } else {
            setClickEffect(forwardButton);
            forwardButton.setOnClickListener(v -> VideoInformation.seekToRelative(SettingsEnum.SB_ADJUST_NEW_SEGMENT_STEP.getInt()));
        }
        ImageButton adjustButton = findViewById(identifier("sb_new_segment_adjust", ResourceType.ID, context));
        if (adjustButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find adjustButton");
        } else {
            setClickEffect(adjustButton);
            adjustButton.setOnClickListener(v -> SponsorBlockUtils.onMarkLocationClicked());
        }
        ImageButton compareButton = findViewById(identifier("sb_new_segment_compare", ResourceType.ID, context));
        if (compareButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find compareButton");
        } else {
            setClickEffect(compareButton);
            compareButton.setOnClickListener(v -> SponsorBlockUtils.onPreviewClicked());
        }
        ImageButton editButton = findViewById(identifier("sb_new_segment_edit", ResourceType.ID, context));
        if (editButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find editButton");
        } else {
            setClickEffect(editButton);
            editButton.setOnClickListener(v -> SponsorBlockUtils.onEditByHandClicked());
        }
        ImageButton publishButton = findViewById(identifier("sb_new_segment_publish", ResourceType.ID, context));
        if (publishButton == null) {
            LogHelper.printException(NewSegmentLayouttmp.class, "Could not find publishButton");
        } else {
            setClickEffect(publishButton);
            publishButton.setOnClickListener(v -> SponsorBlockUtils.onPublishClicked());
        }
        Resources resources = context.getResources();
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
