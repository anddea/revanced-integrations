package app.revanced.integrations.youtube.patches.misc;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.components.ShareSheetMenuFilter;
import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class ShareSheetPatch {
    private static final boolean changeShareSheetEnabled = Settings.CHANGE_SHARE_SHEET.get();

    private static void clickSystemShareButton(final RecyclerView bottomSheetRecyclerView,
                                               final RecyclerView appsContainerRecyclerView) {
        if (appsContainerRecyclerView.getChildAt(appsContainerRecyclerView.getChildCount() - 1) instanceof ViewGroup parentView &&
                parentView.getChildAt(0) instanceof ViewGroup shareWithOtherAppsView) {
            ShareSheetMenuFilter.isShareSheetMenuVisible = false;

            bottomSheetRecyclerView.setVisibility(View.GONE);
            Utils.clickView(shareWithOtherAppsView);
        }
    }

    /**
     * Injection point.
     */
    public static void onShareSheetMenuCreate(final RecyclerView recyclerView) {
        if (!changeShareSheetEnabled)
            return;

        recyclerView.getViewTreeObserver().addOnDrawListener(() -> {
            try {
                if (!ShareSheetMenuFilter.isShareSheetMenuVisible) {
                    return;
                }
                if (!(recyclerView.getChildAt(0) instanceof ViewGroup parentView4th)) {
                    return;
                }
                if (parentView4th.getChildAt(0) instanceof ViewGroup parentView3rd &&
                        parentView3rd.getChildAt(0) instanceof RecyclerView appsContainerRecyclerView) {
                    clickSystemShareButton(recyclerView, appsContainerRecyclerView);
                } else if (parentView4th.getChildAt(1) instanceof ViewGroup parentView3rd &&
                        parentView3rd.getChildAt(0) instanceof RecyclerView appsContainerRecyclerView) {
                    clickSystemShareButton(recyclerView, appsContainerRecyclerView);
                }
            } catch (Exception ex) {
                Logger.printException(() -> "onShareSheetMenuCreate failure", ex);
            }
        });
    }

    /**
     * Injection point.
     */
    public static String overridePackageName(String original) {
        return changeShareSheetEnabled ? "" : original;
    }

}
