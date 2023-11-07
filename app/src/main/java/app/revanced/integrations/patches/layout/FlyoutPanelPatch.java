package app.revanced.integrations.patches.layout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import app.revanced.integrations.patches.components.VideoQualityMenuFilter;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class FlyoutPanelPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean())
            return;

        listView.setVisibility(View.GONE);

        ReVancedUtils.runOnMainThreadDelayed(() -> {
                    listView.setSoundEffectsEnabled(false);
                    listView.performItemClick(null, 4, 0);
                },
                1
        );
    }

    /**
     * hide feed flyout panel for phone
     *
     * @param charSequence raw text
     */
    public static CharSequence hideFeedFlyoutPanel(CharSequence charSequence) {
        if (charSequence == null || !SettingsEnum.HIDE_FEED_FLYOUT_PANEL.getBoolean())
            return charSequence;

        String[] blockList = SettingsEnum.HIDE_FEED_FLYOUT_PANEL_FILTER_STRINGS.getString().split("\\n");
        String targetString = charSequence.toString();

        for (String filter : blockList) {
            if (targetString.equals(filter) && !filter.isEmpty())
                return null;
        }

        return charSequence;
    }

    /**
     * hide feed flyout panel for tablet
     *
     * @param textView     flyout text view
     * @param charSequence raw text
     */
    public static void hideFeedFlyoutPanel(TextView textView, CharSequence charSequence) {
        if (charSequence == null || !SettingsEnum.HIDE_FEED_FLYOUT_PANEL.getBoolean())
            return;

        if (textView.getParent() == null || !(textView.getParent() instanceof View parentView))
            return;

        String[] blockList = SettingsEnum.HIDE_FEED_FLYOUT_PANEL_FILTER_STRINGS.getString().split("\\n");
        String targetString = charSequence.toString();

        for (String filter : blockList) {
            if (targetString.equals(filter) && !filter.isEmpty())
                ReVancedUtils.hideViewByLayoutParams(parentView);
        }
    }

    public static void hideFooterCaptions(View view) {
        ReVancedUtils.hideViewUnderCondition(
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_CAPTIONS_FOOTER.getBoolean(),
                view
        );
    }

    public static void hideFooterQuality(View view) {
        ReVancedUtils.hideViewUnderCondition(
                SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_QUALITY_FOOTER.getBoolean(),
                view
        );
    }

    public static void onFlyoutMenuCreate(final RecyclerView recyclerView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean())
            return;

        recyclerView.getViewTreeObserver().addOnDrawListener(() -> {
            try {
                // Check if the current view is the quality menu.
                if (!VideoQualityMenuFilter.isVideoQualityMenuVisible || recyclerView.getChildCount() == 0)
                    return;

                final ViewGroup AdvancedQualityParentView = (ViewGroup) recyclerView.getChildAt(0);
                if (AdvancedQualityParentView.getChildCount() < 4)
                    return;

                final View AdvancedQualityView = AdvancedQualityParentView.getChildAt(3);
                final View QuickQualityView = (View) recyclerView.getParent().getParent().getParent();
                if (AdvancedQualityView != null && QuickQualityView != null) {
                    QuickQualityView.setVisibility(View.GONE);
                    AdvancedQualityView.setSoundEffectsEnabled(false);
                    AdvancedQualityView.performClick();
                    VideoQualityMenuFilter.isVideoQualityMenuVisible = false;
                }
            } catch (Exception ex) {
                LogHelper.printException(FlyoutPanelPatch.class, "onFlyoutMenuCreate failure", ex);
            }
        });
    }
}
