package app.revanced.integrations.patches;

import android.content.Context;
import android.util.Log;

import app.revanced.integrations.utils.ReVancedUtils;

public class LithoThemePatch {
    private static final int[] WHITECONSTANTS = {
        -1, // comments chip background
        -394759, // music related results panel background
        -83886081 // video chapters list background
    };

    private static final int[] DARKCONSTANTS = {
        -14145496, // drawer content view background
        -14606047, // comments chip background
        -15198184, // music related results panel background
        -15790321, // comments chip background (new layout)
        -98492127 // video chapters list background
    };

    // Used by app.revanced.patches.youtube.layout.theme.patch.LithoThemePatch
    public static int applyLithoTheme(int originalValue) {
        Context context = ReVancedUtils.getContext();
        if (anyEquals(originalValue, DARKCONSTANTS)) return context.getResources().getColor(context.getResources().getIdentifier("yt_black1", "color", context.getPackageName()));
        else if (anyEquals(originalValue, WHITECONSTANTS)) return context.getResources().getColor(context.getResources().getIdentifier("yt_white1", "color", context.getPackageName()));
        return originalValue;
    }

    private static boolean anyEquals(int value, int... of) {
        for (int v : of) if (value == v) return true;
        return false;
    }
}


