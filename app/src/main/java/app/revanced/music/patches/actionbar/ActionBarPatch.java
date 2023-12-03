package app.revanced.music.patches.actionbar;

import static app.revanced.music.utils.StringRef.str;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.VideoHelpers;

public class ActionBarPatch {

    public static boolean hideActionBarLabel(boolean original) {
        return !SettingsEnum.HIDE_ACTION_BAR_LABEL.getBoolean() && original;
    }

    private static void hideRadioButton(ViewGroup viewGroup, int childCount) {
        if (!SettingsEnum.HIDE_ACTION_BAR_RADIO.getBoolean()) {
            return;
        }
        View radioButton = viewGroup.getChildAt(childCount - 1);

        if (radioButton != null) {
            radioButton.setVisibility(View.GONE);
        }
    }

    public static void hookActionBar(ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    try {
                        final int childCount = viewGroup.getChildCount();
                        if (childCount == 0)
                            return;

                        hookDownloadButton(viewGroup, childCount);
                        hideRadioButton(viewGroup, childCount);
                    } catch (Exception ex) {
                        LogHelper.printException(() -> "hookActionBar failure", ex);
                    }
                });
    }

    private static void hookDownloadButton(ViewGroup viewGroup, int childCount) {
        if (!SettingsEnum.HOOK_ACTION_BAR_DOWNLOAD.getBoolean()) {
            return;
        }
        int downloadButtonIndex = 3;
        if (SettingsEnum.SPOOF_APP_VERSION.getBoolean() || childCount < 5) {
            final String description = str("action_add_to_offline_songs");
            downloadButtonIndex = -1;
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView != null) {
                    String buttonDescription = childView.getContentDescription().toString();
                    if (buttonDescription.contains(description)) {
                        downloadButtonIndex = i;
                        break;
                    }
                }
            }
            if (downloadButtonIndex == -1) {
                return;
            }
        }

        View downloadButton = viewGroup.getChildAt(downloadButtonIndex);
        if (downloadButton != null) {
            downloadButton.setOnClickListener(imageView -> VideoHelpers.downloadMusic(imageView.getContext()));
        }
    }
}
