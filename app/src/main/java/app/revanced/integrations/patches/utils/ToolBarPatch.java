package app.revanced.integrations.patches.utils;

import android.view.View;
import android.widget.ImageView;

public class ToolBarPatch {

    public static void hookToolBar(Enum<?> buttonEnum, ImageView imageView) {
        final String enumString = buttonEnum.name();
        if (enumString.isEmpty() || !(imageView.getParent() instanceof View view))
            return;
        hookToolBar(enumString, view);
    }

    private static void hookToolBar(String enumString, View parentView) {
    }
}


