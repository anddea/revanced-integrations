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
        if (!SettingsEnum.HOOK_ACTION_BAR_DOWNLOAD.getBoolean() && !SettingsEnum.HIDE_ACTION_BAR_RADIO.getBoolean()) {
            return;
        }

        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        try {
                            int childCount = viewGroup.getChildCount();
                            if (childCount == 0)
                                return;

                            hookDownloadButton(str("action_add_to_offline_songs"), viewGroup, childCount);
                            hideRadioButton(viewGroup, childCount);
                        } catch (Exception ex) {
                            LogHelper.printException(ActionBarPatch.class, "hookActionBar failure", ex);
                        } finally {
                            viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
    }

    private static void hookDownloadButton(String description, ViewGroup viewGroup, int childCount) {
        if (!SettingsEnum.HOOK_ACTION_BAR_DOWNLOAD.getBoolean()) {
            return;
        }
        int downloadButtonIndex = -1;
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

        if (downloadButtonIndex != -1) {
            View downloadButton = viewGroup.getChildAt(downloadButtonIndex);
            if (downloadButton != null) {
                downloadButton.setOnClickListener(imageView -> VideoHelpers.downloadMusic(imageView.getContext()));
            }
        }
    }
}
