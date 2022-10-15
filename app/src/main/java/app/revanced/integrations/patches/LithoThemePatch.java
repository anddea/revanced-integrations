package app.revanced.integrations.patches;

import app.revanced.integrations.utils.ThemeHelper;

public class LithoThemePatch {
    //Used by app.revanced.patches.youtube.layout.theme.patch.LithoThemePatch
    public static int applyLithoTheme(int originalValue) {
        int newValue = 0;

        if (!ThemeHelper.isDarkTheme() && originalValue == -1) {
            return newValue;
        } else if (originalValue == -14606047 || originalValue == -15790321 || originalValue == -98492127) {
            return newValue;
        }

        return originalValue;
    }
}