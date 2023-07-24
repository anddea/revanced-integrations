package app.revanced.integrations.patches.utils;

import android.content.Context;

public class NavBarIndexPatch {
    private static int currentNavBarIndex = 0;
    private static int lastNavBarIndex = 0;
    private static boolean isLaunched = false;

    public static void setCurrentNavBarIndex(int navBarIndex) {
        if (currentNavBarIndex == navBarIndex)
            return;

        lastNavBarIndex = currentNavBarIndex;
        currentNavBarIndex = navBarIndex;
    }

    public static void setLastNavBarIndex() {
        currentNavBarIndex = lastNavBarIndex;
    }

    public static boolean isNotLibraryTab() {
        int collectionNavButtonIndex = 4;
        return currentNavBarIndex < collectionNavButtonIndex;
    }

    public static boolean isShortsTab() {
        int collectionNavButtonIndex = 1;
        return currentNavBarIndex == collectionNavButtonIndex;
    }

    public static void initializeIndex(Context context) {
        if (isLaunched)
            return;

        isLaunched = true;
        setCurrentNavBarIndex(0);
    }
}


