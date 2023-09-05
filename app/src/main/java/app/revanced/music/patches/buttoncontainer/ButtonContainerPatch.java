package app.revanced.music.patches.buttoncontainer;

import static app.revanced.music.utils.StringRef.str;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.VideoHelpers;

public class ButtonContainerPatch {

    public static boolean hideButtonContainerLabel(boolean original) {
        return !SettingsEnum.HIDE_BUTTON_CONTAINER_LABEL.getBoolean() && original;
    }

    public static void hookButtonContainer(ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int childCount = viewGroup.getChildCount();
                        if (childCount == 0)
                            return;

                        if (!SettingsEnum.BUTTON_CONTAINER_DOWNLOAD_INDEX_FOUND.getBoolean() || SettingsEnum.ENABLE_DEBUG_LOGGING.getBoolean()) {
                            final String downloadButtonDescription = str("action_add_to_offline_songs");
                            for (int i = 0; i < childCount; i++) {
                                View childView = viewGroup.getChildAt(i);
                                String description = childView.getContentDescription().toString();

                                if (!SettingsEnum.BUTTON_CONTAINER_DOWNLOAD_INDEX_FOUND.getBoolean() && description.contains(downloadButtonDescription)) {
                                    SettingsEnum.BUTTON_CONTAINER_DOWNLOAD_INDEX_FOUND.saveValue(true);
                                    SettingsEnum.BUTTON_CONTAINER_DOWNLOAD_INDEX.saveValue(i);
                                }

                                LogHelper.printDebug(ButtonContainerPatch.class, "Button Description: " + description);
                            }
                        }

                        if (SettingsEnum.HOOK_BUTTON_CONTAINER_DOWNLOAD.getBoolean()) {
                            View downloadButton = viewGroup.getChildAt(SettingsEnum.BUTTON_CONTAINER_DOWNLOAD_INDEX.getInt());
                            downloadButton.setOnClickListener(imageView -> VideoHelpers.downloadMusic(imageView.getContext()));
                        }

                        if (SettingsEnum.HIDE_BUTTON_CONTAINER_RADIO.getBoolean()) {
                            viewGroup.getChildAt(viewGroup.getChildCount() - 1).setVisibility(View.GONE);
                        }

                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }
}
