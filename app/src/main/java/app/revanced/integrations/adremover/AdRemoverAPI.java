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
        if (view instanceof LinearLayout) {
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, 0);
            view.setLayoutParams(linearLayoutParams);
        } else if (view instanceof FrameLayout) {
            FrameLayout.LayoutParams frameLayoutParms = new FrameLayout.LayoutParams(0, 0);
            view.setLayoutParams(frameLayoutParms);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams relativeLayoutParms = new RelativeLayout.LayoutParams(0, 0);
            view.setLayoutParams(relativeLayoutParms);
        } else if (view instanceof Toolbar) {
            Toolbar.LayoutParams toolbarParms = new Toolbar.LayoutParams(0, 0);
            view.setLayoutParams(toolbarParms);
        } else if (view instanceof ViewGroup) {
            ViewGroup.LayoutParams viewGroupParms = new ViewGroup.LayoutParams(0, 0);
            view.setLayoutParams(viewGroupParms);
        }
    }

}
