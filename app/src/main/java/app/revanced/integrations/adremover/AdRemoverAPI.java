package app.revanced.integrations.adremover;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

public class AdRemoverAPI {

    /**
     * Removes Reels and Home ads
     *
     */
    //ToDo: refactor this
    public static void HideViewWithLayout1dp(View view) {
        try {
            if (view instanceof LinearLayout) {
                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, 0);
                view.setLayoutParams(linearLayoutParams);
            } else if (view instanceof FrameLayout) {
                FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(0, 0);
                view.setLayoutParams(frameLayoutParams);
            } else if (view instanceof RelativeLayout) {
                RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(0, 0);
                view.setLayoutParams(relativeLayoutParams);
            } else if (view instanceof Toolbar) {
                Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(0, 0);
                view.setLayoutParams(toolbarParams);
            } else if (view instanceof ViewGroup) {
                ViewGroup.LayoutParams viewGroupParams = new ViewGroup.LayoutParams(0, 0);
                view.setLayoutParams(viewGroupParams);
            }
        } catch (Exception ignored) {
        }
    }

}
