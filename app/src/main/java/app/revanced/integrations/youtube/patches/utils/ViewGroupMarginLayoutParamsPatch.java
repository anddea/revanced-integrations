package app.revanced.integrations.youtube.patches.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewGroupMarginLayoutParamsPatch {

    public static void hideViewGroupByMarginLayoutParams(ViewGroup viewGroup) {
        // Rest of the implementation added by patch.
        viewGroup.setVisibility(View.GONE);
    }
}
