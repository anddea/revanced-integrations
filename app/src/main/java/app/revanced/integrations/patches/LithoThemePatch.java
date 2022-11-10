package app.revanced.integrations.patches;

import android.util.Log;

public class LithoThemePatch {
    private static final int[] CONSTANTS = {
        -14606047, // comments chip background
        -15198184, // music related results panel background
        -98492127, // video chapters list background
        -14145496 // drawer content view background
    };

    private static final int[] EXCEPTION = {
        -15790321 // comments chip background (new layout)
    };


    // Used by app.revanced.patches.youtube.layout.theme.patch.LithoThemePatch
    public static int applyLithoTheme(int originalValue) {
        
        if (anyEquals(originalValue, CONSTANTS)) return 0;
        else if (anyEquals(originalValue, EXCEPTION)) return -16777216;
        return originalValue;
    }

    private static boolean anyEquals(int value, int... of) {
        for (int v : of) if (value == v) return true;
        return false;
    }
}
