package app.revanced.integrations.patches.utils;

import app.revanced.integrations.utils.LogHelper;

public class NavBarIndexPatch {
    private static int currentNavBarIndex = 0;

    /**
     * Injection point.
     *
     * @param navBarIndex tab index in PivotBar
     */
    public static void setNavBarIndex(int navBarIndex) {
        setNavBarIndex(navBarIndex, true);
    }

    /**
     * Injection point.
     *
     * @param navBarIndex tab index in PivotBar
     * @param isSelected  whether the current tab is selected
     */
    public static void setNavBarIndex(int navBarIndex, boolean isSelected) {
        if (!isSelected || currentNavBarIndex == navBarIndex)
            return;

        currentNavBarIndex = navBarIndex;
        LogHelper.printDebug(() -> "Setting NavBar Index to: " + navBarIndex);
    }

    public static boolean isNotLibraryTab() {
        return currentNavBarIndex != 4;
    }
}


