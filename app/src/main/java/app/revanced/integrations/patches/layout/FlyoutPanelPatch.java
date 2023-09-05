package app.revanced.integrations.patches.layout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.litho.ComponentHost;

import app.revanced.integrations.patches.ads.VideoQualityMenuFilter;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class FlyoutPanelPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean())
            return;

        listView.setVisibility(View.GONE);

        ReVancedUtils.runOnMainThreadDelayed(() ->
                        listView.performItemClick(null, 4, 0),
                1
        );
    }

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

    public static void onFlyoutMenuCreate(final RecyclerView recyclerView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean()) return;

        recyclerView.getViewTreeObserver().addOnDrawListener(
                () -> {
                    // Check if the current view is the quality menu.
                    if (VideoQualityMenuFilter.isVideoQualityMenuVisible) {
                        VideoQualityMenuFilter.isVideoQualityMenuVisible = false;
                        ((ViewGroup) recyclerView.getParent().getParent().getParent()).setVisibility(View.GONE);

                        // Click the "Advanced" quality menu to show the "old" quality menu.
                        ((ComponentHost) recyclerView.getChildAt(0)).getChildAt(3).performClick();
                    }
                });
    }
}
