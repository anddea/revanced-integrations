package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.patches.ads.LowLevelFilter.isQuickQualityBottomSheet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.litho.ComponentHost;

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
        SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DETECTED.saveValue(false);
    }

    /**
     * Old Quality Layout patch for new player flyout panels.
     *
     * @param linearLayout [BOTTOM_SHEET_FRAGMENT], {@ComponentHost} is located under linearLayout.
     */
    public static void enableOldQualityMenu(LinearLayout linearLayout) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean())
            return;

        // The RecyclerView is placed on the 3rd ChildView.
        if (linearLayout.getChildCount() != 3)
            return;

        // Make sure that this ChildView is castable to the RecyclerView.
        if (!(linearLayout.getChildAt(2) instanceof RecyclerView recyclerView))
            return;

        // When method called from the abstract class, only the RecyclerView is defined.
        // ComponentHost is not defined at here.
        // So we need to add ViewTreeObserver to RecyclerView and check the moment ComponentHost is added to this RecyclerView.
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // The ComponentHost is placed on the 1st ChildView.
                        if (recyclerView.getChildCount() != 1)
                            return;

                        // Make sure that this ChildView is castable to the ComponentHost.
                        if (!(recyclerView.getChildAt(0) instanceof ComponentHost lithoView))
                            return;

                        // Check if [VIDEO_QUALITIES_QUICK_MENU_BOTTOM_SHEET_FRAGMENT] is loaded.
                        // Since ViewTreeObserver is activated even when [VIDEO_QUALITIES_MENU_BOTTOM_SHEET_FRAGMENT] or [SUBTITLE_MENU_BOTTOM_SHEET_FRAGMENT] are loaded,
                        // So this check process is absolutely necessary.
                        // The TargetView is placed on the 4rd ChildView.
                        if (!isQuickQualityBottomSheet || lithoView.getChildCount() < 4)
                            return;

                        // TargetView found, hide [BOTTOM_SHEET_FRAGMENT].
                        linearLayout.setVisibility(View.GONE);

                        // Click the TargetView.
                        lithoView.getChildAt(3).performClick();
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    public static CharSequence hideFeedFlyoutPanel(CharSequence charSequence) {
        if (charSequence == null || !SettingsEnum.HIDE_FEED_FLYOUT_PANEL.getBoolean())
            return charSequence;

        String[] blockList = SettingsEnum.HIDE_FEED_FLYOUT_PANEL_FILTER_STRINGS.getString().split("\\n");

        for (String filter : blockList) {
            if (charSequence.toString().contains(filter) && !filter.isEmpty())
                return null;
        }

        return charSequence;
    }
}
