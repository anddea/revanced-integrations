package app.revanced.integrations.patches.utils;

public class NavBarIndexPatch {
    private static int navBarIndex = 0;

    public static void setNavBarIndex(int value) {
        navBarIndex = value;
    }

    public static boolean isNoneLibraryTab() {
        int collectionNavButtonIndex = 4;
        return navBarIndex < collectionNavButtonIndex;
    }
}


