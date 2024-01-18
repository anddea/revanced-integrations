package app.revanced.integrations.reddit.settingsmenu;

public class SettingsStatus {
    public static boolean generalAds = false;
    public static boolean navigationButtons = false;
    public static boolean openLinksDirectly = false;
    public static boolean openLinksExternally = false;
    public static boolean recentlyVisitedShelf = false;
    public static boolean sanitizeUrlQuery = false;
    public static boolean screenshotPopup = false;
    public static boolean toolBarButton = false;


    public static void GeneralAds() {
        generalAds = true;
    }

    public static void NavigationButtons() {
        navigationButtons = true;
    }

    public static void OpenLinksDirectly() {
        openLinksDirectly = true;
    }

    public static void OpenLinksExternally() {
        openLinksExternally = true;
    }

    public static void RecentlyVisitedShelf() {
        recentlyVisitedShelf = true;
    }

    public static void SanitizeUrlQuery() {
        sanitizeUrlQuery = true;
    }

    public static void ScreenshotPopup() {
        screenshotPopup = true;
    }

    public static void ToolBarButton() {
        toolBarButton = true;
    }

    public static void load() {

    }
}
