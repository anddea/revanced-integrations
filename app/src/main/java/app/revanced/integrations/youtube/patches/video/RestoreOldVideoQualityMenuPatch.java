package app.revanced.integrations.youtube.patches.video;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.components.VideoQualityMenuFilter;
import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class RestoreOldVideoQualityMenuPatch {

    public static boolean restoreOldVideoQualityMenu() {
        return Settings.RESTORE_OLD_VIDEO_QUALITY_MENU.get();
    }

    public static void restoreOldVideoQualityMenu(ListView listView) {
        if (!Settings.RESTORE_OLD_VIDEO_QUALITY_MENU.get())
            return;

        listView.setVisibility(View.GONE);

        Utils.runOnMainThreadDelayed(() -> {
                    listView.setSoundEffectsEnabled(false);
                    listView.performItemClick(null, 2, 0);
                },
                1
        );
    }

    public static void onFlyoutMenuCreate(final RecyclerView recyclerView) {
        if (!Settings.RESTORE_OLD_VIDEO_QUALITY_MENU.get())
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
                    Utils.clickView(AdvancedQualityView);
                    VideoQualityMenuFilter.isVideoQualityMenuVisible = false;
                }
            } catch (Exception ex) {
                Logger.printException(() -> "onFlyoutMenuCreate failure", ex);
            }
        });
    }
}
