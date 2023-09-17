package app.revanced.integrations.patches.utils;

public class NavBarIndexPatch {
    private static volatile int currentNavBarIndex = 0;
    private static volatile int lastNavBarIndex = 0;

    public static void setCurrentNavBarIndex(int navBarIndex) {
        if (currentNavBarIndex == navBarIndex)
            return;

        lastNavBarIndex = currentNavBarIndex;
        currentNavBarIndex = navBarIndex;
    }

    public static void setLastNavBarIndex() {
        currentNavBarIndex = lastNavBarIndex;
    }

    public static boolean isShortsTab() {
        return currentNavBarIndex == 1;
    }
}


