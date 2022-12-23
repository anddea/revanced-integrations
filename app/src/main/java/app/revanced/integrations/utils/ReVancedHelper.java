package app.revanced.integrations.utils;

import android.content.pm.PackageManager;

import java.util.Locale;

public class ReVancedHelper {
    private static final String[] RTLLanguageList = {"ar", "dv", "fa", "ha", "he", "iw", "ji", "ps", "ur", "yi"};
    private static boolean RTL = false;
    private static boolean isFounded = false;

    private ReVancedHelper() {
    } // utility class

    public static boolean isTablet() {
        var context = ReVancedUtils.getContext();
        assert context != null;
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void setRTL() {
        if (isFounded) return;
        try {
            Locale locale = ReVancedUtils.getContext().getResources().getConfiguration().locale;
            final String language = locale.getLanguage();
            for (String s : RTLLanguageList) {
                if (s.equals(language)) {
                    RTL = true;
                    break;
                }
            }
            isFounded = true;
        } catch (Exception ex) {
            LogHelper.printException(ReVancedUtils.class, "Failed to get locale", ex);
            RTL = false;
        }
    }

    public static String getOldString(String oldstring) {
        if (oldstring.contains("|"))
            oldstring = RTL ? oldstring.split(" \\| ")[1] : oldstring.split(" \\| ")[0];

        return oldstring;
    }

    public static String setRTLString(String likeString, String dislikeString) {
        String newString = likeString + " | " + dislikeString;
        if (RTL)
            newString = dislikeString + " | " + likeString;

        return newString;
    }

    public static String getVersionName() {
        try {
            var context = ReVancedUtils.getContext();
            assert context != null;
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "17.49.37";
    }
}