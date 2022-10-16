package app.revanced.integrations.patches;

import app.revanced.integrations.utils.ThemeHelper;

public class LithoThemePatch {
    //Used by app.revanced.patches.youtube.layout.theme.patch.LithoThemePatch
    public static int applyLithoTheme(int originalValue) {
        int[] ColorList = {-14606047, -15198184, -15790321, -98492127};

        if (!ThemeHelper.isDarkTheme() && originalValue == -1) return 0;
        for (int i = 0; i < ColorList.length ; i++) {
            if (ColorList[i] == originalValue) return 0;
        }

        return originalValue;
    }
}