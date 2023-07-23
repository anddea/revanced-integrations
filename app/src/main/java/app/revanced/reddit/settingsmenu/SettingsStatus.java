package app.revanced.reddit.settingsmenu;

public class SettingsStatus {
    public static boolean navigationButtons = false;
    public static boolean openLinksDirectly = false;
    public static boolean openLinksExternally = false;
    public static boolean sanitizeUrlQuery = false;
    public static boolean screenshotPopup = false;


    public static void NavigationButtons() {
        navigationButtons = true;
    }

    public static void OpenLinksDirectly() {
        openLinksDirectly = true;
    }

    public static void OpenLinksExternally() {
        openLinksExternally = true;
    }

    public static void SanitizeUrlQuery() {
        sanitizeUrlQuery = true;
    }

    public static void ScreenshotPopup() {
        screenshotPopup = true;
    }

    public static void load() {

    }
}
