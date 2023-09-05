package app.revanced.music.patches.buttoncontainer;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import app.revanced.music.settings.SettingsEnum;

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

                        if (SettingsEnum.HIDE_BUTTON_CONTAINER_RADIO.getBoolean()) {
                            viewGroup.getChildAt(viewGroup.getChildCount() - 1).setVisibility(View.GONE);
                        }

                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }
}
