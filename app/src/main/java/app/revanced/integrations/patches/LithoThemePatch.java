package app.revanced.integrations.patches;

import android.content.Context;
import android.util.Log;

import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.ThemeHelper;

public class LithoThemePatch {
    private static final int[] CONSTANTS = {
        -15198184, // music related results panel background
        -98492127, // video chapters list background
        -14145496 // drawer content view background
    };

    private static final int[] DARKCONSTANTS = {
        -15790321 // comments chip background (new layout)
    };

    private static final int[] EXCEPTION = {
        -14606047 // comments chip background
    };

    // Used by app.revanced.patches.youtube.layout.theme.patch.LithoThemePatch
    public static int applyLithoTheme(int originalValue) {
        Context context = ReVancedUtils.getContext();
        if (anyEquals(originalValue, CONSTANTS) || (ThemeHelper.isDarkTheme() && anyEquals(originalValue, DARKCONSTANTS))) return 0;
        else if (anyEquals(originalValue, EXCEPTION)) return context.getResources().getColor(context.getResources().getIdentifier("yt_black1", "color", context.getPackageName()));
        return originalValue;
    }

    private static boolean anyEquals(int value, int... of) {
        for (int v : of) if (value == v) return true;
        return false;
    }
}
